/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SessionBean;

import Entity.Category;
import Entity.OrderInfo;
import Entity.Orders;
import Entity.Product;
import XMLAccess.XmlAccessDescriptions;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author 19319
 */
@Stateless
public class CategoryFacade extends AbstractFacade<Category> implements CategoryFacadeLocal {

    @EJB
    private OrdersFacadeLocal ordersF;

    @EJB
    private ProductFacadeLocal productF;

    @PersistenceContext(unitName = "T2MH-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    private XmlAccessDescriptions XAD;

    public CategoryFacade() {
        super(Category.class);
    }

    @Override
    public void refresh() {
        em.getEntityManagerFactory().getCache().evictAll();
    }

    @Override
    public boolean isNameExists(String cateName) {
        this.refresh();
        Query query = em.createNamedQuery("Category.findByCateName");
        query.setParameter("cateName", cateName);
        List<Category> listObject = new ArrayList<Category>();
        listObject = query.getResultList();
        if (listObject.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isNameExistsForUpdate(String cateID, String cateName) {
        this.refresh();
        Query query = em.createNamedQuery("Category.findByCateName");
        query.setParameter("cateName", cateName);
        List<Category> listObject = new ArrayList<Category>();
        listObject = query.getResultList();
        if (listObject.size() > 0) {
            Category cate = (Category) listObject.get(0);
            if (cate.getCateID().toString().equals(cateID)) {
                return false;
            } else {
                return true;
            }

        } else {
            return false;
        }
    }

    @Override
    public Category getCateByName(String cateName) {
        this.refresh();
        Query query = em.createNamedQuery("Category.findByCateName");
        query.setParameter("cateName", cateName);
        List<Category> listObject = new ArrayList<Category>();
        listObject = query.getResultList();
        if (listObject.size() > 0) {
            return listObject.get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<Category> findAllCateActive() {
        try {
            Query query = em.createQuery("SELECT c FROM Category c WHERE c.cateStatus = :cateStatus");
            query.setParameter("cateStatus", 1);
            query.setMaxResults(6);
            List<Category> lisCategory = query.getResultList();
            return lisCategory;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public HashMap<Integer, Long> getHotCategory(int[] listChooseCategory, String typeReport, int branchID, Date dateStart, Date dateEnd) {
        this.refresh();
        productF.refresh();
        ordersF.refresh();
        //change dateEnd to end of the date
        Calendar calendarEndOfDate = Calendar.getInstance();
        calendarEndOfDate.setTime(dateEnd);
        calendarEndOfDate.set(Calendar.HOUR_OF_DAY, 23);
        calendarEndOfDate.set(Calendar.MINUTE, 59);
        calendarEndOfDate.set(Calendar.SECOND, 59);
        dateEnd = calendarEndOfDate.getTime();
        System.out.println("DateStart: " + dateStart);
        System.out.println("DateEnd: " + dateEnd);

        javax.xml.xpath.XPath xp = javax.xml.xpath.XPathFactory.newInstance().newXPath();
        String xquery = "//Descriptions/Products";
        String xqueryGetListProduct = "//Products/Product";
        XAD = new XmlAccessDescriptions();
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        HashMap<Integer, Long> hmHot = new HashMap();//key = category ID, value= total amount of product sold
        Query query;
        if (typeReport.equalsIgnoreCase("system")) {
            query = em.createQuery("SELECT O FROM Orders AS O WHERE O.orderStatusID.orderStatusID = 4 ORDER BY O.orderID DESC");
        } else {
            query = em.createQuery("SELECT O FROM Orders AS O, OrderInfo AS OI WHERE O.orderStatusID.orderStatusID = 4 AND OI.product.branchID.branchID = :branchID AND OI.orders.orderID = O.orderID ORDER BY O.orderID DESC");
            query.setParameter("branchID", branchID);
        }
        final int resultsPerGet = 500;
        int resultPage = 0;
        boolean stillGet = true;
        while (stillGet) {
            List<Orders> listOrdersGet = new ArrayList<>();
            query.setFirstResult(resultPage * resultsPerGet);
            query.setMaxResults(resultsPerGet);
            listOrdersGet.addAll(query.getResultList());
            for (Iterator<Orders> iterator = listOrdersGet.iterator(); iterator.hasNext();) {
                Orders aOrder = iterator.next();
                try {
                    Date date = formatter.parse(aOrder.getTimeDelivery());
                    if (date.compareTo(dateStart) >= 0 && date.compareTo(dateEnd) <= 0)//accept this order in this time
                    {
                        Node node = (Node) xp.evaluate(xquery, XAD.stringToDocument(aOrder.getDescriptions()), XPathConstants.NODE);
                        NodeList nodeList = (NodeList) xp.evaluate(xqueryGetListProduct, XAD.stringToDocument("<Products>" + node.getTextContent() + "</Products>"), XPathConstants.NODESET);
                        for (int i = 0; i < nodeList.getLength(); i++) {
                            Element eleProduct = (Element) nodeList.item(i);
                            Integer productID = Integer.valueOf(eleProduct.getElementsByTagName("ProductID").item(0).getTextContent());
                            Long productValue = Long.valueOf(eleProduct.getElementsByTagName("ValuesProduct").item(0).getTextContent());
                            int CategoryID = productF.find(productID).getGroupID().getCateID().getCateID();
                            boolean containsCategory = false;
                            for (int j = 0; j < listChooseCategory.length; j++) {
                                if (listChooseCategory[j] == CategoryID) {
                                    containsCategory = true;
                                    break;
                                }
                            }
                            if (containsCategory == false) {
                                continue;
                            }
                            if (hmHot.containsKey(CategoryID)) {
                                hmHot.put(CategoryID, hmHot.get(CategoryID) + productValue);
                            } else {
                                hmHot.put(CategoryID, productValue);
                            }
                        }
                    }
                } catch (ParseException ex) {
                    Logger.getLogger(CategoryFacade.class.getName()).log(Level.SEVERE, null, ex);
                } catch (XPathExpressionException ex) {
                    Logger.getLogger(CategoryFacade.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (listOrdersGet.size() < resultsPerGet) {
                stillGet = false;
            }
            //break while
            resultPage++;
        }
        return hmHot;
    }

    public HashMap<Integer, Long> getReport(int[] listChooseCategory, int[] listChooseGroups, String typeReport, String typeOutput, String typeCaculator, int branchID, Date dateStart, Date dateEnd) {
        this.refresh();
        productF.refresh();
        ordersF.refresh();
        //change dateEnd to end of the date
        Calendar calendarEndOfDate = Calendar.getInstance();
        calendarEndOfDate.setTime(dateEnd);
        calendarEndOfDate.set(Calendar.HOUR_OF_DAY, 23);
        calendarEndOfDate.set(Calendar.MINUTE, 59);
        calendarEndOfDate.set(Calendar.SECOND, 59);
        dateEnd = calendarEndOfDate.getTime();
        System.out.println("DateStart: " + dateStart);
        System.out.println("DateEnd: " + dateEnd);

        javax.xml.xpath.XPath xp = javax.xml.xpath.XPathFactory.newInstance().newXPath();
        String xquery = "//Descriptions/Products";
        String xqueryGetListProduct = "//Products/Product";
        XAD = new XmlAccessDescriptions();
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        HashMap<Integer, Long> hmReturn = new HashMap();//key = category ID, value= total amount of product sold
        Query query;
        if (typeReport.equalsIgnoreCase("system")) {
            query = em.createQuery("SELECT O FROM Orders AS O WHERE O.orderStatusID.orderStatusID = 4 ORDER BY O.orderID DESC");
        } else {
            query = em.createQuery("SELECT O FROM Orders AS O, OrderInfo AS OI WHERE O.orderStatusID.orderStatusID = 4 AND OI.product.branchID.branchID = :branchID AND OI.orders.orderID = O.orderID ORDER BY O.orderID DESC");
            query.setParameter("branchID", branchID);
        }
        final int resultsPerGet = 500;
        int resultPage = 0;
        boolean stillGet = true;
        while (stillGet) {
            List<Orders> listOrdersGet = new ArrayList<>();
            query.setFirstResult(resultPage * resultsPerGet);
            query.setMaxResults(resultsPerGet);
            listOrdersGet.addAll(query.getResultList());
            for (Iterator<Orders> iterator = listOrdersGet.iterator(); iterator.hasNext();) {
                Orders aOrder = iterator.next();
                try {
                    //filter date
                    Date date = formatter.parse(aOrder.getTimeDelivery());
                    if (date.compareTo(dateStart) >= 0 && date.compareTo(dateEnd) <= 0)//accept this order in this time
                    {
                        Node node = (Node) xp.evaluate(xquery, XAD.stringToDocument(aOrder.getDescriptions()), XPathConstants.NODE);
                        NodeList nodeList = (NodeList) xp.evaluate(xqueryGetListProduct, XAD.stringToDocument("<Products>" + node.getTextContent() + "</Products>"), XPathConstants.NODESET);
                        for (int i = 0; i < nodeList.getLength(); i++) {
                            Element eleProduct = (Element) nodeList.item(i);
                            Integer productID = Integer.valueOf(eleProduct.getElementsByTagName("ProductID").item(0).getTextContent());
                            Long productValue = Long.valueOf(eleProduct.getElementsByTagName("ValuesProduct").item(0).getTextContent());
                            Product product = productF.find(productID);

                            //test groups(groups choose, ofcourse=> cate choose)
                            if (typeCaculator.equalsIgnoreCase("yes")) {
                                int groupID = product.getGroupID().getGroupID();
                                boolean containsGroups = false;
                                for (int j = 0; j < listChooseGroups.length; j++) {
                                    if (listChooseGroups[j] == groupID) {
                                        containsGroups = true;
                                        break;
                                    }
                                }
                                if (containsGroups == true) {
                                    //test output
                                    if (typeOutput.equalsIgnoreCase("group")) {
                                        if (hmReturn.containsKey(groupID)) {
                                            hmReturn.put(groupID, hmReturn.get(groupID) + productValue);
                                        } else {
                                            hmReturn.put(groupID, productValue);
                                        }
                                    } else {
                                        int categoryID = product.getGroupID().getCateID().getCateID();
                                        if (hmReturn.containsKey(categoryID)) {
                                            hmReturn.put(categoryID, hmReturn.get(categoryID) + productValue);
                                        } else {
                                            hmReturn.put(categoryID, productValue);
                                        }
                                    }
                                }
                            } else {//calculator all groups=> only test category

                                //test Cate Of product Is Exists In List Choose CateId
                                int CategoryID = product.getGroupID().getCateID().getCateID();
                                boolean containsCategory = false;
                                for (int j = 0; j < listChooseCategory.length; j++) {
                                    if (listChooseCategory[j] == CategoryID) {
                                        containsCategory = true;
                                        break;
                                    }
                                }
                                if (containsCategory == true) {
                                    if (hmReturn.containsKey(CategoryID)) {
                                        hmReturn.put(CategoryID, hmReturn.get(CategoryID) + productValue);
                                    } else {
                                        hmReturn.put(CategoryID, productValue);
                                    }
                                }
                            }
                        }
                    }
                } catch (ParseException ex) {
                    Logger.getLogger(CategoryFacade.class.getName()).log(Level.SEVERE, null, ex);
                } catch (XPathExpressionException ex) {
                    Logger.getLogger(CategoryFacade.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (listOrdersGet.size() < resultsPerGet) {
                stillGet = false;
            }
            //break while
            resultPage++;
        }
        return hmReturn;
    }

    public void persist(Object object) {
        em.persist(object);
    }
}
