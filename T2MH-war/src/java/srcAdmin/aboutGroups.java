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
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.RequestDispatcher;
import javax.xml.xpath.XPathExpressionException;
import org.w3c.dom.Document;

/**
 *
 * @author 19319
 */
@Named(value = "aboutGroups")

@SessionScoped
@ManagedBean
public class aboutGroups {

    @EJB
    private ProductFacadeLocal PF;

    @EJB
    private GroupsFacadeLocal GF;

    @EJB
    private CategoryFacadeLocal cateF;

    @EJB
    private EmployeeFacadeLocal EF;

    private String baseURL = "";
    private Category cate;
    private String originalURL = "";
    private boolean isAllowed = false;
    private boolean isNameExists = false;
    private int loadStatus;
    private List<Category> allCate;
    private List<Groups> allGroups;
    private Groups groups;
    private String description;
    private XmlAccessDescriptions XAD = new XmlAccessDescriptions();
    private HashMap<String, String> hmDes;
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

    public Groups getGroups() {
        return groups;
    }

    public void setGroups(Groups groups) {
        this.groups = groups;
    }

    public List<Groups> getAllGroups() {
        return allGroups;
    }

    public void setAllGroups(List<Groups> allGroups) {
        this.allGroups = allGroups;
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
    public aboutGroups() {
    }

    @PostConstruct
    public void init() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        originalURL = (String) externalContext.getRequestMap().get(RequestDispatcher.FORWARD_REQUEST_URI);
        allCate = cateF.findAll();
        allGroups = GF.findAll();
        cate = new Category();
        groups = new Groups();
        hmDes = new HashMap<String, String>();
        baseURL = externalContext.getRequestContextPath();
        if (originalURL == null) {
            originalURL = externalContext.getRequestContextPath() + "/Admin/Groups/Index.xhtml";
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

    //all changedListener
    public void categorySelectionChanged() {
        checkNameForUpdate();
    }

    //
    public void checkName() {
        cateF.refresh();
        cate = cateF.find(cate.getCateID());
        if (GF.isNameExists(cate.getCateID(), groups.getGroupName())) {
            isNameExists = true;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Group Name Is Exists In\nCategory: " + cate.getCateName()));
        } else {
            isNameExists = false;
        }
    }

    public void checkNameForUpdate() {
        cateF.refresh();
        cate = cateF.find(cate.getCateID());
        if (GF.isNameExistsForUpdate(cate.getCateID(), groups.getGroupID(), groups.getGroupName())) {
            isNameExists = true;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Group Name Is Exists In\nCategory: " + cate.getCateName()));
        } else {
            isNameExists = false;
        }
    }

    public void createGroups() throws IOException {
        isAllowed("Role03");
        checkName();
        if (isNameExists == true) {
            return;
        }
        groups.setGroupID(0);
        groups.setCateID(cateF.find(cate.getCateID()));
        groups.setGroupDescriptions(XAD.createDescriptions(hmDes));
        GF.create(groups);
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        externalContext.getSessionMap().put("details_Group", GF.getGroupByName(groups.getGroupName()).getGroupID());
        externalContext.redirect(baseURL + "/Admin/Groups/detailsGroup.xhtml");
    }

    //////////////////////////////////////////////////////////////////////////////
    public void loadDetails() {
        isAllowed("Role03");
        try {
            GF.refresh();
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            if (externalContext.getRequestParameterMap().get("groupID") != null) {
                groups = GF.find(Integer.valueOf(externalContext.getRequestParameterMap().get("groupID")));
                externalContext.getSessionMap().put("details_Group", cate.getCateID());
            } else {
                groups = GF.find(Integer.valueOf(externalContext.getSessionMap().get("details_Group").toString()));
            }
            cate = groups.getCateID();
            //loadStatus for clickStatus and setStatusBefore
            loadStatus = groups.getGroupStatus();
            hmDes=XAD.convertDocumentToHashMap(XAD.stringToDocument(groups.getGroupDescriptions()));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Can't Load Info"));
        }
    }

    public void updateGroups() throws IOException {
        try {
            isAllowed("Role03");
            GF.refresh();
            checkNameForUpdate();
            if (isNameExists == true) {
                return;
            }
            //loadStatus==false && isEnable==false? nothing change
            //loadStatus==false && isEnable==true? change
            //loadStauts==true && isEnable==true? nothing change
            //loadStatus==true && isEnable==false? change
            if (groups.getGroupStatus() != loadStatus) {
                clickStatus(groups.getGroupID().toString(), "update");
            }
            groups.setCateID(cateF.find(cate.getCateID()));
            groups.setGroupDescriptions(XAD.createDescriptions(hmDes));
            GF.edit(groups);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Update Success"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Update Fail"));
        }
    }

    public void clickStatus(String id, String action) {
        isAllowed("Role03");
        try {
            GF.refresh();
            groups = GF.find(Integer.valueOf(id));
            //group do not enable
            if (groups.getGroupStatus() != 1) {
                groups.setGroupStatus(1);
            } else {
                groups.setGroupStatus(0);
            }
            GF.edit(groups);
            loadStatus = groups.getGroupStatus();
            if (action.equalsIgnoreCase("click")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Update Success"));
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Update Fail"));
        }
    }

//    private void turnStatusBefore(String action) {
//        try {
//            List<Product> listProduct;
//            Product product;
//            Document doc;
//            listProduct = (List<Product>) groups.getProductCollection();
//            if (action.equals("Disable")) {
//                if (groups.getGroupStatus() != 0) {
//                    for (Iterator<Product> iteratorProduct = listProduct.iterator(); iteratorProduct.hasNext();) {
//                        try {
//                            //edit product
//                            product = iteratorProduct.next();
//                            doc = XAD.stringToDocument(product.getDescriptions());
//                            HashMap<String, String> hashMapProductDes = XAD.convertDocumentToHashMap(doc);
//
//                            if (hashMapProductDes.containsKey("statusBefore") == false) {
//                                hashMapProductDes.put("statusBefore", product.getProductStatus().toString());
//                            }
//                            product.setDescriptions(XAD.createDescriptions(hashMapProductDes));
//                            product.setProductStatus(0);
//                            PF.edit(product);
//                        } catch (Exception ex) {
//                        }
//                    }
//                }
//            } else if (action.equals("Enable")) {
//                if (groups.getGroupStatus() == 0) {
//                    doc = XAD.stringToDocument(groups.getGroupDescriptions());
//                    HashMap<String, String> hashMapGroupsDes = XAD.convertDocumentToHashMap(doc);
//                    if(hashMapGroupsDes.containsKey("statusBefore"))
//                    {
//                        groups.setGroupStatus(Integer.valueOf(hashMapGroupsDes.get("statusBefore")));
//                    }
//                    for (Iterator<Product> iteratorProduct = listProduct.iterator(); iteratorProduct.hasNext();) {
//                        try {
//                            //edit product
//                            product = iteratorProduct.next();
//                            doc = XAD.stringToDocument(product.getDescriptions());
//                            HashMap<String, String> hashMapProductDes = XAD.convertDocumentToHashMap(doc);
//                            try {
//                                product.setProductStatus(Integer.valueOf(hashMapProductDes.get("statusBefore")));
//                                hashMapProductDes.remove("statusBefore");
//                            } catch (Exception e) {
//                                product.setProductStatus(1);
//                            }
//                            product.setDescriptions(XAD.createDescriptions(hashMapProductDes));
//                            PF.edit(product);
//
//                        } catch (Exception ex) {
//                        }
//                    }
//                }
//
//            }
//        } catch (Exception e) {
//        }
//    }
}
