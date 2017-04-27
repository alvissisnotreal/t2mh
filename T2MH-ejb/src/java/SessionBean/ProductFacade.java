/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SessionBean;

import Entity.Product;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.ejb.Stateless;
import javax.faces.bean.RequestScoped;
import javax.persistence.Cacheable;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.xml.xpath.XPathConstants;
import org.w3c.dom.Node;

/**
 *
 * @author 19319
 */
import XMLAccess.XmlAccessDescriptions;
import org.w3c.dom.Document;

@Stateless
public class ProductFacade extends AbstractFacade<Product> implements ProductFacadeLocal {

    @PersistenceContext(unitName = "T2MH-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    private XmlAccessDescriptions XAD;

    public ProductFacade() {
        super(Product.class);
    }

    @Override
    public void refresh() {
        em.getEntityManagerFactory().getCache().evictAll();
    }

    @Override
    public Product findProductAfterCreate(int branchID, String generateCode) {
        this.refresh();
        try {
            Query query = em.createQuery("SELECT P FROM Product AS P WHERE P.branchID.branchID = :branchID and P.descriptions = :generateCode");
            query.setParameter("branchID", branchID);
            query.setParameter("generateCode", generateCode);
            List<Product> list = query.getResultList();

            if (list.size() == 1) {
                return list.get(0);
            } else {
                query = em.createQuery("SELECT P FROM Product AS P WHERE P.branchID.branchID = :branchID");
                query.setParameter("branchID", branchID);
                list = query.getResultList();
                Product product = list.get(list.size() - 1);
                if (product == null) {
                    query = em.createQuery("SELECT P FROM Product AS P WHERE P.descriptions = :generateCode");
                    query.setParameter("descriptions", generateCode);
                    product = (Product) query.getSingleResult();
                    return product;
                }
                return product;
            }
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public List<Product> findAllProductActive() {
        try {
            Query query = em.createQuery("SELECT p FROM Product AS p WHERE p.productStatus = 1 AND p.branchID.branchStatus = 1 AND p.groupID.groupStatus = 1 AND p.groupID.cateID.cateStatus = 1");
            List<Product> list = query.getResultList();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Product getProductNeedApprova() {
        try {
            Query query = em.createQuery("SELECT p FROM Product AS p WHERE p.productStatus = :pStatus");
            query.setParameter("pStatus", -1);
            Product product = (Product) query.setMaxResults(1).getSingleResult();
            return product;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean productIsActive(int productID) {
        try {
            Query query = em.createQuery("SELECT p FROM Product AS p WHERE p.productStatus = 1 AND p.branchID.branchStatus = 1 AND p.groupID.groupStatus = 1 AND p.groupID.cateID.cateStatus = 1 AND p.productID = :productID");
            query.setParameter("productID", productID);
            Product product = (Product) query.getSingleResult();
            if (product != null) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<Product> findProductByNameLike(String searchString) {
        Query query = em.createQuery("SELECT P FROM Product AS P WHERE P.productName LIKE :searchString AND P.productStatus = 1 ORDER BY P.productID DESC");
        query.setParameter("searchString", "%" + searchString + "%");
        query.setMaxResults(200);
        List<Product> listProduct = query.getResultList();
        return listProduct;
    }

    @Override
    public List<Product> findProductByTag(String searchString) {
        Query query = em.createQuery("SELECT p FROM Product AS p WHERE p.productStatus = 1 AND p.branchID.branchStatus = 1 AND p.groupID.groupStatus = 1 AND p.groupID.cateID.cateStatus = 1 ORDER BY P.productID DESC");
        final int resultsPerGet = 500;
        int resultPage = 0;
        boolean stillGet = true;

        List<Product> listProduct = new ArrayList<>();
        XAD = new XmlAccessDescriptions();
        while (stillGet) {
            List<Product> listOrderGet = new ArrayList<>();
            query.setFirstResult(resultPage * resultsPerGet);
            query.setMaxResults(resultsPerGet);
            listOrderGet.addAll(query.getResultList());
            for (Iterator<Product> iterator = listOrderGet.iterator(); iterator.hasNext();) {
                try {
                    Product product = iterator.next();
                    //filter tag value
                    javax.xml.xpath.XPath xp = javax.xml.xpath.XPathFactory.newInstance().newXPath();
                    List<String> listTag=XAD.getListContent(product.getDescriptions(), "Tags");
                    boolean isExistsTag=false;
                    for (int i = 0; i < listTag.size(); i++) {
                        if(listTag.get(i).equalsIgnoreCase(searchString.trim()))
                        {
                            isExistsTag=true;
                            break;
                        }
                    }
                    if (isExistsTag)//tag exists
                    {
                        listProduct.add(product);
                    }
                    
                } catch (Exception e) {
                }
            }
            if (listOrderGet.size() < resultsPerGet) {
                stillGet = false;
            }
            if (listProduct.size() >= 200) {
                stillGet = false;
            }
            //break while
            resultPage++;
        }
        return listProduct;
    }

}
