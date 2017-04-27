/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package srcBranchManager;

import srcAdmin.*;
import Entity.Employee;
import Entity.EmployeeInfo;
import Entity.AdminRole;
import Entity.Branch;
import Entity.BranchManager;
import Entity.Payment;
import SessionBean.AdminRoleFacadeLocal;
import SessionBean.BranchFacadeLocal;
import SessionBean.BranchManagerFacadeLocal;
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
@Named(value = "bmPayment")

@SessionScoped
@ManagedBean
public class bmPayment implements Serializable {

    @EJB
    private BranchManagerFacadeLocal BMF;

    @EJB
    private BranchFacadeLocal branchFacade;

    @EJB
    private EmployeeFacadeLocal EF;
    @EJB
    private PaymentFacadeLocal paymentF;

    private String baseURL = "";
    private String originalURL = "";

    private BranchManager bm;
    private Date dateDebt;
    private Branch branch;
    private Payment payment;
    private boolean isAllowed = false;
    private boolean isEnable = true;
    private HashMap<String, String> hmDes = new HashMap<String, String>();

    //getter and setter
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
        bm = new BranchManager();
        originalURL = (String) externalContext.getRequestMap().get(RequestDispatcher.FORWARD_REQUEST_URI);
        baseURL = externalContext.getRequestContextPath();
        if (originalURL == null) {
            originalURL = externalContext.getRequestContextPath() + "/BranchManager/Payment/Index.xhtml";
        } else {
            String originalQuery = (String) externalContext.getRequestMap().get(RequestDispatcher.FORWARD_QUERY_STRING);

            if (originalQuery != null) {
                originalURL += "?" + originalQuery;
            }
        }
    }

    public void isAllowed() throws IOException {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        try {
            bm = (BranchManager) externalContext.getSessionMap().get("LOGGED_BM");
            BMF.refresh();
            BranchManager currentInfo = BMF.find(bm.getBmUsername());

            if (currentInfo.getBMStatus() != 1 || currentInfo.getBmPassword().equals(bm.getBmPassword()) == false) {
                externalContext.redirect(baseURL + "/BranchManager/Error.xhtml");
            } else {
                isAllowed = true;
            }
        } catch (Exception e) {
            externalContext.redirect(baseURL + "/BranchManager/Error.xhtml");
        }
    }

    public void onDateSelect(SelectEvent event) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        payment.setDateDebt(format.format(event.getObject()));
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Date Selected To Create", payment.getDateDebt()));
    }

    public void loadDetails() throws IOException {
        isAllowed();
        try {
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            if (externalContext.getRequestParameterMap().get("paymentID") != null) {
                payment = paymentF.find(externalContext.getRequestParameterMap().get("paymentID"));
                externalContext.getSessionMap().put("details_Payment", payment.getPaymentID());
            } else {
                payment = paymentF.find(externalContext.getSessionMap().get("details_Payment"));
            }
            branch = payment.getBranchID();

//            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
//            dateDebt = format.parse(payment.getDateDebt());
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Can't load info"));
        }
    }

    public void updatePayment() {

    }
}
