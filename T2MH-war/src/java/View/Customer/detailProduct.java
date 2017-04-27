/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View.Customer;

import Entity.AdminRole;
import Entity.BranchCommentMark;
import Entity.BranchReview;
import Entity.BranchReviewComment;
import Entity.Category;
import Entity.Customer;
import Entity.CustomerInfo;
import Entity.Employee;

import Entity.Product;
import Entity.ProductCommentMark;
import Entity.ProductCommentMarkPK;
import static Entity.ProductCommentMark_.productReview;
import Entity.ProductReview;
import Entity.ProductReviewComment;
import Entity.Specifics;
import SessionBean.CustomerFacadeLocal;
import SessionBean.CustomerInfoFacadeLocal;
import SessionBean.ProductCommentMarkFacadeLocal;

import SessionBean.ProductFacadeLocal;
import SessionBean.ProductReviewCommentFacadeLocal;
import SessionBean.ProductReviewFacadeLocal;
import SessionBean.SpecificsFacadeLocal;
import static View.Customer.detailBranch.comparator;
import XMLAccess.XmlAccessDescriptions;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.xml.xpath.XPathExpressionException;
import org.w3c.dom.Document;

/**
 *
 * @author Huy-PC
 */
@Named(value = "detailProduct")
@SessionScoped
public class detailProduct implements Serializable {

    @EJB
    private CustomerInfoFacadeLocal customerInfoFacade;

    @EJB
    private CustomerFacadeLocal customerFacade;

    @EJB
    private ProductCommentMarkFacadeLocal productCommentMarkFacade;

    @EJB
    private ProductReviewCommentFacadeLocal productReviewCommentFacade;

    @EJB
    private ProductReviewFacadeLocal productReviewFacade;

    @EJB
    private SpecificsFacadeLocal specF;

    @EJB
    private ProductFacadeLocal productFacade;

    Category cate;
    String description, avatar, cateName, productName, branchName, Unit, groupName;
    float price, count;
    int rank, idProduct, rateCurrent;
    Product product;
    HashMap<String, String> hmDes;
    List<String> listImg;
    List<ProductReview> listReview;
    int inventory;
    HashMap<String, String> hmSpec;
    HashMap<String, String> hmCount;
    private List<Entry<String, String>> listSpec;
    private HashMap<String, String> fullDes = new HashMap<>();
    ShowHotProduct showpr = new ShowHotProduct();
    List<Integer> listPages = new ArrayList<>();
    private ProductReview productReview = new ProductReview();
    private ProductReviewComment productComment = new ProductReviewComment();
    private ProductCommentMark productCommentMark = new ProductCommentMark();
    Customer customer = new Customer();
    private Customer cutsomer = new Customer();
    private boolean isRated = false;
    private boolean isValidate = false;
    List<ProductReview> list;
    private int lastId;
    private ProductCommentMarkPK productCMPK = new ProductCommentMarkPK();
    private Employee employee = new Employee();
    private CustomerInfo customerInfo = new CustomerInfo();
    private boolean isValidname = false;

    public boolean isIsValidname() {
        return isValidname;
    }

