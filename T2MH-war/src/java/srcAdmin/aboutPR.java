/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package srcAdmin;

import Entity.AdminRole;
import Entity.Employee;
import Entity.ProductCommentMark;
import Entity.ProductReview;
import Entity.ProductReviewComment;
import SessionBean.EmployeeFacadeLocal;
import SessionBean.ProductCommentMarkFacadeLocal;
import SessionBean.ProductReviewCommentFacadeLocal;
import SessionBean.ProductReviewFacadeLocal;
import XMLAccess.XmlAccessDescriptions;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
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
import javax.servlet.RequestDispatcher;

/**
 *
 * @author 19319
 */
@Named(value = "aboutBR")

@SessionScoped
@ManagedBean
public class aboutPR implements Serializable {

    @EJB
    private ProductReviewCommentFacadeLocal PRCF;

    @EJB
    private ProductReviewFacadeLocal PRF;

    @EJB
    private ProductCommentMarkFacadeLocal PCMF;

    @EJB
    private EmployeeFacadeLocal EF;

    private String baseURL = "";
    private ProductReview pr;
    private String originalURL = "";
    private boolean isAllowed = false;
    private boolean isNameExists = false;
    private XmlAccessDescriptions XAD = new XmlAccessDescriptions();
    //getter and setter

    public ProductReview getPr() {
        return pr;
    }

    public void setPr(ProductReview pr) {
        this.pr = pr;
    }

    public boolean isIsNameExists() {
        return isNameExists;
    }

    public void setIsNameExists(boolean isNameExists) {
        this.isNameExists = isNameExists;
    }

    //end getter and setter
    public aboutPR() {
    }

    @PostConstruct
    public void init() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        originalURL = (String) externalContext.getRequestMap().get(RequestDispatcher.FORWARD_REQUEST_URI);
        pr = new ProductReview();
        baseURL = externalContext.getRequestContextPath();
        if (originalURL == null) {
            originalURL = externalContext.getRequestContextPath() + "/Admin/PR/Index.xhtml";
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

    //////////////////////////////////////////////////////////////////////////////
    public void loadDetails() {
        isAllowed("Role03");
        try {
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            if (externalContext.getRequestParameterMap().get("PRID") != null) {
                pr = PRF.find(Integer.valueOf(externalContext.getRequestParameterMap().get("PRID")));
                externalContext.getSessionMap().put("details_PR", pr.getPrid());
            } else {
                pr = PRF.find(Integer.valueOf(externalContext.getSessionMap().get("details_PR").toString()));
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Can't Load Info"));
        }
    }

    public void delete(int PRID) {
//        PRCF ProductReviewCommentFacadeLocal
//        PCMF ProductCommentMarkFacadeLocal
//        PRF
        pr = PRF.find(PRID);
        //remove mark
        Collection<ProductCommentMark> pcm = pr.getProductCommentMarkCollection();
        for (Iterator<ProductCommentMark> iterator = pcm.iterator(); iterator.hasNext();) {
            ProductCommentMark next = iterator.next();
            PCMF.remove(next);
        }
        //remove comment
        Collection<ProductReviewComment> prc = pr.getProductReviewCommentCollection();
        for (Iterator<ProductReviewComment> iterator = prc.iterator(); iterator.hasNext();) {
            ProductReviewComment next = iterator.next();
            PRCF.remove(next);
        }
        //remove product review
        PRF.remove(pr);
    }
}
