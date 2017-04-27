/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package srcAdmin;

import Entity.AdminRole;
import Entity.Branch;
import Entity.BranchManager;
import Entity.Employee;
import SessionBean.BranchFacadeLocal;
import SessionBean.BranchManagerFacadeLocal;
import SessionBean.EmployeeFacadeLocal;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.List;
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

/**
 *
 * @author 19319
 */
@Named(value = "aboutBM")

@SessionScoped
@ManagedBean
@RequestScoped
public class aboutBranchManager {

    @EJB
    private BranchManagerFacadeLocal BMF;

    @EJB
    private BranchFacadeLocal BF;

    @EJB
    private EmployeeFacadeLocal EF;

    private boolean isAllowed;
    private boolean isNameExists;
    private boolean isValidPassword = true;
    private boolean isBIDExists = true;
    private String confirmPassword = "";
    private String branchID;
    private String originalURL;
    private String baseURL;
    private String searchString;
    private BranchManager branchManager;
    private String password;
    //getter setter
    ////////////////////////////////////////////////////////////

    public String getBranchID() {
        return branchID;
    }

    public void setBranchID(String branchID) {
        this.branchID = branchID;
    }

    public boolean isIsBIDExists() {
        return isBIDExists;
    }

    public void setIsBIDExists(boolean isBIDExists) {
        this.isBIDExists = isBIDExists;
    }

    public boolean isIsNameExists() {
        return isNameExists;
    }

    public void setIsNameExists(boolean isNameExists) {
        this.isNameExists = isNameExists;
    }

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

    public String getOriginalURL() {
        return originalURL;
    }

    public void setOriginalURL(String originalURL) {
        this.originalURL = originalURL;
    }

    public String getBaseURL() {
        return baseURL;
    }

    public void setBaseURL(String baseURL) {
        this.baseURL = baseURL;
    }

    public BranchManager getBranchManager() {
        return branchManager;
    }

    public void setBranchManager(BranchManager branchManager) {
        this.branchManager = branchManager;
    }

    public String getPassword() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        String sessionPassword = "";
        try {
            sessionPassword = externalContext.getSessionMap().get("cre_bm_password").toString();
            password = sessionPassword;
        } catch (Exception e) {
        }
        return password;
    }

    public void setPassword(String password) {
        try {
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            externalContext.getSessionMap().put("cre_bm_password", password);
            this.password = password;
        } catch (Exception e) {
        }
    }

    ////////////////////////////////////////////////////////////
    //end getter setter
    @PostConstruct

    public void init() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        if (externalContext.getSessionMap().get("details_BM") == null) {
            branchManager = new BranchManager();
            branchManager.setBranchID(new Branch());
        } else {
            try {
                branchManager = BMF.find(externalContext.getSessionMap().get("details_BM"));
            } catch (Exception e) {
            }
        }
        originalURL = (String) externalContext.getRequestMap().get(RequestDispatcher.FORWARD_REQUEST_URI);
        baseURL = externalContext.getRequestContextPath();
        if (originalURL == null) {
            originalURL = externalContext.getRequestContextPath() + "/Admin/BranchManager/Index.xhtml";
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

    public void checkUsername() {
        if (BMF.isNameExists(branchManager.getBmUsername()) && branchManager.getBmUsername().length() >= 5) {
            isNameExists = true;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Username Is Exists"));
        } else {
            isNameExists = false;
        }
    }

    public void checkBID() {
        if (BF.find(branchManager.getBranchID().getBranchID()) != null) {
            isBIDExists = true;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Branch Name: " + BF.find(branchManager.getBranchID().getBranchID()).getBranchName()));
        } else {
            isBIDExists = false;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Branch ID Is Not Exists"));
        }
    }

    public String encryptPassword(String Password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update((Password).getBytes());
        byte byteData[] = md.digest();
        //convert the byte to hex format method 1
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    public void passwordChangeListener(ValueChangeEvent event) {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        externalContext.getSessionMap().put("cre_bm_password", (String) event.getNewValue());
    }

    public void confirmPasswordChangeListener(ValueChangeEvent event) {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        externalContext.getSessionMap().put("cre_bm_confirmPassword", (String) event.getNewValue());
    }

    public void checkConfirmPassword() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        try {
            password = externalContext.getSessionMap().get("cre_bm_password").toString();
            confirmPassword = externalContext.getSessionMap().get("cre_bm_confirmPassword").toString();
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

    public void createBM() throws IOException, NoSuchAlgorithmException {
        isAllowed("Role01");
        checkUsername();
        checkConfirmPassword();
        checkBID();
        if (isNameExists || isBIDExists == false || isValidPassword == false) {
            return;
        }
        if (password.trim().length() != password.length()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Password Cannot Have Space Character At First Or Last"));
            return;
        }
        if (password != null && password.equals("") == false && password.length() >= 5 && confirmPassword != null && confirmPassword.equals("") == false) {
            branchManager.setBmPassword(encryptPassword(password));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Password Cannot Empty"));
            return;
        }
        branchManager.setBmPassword(encryptPassword(password));
        BMF.create(branchManager);
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        externalContext.getSessionMap().put("details_BM", branchManager.getBmUsername());
        externalContext.redirect(baseURL + "/Admin/BranchManager/detailsBM.xhtml");
    }

    public void loadDetails() {
        isAllowed("Role01");
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        externalContext.getSessionMap().remove("cre_bm_password");//delete session input for password
        externalContext.getSessionMap().remove("cre_bm_confirmPassword");
        if (externalContext.getRequestParameterMap().get("username") != null) {
            branchManager = BMF.find(externalContext.getRequestParameterMap().get("username"));
            externalContext.getSessionMap().put("details_BM", branchManager.getBmUsername());
        } else {
            branchManager = BMF.find(externalContext.getSessionMap().get("details_BM"));
        }

    }

    public void clickStatus(String bmUsername,Integer bmStatus) {
        isAllowed("Role01");
        try {
            branchManager = BMF.find(bmUsername);
            branchManager.setBMStatus(bmStatus);
            BMF.edit(branchManager);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Update Success"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Update Fail"));
        }
    }

    public void updateBM() throws NoSuchAlgorithmException {
        isAllowed("Role01");
        try {
            checkConfirmPassword();
            if (isValidPassword == false) {
                return;
            }
            if (password.trim().length() != password.length()) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Password Cannot Have Space Character At First Or Last"));
                return;
            }
            if (password != null && !password.equals("") && password.length() >= 5) {
                branchManager.setBmPassword(encryptPassword(password));
            }

            BMF.edit(branchManager);
            ExternalContext externalContext=FacesContext.getCurrentInstance().getExternalContext();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Update Success"));
            password = "";
            confirmPassword = "";
            externalContext.getSessionMap().remove("cre_bm_password");
            externalContext.getSessionMap().remove("cre_bm_confirmPassword");
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Update Fail"));
        }
    }

    public void goFastCreate() throws IOException {
        try {
            branchManager.getBranchID().setBranchID(Integer.valueOf(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("details_Branch").toString()));
        } catch (Exception e) {
            branchManager = new BranchManager();
        }
    }

    public void reloadForCreate() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        externalContext.getSessionMap().remove("cre_bm_password");
        externalContext.getSessionMap().remove("cre_bm_confirmPassword");
    }
}
