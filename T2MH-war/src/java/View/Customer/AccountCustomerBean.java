package View.Customer;

import Entity.AdminRole;
import Entity.BranchCommentMark;
import Entity.BranchCommentMarkPK;
import Entity.BranchReview;
import Entity.BranchReviewComment;
import Entity.Customer;
import Entity.CustomerInfo;
import Entity.Employee;
import Entity.ProductReview;
import Entity.ProductReviewComment;
import SessionBean.BranchCommentMarkFacadeLocal;
import SessionBean.BranchReviewCommentFacadeLocal;
import SessionBean.BranchReviewFacadeLocal;
import SessionBean.CustomerFacadeLocal;
import SessionBean.CustomerInfoFacadeLocal;
import SessionBean.ProductReviewCommentFacadeLocal;
import SessionBean.ProductReviewFacadeLocal;
import XMLAccess.XmlAccessDescriptions;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import org.jboss.weld.bean.builtin.ee.HttpServletRequestBean;
import org.primefaces.event.FileUploadEvent;

@Named(value = "accountCustomerBean")
@SessionScoped
public class AccountCustomerBean implements Serializable {

    @EJB
    private ProductReviewCommentFacadeLocal productReviewCommentFacade;

    @EJB
    private ProductReviewFacadeLocal productReviewFacade;

    @EJB
    private CustomerInfoFacadeLocal customerInfoFacade;

    @EJB
    private BranchCommentMarkFacadeLocal branchCommentMarkFacade;

    @EJB
    private BranchReviewCommentFacadeLocal branchReviewCommentFacade;

    @EJB
    private BranchReviewFacadeLocal branchReviewFacade;

    @EJB
    private CustomerInfoFacadeLocal CIF;

    @EJB
    private CustomerFacadeLocal customerF;

    private Customer customer = new Customer();
    ;
    private CustomerInfo customerInfo = new CustomerInfo();
    private boolean success = false;
    private String passCF;
    private String userName, password;
    private BranchReview branchRiview = new BranchReview();
    private BranchReviewComment branchRVCM = new BranchReviewComment();
    private BranchCommentMark branchCMMark = new BranchCommentMark();
    private BranchCommentMarkPK branchCMMrakPk = new BranchCommentMarkPK();
    private String comment;
    private boolean isRate = false;
    private String passNewCF;
    private boolean isValidname = false;
    private boolean isLink =false;

    public boolean isIsLink() {
        return isLink;
    }

    public void setIsLink(boolean isLink) {
        this.isLink = isLink;
    }
    
    public boolean isIsValidname() {
        return isValidname;
    }

    public void setIsValidname(boolean isValidname) {
        this.isValidname = isValidname;
    }

    public XmlAccessDescriptions getXAD() {
        return XAD;
    }

    public void setXAD(XmlAccessDescriptions XAD) {
        this.XAD = XAD;
    }

    public String getPassNewCF() {
        return passNewCF;
    }

    public void setPassNewCF(String passNewCF) {
        this.passNewCF = passNewCF;
    }

    public boolean isIsRate() {
        return isRate;
    }

    public void setIsRate(boolean isRate) {
        this.isRate = isRate;
    }

    public BranchCommentMark getBranchCMMark() {
        return branchCMMark;
    }

    public BranchCommentMarkPK getBranchCMMrakPk() {
        return branchCMMrakPk;
    }

    public void setBranchCMMark(BranchCommentMark branchCMMark) {
        this.branchCMMark = branchCMMark;
    }

