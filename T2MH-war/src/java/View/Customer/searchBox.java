/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View.Customer;

import Entity.Branch;
import Entity.Product;
import SessionBean.BranchFacadeLocal;
import SessionBean.ProductFacadeLocal;
import XMLAccess.XmlAccessDescriptions;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.xml.xpath.XPathExpressionException;

/**
 *
 * @author 19319
 */
@Named(value = "searchBox")
@SessionScoped
@ManagedBean
public class searchBox implements Serializable {

    @EJB
    private BranchFacadeLocal branchF;

    @EJB
    private ProductFacadeLocal productF;

    
    
    private List<Product> listProduct;
    private String searchInput;
    private String baseURL;
    private XmlAccessDescriptions XAD;
    private Branch branch;
    private HashMap<String,String> hmDesBranch;
    public searchBox() {
    }

    //getter and setter
    public List<Product> getListProduct() {
        return listProduct;
    }

    public void setListProduct(List<Product> listProduct) {
        this.listProduct = listProduct;
    }

    public String getSearchInput() {
        return searchInput;
    }

    public void setSearchInput(String searchInput) {
        this.searchInput = searchInput;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public HashMap<String, String> getHmDesBranch() {
        return hmDesBranch;
    }

    public void setHmDesBranch(HashMap<String, String> hmDesBranch) {
        this.hmDesBranch = hmDesBranch;
    }

    
    
    //end getter and setter
    @PostConstruct
    public void init() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        searchInput = new String();
        listProduct = new ArrayList<>();
        baseURL = externalContext.getRequestContextPath();
        XAD=new XmlAccessDescriptions();
    }

    public void searchBox() throws IOException {
        try {
            listProduct = new ArrayList<>();
            productF.refresh();
            //searchById
            try {
                Product product = productF.find(Integer.valueOf(searchInput));
                if (product.getProductStatus() == 1) {
                    listProduct.add(product);
                }
            } catch (Exception e) {
            }
            //searchByName 
            if (searchInput.length() >= 2) {
                listProduct.addAll(productF.findProductByNameLike(searchInput));
            }
            //search by tag
            List<Product> listProductTag=productF.findProductByTag(searchInput);
            for (Iterator<Product> iterator = listProductTag.iterator(); iterator.hasNext();) {
                Product next = iterator.next();
                if(listProduct.contains(next)==false)
                {
                    listProduct.add(next);
                }
            }
            FacesContext.getCurrentInstance().getExternalContext().redirect(baseURL + "/Customer/resultSearchPage.xhtml");
            
        } catch (Exception e) {
            FacesContext.getCurrentInstance().getExternalContext().redirect(baseURL + "/index.xhtml");
        }
    }
    public String getAvatar(int ProductID) throws XPathExpressionException {
        return XAD.getAvatar(productF.find(ProductID).getDescriptions());
    }
    public boolean existsBrachNameLikeSearchString(String branchName)
    {
        branch=branchF.getBranchByName(branchName);
        if(branch!=null)
        {
            hmDesBranch = XAD.convertDocumentToHashMap(XAD.stringToDocument(branch.getBranchDescriptions()));
        }
        return branch !=null ? true : false;
    }
}
