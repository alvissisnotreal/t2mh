/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package srcAdmin;

import Entity.Employee;
import Entity.EmployeeInfo;
import Entity.AdminRole;
import SessionBean.AdminRoleFacadeLocal;
import SessionBean.EmployeeFacadeLocal;
import SessionBean.EmployeeInfoFacadeLocal;
import XMLAccess.XmlAccessDescriptions;
import java.io.IOException;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
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

/**
 *
 * @author 19319
 */
@Named(value = "aboutEmployee")

@SessionScoped
@ManagedBean
public class aboutEmployee implements Serializable {

    @EJB
    private AdminRoleFacadeLocal ARF;

    @EJB
    private EmployeeFacadeLocal EF;

    @EJB
    private EmployeeInfoFacadeLocal EIF;

    private Employee employee;
    private EmployeeInfo employeeInfo;
    private String email;
    private String phone;
    private String name;
    private String baseURL = "";
    private List<AdminRole> listAllRole;
    private List<AdminRole> roleList;
    private String[] empRole;
    private String username = "";
    private String password = "";
    private String confirmPassword = "";
    private String currentPassword;
    private String currentConfirm;
    private String originalURL = "";
    private List<Employee> listEmpChoose;
    private List<Employee> listEmpToShow;
    private boolean isAllowed = false;
    private boolean isNameExists = false;
    private boolean isValidEmail = true;
    private boolean isValidPassword = true;
    private boolean isEnable = true;
    private HashMap<String, String> hmDes;
    private XmlAccessDescriptions XAD;

    //getter and setter
    public EmployeeInfo getEmployeeInfo() {
        return employeeInfo;
    }

    public void setEmployeeInfo(EmployeeInfo employeeInfo) {
        this.employeeInfo = employeeInfo;
    }