    public void setBranchCMMrakPk(BranchCommentMarkPK branchCMMrakPk) {
        this.branchCMMrakPk = branchCMMrakPk;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public BranchReview getBranchRiview() {
        return branchRiview;
    }

    public void setBranchRiview(BranchReview branchRiview) {
        this.branchRiview = branchRiview;
    }

    public BranchReviewComment getBranchRVCM() {
        return branchRVCM;
    }

    public void setBranchRVCM(BranchReviewComment branchRVCM) {
        this.branchRVCM = branchRVCM;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public CustomerInfo getCustomerInfo() {
        return customerInfo;
    }

    public void setCustomerInfo(CustomerInfo customerInfo) {
        this.customerInfo = customerInfo;
    }

    public String getPassCF() {
        return passCF;
    }

    public void setPassCF(String passCF) {
        this.passCF = passCF;
    }

    @PostConstruct
    public void init() {
        totalrank = 0;
        comment = "";
        isExit = false;
        isValidate = false;
        isValidname = false;
        branchReviewFacade.refresh();
        branchReviewCommentFacade.refresh();
        branchCommentMarkFacade.refresh();

    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    //getter and setter
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    //end getter and setter
    public AccountCustomerBean() {
    }
    private int lastId;

    public int getLastId() {
        return lastId;
    }

    public void setLastId(int lastId) {
        this.lastId = lastId;
    }

    public void idListener(int brid) {
        lastId = brid;
        System.out.println("lastID: " + lastId);
    }
    private boolean isExit = false;

    public boolean isIsExit() {
        return isExit;
    }

    public void setIsExit(boolean isExit) {
        this.isExit = isExit;
    }

    public void reloadPage() {
        customer = new Customer();
        customerInfo = new CustomerInfo();
        description = "";
    }

    public void loadDetailsCustomer() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("LOGGED_CUSTOMER") != null) {
            displayInforToEdit();
        } else {
            customerInfo = new CustomerInfo();
            description = "";
        }
    }

    public void registerCustomer() throws NoSuchAlgorithmException {
        ExternalContext ec = FacesContext.getCurrentInstance()
                .getExternalContext();
        try {
            //set ID when creating customer, not null EJB
            customer.setCustomerID(0);
            customer.setCustomerStatus(1);
            customer.setCustomerUsername(userName);
            customer.setCustomerPassword(encryptPassword(password));
            List<Customer> listcus = new ArrayList<>();
            listcus = customerF.findAll();
            for (int i = 0; i < listcus.size(); i++) {
                if (userName.equalsIgnoreCase(listcus.get(i).getCustomerUsername())) {
                    isExit = true;
                    break;
                } else {
                    isExit = false;
                }

            }
            if (isExit == false) {
                HashMap<String, String> hmdes = new HashMap<>();
                customerInfo.setCustomerName("");
                customerInfo.setCustomerEmail("");
                customerInfo.setCustomerAddress("");
                customerInfo.setCustomerPhone("");
                hmdes.put("Description", description);
                customerInfo.setDescriptions(XAD.createDescriptions(hmdes));

                customerF.create(customer);
                customerInfo.setCustomerID(customer.getCustomerID());
                CIF.create(customerInfo);
                customer = customerF.findCustomerByUsername(customer.getCustomerUsername());
                System.out.println("customer ID:" + customer.getCustomerID());
                ec.getSessionMap().put("LOGGED_CUSTOMER", customer);
                ec.redirect(ec.getRequestContextPath()
                        + "/faces/index.xhtml");
            }

            //put a session have id from last created customer
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    private List<FileUploadEvent> fileList;
    private XmlAccessDescriptions XAD = new XmlAccessDescriptions();
    private String name, phone, email, address, description;

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<FileUploadEvent> getFileList() {
        return fileList;
    }

    public void setFileList(List<FileUploadEvent> fileList) {
        this.fileList = fileList;
    }

    public String encryptPassword(String Password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update((Password).getBytes());
        byte byteData[] = md.digest();
        //convert the byte to hex format method 1
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    public void handleFileUpload(FileUploadEvent event) throws IOException {
        fileList = new ArrayList<FileUploadEvent>();
        fileList.add(event);
        FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
        FacesContext.getCurrentInstance().addMessage("messagesUpdate", message);
    }

    public String writeFile(int cusID) {
        String content = "/Images/Customer/";
        for (int i = 0; i < this.fileList.size(); i++) {
            try {
                String fileName = fileList.get(i).getFile().getFileName();
                InputStream fileContentDeploy = fileList.get(i).getFile().getInputstream();
                String root = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/Images/Customer/");
                File path = new File(root);
                if (!path.exists()) {
                    boolean status = path.mkdirs();
                }

                String extension = "";
                int index = fileName.lastIndexOf('.');
                if (index > 0) {
                    extension = fileName.substring(index);
                }

                File uploadFile = new File(path + "/" + cusID + extension);
                content += cusID + extension;//
                Files.copy(fileContentDeploy, uploadFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception e) {
                return content;
            }
        }
        return content;
    }

    private List<Map.Entry<String, String>> listInform;

    public List<Map.Entry<String, String>> getListInform() {
        return listInform;
    }

    public void setListInform(List<Map.Entry<String, String>> listInform) {
        this.listInform = listInform;
    }
    private String avatar;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void inforCustomer() {
        ExternalContext ec = FacesContext.getCurrentInstance()
                .getExternalContext();
        try {
            customer = new Customer();
            customer = (Customer) ec.getSessionMap().get("LOGGED_CUSTOMER");
            XmlAccessDescriptions xml = new XmlAccessDescriptions();
            if (customer == null) {
                ec.redirect(ec.getRequestContextPath()
                        + "/faces/index.xhtml");
            } else {
                if (ec.getSessionMap().get("isBranch") != null) {
                    ec.getSessionMap().remove("isBranch");
                    ec.getSessionMap().put("isCusInfo", customer.getCustomerID());
                } else {
                    ec.getSessionMap().put("isCusInfo", customer.getCustomerID());
                }
                customerInfo = CIF.find(customer.getCustomerID());
                String des = customerInfo.getDescriptions();
                avatar = XAD.getAvatar(des);
                HashMap<String, String> hasInform = new HashMap<String, String>();
                hasInform.put("Name", customerInfo.getCustomerName());
                hasInform.put("Email", customerInfo.getCustomerEmail());
                hasInform.put("Phone", customerInfo.getCustomerPhone());
                hasInform.put("Address", customerInfo.getCustomerAddress());
                hasInform.put("Description", XAD.getSpecAsIndiv(des, "Description"));
                listInform = new ArrayList<>(hasInform.entrySet());
                for (Map.Entry<String, String> entry : hasInform.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();

                }

            }

        } catch (Exception e) {
        }

    }
    private List<BranchReview> listbranchRV;
    private List<ProductReview> listproductRV;
    private List<BranchReviewComment> listbranchRVCm;
    private List<branchReivewHistory> listBranchRVHistory;
    private List<productReviewHistory> listProductRVHistory;

    public List<BranchReview> getListbranchRV() {
        return listbranchRV;
    }

    public List<BranchReviewComment> getListbranchRVCm() {
        return listbranchRVCm;
    }

    public List<branchReivewHistory> getListBranchRVHistory() {
        return listBranchRVHistory;
    }

    public List<productReviewHistory> getListProductRVHistory() {
        return listProductRVHistory;
    }

    public void setListProductRVHistory(List<productReviewHistory> listProductRVHistory) {
        this.listProductRVHistory = listProductRVHistory;
    }

    public void allReviewBranchByCus() {
        ExternalContext ec = FacesContext.getCurrentInstance()
                .getExternalContext();
        customerF.refresh();
        branchReviewFacade.refresh();
        branchReviewCommentFacade.refresh();
        try {
            customer = new Customer();
            customer = (Customer) ec.getSessionMap().get("LOGGED_CUSTOMER");
            if (customer == null) {
                ec.redirect(ec.getRequestContextPath()
                        + "/faces/index.xhtml");
            } else {

                customer = customerF.find(customer.getCustomerID());
                System.out.println(customer.getCustomerID());
                listbranchRV = new ArrayList<>();
                listBranchRVHistory = new ArrayList<>();
                List<BranchReviewComment> listCMRV = new ArrayList<BranchReviewComment>();
                listbranchRV = (List<BranchReview>) customer.getBranchReviewCollection();
                for (int i = 0; i < listbranchRV.size(); i++) {
                    String branchName = listbranchRV.get(i).getBranchID().getBranchName();
                    String timePost = listbranchRV.get(i).getTimePost();
                    listCMRV = (List<BranchReviewComment>) listbranchRV.get(i).getBranchReviewCommentCollection();
                    String datePostNew;
                    if (listCMRV.isEmpty()) {
                        datePostNew = null;
                    } else {
                        datePostNew = listCMRV.get(listCMRV.size() - 1).getDatePost();

                    }
                    int brID = listbranchRV.get(i).getBrid();
                    listBranchRVHistory.add(new branchReivewHistory(branchName, timePost, datePostNew, brID));
                    Collections.sort(listBranchRVHistory, comparator);
                }

            }
        } catch (Exception e) {
        }
    }

    public void allReviewProductByCus() {
        ExternalContext ec = FacesContext.getCurrentInstance()
                .getExternalContext();
        customerF.refresh();
        productReviewFacade.refresh();
        productReviewCommentFacade.refresh();
        try {
            customer = new Customer();
            customer = (Customer) ec.getSessionMap().get("LOGGED_CUSTOMER");
            if (customer == null) {
                ec.redirect(ec.getRequestContextPath()
                        + "/faces/index.xhtml");
            } else {

                customer = customerF.find(customer.getCustomerID());
                System.out.println(customer.getCustomerID());
                listproductRV = new ArrayList<>();
                listProductRVHistory = new ArrayList<>();
                List<ProductReviewComment> listCMRV = new ArrayList<ProductReviewComment>();
                listproductRV = (List<ProductReview>) customer.getProductReviewCollection();
                for (int i = 0; i < listproductRV.size(); i++) {
                    String productName = listproductRV.get(i).getProductID().getProductName();
                    String timePost = listproductRV.get(i).getTimePost();
                    listCMRV = (List<ProductReviewComment>) listproductRV.get(i).getProductReviewCommentCollection();
                    String datePostNew;
                    if (listCMRV.isEmpty()) {
                        datePostNew = null;
                    } else {
                        datePostNew = listCMRV.get(listCMRV.size() - 1).getTimePost();

                    }
                    int prID = listproductRV.get(i).getPrid();
                    listProductRVHistory.add(new productReviewHistory(productName, timePost, datePostNew, prID));
                    Collections.sort(listProductRVHistory, comparator1);
                }

            }
        } catch (Exception e) {
        }
    }
    public static Comparator<branchReivewHistory> comparator = new Comparator<branchReivewHistory>() {
        DateFormat f = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        @Override
        public int compare(branchReivewHistory o1, branchReivewHistory o2) {
            try {
                return f.parse(o2.getTimePost()).compareTo(f.parse(o1.getTimePost()));

            } catch (ParseException ex) {
                Logger.getLogger(detailBranch.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
            return 0;
        }

    };
    public static Comparator<productReviewHistory> comparator1 = new Comparator<productReviewHistory>() {
        DateFormat f = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        @Override
        public int compare(productReviewHistory o1, productReviewHistory o2) {
            try {
                return f.parse(o2.getTimePost()).compareTo(f.parse(o1.getTimePost()));

            } catch (ParseException ex) {
                Logger.getLogger(detailBranch.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
            return 0;
        }

    };

    public void removeReview(int reviewID) {
        try {
            branchRiview = branchReviewFacade.find(reviewID);
            listbranchRVCm = new ArrayList<>();
            listbranchRVCm = (List<BranchReviewComment>) branchRiview.getBranchReviewCommentCollection();
            if (listbranchRVCm.isEmpty()) {
                branchReviewFacade.remove(branchRiview);
            } else {
                for (int i = 0; i < listbranchRVCm.size(); i++) {
                    int id = listbranchRVCm.get(i).getBrcid();
                    branchRVCM = branchReviewCommentFacade.find(id);
                    branchReviewCommentFacade.remove(branchRVCM);
                }
                branchReviewFacade.remove(branchRiview);
            }
            branchReviewFacade.refresh();
            allReviewBranchByCus();
        } catch (Exception e) {
        }

    }
    private ProductReview productReview;
    private List<ProductReviewComment> listProductReivewComment;
    private ProductReviewComment productRVCM;

    public void removeProductReview(int reviewPID) {
        try {
            productReview = new ProductReview();
            productRVCM = new ProductReviewComment();
            productReview = productReviewFacade.find(reviewPID);
            listProductReivewComment = new ArrayList<>();
            listProductReivewComment = (List<ProductReviewComment>) productReview.getProductReviewCommentCollection();
            if (listProductReivewComment.isEmpty()) {
                productReviewFacade.remove(productReview);
            } else {
                for (int i = 0; i < listProductReivewComment.size(); i++) {
                    int id = listProductReivewComment.get(i).getPrcid();
                    productRVCM = productReviewCommentFacade.find(id);
                    productReviewCommentFacade.remove(productRVCM);
                }
                productReviewFacade.remove(productReview);
            }
            productReviewFacade.refresh();
            allReviewProductByCus();
        } catch (Exception e) {
        }

    }
    private boolean islogin = false;

    public boolean isIslogin() {
        return islogin;
    }

    public void setIslogin(boolean islogin) {
        this.islogin = islogin;
    }

    public void detailBranchReview() {
        ExternalContext ec = FacesContext.getCurrentInstance()
                .getExternalContext();
        branchReviewFacade.refresh();
        branchReviewCommentFacade.refresh();
        boolean isexit = false;
        List<BranchCommentMark> listRate = new ArrayList<BranchCommentMark>();
        comment = "";
        ratting = 0;
        this.isexit = false;
        try {
            customer = new Customer();
            customer = (Customer) ec.getSessionMap().get("LOGGED_CUSTOMER");
            if (ec.getRequestParameterMap().get("reivewBranchID") != null) {
                branchReviewFacade.refresh();
                String branchRVID = ec.getRequestParameterMap().get("reivewBranchID");
                branchRiview = branchReviewFacade.find(Integer.valueOf(branchRVID));
                if (branchRiview == null) {
                    if (ec.getSessionMap().get("isCusInfo") == null && ec.getSessionMap().get("isBranch") != null) {
                        ec.redirect(ec.getRequestContextPath()
                                + "/faces/Customer/detailbrand.xhtml");
                    } else {
                        ec.redirect(ec.getRequestContextPath()
                                + "/faces/Customer/infoCustomer.xhtml");
                    }
                }
                ec.getSessionMap().put("branchRVID", branchRiview.getBrid());

            } else {
                String branchRVID = ec.getSessionMap().get("branchRVID").toString();
                branchRiview = branchReviewFacade.find(Integer.valueOf(branchRVID));
                if (branchRiview == null) {
                    if (ec.getSessionMap().get("isCusInfo") == null && ec.getSessionMap().get("isBranch") != null) {
                        ec.redirect(ec.getRequestContextPath()
                                + "/faces/Customer/detailbrand.xhtml");
                    } else {
                        ec.redirect(ec.getRequestContextPath()
                                + "/faces/Customer/infoCustomer.xhtml");
                    }
                }

            }

            int cusID;
            listRate = (List<BranchCommentMark>) branchRiview.getBranchCommentMarkCollection();
            if (customer != null) {
                cusID = customer.getCustomerID();
                for (int i = 0; i < listRate.size(); i++) {
                    if (cusID == listRate.get(i).getCustomer().getCustomerID()) {
                        isexit = true;
                        ratting = listRate.get(i).getBCMStars();
                        break;
                    } else {
                        isexit = false;

                    }
                }
            } else {
                comment = "";
                ratting = 0;
            }

            listbranchRVCm = new ArrayList<>();
            List<BranchReviewComment> listTest = new ArrayList<>();
            listTest = (List<BranchReviewComment>) branchRiview.getBranchReviewCommentCollection();
            Collections.sort(listTest, comparator2);
            if (listTest.size() >= 3) {
                first = 0;
                end = 3;
                isLink = true;
                for (int i = 0; i < listTest.size(); i++) {
                    if (i >= first && i < end) {
                        listbranchRVCm.add(listTest.get(i));
                    }
                }
                first = 0;
                end = 0;
            } else {
                isLink=false;
                first = 0;
                for (int i = 0; i < listTest.size(); i++) {

                    listbranchRVCm.add(listTest.get(i));

                }
                first = 0;
                end = 0;
            }

            List<BranchCommentMark> list = new ArrayList<>();
            list = (List<BranchCommentMark>) branchRiview.getBranchCommentMarkCollection();
            int star;
            total1 = 0;
            total2 = 0;
            totat3 = 0;
            total4 = 0;
            total5 = 0;
            for (int i = 0; i < list.size(); i++) {
                star = list.get(i).getBCMStars();
                if (star == 5) {
                    total5 = total5 + 1;
                }
                if (star == 4) {
                    total4 = total4 + 1;
                }
                if (star == 3) {
                    totat3 = totat3 + 1;
                }
                if (star == 2) {
                    total2 = total2 + 1;

                }
                if (star == 1) {
                    total1 = total1 + 1;

                }

            }
            System.out.println((total5 * 5) + (total4 * 4) + (totat3 * 3) + (total2 * 2) + (total1));
            System.out.println(list.size());

            totalrank = ((total5 * 5) + (total4 * 4) + (totat3 * 3) + (total2 * 2) + (total1)) / list.size();
            System.out.println("Rank: " + totalrank);

        } catch (Exception e) {
        }
    }

    public void insertComment() {
        ExternalContext ec = FacesContext.getCurrentInstance()
                .getExternalContext();
        String dateFormat = "dd/MM/yyyy HH:mm:ss";
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

        branchReviewFacade.refresh();
        branchReviewCommentFacade.refresh();
        List<BranchCommentMark> listBranchCoomentMark = new ArrayList<>();
        listBranchCoomentMark = branchCommentMarkFacade.findAll();
        try {
            customer = new Customer();
            customerInfo = new CustomerInfo();
            branchRiview = new BranchReview();
            customer = (Customer) ec.getSessionMap().get("LOGGED_CUSTOMER");
            if (customer == null) {
                ec.redirect(ec.getRequestContextPath()
                        + "/faces/index.xhtml");
            } else {
                customer = customerF.find(customer.getCustomerID());
                customerInfo = customerInfoFacade.find(customer.getCustomerID());
                if (customerInfo.getCustomerName().equalsIgnoreCase("")) {
                    isValidname = true;
                } else {
                    isValidname = false;
                }
                String branchRVID = ec.getSessionMap().get("branchRVID").toString();
                System.out.println(branchRVID);
                branchRiview = branchReviewFacade.find(Integer.valueOf(branchRVID));
                String datePost = sdf.format(cal.getTime());
                int cusID = customer.getCustomerID();
                int bRId = branchRiview.getBrid();

                branchRVCM.setBrcid(0);
                branchRVCM.setCustomerID(customer);
                branchRVCM.setBrid(branchRiview);
                branchRVCM.setDatePost(datePost);
                branchRVCM.setComments(comment);
                if (isValidname == false) {
                    branchReviewCommentFacade.create(branchRVCM);
                }

                branchRVCM = new BranchReviewComment();
                branchRiview = new BranchReview();

                customerF.refresh();
                branchReviewFacade.refresh();
                branchReviewCommentFacade.refresh();

                detailBranchReview();
                allReviewBranchByCus();
            }
        } catch (Exception e) {
        }
    }
    boolean isexit = false;

    public void insertReviewMark() {
        ExternalContext ec = FacesContext.getCurrentInstance()
                .getExternalContext();
        branchReviewFacade.refresh();
        branchReviewCommentFacade.refresh();
        branchCommentMarkFacade.refresh();
        isexit = false;

        try {
            customer = new Customer();
            branchRiview = new BranchReview();
            List<BranchCommentMark> list = new ArrayList<BranchCommentMark>();
            branchCMMrakPk = new BranchCommentMarkPK();
            customer = (Customer) ec.getSessionMap().get("LOGGED_CUSTOMER");
            if (customer == null) {
                ec.redirect(ec.getRequestContextPath()
                        + "/faces/index.xhtml");
            } else {
                customer = customerF.find(customer.getCustomerID());
                String branchRVID = ec.getSessionMap().get("branchRVID").toString();
                System.out.println(branchRVID);
                branchRiview = branchReviewFacade.find(Integer.valueOf(branchRVID));
                int cusID = customer.getCustomerID();
                int bRId = branchRiview.getBrid();
                branchCMMrakPk = new BranchCommentMarkPK(bRId, cusID);
                branchCMMrakPk.setBrid(bRId);
                branchCMMrakPk.setCustomerID(cusID);
                branchCMMark.setBranchCommentMarkPK(branchCMMrakPk);//run
                branchCMMark.setBCMStars(ratting);
                list = (List<BranchCommentMark>) branchRiview.getBranchCommentMarkCollection();
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
                    bRId = branchRiview.getBrid();
                    branchCMMrakPk = new BranchCommentMarkPK(bRId, cusID);
                    branchCMMark = new BranchCommentMark(branchCMMrakPk);
                    branchCMMark.setBCMStars(ratting);
                    branchCommentMarkFacade.edit(branchCMMark);
                } else {
                    branchCommentMarkFacade.create(branchCMMark);
                }
                branchReviewFacade.refresh();
                branchReviewCommentFacade.refresh();
                branchCommentMarkFacade.refresh();
                detailBranchReview();

            }
        } catch (Exception e) {
        }
    }
    private int ratting;

    public int getRatting() {
        return ratting;
    }

    public void setRatting(int ratting) {
        this.ratting = ratting;
    }

    public void deleteAvatarFile() {
        try {
            String root = FacesContext.getCurrentInstance().getExternalContext().getRealPath("");
            File fileDelete = new File(root + XAD.getAvatar(customerInfo.getDescriptions()));
            if (fileDelete.delete()) {
                System.out.println(fileDelete.getName() + " is deleted!");
            } else {
                System.out.println("Delete operation is failed.");
            }
        } catch (Exception e) {
        }
    }

    public void editCustomer() {
        ExternalContext ec = FacesContext.getCurrentInstance()
                .getExternalContext();
        try {
            customer = new Customer();
            customer = (Customer) ec.getSessionMap().get("LOGGED_CUSTOMER");
            if (customer == null) {
                ec.redirect(ec.getRequestContextPath()
                        + "/faces/index.xhtml");
            } else {
                customer = customerF.find(customer.getCustomerID());

                int cusID = customer.getCustomerID();
                HashMap<String, String> hmdes = new HashMap<String, String>();
                customerInfo.setCustomerID(cusID);
                customerInfo.setCustomerName(name);
                customerInfo.setCustomerEmail(email);
                customerInfo.setCustomerAddress(address);
                customerInfo.setCustomerPhone(phone);
                hmdes.put("Description", description);
                if (fileList.isEmpty()) {
                    hmdes.put("Avatar", writeFile(cusID));
                } else {
                    deleteAvatarFile();
                    hmdes.put("Avatar", writeFile(cusID));
                }
                customerInfo.setDescriptions(XAD.createDescriptions(hmdes));
                CIF.edit(customerInfo);
                customer = new Customer();
                customerInfo = new CustomerInfo();
                inforCustomer();

            }
        } catch (Exception e) {
        }
    }

    public void displayInforToEdit() {
        ExternalContext ec = FacesContext.getCurrentInstance()
                .getExternalContext();
        isValidate = false;
        try {
            customer = new Customer();
            customerInfo = new CustomerInfo();
            customer = (Customer) ec.getSessionMap().get("LOGGED_CUSTOMER");
            if (customer == null) {
                ec.redirect(ec.getRequestContextPath()
                        + "/faces/index.xhtml");
            } else {

                customer = customerF.find(customer.getCustomerID());
                int cusID = customer.getCustomerID();
                customerInfo = customer.getCustomerInfo();
                name = customerInfo.getCustomerName();
                phone = customerInfo.getCustomerPhone();
                email = customerInfo.getCustomerEmail();
                address = customerInfo.getCustomerAddress();
                String des = customerInfo.getDescriptions();
                description = XAD.getSpecAsIndiv(des, "Description");
            }
        } catch (Exception e) {
        }
    }
    private String passChange;

    public String getPassChange() {
        return passChange;
    }

    public void setPassChange(String passChange) {
        this.passChange = passChange;
    }

    private double totalrank;
    private int total1 = 0, total2 = 0, totat3 = 0, total4 = 0, total5 = 0;

    public double getTotalrank() {
        return totalrank;
    }

    public void setTotalrank(double totalrank) {
        this.totalrank = totalrank;
    }

    public int getTotal1() {
        return total1;
    }

    public void setTotal1(int total1) {
        this.total1 = total1;
    }

    public int getTotal2() {
        return total2;
    }

    public void setTotal2(int total2) {
        this.total2 = total2;
    }

    public int getTotat3() {
        return totat3;
    }

    public void setTotat3(int totat3) {
        this.totat3 = totat3;
    }

    public int getTotal4() {
        return total4;
    }

    public void setTotal4(int total4) {
        this.total4 = total4;
    }

    public int getTotal5() {
        return total5;
    }

    public void setTotal5(int total5) {
        this.total5 = total5;
    }
    private boolean isValidate = false;

    public boolean isIsValidate() {
        return isValidate;
    }

    public void setIsValidate(boolean isValidate) {
        this.isValidate = isValidate;
    }

    public void changePass() {
        ExternalContext ec = FacesContext.getCurrentInstance()
                .getExternalContext();
        try {
            customer = new Customer();
            customerInfo = new CustomerInfo();
            customer = (Customer) ec.getSessionMap().get("LOGGED_CUSTOMER");

            if (customer == null) {
                ec.redirect(ec.getRequestContextPath()
                        + "/faces/index.xhtml");
            } else {
                customer = customerF.find(customer.getCustomerID());

                if (encryptPassword(password).equalsIgnoreCase(customer.getCustomerPassword())) {
                    isValidate = false;
                } else {
                    isValidate = true;
                }
                if (isValidate == false) {
                    customer.setCustomerPassword(encryptPassword(passChange));
                    customerF.edit(customer);

                }
            }
            inforCustomer();

        } catch (Exception e) {
        }
    }

    public void isAllowregis() {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        if (ec.getSessionMap().get("LOGGED_CUSTOMER") != null) {
            try {
                ec.redirect(ec.getRequestContextPath()
                        + "/faces/Customer/infoCustomer.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(AccountCustomerBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        customer=new Customer();
        customerInfo=new CustomerInfo();
    }
    BranchReviewComment branchComment = new BranchReviewComment();
    Employee employee = new Employee();

    public boolean isAllowCustomerDelete(int brCID) {
        customer = (Customer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("LOGGED_CUSTOMER");
        branchComment = branchReviewCommentFacade.find(brCID);
        if (customer != null && branchComment.getCustomerID().getCustomerID().equals(customer.getCustomerID())) {
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

    public void deleteBRCommnet(int brCid) {
        try {
            branchReviewCommentFacade.refresh();
            branchComment = branchReviewCommentFacade.find(brCid);
            branchReviewCommentFacade.remove(branchComment);

            detailBranchReview();
        } catch (Exception e) {
        }
    }
    int first, end, current, total;

    public List<BranchReviewComment> linkMore() {
        List<BranchReviewComment> listTest = new ArrayList<>();
        listTest = (List<BranchReviewComment>) branchRiview.getBranchReviewCommentCollection();
        Collections.sort(listTest, comparator2);
        total = listTest.size();
        current = listbranchRVCm.size();
        if ((total - current) >= 3) {
            first = current;
            end = current + 3;
            isLink = true;
            for (int i = 0; i < listTest.size(); i++) {
                if (i > first && i <= end) {
                    listbranchRVCm.add(listTest.get(i));
                }
            }
            first = 0;
            end = 0;
            current = 0;
            total = 0;
        } else {
            isLink=false;
            first = current - 1;
            for (int i = 0; i < listTest.size(); i++) {
                if (i > first) {
                    listbranchRVCm.add(listTest.get(i));
                }

            }
            first = 0;
            end = 0;
            current = 0;
            total = 0;
        }

        return listbranchRVCm;
    }
    public static Comparator<BranchReviewComment> comparator2 = new Comparator<BranchReviewComment>() {
        DateFormat f = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        @Override
        public int compare(BranchReviewComment o1, BranchReviewComment o2) {
            try {
                return f.parse(o2.getDatePost()).compareTo(f.parse(o1.getDatePost()));

            } catch (ParseException ex) {
                Logger.getLogger(detailBranch.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
            return 0;
        }

    };
}
