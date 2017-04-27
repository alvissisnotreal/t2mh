/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package srcBranchManager;

import Entity.Branch;
import Entity.BranchManager;
import SessionBean.BranchManagerFacadeLocal;
import SessionBean.EmployeeFacadeLocal;
import SessionBean.EmployeeInfoFacadeLocal;
import java.io.IOException;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.inject.Named;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author 19319
 */
@Named(value = "bmLogin")

@SessionScoped
@ManagedBean
public class BMLogin implements Serializable {

    @EJB
    private BranchManagerFacadeLocal BMF;

    private String password;
    private String originalURL;
    private String baseURL;
    private String webURL;
    private String realPath;
    private boolean isLoggedBM = false;
    private BranchManager branchManager;
    private String confirmPassword = "";
    private boolean isValidPassword = true;

    public String getWebURL() {
        return webURL;
    }

    //Getter and Setter
    public boolean isIsValidPassword() {
        return isValidPassword;
    }

    public void setIsValidPassword(boolean isValidPassword) {
        this.isValidPassword = isValidPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

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

    public String reWriteUrl(String path, String nameProject) {
        Pattern pat = Pattern.compile("(\\bdist\\b.*?war_war)");
        Matcher mat = pat.matcher(path);
        String url = mat.replaceAll(nameProject + "-war\\\\web");
        return url;
    }

    //End of Getter and Setter
    @PostConstruct
    public void init() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        originalURL = (String) externalContext.getRequestMap().get(RequestDispatcher.FORWARD_REQUEST_URI);
        baseURL = externalContext.getRequestContextPath();
        branchManager = new BranchManager();
        realPath = externalContext.getRealPath("/");
        webURL = reWriteUrl(FacesContext.getCurrentInstance().getExternalContext().getRealPath(""), "T2MH");
        if (originalURL == null) {
            originalURL = externalContext.getRequestContextPath() + "//BranchManager/Index.xhtml";
        } else {
            String originalQuery = (String) externalContext.getRequestMap().get(RequestDispatcher.FORWARD_QUERY_STRING);

            if (originalQuery != null) {
                originalURL += "?" + originalQuery;
            }
        }
    }

    public void reloadLogin() {
        branchManager = new BranchManager();
    }

    private String encryptPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update((password).getBytes());
        byte byteData[] = md.digest();
        //convert the byte to hex format method 1
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    public void normalLogin() throws IOException, NoSuchAlgorithmException {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        //  HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
        String password = branchManager.getBmPassword();
        String username = branchManager.getBmUsername();
        BMF.refresh();
        branchManager = BMF.find(username);

        if (branchManager == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Username is not exists,<br/>Try again"));
            return;
        } else {
            if (branchManager.getBranchID().getBranchStatus() != 1) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Branch has been locked. <br/>You cannot login"));
                return;
            }
            if (branchManager.getBMStatus() == 1) {
                if (!encryptPassword(password).equals(branchManager.getBmPassword())) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Password incorrect,<br/>Try again"));
                    return;
                }
                if (encryptPassword(password).equals(branchManager.getBmPassword())) {
                    externalContext.getSessionMap().put("LOGGED_BM", branchManager);
                    isLoggedBM = true;
                    externalContext.redirect(baseURL + "//BranchManager/Index.xhtml");
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "This account has been locked. "
                        + "<br/>Please contact Manage Admin to resolve"));
                return;
            }
        }
    }

    public void logout() throws IOException {
//        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
//        isLoggedCustomer=false;
//        externalContext.invalidateSession();
//        externalContext.redirect(externalContext.getRequestContextPath() + "//Admin/Login.xhtml");

        //code below only remove logged_
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
        externalContext.getSessionMap().remove("LOGGED_BM");
        externalContext.redirect(externalContext.getRequestContextPath() + "/BranchManager/Login.xhtml");
    }

    public void passwordChangeListener(ValueChangeEvent event) {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        externalContext.getSessionMap().put("upd_bm_password", (String) event.getNewValue());
    }

    public void confirmPasswordChangeListener(ValueChangeEvent event) {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        externalContext.getSessionMap().put("upd_bm_confirmPassword", (String) event.getNewValue());
    }

    public void checkConfirmPassword() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        try {
            password = externalContext.getSessionMap().get("upd_bm_password").toString();
            confirmPassword = externalContext.getSessionMap().get("upd_bm_confirmPassword").toString();
            if (confirmPassword == null | confirmPassword.equals("")) {
                return;
            }
        } catch (Exception e) {
            isValidPassword = false;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Password Confirm Not Match"));
            return;
        }
        if (confirmPassword.equals(password)) {
            isValidPassword = true;
        } else {
            isValidPassword = false;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Password Confirm Not Match"));
        }
    }

    public void loadDetails() {
        BMF.refresh();
        branchManager = BMF.find(branchManager.getBmUsername());

    }

    public void updateBM() {
        try {
            checkConfirmPassword();
            if (isValidPassword == false) {
                return;
            }
            if (password.trim().length() != password.length()) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Password Cannot Have Space Character At First Or Last"));
                return;
            }
            if (password.equals("") == false && password != null && password.length() >= 5) {
                branchManager.setBmPassword(encryptPassword(password));
            }

            BMF.edit(branchManager);
            BMF.refresh();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Update Success"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Update Fail"));
        }
    }
}
