/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package srcBranchManager;

import Entity.Branch;
import Entity.BranchManager;
import Entity.OrderInfo;
import Entity.OrderInfoPK;
import SessionBean.BranchManagerFacadeLocal;
import SessionBean.OrderInfoFacadeLocal;
import SessionBean.OrdersFacadeLocal;
import java.io.IOException;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.inject.Named;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.RequestDispatcher;

/**
 *
 * @author 19319
 */
@Named(value = "bmOrderInfo")

@SessionScoped
@ManagedBean
public class bmOrderInfo implements Serializable {

    @EJB
    private OrderInfoFacadeLocal orderInfoF;

    @EJB
    private OrdersFacadeLocal ordersF;

    @EJB
    private BranchManagerFacadeLocal BMF;

    private String password;
    private String messageLogin;
    private String originalURL;
    private String baseURL;
    private String webURL;
    private String realPath;
    private boolean isLoggedBM = false;
    private BranchManager branchManager;
    private Branch branch;

    public String getWebURL() {
        return webURL;
    }

    //Getter and Setter
    public BranchManager getBranchManager() {
        return branchManager;
    }

    public void setBranchManager(BranchManager branchManager) {
        this.branchManager = branchManager;
    }

    public String getRealPath() {
        return realPath;
    }

    public void setRealPath(String realPath) {
        this.realPath = realPath;
    }

    public void setWebURL(String webURL) {
        this.webURL = webURL;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMessageLogin() {
        return messageLogin;
    }

    public void setMessageLogin(String messageLogin) {
        this.messageLogin = messageLogin;
    }

    public String getBaseURL() {
        return baseURL;
    }

    public void setBaseURL(String baseURL) {
        this.baseURL = baseURL;
    }

    public boolean isIsLoggedBM() {
        return isLoggedBM;
    }

    public void setIsLoggedBM(boolean isLoggedBM) {
        this.isLoggedBM = isLoggedBM;
    }

    @PostConstruct
    public void init() {
        String currentURL = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        branchManager = new BranchManager();
        branch = new Branch();
        originalURL = (String) externalContext.getRequestMap().get(RequestDispatcher.FORWARD_REQUEST_URI);
        baseURL = externalContext.getRequestContextPath();
        if (originalURL == null) {
            originalURL = externalContext.getRequestContextPath() + "/BranchManager/Branch/Index.xhtml";
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
            branchManager = (BranchManager) externalContext.getSessionMap().get("LOGGED_BM");
            BMF.refresh();
            BranchManager currentInfo = BMF.find(branchManager.getBmUsername());

            if (currentInfo.getBMStatus() != 1 || currentInfo.getBmPassword().equals(branchManager.getBmPassword()) == false) {
                externalContext.redirect(baseURL + "/BranchManager/Error.xhtml");
            }
        } catch (Exception e) {
            externalContext.redirect(baseURL + "/BranchManager/Error.xhtml");
        }
    }

    public void changeStatus(OrderInfoPK OIPK, String Status) {
        try {
            OrderInfo orderInfo = orderInfoF.find(OIPK);
            orderInfo.setDescriptions(Status);
            orderInfoF.edit(orderInfo);
            FacesContext.getCurrentInstance().addMessage("messagesUpdate", new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Update Success"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage("messagesUpdate", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Info", "Update Fail"));
        }

    }
}
