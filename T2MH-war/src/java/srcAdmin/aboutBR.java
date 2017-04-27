/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package srcAdmin;

import Entity.AdminRole;
import Entity.BranchCommentMark;
import Entity.BranchReview;
import Entity.BranchReviewComment;
import Entity.Employee;
import SessionBean.BranchCommentMarkFacadeLocal;
import SessionBean.BranchReviewCommentFacadeLocal;
import SessionBean.BranchReviewFacadeLocal;
import SessionBean.EmployeeFacadeLocal;
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
import org.primefaces.component.datatable.DataTable;

/**
 *
 * @author 19319
 */
@Named(value = "aboutBR")

@SessionScoped
@ManagedBean
public class aboutBR implements Serializable{

    @EJB
    private BranchReviewCommentFacadeLocal BRCF;

    @EJB
    private BranchCommentMarkFacadeLocal BCMF;

    @EJB
    private BranchReviewFacadeLocal BRF;

    @EJB
    private EmployeeFacadeLocal EF;

    private String baseURL = "";
    private BranchReview br;
    private String originalURL = "";
    private boolean isAllowed = false;
    private boolean isNameExists = false;
    private XmlAccessDescriptions XAD = new XmlAccessDescriptions();
    //getter and setter

    public BranchReview getBr() {
        return br;
    }

    public void setBr(BranchReview br) {
        this.br = br;
    }

    public boolean isIsNameExists() {
        return isNameExists;
    }

    public void setIsNameExists(boolean isNameExists) {
        this.isNameExists = isNameExists;
    }

    //end getter and setter
    public aboutBR() {
    }

    @PostConstruct
    public void init() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        originalURL = (String) externalContext.getRequestMap().get(RequestDispatcher.FORWARD_REQUEST_URI);
        br = new BranchReview();
        baseURL = externalContext.getRequestContextPath();
        if (originalURL == null) {
            originalURL = externalContext.getRequestContextPath() + "/Admin/BR/Index.xhtml";
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
            if (externalContext.getRequestParameterMap().get("BRID") != null) {
                br = BRF.find(Integer.valueOf(externalContext.getRequestParameterMap().get("BRID")));
                externalContext.getSessionMap().put("details_BR", br.getBrid());
            } else {
                br = BRF.find(Integer.valueOf(externalContext.getSessionMap().get("details_BR").toString()));
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Cant Load Info"));
        }
    }

    public void delete(int BRID) {
//        BRCF BranchReviewCommentFacadeLocal
//        BCMF BranchCommentMarkFacadeLocal
//        BRF
        br = BRF.find(BRID);
        //remove mark
        Collection<BranchCommentMark> bcm = br.getBranchCommentMarkCollection();
        for (Iterator<BranchCommentMark> iterator = bcm.iterator(); iterator.hasNext();) {
            BranchCommentMark next = iterator.next();
            BCMF.remove(next);
        }
        //remove comment
        Collection<BranchReviewComment> brc = br.getBranchReviewCommentCollection();
        for (Iterator<BranchReviewComment> iterator = brc.iterator(); iterator.hasNext();) {
            BranchReviewComment next = iterator.next();
            BRCF.remove(next);
        }
        //remove branch review
        BRF.remove(br);
    }
}
