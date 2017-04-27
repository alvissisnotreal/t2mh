/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package srcCustomer;

import Entity.Customer;
import Entity.Feedback;
import SessionBean.CustomerFacadeLocal;
import SessionBean.FeedbackFacadeLocal;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.RequestDispatcher;
import org.primefaces.context.RequestContext;

/**
 *
 * @author 19319
 */
@Named(value = "cusFeedback")
@SessionScoped
@ManagedBean
public class cusFeedback {

    @EJB
    private FeedbackFacadeLocal feedbackF;

    @EJB
    private CustomerFacadeLocal customerF;

    @ManagedProperty(value = "#{customerLogin}")
    private customerLogin customerLoginBean;
    // +getter setter

    public customerLogin getCustomerLoginBean() {
        return customerLoginBean;
    }

    public void setCustomerLoginBean(customerLogin customerLoginBean) {
        this.customerLoginBean = customerLoginBean;
    }

    private String originalURL;
    private String baseURL;
    private List<Feedback> listFeedback;
    private Customer customer;
    private Feedback feedback;
    private final int maximumFeedback = 5;
    //getter and setter

    public List<Feedback> getListFeedback() {
        return listFeedback;
    }

    public void setListFeedback(List<Feedback> listFeedback) {
        this.listFeedback = listFeedback;
    }

    public Feedback getFeedback() {
        return feedback;
    }

    public void setFeedback(Feedback feedback) {
        this.feedback = feedback;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    //end getter and setter
    public cusFeedback() {
    }

    @PostConstruct
    public void init() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        originalURL = (String) externalContext.getRequestMap().get(RequestDispatcher.FORWARD_REQUEST_URI);
        baseURL = externalContext.getRequestContextPath();
        listFeedback = new ArrayList<>();
        feedback = new Feedback();
        if (originalURL == null) {
            originalURL = externalContext.getRequestContextPath() + "/index.xhtml";
        } else {
            String originalQuery = (String) externalContext.getRequestMap().get(RequestDispatcher.FORWARD_QUERY_STRING);

            if (originalQuery != null) {
                originalURL += "?" + originalQuery;
            }
        }
    }

    public void reloadPage() throws IOException {

        try {
            if (feedback.getComplainID() != null) {
                feedback = new Feedback();
            }
        } catch (Exception e) {
            if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("LOGGED_CUSTOMER") == null) {
                FacesContext.getCurrentInstance().getExternalContext().redirect(baseURL + "/index.xhtml");
            }
        }
    }

    public void createFeedback() {
        try {
            //maximum feedbacks wait for respon = 5
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            customer = (Customer) externalContext.getSessionMap().get("LOGGED_CUSTOMER");
            if (feedbackF.countFeedbackEmptyEmpID(customer.getCustomerID()) >= maximumFeedback) {
                FacesContext.getCurrentInstance().addMessage("dialogFeedbackMessage",
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "You Have " + maximumFeedback + " Feedback Wait For Administrator Response."
                                + "<br/>Waiting For At Least 1 Of Those Have Response"));
                RequestContext contextRequest = RequestContext.getCurrentInstance();
                contextRequest.execute("PF('dialogFeedback').show();");
                return;
            }
            if (feedback.getTitle().length() > 100) {
                FacesContext.getCurrentInstance().addMessage("dialogFeedbackMessage",
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Error", "Length's Title\n\nOnly Accept From 10 to 100 Characters"));
                return;
            }
            if (feedback.getContent().length() < 10 || feedback.getContent().length() > 5000) {
                FacesContext.getCurrentInstance().addMessage("dialogFeedbackMessage",
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Error", "Length's Feedback Content\n\n<br/>Only Accept From 10 to 5000 Characters"));
                return;
            }
            Date date = new Date();
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

            feedback.setComplainID(0);
            feedback.setCustomerID(customer);
            feedback.setTimePost(formatter.format(date));

            //avoid error when sent feedback at details feedback
            feedback.setEmployeeID(null);
            feedback.setReplyContent("");
            feedback.setTimeReply("");

            feedbackF.create(feedback);
            RequestContext contextRequest = RequestContext.getCurrentInstance();
            feedback = new Feedback();
            contextRequest.execute("PF('dialogFeedback').hide();");
            FacesContext.getCurrentInstance().addMessage("dialogFeedbackMessage",
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Success, You Just Sent A Feedback.\n\nWe Will Reply As Fast As Possible.\n\nThank You"));

            return;
        } catch (Exception e) {
            RequestContext contextRequest = RequestContext.getCurrentInstance();
            contextRequest.execute("PF('dialogFeedback').hide();");
            FacesContext.getCurrentInstance().addMessage("dialogFeedbackMessage",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Sorry, Cannot Sent Feedback"));
        }
    }

    public void getAllListFeedback() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        if (externalContext.getSessionMap().get("LOGGED_CUSTOMER") == null) {

            return;
        }
        listFeedback = new ArrayList<>();
        customerF.refresh();
        customer = customerF.find(((Customer) externalContext.getSessionMap().get("LOGGED_CUSTOMER")).getCustomerID());
        listFeedback = (List<Feedback>) customer.getFeedbackCollection();
        List<Feedback> listFeedbackOutput = new ArrayList<>();
        for (int i = listFeedback.size(); i > 0; i--) {
            listFeedbackOutput.add(listFeedback.get(i - 1));
        }
        listFeedback = listFeedbackOutput;
    }

    public void loadDetails() {
        try {
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            customer = (Customer) externalContext.getSessionMap().get("LOGGED_CUSTOMER");
            if (externalContext.getRequestParameterMap().get("feedbackID") != null) {
                feedback = feedbackF.find(Integer.valueOf(externalContext.getRequestParameterMap().get("feedbackID")));
                externalContext.getSessionMap().put("details_Feedback", feedback.getComplainID());
            } else {
                feedback = feedbackF.find(externalContext.getSessionMap().get("details_Feedback"));
            }
            if (customer == null) {
                feedback = new Feedback();
                return;
            }
            if (feedback.getCustomerID().getCustomerID().intValue() != customer.getCustomerID().intValue()) {
                FacesContext.getCurrentInstance().addMessage("messageUpdate",
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Sorry, You Cannot View This Feedback"));
                feedback = new Feedback();
            }
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage("messageUpdate",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Sorry, Cannot Load Info"));
        }

    }

    public boolean isAllowViewFeedback() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        if (externalContext.getSessionMap().get("LOGGED_CUSTOMER") == null) {
            return false;
        }
        if (externalContext.getRequestParameterMap().get("feedbackID") != null) {
            feedback = feedbackF.find(Integer.valueOf(externalContext.getRequestParameterMap().get("feedbackID")));
            externalContext.getSessionMap().put("details_Feedback", feedback.getComplainID());
        } else {
            feedback = feedbackF.find(externalContext.getSessionMap().get("details_Feedback"));
        }
        if (feedback.getComplainID() != null && feedback.getCustomerID().getCustomerID().intValue() != customerLoginBean.getCustomer().getCustomerID().intValue()) {
            return false;
        }
        return true;
    }

    @PreDestroy
    public void destroyRequestBean() {
        System.out.println("Complete Request cusOrder bean");
    }
}
