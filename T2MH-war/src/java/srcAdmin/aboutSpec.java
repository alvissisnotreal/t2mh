/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package srcAdmin;

import Entity.AdminRole;
import Entity.Employee;
import Entity.EmployeeInfo;
import Entity.Product;
import Entity.Specifics;
import SessionBean.EmployeeFacadeLocal;
import SessionBean.SpecificsFacadeLocal;
import XMLAccess.XmlAccessDescriptions;
import java.io.IOException;
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
import javax.servlet.RequestDispatcher;
import org.w3c.dom.Document;

/**
 *
 * @author 19319
 */
@Named(value = "aboutSpec")

@SessionScoped
@ManagedBean
public class aboutSpec {

    @EJB
    private EmployeeFacadeLocal EF;

    @EJB
    private SpecificsFacadeLocal specF;

    private String baseURL = "";

    private Specifics spec = new Specifics();
    private String originalURL = "";
    private boolean isAllowed = false;
    private boolean isNameExists = false;
    private XmlAccessDescriptions XAD = new XmlAccessDescriptions();
    //getter and setter

    public Specifics getSpec() {
        return spec;
    }

    public void setSpec(Specifics spec) {
        this.spec = spec;
    }

    public boolean isIsNameExists() {
        return isNameExists;
    }

    public void setIsNameExists(boolean isNameExists) {
        this.isNameExists = isNameExists;
    }

    //end getter and setter
    public aboutSpec() {
    }

    @PostConstruct
    public void init() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        originalURL = (String) externalContext.getRequestMap().get(RequestDispatcher.FORWARD_REQUEST_URI);
        if (externalContext.getSessionMap().get("details_Spec") == null) {
            spec = new Specifics();
        } else {
            try {
                spec = specF.find(externalContext.getSessionMap().get("details_Spec"));
            } catch (Exception e) {
            }
        }
        baseURL = externalContext.getRequestContextPath();
        if (originalURL == null) {
            originalURL = externalContext.getRequestContextPath() + "/Admin/Specs/Index.xhtml";
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

    public void clickStatus(String id) {
        isAllowed("Role03");
        try {
            specF.refresh();
            spec = specF.find(Integer.valueOf(id));
            if (spec.getSpecStatus() != 1) {
                spec.setSpecStatus(1);
            } else {
                spec.setSpecStatus(0);
            }
            specF.edit(spec);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Update Success"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Update Fail"));
        }
    }

    public void checkName() {
        if (specF.isNameExists(spec.getSpecName()) && spec.getSpecName().length() >= 5) {
            isNameExists = true;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Spec Name Is Exists"));
        } else {
            isNameExists = false;
        }
    }

    public void checkNameForUpdate() {
        try {
            if (specF.isNameExistsForUpdate(spec.getSpecID().toString(), spec.getSpecName())) {
                isNameExists = true;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Name is Exists"));
            } else {
                isNameExists = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createSpec() throws IOException {
        isAllowed("Role03");
        checkName();
        if (isNameExists == true) {
            return;
        }
        spec.setSpecID(0);
        specF.create(spec);
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        externalContext.getSessionMap().put("details_Spec", specF.getSpecByName(spec.getSpecName()).getSpecID());
        externalContext.redirect(baseURL + "/Admin/Specs/detailsSpec.xhtml");
    }

    //////////////////////////////////////////////////////////////////////////////
    public void loadDetails() {
        isAllowed("Role03");
        try {
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            if (externalContext.getRequestParameterMap().get("idSpec") != null) {
                spec = specF.find(Integer.valueOf(externalContext.getRequestParameterMap().get("idSpec")));
                externalContext.getSessionMap().put("details_Spec", spec.getSpecID());
            } else {
                spec = specF.find(Integer.valueOf(externalContext.getSessionMap().get("details_Spec").toString()));
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Can't Load Info"));
        }
    }

    public void updateSpec() throws IOException {
        try {
            isAllowed("Role03");
            checkNameForUpdate();
            if (isNameExists == true) {
                return;
            }
            specF.edit(spec);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Update Success"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Update Fail"));
        }
    }
}
