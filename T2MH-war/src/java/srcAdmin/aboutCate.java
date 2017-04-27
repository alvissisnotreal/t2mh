/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package srcAdmin;

import Entity.AdminRole;
import Entity.Category;
import Entity.Employee;
import Entity.Groups;
import Entity.Product;
import SessionBean.CategoryFacadeLocal;
import SessionBean.EmployeeFacadeLocal;
import SessionBean.GroupsFacadeLocal;
import SessionBean.ProductFacadeLocal;
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
import javax.xml.xpath.XPathExpressionException;
import org.w3c.dom.Document;

/**
 *
 * @author 19319
 */
@Named(value = "aboutCate")

@SessionScoped
@ManagedBean
public class aboutCate {

    @EJB
    private ProductFacadeLocal PF;

    @EJB
    private GroupsFacadeLocal GF;

    @EJB
    private CategoryFacadeLocal cateF;

    @EJB
    private EmployeeFacadeLocal EF;

    private String baseURL = "";
    private Category cate = new Category();
    private String originalURL = "";
    private boolean isAllowed = false;
    private boolean isNameExists = false;
    private int loadStatus;
    private String description;
    private List<Category> allCate;
    private HashMap<String, String> hmDes;
    private XmlAccessDescriptions XAD = new XmlAccessDescriptions();
    //getter and setter

    public HashMap<String, String> getHmDes() {
        return hmDes;
    }

    public void setHmDes(HashMap<String, String> hmDes) {
        this.hmDes = hmDes;
    }

    
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Category> getAllCate() {
        return allCate;
    }

    public void setAllCate(List<Category> allCate) {
        this.allCate = allCate;
    }

    public CategoryFacadeLocal getCateF() {
        return cateF;
    }

    public void setCateF(CategoryFacadeLocal cateF) {
        this.cateF = cateF;
    }

    public Category getCate() {
        return cate;
    }

    public void setCate(Category cate) {
        this.cate = cate;
    }

    public boolean isIsNameExists() {
        return isNameExists;
    }

    public void setIsNameExists(boolean isNameExists) {
        this.isNameExists = isNameExists;
    }

    //end getter and setter
    public aboutCate() {
    }

    @PostConstruct
    public void init() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        originalURL = (String) externalContext.getRequestMap().get(RequestDispatcher.FORWARD_REQUEST_URI);
        allCate = cateF.findAll();
        cate = new Category();
        hmDes = new HashMap<String, String>();
        baseURL = externalContext.getRequestContextPath();
        if (originalURL == null) {
            originalURL = externalContext.getRequestContextPath() + "/Admin/Category/Index.xhtml";
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

    public void checkName() {
        if (cateF.isNameExists(cate.getCateName())) {
            isNameExists = true;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Category Name Is Exists"));
        } else {
            isNameExists = false;
        }
    }

    public void checkNameForUpdate() {
        try {
            if (cateF.isNameExistsForUpdate(cate.getCateID().toString(), cate.getCateName())) {
                isNameExists = true;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Category Name is Exists"));
            } else {
                isNameExists = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void prepareCreate()
    {
        allCate = cateF.findAll();
        cate = new Category();
        hmDes = new HashMap<String, String>();
    }
    public void createCate() throws IOException {
        isAllowed("Role03");
        checkName();
        if (isNameExists == true) {
            return;
        }
        cate.setCateID(0);
        cate.setCateDescriptions(XAD.createDescriptions(hmDes));
        cateF.create(cate);
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        externalContext.getSessionMap().put("details_Cate", cateF.getCateByName(cate.getCateName()).getCateID());
        externalContext.redirect(baseURL + "/Admin/Category/detailsCate.xhtml");
    }

    //////////////////////////////////////////////////////////////////////////////
    public void loadDetails() {
        isAllowed("Role03");
        try {
            cateF.refresh();
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            if (externalContext.getRequestParameterMap().get("cateID") != null) {
                cate = cateF.find(Integer.valueOf(externalContext.getRequestParameterMap().get("cateID")));
                externalContext.getSessionMap().put("details_Cate", cate.getCateID());
            } else {
                cate = cateF.find(Integer.valueOf(externalContext.getSessionMap().get("details_Cate").toString()));
            }
            loadStatus = cate.getCateStatus();
            hmDes=XAD.convertDocumentToHashMap(XAD.stringToDocument(cate.getCateDescriptions()));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Can't Load Info"));
        }
    }

    public void updateCate() throws IOException {
        try {
            isAllowed("Role03");
            checkNameForUpdate();
            if (isNameExists == true) {
                return;
            }
            //loadStatus==false && isEnable==false? nothing change
            //loadStatus==false && isEnable==true? change
            //loadStauts==true && isEnable==true? nothing change
            //loadStatus==true && isEnable==false? change
            if (cate.getCateStatus() != loadStatus) {
                clickStatus(cate.getCateID().toString(), "update");
            }
            cate.setCateDescriptions(XAD.createDescriptions(hmDes));
            cateF.edit(cate);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Update Success"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Update Fail"));
        }
    }

    public void clickStatus(String id, String action) {
        isAllowed("Role03");
        try {
            cateF.refresh();
            cate = cateF.find(Integer.valueOf(id));
            //cate do not enable
            if (cate.getCateStatus() != 1) {
                cate.setCateStatus(1);
            } else {
                cate.setCateStatus(0);
            }
            cateF.edit(cate);
            loadStatus = cate.getCateStatus();
            if (action.equalsIgnoreCase("click")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Update Complete"));
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Update Fail"));
        }
    }
}