    public List<AdminRole> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<AdminRole> roleList) {
        this.roleList = roleList;
    }

    public String getBaseURL() {
        return baseURL;
    }

    public void setBaseURL(String baseURL) {
        this.baseURL = baseURL;
    }

    public List<Employee> getListEmpToShow() {
        return listEmpToShow;
    }

    public void setListEmpToShow(List<Employee> listEmpToShow) {
        this.listEmpToShow = listEmpToShow;
    }

    public List<Employee> getListEmpChoose() {
        return listEmpChoose;
    }

    public void setListEmpChoose(List<Employee> listEmpChoose) {
        this.listEmpChoose = listEmpChoose;
    }

    public String[] getEmpRole() {
        return empRole;
    }

    public void setEmpRole(String[] empRole) {
        this.empRole = empRole;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<AdminRole> getListAllRole() {
        return listAllRole;
    }

    public void setListAllRole(List<AdminRole> listAllRole) {
        this.listAllRole = listAllRole;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getCurrentConfirm() {
        return currentConfirm;
    }

    public void setCurrentConfirm(String currentConfirm) {
        this.currentConfirm = currentConfirm;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String Email) {
        this.email = Email;
    }

    public boolean isIsEnable() {
        return isEnable;
    }

    public void setIsEnable(boolean isEnable) {
        this.isEnable = isEnable;
    }

    public boolean isIsNameExists() {
        return isNameExists;
    }

    public boolean isIsValidEmail() {
        return isValidEmail;
    }

    public void setIsValidEmail(boolean isValidEmail) {
        this.isValidEmail = isValidEmail;
    }

    public void setIsNameExists(boolean isNameExists) {
        this.isNameExists = isNameExists;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public HashMap<String, String> getHmDes() {
        return hmDes;
    }

    public void setHmDes(HashMap<String, String> hmDes) {
        this.hmDes = hmDes;
    }

    //end getter and setter
    @PostConstruct
    public void init() {
        String currentURL = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        listAllRole = ARF.findAll();
        if (externalContext.getSessionMap().get("details_Employee") == null) {
            employee = new Employee();
            employeeInfo = new EmployeeInfo();
        } else {
            try {
                employee = EF.find(externalContext.getSessionMap().get("details_Employee"));
                employeeInfo = employee.getEmployeeInfo();
            } catch (Exception e) {
            }
        }
        XAD = new XmlAccessDescriptions();
        hmDes = new HashMap<String, String>();
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

    ///////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////
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
        if (EF.findEmpByUsername(employee.getEmployeeUsername()) != null && employee.getEmployeeUsername().length() >= 5) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Username Is Exists"));
            isNameExists = true;
        } else {
            isNameExists = false;
        }
    }

    public void checkUsernameForUpdate() {
        isAllowed("Role00");
        try {
            if (EF.isNameExistsForUpdate(employee.getEmployeeID().toString(), employee.getEmployeeUsername())) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Username is Exists"));
                isNameExists = true;
            } else {
                isNameExists = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void passwordChangeListener(ValueChangeEvent event) {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        externalContext.getSessionMap().put("emp_password", (String) event.getNewValue());
    }

    public void confirmPasswordChangeListener(ValueChangeEvent event) {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        externalContext.getSessionMap().put("emp_confirmPassword", (String) event.getNewValue());
    }

    public void checkConfirmPassword() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        try {
            password = externalContext.getSessionMap().get("emp_password").toString();
            confirmPassword = externalContext.getSessionMap().get("emp_confirmPassword").toString();
            if (password.equals(confirmPassword)) {
                isValidPassword = true;
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Confirm Password Is Incorrect"));
                isValidPassword = false;
            }
        } catch (Exception e) {
            if (externalContext.getSessionMap().get("emp_password") == null && externalContext.getSessionMap().get("emp_confirmPassword") == null) {
                isValidPassword = true;
                return;
            }
            if (externalContext.getSessionMap().get("emp_password") == null || externalContext.getSessionMap().get("emp_confirmPassword") == null) {
                isValidPassword = false;
                return;
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Confirm Password Is Incorrect"));
            isValidPassword = false;
        }
    }

    public void checkEmail() {
        try {
            if (employeeInfo.getEmployeeEmail() == null) {
                isValidEmail = true;
                return;
            }
            if (employeeInfo.getEmployeeEmail().matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")) {
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

    //////////////////////////////////////////////////////////////////////////////
    public void clickStatus(String id) {
        isAllowed("Role00");
        try {
            employee = EF.find(Integer.valueOf(id));
            if (employee.getEmployeeStatus() != 1) {
                employee.setEmployeeStatus(1);
            } else {
                employee.setEmployeeStatus(0);
            }
            EF.edit(employee);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Update Success"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Update Fail"));
        }
    }

    public void statusValueChange(ValueChangeEvent event) {
        try {
            clickStatus(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idClick"));
        } catch (Exception e) {
        }
    }

    public void createEmployee() throws IOException, NoSuchAlgorithmException {
        try {
            isAllowed("Role00");
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            checkUsername();
            checkConfirmPassword();
            checkEmail();
            if (isNameExists == true || isValidPassword == false || isValidEmail == false) {
                return;
            }
            if (password.trim().length() != password.length()) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Password Cannot Have Space Character At First Or Last"));
                return;
            }
            if (password != null && password.equals("") == false && confirmPassword != null && confirmPassword.equals("") == false) {
                employee.setEmployeePassword(encryptPassword(password));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Password Cannot Empty"));
                return;
            }
            employee.setEmployeePassword(encryptPassword(password));
            employee.setEmployeeID(0);
            if (empRole.length > 0) {
                roleList = new ArrayList<AdminRole>();
                for (int i = 0; i < empRole.length; i++) {
                    roleList.add(ARF.findByRoleID(empRole[i]));
                }
                employee.setAdminRoleCollection(roleList);
            }
//            employee.getEmployeeInfo().setEmployeeID(0);
            EF.create(employee);
            //edit inverseField
            employee = EF.findEmpByUsername(employee.getEmployeeUsername());
            employeeInfo.setEmployeeID(employee.getEmployeeID());

            //create Descriptions
            XmlAccessDescriptions XAD = new XmlAccessDescriptions();
            if (this.hmDes == null) {
                hmDes = new HashMap<String, String>();
            }

            employeeInfo.setEmployeeDescriptions(XAD.createDescriptions(hmDes));
            //end edit inverse Field

            employee.setEmployeeInfo(employeeInfo);
            EF.edit(employee);
            externalContext.getSessionMap().put("details_Employee", EF.findEmpByUsername(employee.getEmployeeUsername()).getEmployeeID());
            externalContext.redirect(baseURL + "/Admin/Employee/detailsEmployee.xhtml");
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Create Fail"));
        }
    }

    public void loadDetails() {
        isAllowed("Role00");
        try {
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            externalContext.getSessionMap().remove("emp_password");//remove session input for password
            externalContext.getSessionMap().remove("emp_confirmPassword");//remove session input for password
            if (externalContext.getRequestParameterMap().get("idEmp") != null) {
                employee = EF.find(Integer.valueOf(externalContext.getRequestParameterMap().get("idEmp")));
                externalContext.getSessionMap().put("details_Employee", employee.getEmployeeID());
            } else {
                employee = EF.find(Integer.valueOf(externalContext.getSessionMap().get("details_Employee").toString()));
            }

            employeeInfo = employee.getEmployeeInfo();
            roleList = (List<AdminRole>) employee.getAdminRoleCollection();
            empRole = new String[roleList.size()];
            for (int i = 0; i < roleList.size(); i++) {
                empRole[i] = roleList.get(i).getRoleID();
            }
            hmDes = XAD.convertDocumentToHashMap(XAD.stringToDocument(employeeInfo.getEmployeeDescriptions()));
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Can't load info"));
        }
    }

    public void updateEmployee() throws NoSuchAlgorithmException, IOException {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        try {
            //authorization
            isAllowed("Role00");

            //check validate
            {
                checkUsernameForUpdate();
                checkConfirmPassword();
                checkEmail();
                if (isNameExists == true || isValidPassword == false || isValidEmail == false) {
                    return;
                }
                if (password.trim().length() != password.length()) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Password Cannot Have Space Character At First Or Last"));
                    return;
                }
                if (password != null && !password.equals("") && password.length() >= 5) {
                    employee.setEmployeePassword(encryptPassword(password));
                }
            }

            //edit info and reverseField
            if (empRole.length > 0) {
                roleList = new ArrayList<AdminRole>();
                for (int i = 0; i < empRole.length; i++) {
                    roleList.add(ARF.findByRoleID(empRole[i]));
                }
                employee.setAdminRoleCollection(roleList);
            }
            //create Descriptions
            employeeInfo.setEmployeeDescriptions(XAD.createDescriptions(hmDes));
            
            //save info
            employee.setEmployeeInfo(employeeInfo);
            EF.edit(employee);

            externalContext.getSessionMap().put("details_Employee", EF.findEmpByUsername(employee.getEmployeeUsername()).getEmployeeID());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Update Success"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Update Fail"));
        }
    }

    public void reloadForCreate() {
        employeeInfo = new EmployeeInfo();
        employee = new Employee();
        hmDes = new HashMap<>();
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        externalContext.getSessionMap().remove("emp_password");
        externalContext.getSessionMap().remove("emp_confirmPassword");
    }
}