    public void setIsValidname(boolean isValidname) {
        this.isValidname = isValidname;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public ProductCommentMarkPK getProductCMPK() {
        return productCMPK;
    }

    public void setProductCMPK(ProductCommentMarkPK productCMPK) {
        this.productCMPK = productCMPK;
    }

    public int getLastId() {
        return lastId;
    }

    public void setLastId(int lastId) {
        this.lastId = lastId;
    }

    public boolean isIsValidate() {
        return isValidate;
    }

    public List<Integer> getListPages() {
        return listPages;
    }

    public void setListPages(List<Integer> listPages) {
        this.listPages = listPages;
    }

    public void setIsValidate(boolean isValidate) {
        this.isValidate = isValidate;
    }

    public int getRateCurrent() {
        return rateCurrent;
    }

    public void setRateCurrent(int rateCurrent) {
        this.rateCurrent = rateCurrent;
    }

    public boolean isIsRated() {
        return isRated;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public void setIsRated(boolean isRated) {
        this.isRated = isRated;
    }

    public ProductReview getProductReview() {
        return productReview;
    }

    public void setProductReview(ProductReview productReview) {
        this.productReview = productReview;
    }

    public ProductReviewComment getProductComment() {
        return productComment;
    }

    public void setProductComment(ProductReviewComment productComment) {
        this.productComment = productComment;
    }

    public ProductCommentMark getProductCommentMark() {
        return productCommentMark;
    }

    public void setProductCommentMark(ProductCommentMark productCommentMark) {
        this.productCommentMark = productCommentMark;
    }

    private int branchID;
    private XMLAccess.XmlAccessDescriptions xml = new XmlAccessDescriptions();

    public XmlAccessDescriptions getXml() {
        return xml;
    }

    public void setXml(XmlAccessDescriptions xml) {
        this.xml = xml;
    }

    public int getBranchID() {
        return branchID;
    }

    public void setBranchID(int branchID) {
        this.branchID = branchID;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public HashMap<String, String> getHmSpec() {
        return hmSpec;
    }

    public ShowHotProduct getShowpr() {
        return showpr;
    }

    public void setShowpr(ShowHotProduct showpr) {
        this.showpr = showpr;
    }

//    public List<String> getListSpec() {
//        return listSpec;
//    }
//
//    public void setListSpec(List<String> listSpec) {
//        this.listSpec = listSpec;
//    }
    public List<Entry<String, String>> getListSpec() {
        return listSpec;
    }

    public void setListSpec(List<Entry<String, String>> listSpec) {
        this.listSpec = listSpec;
    }

    public void setHmSpec(HashMap<String, String> hmSpec) {
        this.hmSpec = hmSpec;
    }

    public int getInventory() {
        return inventory;
    }

    public void setInventory(int inventory) {
        this.inventory = inventory;
    }

    public List<ProductReview> getListReview() {
        return listReview;
    }

    public void setListReview(List<ProductReview> listReview) {
        this.listReview = listReview;
    }

    public List<String> getListImg() {
        return listImg;
    }

    public void setListImg(List<String> listImg) {
        this.listImg = listImg;
    }

    public String getCateName() {
        return cateName;
    }

    public void setCateName(String cateName) {
        this.cateName = cateName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Category getCate() {
        return cate;
    }

    public void setCate(Category cate) {
        this.cate = cate;
    }

    public String getDescription() {
        return description;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String Unit) {
        this.Unit = Unit;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public detailProduct() {

    }

    public HashMap<String, String> getHmDes() {
        return hmDes;
    }

    public void setHmDes(HashMap<String, String> hmDes) {
        this.hmDes = hmDes;
    }

    public detailProduct(Category cate, String description, String avatar, String cateName, ProductFacadeLocal productFacade) {
        this.cate = cate;

        this.description = description;
        this.avatar = avatar;
        this.cateName = cateName;
        this.productFacade = productFacade;
    }

    public float getCount() {
        return count;
    }

    public HashMap<String, String> getHmCount() {
        return hmCount;
    }

    public HashMap<String, String> getFullDes() {
        return fullDes;
    }

    @PostConstruct
    public void init() {
        rateCurrent = 0;
        comment = "";
        isExit = false;
        isValidate = false;
        isRated = false;
        rank = 0;
        isValidname = false;
    }

    public void detailProduct() throws XPathExpressionException {
        ExternalContext ec = FacesContext.getCurrentInstance()
                .getExternalContext();
        String productID = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("productID");
        product = new Product();
        customer = (Customer) ec.getSessionMap().get("LOGGED_CUSTOMER");
        review = "";
        commentRV = "";
        rateCurrent = 0;
        comment = "";
        ratting = 0;
        rateMark = 0;
        isExit = false;
        isValidate = false;
        isRated = false;
        rank = 0;
        isValidname = false;
        productComment = new ProductReviewComment();
        productComment.setComments("");
        productFacade.refresh();
        List<ProductReview> listReview = new ArrayList<ProductReview>();
        if (productID != null) {
            product = productFacade.find(Integer.parseInt(productID));
            ec.getSessionMap().put("product-detail", product.getProductID());
            ec.getSessionMap().put("isProduct", product.getProductID());
        } else {
            if (ec.getSessionMap().get("isProduct") != null) {
                String productid = ec.getSessionMap().get("isProduct").toString();
                product = productFacade.find(Integer.parseInt(productid));
            } else {
                productID = ec.getSessionMap().get("product-detail").toString();
                product = productFacade.find(Integer.parseInt(productID));
            }
        }
        hmDes = new HashMap<String, String>();
        listImg = new ArrayList<String>();
        listReview = new ArrayList<ProductReview>();
        hmCount = new HashMap<String, String>();
        int star, total1 = 0, total2 = 0, total3 = 0, total4 = 0, total5 = 0;
        XMLAccess.XmlAccessDescriptions xmlaccess = new XMLAccess.XmlAccessDescriptions();
        description = product.getDescriptions();
        avatar = xmlaccess.getAvatar(description);
        cateName = product.getGroupID().getCateID().getCateName();
        branchID = product.getBranchID().getBranchID();
        idProduct = product.getProductID();
        productName = product.getProductName();
        branchName = product.getBranchID().getBranchName();
        groupName = product.getGroupID().getGroupName();
        listImg = xmlaccess.getListContent(product.getDescriptions(), "Images");
        price = xmlaccess.getPrice(description);
        
       listReview = (List<ProductReview>) product.getProductReviewCollection();
        Collections.sort(listReview, comparator);
        for (int i = 0; i < listReview.size(); i++) {
            star = listReview.get(i).getPRStars();
            if (star == 5) {
                total5 = total5 + 1;
            }
            if (star == 4) {
                total4 = total4 + 1;
            }
            if (star == 3) {
                total3 = total3 + 1;
            }
            if (star == 2) {
                total2 = total2 + 1;

            }
            if (star == 1) {
                total1 = total1 + 1;

            }

        }
        if (listReview.size() <=0) {
            rank = 0;
        }
        else{
        rank = (Integer) (((total5 * 5) + (total4 * 4) + (total3 * 3) + (total2 * 2) + (total1)) / listReview.size());
        System.out.println("Rank: " + rank);
        rank = (Integer) ((Integer) Math.round((float) (rank * 10)) / 10);
        }
        inventory = product.getInventory();
        fullDes = xmlaccess.convertDocumentToHashMap(xmlaccess.stringToDocument(product.getDescriptions()));
        hmSpec = xmlaccess.getListSpecOfProductByStringSpec(fullDes.get("Specs"));
        HashMap<String, String> hmSpecNew = new HashMap<>();
        for (Entry<String, String> entry : hmSpec.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            hmSpecNew.put(specF.find(Integer.valueOf(key)).getSpecName(), value);
        }
        hmSpec = new HashMap<>();
        hmSpec.putAll(hmSpecNew);
        listSpec = new ArrayList<>(hmSpec.entrySet());
        for (Entry<String, String> entry : hmSpec.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            System.out.println("Key: " + key);
            System.out.println("Value: " + value);
        }

        Document doc = xmlaccess.stringToDocument(product.getDescriptions());
        hmCount = xmlaccess.convertDocumentToHashMap(doc);

        if (customer != null) {
            int cusID = customer.getCustomerID();
            for (int i = 0; i < listReview.size(); i++) {
                if (cusID == listReview.get(i).getCustomerID().getCustomerID() && idProduct == listReview.get(i).getProductID().getProductID()) {
                    rateCurrent = listReview.get(i).getPRStars();
                    isRated = true;
                    break;
                } else {
                    isRated = false;
                }
            }
        } else {

        }
        int updated = Integer.valueOf(hmCount.get("Count")) + 1;
        hmCount.put("Count", String.valueOf(updated));
        product.setDescriptions(xmlaccess.createDescriptions(hmCount));
        productFacade.edit(product);
    }

    public String getNameOfSpecByID(String specID) {
        System.out.println("Name Of Spec" + specF.find(Integer.valueOf(specID)).getSpecName());
        return specF.find(Integer.valueOf(specID)).getSpecName();
    }

    public boolean isActiveSpec(String specName) {
        try {
            Specifics spec = specF.getSpecByName(specName);
            if (spec.getSpecStatus() == 1) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public List<ProductReviewComment> convert(Collection<ProductReviewComment> co) {
        List<ProductReviewComment> list = new ArrayList<ProductReviewComment>(co);
        return list;
    }

    private int rateMark;
    private String commentRV;
    private String review;
    private boolean isExit = false;

    public boolean isIsExit() {
        return isExit;
    }

    public void setIsExit(boolean isExit) {
        this.isExit = isExit;
    }

    public int getRateMark() {
        return rateMark;
    }

    public void setRateMark(int rateMark) {
        this.rateMark = rateMark;
    }

    public String getCommentRV() {
        return commentRV;
    }

    public void setCommentRV(String commentRV) {
        this.commentRV = commentRV;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public void insertReviewProduct() {
        ExternalContext ec = FacesContext.getCurrentInstance()
                .getExternalContext();
        String dateFormat = "dd/MM/yyyy HH:mm:ss";
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        listReview = new ArrayList<>();
        try {
            cutsomer = new Customer();
            productReview = new ProductReview();
            cutsomer = (Customer) ec.getSessionMap().get("LOGGED_CUSTOMER");
            if (cutsomer == null) {
                ec.redirect(ec.getRequestContextPath()
                        + "/faces/index.xhtml");
            } else {
                cutsomer = customerFacade.find(cutsomer.getCustomerID());
                customerInfo = customerInfoFacade.find(customer.getCustomerID());
                if (customerInfo.getCustomerName().equalsIgnoreCase("")) {
                    isValidname = true;
                } else {
                    isValidname = false;
                }
                String productID = ec.getSessionMap().get("product-detail").toString();
                System.out.println(productID);
                product = productFacade.find(Integer.valueOf(productID));
                listReview = (List<ProductReview>) product.getProductReviewCollection();
                int cusID = cutsomer.getCustomerID();
                int proID = product.getProductID();
                String datePost = sdf.format(cal.getTime());
                productReview.setPrid(0);
                productReview.setProductID(product);
                productReview.setCustomerID(cutsomer);
                productReview.setComments(review);
                productReview.setPRStars(rateMark);
                productReview.setTimePost(datePost);
                for (int i = 0; i < listReview.size(); i++) {
                    if (cusID == listReview.get(i).getCustomerID().getCustomerID() && proID == listReview.get(i).getProductID().getProductID()) {
                        isExit = true;
                        break;
                    } else {
                        isExit = false;
                    }
                }
                if (isExit == false) {
                    if (review.equalsIgnoreCase("") || rateMark == 0) {
                        isValidate = true;
                    } else {
                        isValidate = false;
                    }
                    if (isValidate == false && isValidname == false) {
                        productReviewFacade.create(productReview);
                        review = "";
                        rateCurrent = rateMark;
                        isRated = true;
                    }

                }
                productReview = new ProductReview();
                cutsomer = new Customer();
                productReviewFacade.refresh();
                customerFacade.refresh();
                productFacade.refresh();
                allReivew();
                detailProduct();
            }
        } catch (Exception e) {
        }
    }
    public static Comparator<ProductReview> comparator = new Comparator<ProductReview>() {
        DateFormat f = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        @Override
        public int compare(ProductReview o1, ProductReview o2) {
            try {
                return f.parse(o2.getTimePost()).compareTo(f.parse(o1.getTimePost()));

            } catch (ParseException ex) {
                Logger.getLogger(detailProduct.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
            return 0;
        }

    };
    private int recordPerPage, totalRecord, totalPage, fristRecord, endRecord;

    public void allReivew() {
        ExternalContext ec = FacesContext.getCurrentInstance()
                .getExternalContext();
        listReview = new ArrayList<ProductReview>();
        listPages = new ArrayList<Integer>();
        productComment = new ProductReviewComment();
        productComment.setComments("");
        commentRV = "";
        try {
            if (ec.getRequestParameterMap().get("productID") != null) {
                String productId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("productID");
                list = new ArrayList<>();
                product = productFacade.find(Integer.valueOf(productId));

                ec.getSessionMap().put("detail-customer-product", product.getProductID());
            } else {
                String productId = ec.getSessionMap().get("detail-customer-product").toString();
                product = productFacade.find(Integer.valueOf(productId));
            }
            list = (List<ProductReview>) product.getProductReviewCollection();
            Collections.sort(list, comparator);

            recordPerPage = 3;
            totalRecord = list.size();
            if ((totalRecord % recordPerPage) == 0) {
                totalPage = totalRecord / recordPerPage;
            } else {
                totalPage = (totalRecord / recordPerPage) + 1;
            }

            if (totalRecord > 3) {
                fristRecord = 0;
                endRecord = recordPerPage;
            } else {
                fristRecord = 0;
                endRecord = totalRecord;
            }

            for (int i = 0; i < list.size(); i++) {
                if (i >= fristRecord && i < endRecord) {
                    listReview.add(list.get(i));
                }
            }
            if (totalPage <= 3) {
                for (int j = 1; j <= totalPage; j++) {
                    listPages.add(j);
                }
            } else {
                for (int j = 1; j <= 3; j++) {
                    listPages.add(j);
                }

            }

        } catch (Exception e) {
        }

    }

    public List<ProductReview> loadReviewProduct(int id) {
        recordPerPage = 3;
        System.out.println(id);
        listReview = new ArrayList<ProductReview>();
        totalRecord = list.size();
        for (int j = 0; j < list.size(); j++) {
            System.out.println("J: " + list.get(j).getTimePost());
        }
        if (id == 1) {
            if (totalRecord > 3) {
                fristRecord = 0;
                endRecord = recordPerPage;
            } else {

                fristRecord = 0;
                endRecord = totalRecord;
            }
        } else {

            fristRecord = (recordPerPage * (id - 1));
            endRecord = fristRecord + recordPerPage;
        }
        Collections.sort(list, comparator);
        for (int i = 0; i < list.size(); i++) {
            if (i >= fristRecord && i < endRecord) {
                listReview.add(list.get(i));
                Collections.sort(listReview, comparator);
                System.out.println(" i " + list.get(i).getTimePost());
            }
        }
        fristRecord = 0;
        endRecord = 0;
        loadPageTotal(id);

        return listReview;
    }

    public List<Integer> loadPageTotal(int id) {

        listPages = new ArrayList<Integer>();
        totalRecord = list.size();
        if ((totalRecord % recordPerPage) == 0) {
            totalPage = totalRecord / recordPerPage;
        } else {
            totalPage = (totalRecord / recordPerPage) + 1;
        }
        for (int i = 1; i <= totalPage; i++) {
            listPages.add(i);
        }
        int currentPage = id;
        if (totalPage < 3) {
            if (currentPage == 1) {
                listPages.removeAll(listPages);
                for (int j = 1; j <= totalPage; j++) {
                    listPages.add(j);
                }
            } else {
                listPages.removeAll(listPages);
                for (int j = 1; j <= totalPage; j++) {
                    listPages.add(j);
                }
            }
        } else {
            if (currentPage == 1) {
                listPages.removeAll(listPages);
                for (int j = 1; j <= (currentPage + 2); j++) {
                    listPages.add(j);
                }
            } else {
                if (currentPage == totalPage) {

                    listPages.removeAll(listPages);
                    for (int j = currentPage - 2; j <= currentPage; j++) {
                        listPages.add(j);
                    }

                } else {
                    listPages.removeAll(listPages);
                    for (int j = currentPage - 1; j <= (currentPage + 1); j++) {
                        listPages.add(j);
                    }
                }
            }

        }

        return listPages;
    }

    public void insertcommentPR() {
        ExternalContext ec = FacesContext.getCurrentInstance()
                .getExternalContext();
        String dateFormat = "dd/MM/yyyy HH:mm:ss";
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

        productReviewFacade.refresh();
        productReviewCommentFacade.refresh();
        List<ProductCommentMark> listProductCoomentMark = new ArrayList<>();
        listProductCoomentMark = productCommentMarkFacade.findAll();
        try {
            customer = new Customer();
            productReview = new ProductReview();
            customer = (Customer) ec.getSessionMap().get("LOGGED_CUSTOMER");
            if (customer == null) {
                ec.redirect(ec.getRequestContextPath()
                        + "/faces/index.xhtml");
            } else {
                customer = customerFacade.find(customer.getCustomerID());
                customerInfo = customerInfoFacade.find(customer.getCustomerID());
                if (customerInfo.getCustomerName().equalsIgnoreCase("")) {
                    isValidname = true;
                } else {
                    isValidname = false;
                }
                String prID = ec.getSessionMap().get("productRVID").toString();
                System.out.println(prID);
                productReview = productReviewFacade.find(Integer.valueOf(prID));
                String datePost = sdf.format(cal.getTime());
                int cusID = customer.getCustomerID();
                int pRID = productReview.getPrid();

                productComment.setPrcid(0);
                productComment.setCustomerID(customer);
                productComment.setPrid(productReview);
                productComment.setTimePost(datePost);
                productComment.setComments(commentRV);
                if (isValidname == false) {
                    productReviewCommentFacade.create(productComment);
                }
                productComment = new ProductReviewComment();
                commentRV = "";
                customerFacade.refresh();
                productReviewFacade.refresh();
                productReviewCommentFacade.refresh();
                detailProductReview();
            }
        } catch (Exception e) {
        }
    }

    public void insertReviewMark() {
        ExternalContext ec = FacesContext.getCurrentInstance()
                .getExternalContext();
        productReviewFacade.refresh();
        productReviewCommentFacade.refresh();
        productCommentMarkFacade.refresh();
        List<ProductCommentMark> list = new ArrayList<>();
        boolean isexit = false;
        try {
            customer = new Customer();
            productReview = new ProductReview();
            productCMPK = new ProductCommentMarkPK();
            customer = (Customer) ec.getSessionMap().get("LOGGED_CUSTOMER");
            if (customer == null) {
                ec.redirect(ec.getRequestContextPath()
                        + "/faces/index.xhtml");
            } else {
                customer = customerFacade.find(customer.getCustomerID());
                String pRID = ec.getSessionMap().get("productRVID").toString();
                System.out.println(pRID);
                productReview = productReviewFacade.find(Integer.valueOf(pRID));
                int cusID = customer.getCustomerID();
                int prID = productReview.getPrid();
                productCMPK = new ProductCommentMarkPK(prID, cusID);
                productCMPK.setPrid(prID);
                productCMPK.setCustomerID(cusID);
                productCommentMark.setProductCommentMarkPK(productCMPK);//run
                productCommentMark.setStars(ratting);
                list = (List<ProductCommentMark>) productReview.getProductCommentMarkCollection();
                for (int i = 0; i < list.size(); i++) {
                    if (cusID == list.get(i).getCustomer().getCustomerID()) {
                        isexit = true;
                        break;
                    } else {
                        isexit = false;

                    }
                }
                if (isexit == true) {
                    cusID = customer.getCustomerID();
                    prID = productReview.getPrid();
                    productCMPK = new ProductCommentMarkPK(prID, cusID);
                    productCommentMark = new ProductCommentMark(productCMPK);
                     productCommentMark.setStars(ratting);
                    productCommentMarkFacade.edit(productCommentMark);
                } else {
                    productCommentMarkFacade.create(productCommentMark);
                }
                productReviewFacade.refresh();
                productReviewCommentFacade.refresh();
                detailProductReview();

            }
        } catch (Exception e) {
        }
    }

    public void idListener(int prid) {
        lastId = prid;
        System.out.println("lastID: " + lastId);
    }

    public boolean isAllowCustomerDelete(int prCID) {
        customer = (Customer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("LOGGED_CUSTOMER");
        productComment = productReviewCommentFacade.find(prCID);
        if (customer != null && productComment.getCustomerID().getCustomerID().equals(customer.getCustomerID())) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isAllowAdminDelete() {
        employee = (Employee) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("LOGGED_ADMIN");
        if (employee != null) {
            List<AdminRole> listAdminRole = (List<AdminRole>) employee.getAdminRoleCollection();
            for (Iterator<AdminRole> iterator = listAdminRole.iterator(); iterator.hasNext();) {
                AdminRole next = iterator.next();
                if (next.getRoleID().equalsIgnoreCase("Role02") || next.getRoleID().equalsIgnoreCase("Role00")) {
                    return true;
                }
            }
        } else {
            return false;
        }
        return false;
    }

    public void deletePRCommnet(int prCid) {
        try {
            productReviewCommentFacade.refresh();
            productComment = productReviewCommentFacade.find(prCid);
            productReviewCommentFacade.remove(productComment);

            detailProductReview();
        } catch (Exception e) {
        }
    }
    private List<ProductReviewComment> listPRCM = new ArrayList<>();

    public List<ProductReviewComment> getListPRCM() {
        return listPRCM;
    }

    public void setListPRCM(List<ProductReviewComment> listPRCM) {
        this.listPRCM = listPRCM;
    }
    private String comment;
    private int ratting;
    int total1, total2, total3, total4, total5;
    private int totalRank;

    public int getTotalRank() {
        return totalRank;
    }

    public void setTotalRank(int totalRank) {
        this.totalRank = totalRank;
    }

    private List<ProductReviewComment> listProductRVCM;

    public List<ProductReviewComment> getListProductRVCM() {
        return listProductRVCM;
    }

    public void setListProductRVCM(List<ProductReviewComment> listProductRVCM) {
        this.listProductRVCM = listProductRVCM;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getRatting() {
        return ratting;
    }

    public void setRatting(int ratting) {
        this.ratting = ratting;
    }

    public void detailProductReview() {
        ExternalContext ec = FacesContext.getCurrentInstance()
                .getExternalContext();
        productReviewFacade.refresh();
        productReviewCommentFacade.refresh();
        boolean isexit = false;
        List<ProductCommentMark> listRate = new ArrayList<>();
        commentRV = "";
        ratting = 0;
        rateCurrent = 0;
        comment = "";
        isExit = false;
        isValidate = false;
        isRated = false;
        rank = 0;
        isValidname = false;
        try {
            customer = new Customer();
            customer = (Customer) ec.getSessionMap().get("LOGGED_CUSTOMER");
            if (ec.getRequestParameterMap().get("reviewProductID") != null) {
                productReviewFacade.refresh();
                String productRVID = ec.getRequestParameterMap().get("reviewProductID");
                productReview = productReviewFacade.find(Integer.valueOf(productRVID));

                if (productReview == null) {
                    if (ec.getSessionMap().get("isProduct") != null) {
                        ec.redirect(ec.getRequestContextPath()
                                + "/faces/Customer/DetailProduct.xhtml");
                    }
                }
                ec.getSessionMap().put("productRVID", productReview.getPrid());
            } else {
                String branchRVID = ec.getSessionMap().get("productRVID").toString();
                if (branchRVID != null) {
                    productReview = productReviewFacade.find(Integer.valueOf(branchRVID));
                } else {
                    if (ec.getSessionMap().get("isProduct") != null) {
                        ec.redirect(ec.getRequestContextPath()
                                + "/faces/Customer/DetailProduct.xhtml");
                    }
                }

            }
            int cusID;
            listRate = (List<ProductCommentMark>) productReview.getProductCommentMarkCollection();
            if (customer != null) {
                cusID = customer.getCustomerID();
                for (int i = 0; i < listRate.size(); i++) {
                    if (cusID == listRate.get(i).getCustomer().getCustomerID()) {
                        isexit = true;
                        ratting = listRate.get(i).getStars();
                        break;
                    } else {
                        isexit = false;

                    }
                }
            } else {
                commentRV = "";
                ratting = 0;
            }

            listProductRVCM = new ArrayList<>();
            listProductRVCM = (List<ProductReviewComment>) productReview.getProductReviewCommentCollection();
            List<ProductCommentMark> list = new ArrayList<>();
            list = (List<ProductCommentMark>) productReview.getProductCommentMarkCollection();
            int star;
            total1 = 0;
            total2 = 0;
            total3 = 0;
            total4 = 0;
            total5 = 0;
            for (int i = 0; i < list.size(); i++) {
                star = list.get(i).getStars();
                if (star == 5) {
                    total5 = total5 + 1;
                }
                if (star == 4) {
                    total4 = total4 + 1;
                }
                if (star == 3) {
                    total3 = total3 + 1;
                }
                if (star == 2) {
                    total2 = total2 + 1;

                }
                if (star == 1) {
                    total1 = total1 + 1;

                }

            }
            System.out.println((total5 * 5) + (total4 * 4) + (total3 * 3) + (total2 * 2) + (total1));
            System.out.println(list.size());

            totalRank = ((total5 * 5) + (total4 * 4) + (total3 * 3) + (total2 * 2) + (total1)) / list.size();
            System.out.println("Rank: " + totalRank);

        } catch (Exception e) {
        }
    }

}
