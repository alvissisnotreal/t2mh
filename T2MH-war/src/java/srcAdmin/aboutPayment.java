/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package srcAdmin;

import Entity.Employee;
import Entity.EmployeeInfo;
import Entity.AdminRole;
import Entity.Branch;
import Entity.Payment;
import SessionBean.AdminRoleFacadeLocal;
import SessionBean.BranchFacadeLocal;
import SessionBean.EmployeeFacadeLocal;
import SessionBean.EmployeeInfoFacadeLocal;
import SessionBean.PaymentFacadeLocal;
import XMLAccess.XmlAccessDescriptions;
import java.io.IOException;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.RequestDispatcher;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author 19319
 */
@Named(value = "aboutPayment")

@SessionScoped
@ManagedBean
public class aboutPayment implements Serializable {

    @EJB
    private BranchFacadeLocal branchFacade;

    @EJB
    private EmployeeFacadeLocal EF;
    @EJB
    private PaymentFacadeLocal paymentF;

    private String baseURL = "";
    private String originalURL = "";

    private Date dateDebt;
    private Branch branch;
    private Payment payment;
    private boolean isAllowed = false;
    private boolean isEnable = true;
    private long newPaid;
    private HashMap<String, String> hmDes = new HashMap<String, String>();

    //getter and setter
    public long getNewPaid() {
        return newPaid;
    }

    public void setNewPaid(long newPaid) {
        this.newPaid = newPaid;
    }

    public Date getDateDebt() {
        return dateDebt;
    }

    public void setDateDebt(Date dateDebt) {
        this.dateDebt = dateDebt;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public String getBaseURL() {
        return baseURL;
    }

    public void setBaseURL(String baseURL) {
        this.baseURL = baseURL;
    }

//end getter and setter
    @PostConstruct
    public void init() {
        String currentURL = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        hmDes = new HashMap<String, String>();
        payment = new Payment();
        branch = new Branch();
        newPaid = 0;
        originalURL = (String) externalContext.getRequestMap().get(RequestDispatcher.FORWARD_REQUEST_URI);
        baseURL = externalContext.getRequestContextPath();
        if (originalURL == null) {
            originalURL = externalContext.getRequestContextPath() + "/Admin/Employee/Index.xhtml";
        } else {
            String originalQuery = (String) externalContext.getRequestMap().get(RequestDispatcher.FORWARD_QUERY_STRING);

            if (originalQuery != null) {
                originalURL += "?" + originalQuery;
            }
        }
    }

    public void isAllowed(String role) {
        isAllowed = false;
        List<AdminRole> listRole;
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        try {
            Employee emp = (Employee) externalContext.getSessionMap().get("LOGGED_ADMIN");
            EF.refresh();
            emp = EF.find(emp.getEmployeeID());
            listRole = (List<AdminRole>) emp.getAdminRoleCollection();
            for (Iterator<AdminRole> iterator = listRole.iterator(); iterator.hasNext();) {
                AdminRole aRole = iterator.next();
                //Role00=Manage
                if (aRole.getRoleID().equals("Role00") || aRole.getRoleID().equals(role)) {
                    isAllowed = true;
                    break;
                }
            }
            if (isAllowed == false || emp.getEmployeeStatus() != 1) {
                externalContext.redirect(baseURL + "/Admin/Error.xhtml");
            }
        } catch (Exception e) {
            try {
                e.printStackTrace();
                externalContext.redirect(baseURL + "/Admin/Error.xhtml");
            } catch (Exception ex) {
            }
        }
    }

    public void onDateSelect(SelectEvent event) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        payment.setDateDebt(format.format(event.getObject()));
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Date Selected To Create", payment.getDateDebt()));
    }

    public void loadDetails() {
        isAllowed("Role01");
        try {
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            if (externalContext.getRequestParameterMap().get("paymentID") != null) {
                payment = paymentF.find(externalContext.getRequestParameterMap().get("paymentID"));
                externalContext.getSessionMap().put("details_Payment", payment.getPaymentID());
            } else {
                payment = paymentF.find(externalContext.getSessionMap().get("details_Payment"));
            }
            branch = payment.getBranchID();
            newPaid = 0;
//            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
//            dateDebt=format.parse(payment.getDateDebt());
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Can't load info"));
        }
    }

    //Amount>0 , amount is the amount that t2mh will pay for branch
    //if amount<0, amount*-1 is the amount that t2mh will receive from branch
    //newPaid<0   => thePaid*-1 is the amount that t2mh received from branch
    //newPaid>0   => thePaid is the amount that t2mh will pay for branch
    //debt>0, debt is the amount that t2mh will pay for branch
    //debt<0, debt*-1 is the amount that t2mh will receive from branch
    //debt + paid=amount
    //debt<0 && debt=Amount  -   Paid
    //<=> Debt <= newPaid < 0
    //debt>0 && debt=Amount  -   Paid
    //<=> 0< newPaid <= Debt
    public void updatePayment() {
        payment = paymentF.find(payment.getPaymentID());
        if (payment.getDebt() < 0) {
            if ((newPaid >= payment.getDebt() && newPaid < 0) == false) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "New Paid Accept Value \t\tFrom " + String.format("%.0f", payment.getDebt())
                        + " \t\tTo Less Than 0"));
                newPaid = 0;
                return;
            }
        } else if (payment.getDebt() > 0) {
            if ((newPaid <= payment.getDebt() && newPaid > 0) == false) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "New Paid Accept Value \t\tGreater Than 0"
                        + " \t\tTo " + String.format("%.0f", payment.getDebt())));
                newPaid = 0;
                return;
            }
        } else {//debt=0
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Cannot Update"));
            newPaid = 0;
            return;
        }
        payment.setPaid(payment.getPaid() + newPaid);
        payment.setDebt(payment.getDebt() - newPaid);
        paymentF.edit(payment);
        newPaid = 0;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Edit Sucess"));
    }
}
