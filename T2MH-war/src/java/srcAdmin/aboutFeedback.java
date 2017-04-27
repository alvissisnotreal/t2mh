/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package srcAdmin;

import Entity.AdminRole;
import Entity.Employee;
import Entity.Feedback;
import Entity.ProductCommentMark;
import Entity.ProductReview;
import Entity.ProductReviewComment;
import SessionBean.EmployeeFacadeLocal;
import SessionBean.FeedbackFacadeLocal;
import SessionBean.ProductCommentMarkFacadeLocal;
import SessionBean.ProductReviewCommentFacadeLocal;
import SessionBean.ProductReviewFacadeLocal;
import XMLAccess.XmlAccessDescriptions;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.RequestDispatcher;

/**
 *
 * @author 19319
 */
@Named(value = "aboutBR")

@SessionScoped
@ManagedBean
public class aboutFeedback implements Serializable {

    @EJB
    private FeedbackFacadeLocal feedbackF;

    @EJB
    private EmployeeFacadeLocal EF;

    private String baseURL = "";
    private String originalURL = "";
    private boolean isAllowed = false;
    private Feedback feedback;
    private Employee employee;
    private XmlAccessDescriptions XAD = new XmlAccessDescriptions();
    //getter and setter

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Feedback getFeedback() {
        return feedback;
    }

    public void setFeedback(Feedback feedback) {
        this.feedback = feedback;
    }

    //end getter and setter
    public aboutFeedback() {
    }

    @PostConstruct
    public void init() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        originalURL = (String) externalContext.getRequestMap().get(RequestDispatcher.FORWARD_REQUEST_URI);
        feedback = new Feedback();
        baseURL = externalContext.getRequestContextPath();
        if (originalURL == null) {
            originalURL = externalContext.getRequestContextPath() + "/Admin/Index.xhtml";
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

    //////////////////////////////////////////////////////////////////////////////
    public void loadDetails() {
        isAllowed("Role04");
        try {
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            if (externalContext.getRequestParameterMap().get("complainID") != null) {
                feedback = feedbackF.find(Integer.valueOf(externalContext.getRequestParameterMap().get("complainID")));
                externalContext.getSessionMap().put("details_Feedback", feedback.getComplainID());
            } else {
                feedback = feedbackF.find(externalContext.getSessionMap().get("details_Feedback"));
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Can't Load Info"));
        }
    }

    public void updateFeedback() {
        EF.refresh();
        try {
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            employee = (Employee) externalContext.getSessionMap().get("LOGGED_ADMIN");
            feedback.setEmployeeID(employee);
            Date date = new Date();
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String timeReply = formatter.format(date);
            feedback.setTimeReply(timeReply);
            feedbackF.edit(feedback);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Reply Success"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Can't Reply In This Time"));
        }
    }

}
