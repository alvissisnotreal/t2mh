/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package srcAdmin;

import Entity.AdminRole;
import Entity.Customer;
import Entity.CustomerInfo;
import Entity.Employee;
import SessionBean.CustomerFacadeLocal;
import SessionBean.CustomerInfoFacadeLocal;
import SessionBean.EmployeeFacadeLocal;
import XMLAccess.XmlAccessDescriptions;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
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
@Named(value = "aboutCustomer")

@SessionScoped
@ManagedBean
public class aboutCustomer {

    @EJB
    private CustomerInfoFacadeLocal CID;

    @EJB
    private CustomerFacadeLocal CF;

    @EJB
    private EmployeeFacadeLocal EF;

    private boolean isNameExists;
    private boolean isValidPassword;
    private String password;
    private String confirmPassword;
    private Customer customer;
    private CustomerInfo cInfo;
    private String originalURL;
    private String baseURL;
    private String descriptions;
    private boolean isAllowed;
    private boolean isValidEmail;
    private HashMap<String, String> hmDes;
    private XmlAccessDescriptions XAD;
    private Employee emp;

    //getter setter
    ////////////////////////////////////////////////////////////
    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }

    public boolean isIsValidEmail() {
        return isValidEmail;
    }

    public void setIsValidEmail(boolean isValidEmail) {
        this.isValidEmail = isValidEmail;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public CustomerInfo getcInfo() {
        return cInfo;
    }

    public void setcInfo(CustomerInfo cInfo) {
        this.cInfo = cInfo;
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

    public HashMap<String, String> getHmDes() {
        return hmDes;
    }

    public void setHmDes(HashMap<String, String> hmDes) {
        this.hmDes = hmDes;
    }

    public String getPassword() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        String sessionPassword = "";
        try {
            sessionPassword = externalContext.getSessionMap().get("cre_cus_password").toString();
            password = sessionPassword;
        } catch (Exception e) {
        }
        return password;
    }

    public void setPassword(String password) {
        try {
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            externalContext.getSessionMap().put("cre_cus_password", password);
            this.password = password;
        } catch (Exception e) {
        }
    }

    public Employee getEmp() {
        return emp;
    }

    public void setEmp(Employee emp) {
        this.emp = emp;
    }

    ////////////////////////////////////////////////////////////
    //end getter setter
    @PostConstruct

    public void init() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        customer = new Customer();
        emp = new Employee();
        hmDes = new HashMap<String, String>();
        XAD = new XmlAccessDescriptions();
        originalURL = (String) externalContext.getRequestMap().get(RequestDispatcher.FORWARD_REQUEST_URI);
        baseURL = externalContext.getRequestContextPath();
        if (originalURL == null) {
            originalURL = externalContext.getRequestContextPath() + "/Admin/Customer/Index.xhtml";
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
            emp = (Employee) externalContext.getSessionMap().get("LOGGED_ADMIN");
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

    public void checkUsername() {
        if (CF.findCustomerByUsername(customer.getCustomerUsername()) != null) {
            isNameExists = true;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Username Is Exists"));
        } else {
            isNameExists = false;
        }
    }

    public void checkUsernameForUpdate() {
        try {
            if (CF.isNameExistsForUpdate(customer.getCustomerID().toString(), customer.getCustomerUsername())) {
                isNameExists = true;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Username is Exists"));
            } else {
                isNameExists = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void checkEmail() {
        try {
            if (customer.getEmail() == null) {
                isValidEmail = true;
                return;
            }
            if (customer.getEmail().matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")) {
                isValidEmail = true;
            } else {
                isValidEmail = false;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Email Invalid"));
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Email Invalid"));
            isValidEmail = false;
        }
    }

    public void passwordChangeListener(ValueChangeEvent event) {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        externalContext.getSessionMap().put("cre_cus_password", (String) event.getNewValue());
    }

    public void confirmPasswordChangeListener(ValueChangeEvent event) {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        externalContext.getSessionMap().put("cre_cus_confirmPassword", (String) event.getNewValue());
    }

    public void checkConfirmPassword() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        try {
            password = externalContext.getSessionMap().get("cre_cus_password").toString();
            confirmPassword = externalContext.getSessionMap().get("cre_cus_confirmPassword").toString();
            if (password.equals(confirmPassword)) {
                isValidPassword = true;
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Confirm Password Is Incorrect"));
                isValidPassword = false;
            }
        } catch (Exception e) {
            if (externalContext.getSessionMap().get("cre_cus_password") == null && externalContext.getSessionMap().get("cre_cus_confirmPassword") == null) {
                isValidPassword = true;
                return;
            }
            if (externalContext.getSessionMap().get("cre_cus_password") == null || externalContext.getSessionMap().get("cre_cus_confirmPassword") == null) {
                isValidPassword = false;
                return;
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Confirm Password Is Incorrect"));
            isValidPassword = false;
        }
    }

    public void loadDetails() {
        hmDes = new HashMap<>();
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        externalContext.getSessionMap().remove("cre_cus_password");
        externalContext.getSessionMap().remove("cre_cus_confirmPassword");
        try {
            isAllowed("Role02");
            if (externalContext.getRequestParameterMap().get("customerID") != null) {
                customer = CF.find(Integer.valueOf(externalContext.getRequestParameterMap().get("customerID")));
                externalContext.getSessionMap().put("details_Customer", customer.getCustomerID());
            } else {
                customer = CF.find(externalContext.getSessionMap().get("details_Customer"));
            }
            cInfo = customer.getCustomerInfo();
            hmDes = XAD.convertDocumentToHashMap(XAD.stringToDocument(cInfo.getDescriptions()));
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Can't load info"));
        }
    }

    public void clickStatus(String customerID, Integer statusID) {
        isAllowed("Role02");
        try {
            customer = CF.find(Integer.valueOf(customerID));
            customer.setCustomerStatus(statusID);
            hmDes = XAD.convertDocumentToHashMap(XAD.stringToDocument(customer.getCustomerInfo().getDescriptions()));
            Date date = new Date();
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            hmDes.put("AdminLastEdit", formatter.format(date));
            hmDes.put("AdminEditBy", emp.getEmployeeID().toString());
            hmDes.put("AdminEditReason", "Change Status To " + statusID);

            customer.getCustomerInfo().setDescriptions(XAD.createDescriptions(hmDes));
            CF.edit(customer);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Update Successs"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Update Fail"));
        }
    }

    public void updateCustomer() throws NoSuchAlgorithmException {
        isAllowed("Role02");
        try {
            checkEmail();
            checkUsernameForUpdate();
            checkConfirmPassword();

            if (isNameExists || isValidEmail != true || isValidPassword != true) {
                return;
            }
            System.out.println("password trim       " + password.trim().length());
            System.out.println("password length:    " + password.length());
            if (password.trim().length() != password.length()) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Password Cannot Have Space Character At First Or Last"));
                return;
            }
            if (password != null && !password.equals("") && password.length() >= 5) {
                customer.setCustomerPassword(encryptPassword(password));
            }
            hmDes = XAD.convertDocumentToHashMap(XAD.stringToDocument(customer.getCustomerInfo().getDescriptions()));
            Date date = new Date();
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            hmDes.put("AdminLastEdit", formatter.format(date));
            hmDes.put("AdminEditBy", emp.getEmployeeID().toString());
            hmDes.put("AdminEditReason", "Change Customer's Information");
            customer.getCustomerInfo().setDescriptions(XAD.createDescriptions(hmDes));
            CF.edit(customer);
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            externalContext.getSessionMap().remove("cre_cus_password");
            externalContext.getSessionMap().remove("cre_cus_confirmPassword");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Update Success"));
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Update Fail"));
        }
    }

}
