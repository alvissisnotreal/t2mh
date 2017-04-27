/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package srcAdmin;

import Entity.AdminRole;
import Entity.Employee;
import Entity.EmployeeInfo;
import SessionBean.EmployeeFacadeLocal;
import SessionBean.EmployeeInfoFacadeLocal;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.inject.Named;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author 19319
 */
@Named(value = "adminLogin")

@SessionScoped
@ManagedBean
public class AdminLogin {

    @EJB
    private EmployeeFacadeLocal EF;

    private Employee employee;
    private String originalURL;
    private String baseURL;
    private String webURL;
    private String realPath;
    private boolean isLoggedEmployee = false;

    public String getWebURL() {
        return webURL;
    }

    //Getter and Setter
    public String getRealPath() {
        return realPath;
    }

    public void setRealPath(String realPath) {
        this.realPath = realPath;
    }

    public void setWebURL(String webURL) {
        this.webURL = webURL;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public String getBaseURL() {
        return baseURL;
    }

    public void setBaseURL(String baseURL) {
        this.baseURL = baseURL;
    }

    public boolean isIsLoggedEmployee() {
        return isLoggedEmployee;
    }

    public void setIsLoggedEmployee(boolean isLoggedEmployee) {
        this.isLoggedEmployee = isLoggedEmployee;
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
        realPath = externalContext.getRealPath("/");
        webURL = reWriteUrl(FacesContext.getCurrentInstance().getExternalContext().getRealPath(""), "T2MH");
        if (originalURL == null) {
            originalURL = externalContext.getRequestContextPath() + "/Admin/Index.xhtml";
        } else {
            String originalQuery = (String) externalContext.getRequestMap().get(RequestDispatcher.FORWARD_QUERY_STRING);

            if (originalQuery != null) {
                originalURL += "?" + originalQuery;
            }
        }
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

    public void reloadLogin() {
        employee = new Employee();
    }

    public void normalLogin() throws IOException, NoSuchAlgorithmException {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        //  HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
        EF.refresh();
        String username = employee.getEmployeeUsername();
        String password = employee.getEmployeePassword();
        employee = EF.findEmpByUsername(username);
        if (employee == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Username is not exists,<br/>Try again"));
            return;
        } else {
            if (employee.getEmployeeStatus() == 1) {
                if (employee != null && !encryptPassword(password).equals(employee.getEmployeePassword())) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Password incorrect,<br/>Try again"));

                    return;
                }
                if (employee != null && encryptPassword(password).equals(employee.getEmployeePassword())) {
                    externalContext.getSessionMap().put("LOGGED_ADMIN", employee);
                    isLoggedEmployee = true;
                    //Index page is contains all page with rendered with role :D
                    externalContext.getSessionMap().put("LOGGED_ADMIN", employee);
                    externalContext.redirect(baseURL + "/Admin/Index.xhtml");
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "This account has been locked."
                                                                                                        + "<br/>Please contact Manage Admin to resolve"));
                return;
            }
        }
    }

    public boolean isHadRole(String idAdminRole) {
        boolean isAllowed = false;
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
                if (aRole.getRoleID().equals("Role00") || aRole.getRoleID().equals(idAdminRole)) {
                    isAllowed = true;
                    break;
                }
            }
            if (isAllowed == false || emp.getEmployeeStatus() != 1) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public void logout() throws IOException {
//        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
//        isLoggedCustomer=false;
//        externalContext.invalidateSession();
//        externalContext.redirect(externalContext.getRequestContextPath() + "/Admin/Login.xhtml");

        //code below only remove logged_
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
        externalContext.getSessionMap().remove("LOGGED_ADMIN");
        externalContext.redirect(externalContext.getRequestContextPath() + "/Admin/Login.xhtml");
    }

    public boolean isAllowed(String role) {
        boolean isAllowed = false;
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
        } catch (Exception e) {
            try {
                e.printStackTrace();
                externalContext.redirect(baseURL + "/Admin/Error.xhtml");
            } catch (Exception ex) {
            }
        }
        return isAllowed;
    }
}
