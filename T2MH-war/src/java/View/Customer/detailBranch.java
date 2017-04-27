package View.Customer;

import Entity.AdminRole;
import Entity.Branch;
import Entity.BranchCommentMark;
import Entity.BranchCommentMarkPK;
import Entity.BranchReview;
import Entity.BranchReviewComment;
import Entity.Customer;
import Entity.CustomerInfo;
import Entity.Employee;
import SessionBean.BranchCommentMarkFacadeLocal;
import SessionBean.BranchFacadeLocal;
import SessionBean.BranchReviewCommentFacadeLocal;
import SessionBean.BranchReviewFacadeLocal;
import SessionBean.CustomerFacadeLocal;
import SessionBean.CustomerInfoFacadeLocal;

import XMLAccess.XmlAccessDescriptions;
import java.io.IOException;
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
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.xml.xpath.XPathExpressionException;

@Named(value = "detailBranch")
@SessionScoped
public class detailBranch implements Serializable {

    @EJB
    private CustomerInfoFacadeLocal customerInfoFacade;

    @EJB
    private BranchCommentMarkFacadeLocal branchCommentMarkFacade;

    @EJB
    private CustomerFacadeLocal customerFacade;

    @EJB
    private BranchFacadeLocal branchFacade;

    @EJB
    private BranchReviewCommentFacadeLocal branchReviewCommentFacade;

    @EJB
    private BranchReviewFacadeLocal branchReviewFacade;

    List<BranchReview> list;
    List<BranchReviewComment> listCM;
    boolean isValidName = false;

    public boolean isIsValidName() {
        return isValidName;
    }

    public void setIsValidName(boolean isValidName) {
        this.isValidName = isValidName;
    }

    CustomerInfo customerInfo = new CustomerInfo();

    public BranchReviewCommentFacadeLocal getBranchReviewCommentFacade() {
        return branchReviewCommentFacade;
    }

    public void setBranchReviewCommentFacade(BranchReviewCommentFacadeLocal branchReviewCommentFacade) {
        this.branchReviewCommentFacade = branchReviewCommentFacade;
    }

    public List<BranchReviewComment> getListCM() {
        return listCM;
    }

    public void setListCM(List<BranchReviewComment> listCM) {
        this.listCM = listCM;
    }
    private BranchReview branchReview = new BranchReview();
    private BranchReviewComment branchComment = new BranchReviewComment();
    private Branch branch = new Branch();
    private Customer customer = new Customer();
    private int ratting;
    private String reiview;
    private String comment;
    int recordPerPage, totalRecord, totalPage, fristRecord, endRecord;
    private List<Integer> listPages;
    private List<BranchReview> listRV;
    private Employee employee = new Employee();
    private List<BranchReviewComment> listRVCM;

    public List<BranchReviewComment> getListRVCM() {
        return listRVCM;
    }

    public void setListRVCM(List<BranchReviewComment> listRVCM) {
        this.listRVCM = listRVCM;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public List<BranchReview> getListRV() {
        return listRV;
    }

    public void setListRV(List<BranchReview> listRV) {
        this.listRV = listRV;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public int getRatting() {
        return ratting;
    }

    public void setRatting(int ratting) {
        this.ratting = ratting;
    }

    public String getReiview() {
        return reiview;
    }

    public void setReiview(String reiview) {
        this.reiview = reiview;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public BranchReview getBranchReview() {
        return branchReview;
    }
    private XmlAccessDescriptions XMl = new XmlAccessDescriptions();

    public XmlAccessDescriptions getXMl() {
        return XMl;
    }

    public void setXMl(XmlAccessDescriptions XMl) {
        this.XMl = XMl;
    }

    public void setBranchReview(BranchReview branchReview) {
        this.branchReview = branchReview;
    }

    public BranchReviewComment getBranchComment() {
        return branchComment;
    }

    public void setBranchComment(BranchReviewComment branchComment) {
        this.branchComment = branchComment;
    }
    private List<branchRV> listrv;

    public detailBranch() {
    }

    public List<Integer> getListPages() {
        return listPages;
    }

    public void setListPages(List<Integer> listPages) {
        this.listPages = listPages;
    }

    @PostConstruct
    public void init() {
        rateCurrent = 0;
        comment = "";
        isratting = false;
        isValidate = false;
        isRated = false;
        isValidName = false;
        totalrank = 0;
    }

    public void allReivew() {
        ExternalContext ec = FacesContext.getCurrentInstance()
                .getExternalContext();
        listRV = new ArrayList<BranchReview>();
        listPages = new ArrayList<Integer>();
        listRVCM = new ArrayList<BranchReviewComment>();
        try {
            if (ec.getRequestParameterMap().get("branchID") != null) {
                String branchID = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("branchID");
                list = new ArrayList<>();
                branch = branchFacade.find(Integer.valueOf(branchID));

                ec.getSessionMap().put("detail-customer-branch", branch.getBranchID());
            } else {
                String branchId = ec.getSessionMap().get("detail-customer-branch").toString();
                branch = branchFacade.find(Integer.valueOf(branchId));
            }
            list = (List<BranchReview>) branch.getBranchReviewCollection();
            Collections.sort(list, comparator);
            List<BranchReviewComment> listBRCM = new ArrayList<>();
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
                    listRV.add(list.get(i));

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

    public List<BranchReview> loadReviewBranch(int id) {
        recordPerPage = 3;
        System.out.println(id);
        listRV = new ArrayList<BranchReview>();
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
                listRV.add(list.get(i));
                Collections.sort(listRV, comparator);
                System.out.println(" i " + list.get(i).getTimePost());
            }
        }
        fristRecord = 0;
        endRecord = 0;
        loadPageTotal(id);

        return listRV;
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
    private String name;
    private String email;
    private String Address;
    private String phone;
    private String avatar;
    private String description;
    private double totalrank;
    private int total1 = 0, total2 = 0, totat3 = 0, total4 = 0, total5 = 0;
    private boolean isValidate = false;
    private int rateCurrent;
    private boolean isRated = false;

    public boolean isIsRated() {
        return isRated;
    }

    public void setIsRated(boolean isRated) {
        this.isRated = isRated;
    }

    public int getRateCurrent() {
        return rateCurrent;
    }

    public void setRateCurrent(int rateCurrent) {
        this.rateCurrent = rateCurrent;
    }

    public boolean isIsValidate() {
        return isValidate;
    }

    public void setIsValidate(boolean isValidate) {
        this.isValidate = isValidate;
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void detailbranch() throws XPathExpressionException {
        ExternalContext ec = FacesContext.getCurrentInstance()
                .getExternalContext();
        XMLAccess.XmlAccessDescriptions xml = new XmlAccessDescriptions();
        branch = new Branch();
        branchFacade.refresh();
        customerFacade.refresh();
        branchReviewFacade.refresh();
        rateCurrent = 0;
        comment = "";
        ratting = 0;
       isratting = false;
        isValidate = false;
        isRated = false;
        isValidName = false;
        totalrank = 0;
        try {
            if (ec.getRequestParameterMap().get("branchID") != null) {
                String branchID = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("branchID");
                list = new ArrayList<>();
                branch = branchFacade.find(Integer.valueOf(branchID));

                ec.getSessionMap().put("detail-customer-branch", branch.getBranchID());
            } else {

                if (ec.getSessionMap().get("isBranh") != null) {
                    String branchIds = ec.getSessionMap().get("isBranh").toString();
                    branch = branchFacade.find(Integer.valueOf(branchIds));
                } else {
                    String branchId = ec.getSessionMap().get("detail-customer-branch").toString();
                    branch = branchFacade.find(Integer.valueOf(branchId));
                }
            }
            if (ec.getSessionMap().get("isCusInfo") != null) {
                ec.getSessionMap().remove("isCusInfo");
                ec.getSessionMap().put("isBranch", branch.getBranchID());
            } else {
                ec.getSessionMap().put("isBranch", branch.getBranchID());
            }
            name = branch.getBranchName();
            String des = branch.getBranchDescriptions();
            email = xml.getSpecAsIndiv(des, "Email");
            phone = xml.getSpecAsIndiv(des, "Phone");
            Address = xml.getSpecAsIndiv(des, "Address");
            description = xml.getSpecAsIndiv(des, "Description");
            avatar = xml.getAvatar(des);
            list = (List<BranchReview>) branch.getBranchReviewCollection();

            int star;
            total1 = 0;
            total2 = 0;
            totat3 = 0;
            total4 = 0;
            total5 = 0;
            for (int i = 0; i < list.size(); i++) {
                star = list.get(i).getBRStars();
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
            if (list.size() <=0) {
                totalrank = 0;
            }
            else{
            totalrank = ((total5 * 5) + (total4 * 4) + (totat3 * 3) + (total2 * 2) + (total1)) / list.size();
            totalrank = (Integer) (Math.round((float) (totalrank * 10)) / 10);
            }
            System.out.println("Rank: " + totalrank);
            customer = new Customer();
            customer = (Customer) ec.getSessionMap().get("LOGGED_CUSTOMER");
            int cusID = customer.getCustomerID();
            int bId = branch.getBranchID();
            for (int i = 0; i < list.size(); i++) {
                if (cusID == list.get(i).getCustomerID().getCustomerID() && bId == list.get(i).getBranchID().getBranchID()) {
                    rateCurrent = list.get(i).getBRStars();
                    isRated = true;
                    break;
                } else {
                    isRated = false;
                }
            }
        } catch (Exception e) {

        }

    }

    public void insertReviewBranch() {
        ExternalContext ec = FacesContext.getCurrentInstance()
                .getExternalContext();
        String dateFormat = "dd/MM/yyyy HH:mm:ss";
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        list = new ArrayList<>();
        isValidate = false;
        isValidName = false;

        try {
            customer = new Customer();
            branchReview = new BranchReview();
            customer = (Customer) ec.getSessionMap().get("LOGGED_CUSTOMER");
            if (customer == null) {
                ec.redirect(ec.getRequestContextPath()
                        + "/faces/Customer/detailbrand.xhtml");

            } else {

                customer = customerFacade.find(customer.getCustomerID());
                customerInfo = customerInfoFacade.find(customer.getCustomerID());
                if (customerInfo.getCustomerName().equalsIgnoreCase("")) {
                    isValidName = true;
                } else {
                    isValidName = false;
                }
                String branchID = ec.getSessionMap().get("detail-customer-branch").toString();
                System.out.println(branchID);
                branch = branchFacade.find(Integer.valueOf(branchID));
                list = (List<BranchReview>) branch.getBranchReviewCollection();
                int cusID = customer.getCustomerID();
                int bId = branch.getBranchID();
                String datePost = sdf.format(cal.getTime());
                branchReview.setBrid(0);
                branchReview.setBranchID(branch);
                branchReview.setCustomerID(customer);
                branchReview.setTimePost(datePost);
                branchReview.setBRStars(ratting);
                branchReview.setComments(comment);
                for (int i = 0; i < list.size(); i++) {
                    if (cusID == list.get(i).getCustomerID().getCustomerID() && bId == list.get(i).getBranchID().getBranchID()) {
                        isratting = true;
                        break;
                    } else {
                        isratting = false;
                    }
                }
                if (isratting == false) {
                    if (comment.equalsIgnoreCase("") || ratting == 0) {
                        isValidate = true;
                    } else {
                        isValidate = false;
                    }
                    if (isValidate == false && isValidName == false) {
                        branchReviewFacade.create(branchReview);
                        comment = "";
                        rateCurrent = ratting;
                        isRated = true;
                    }

                }
                branchReview = new BranchReview();
                customer = new Customer();

                branchFacade.refresh();
                customerFacade.refresh();
                branchReviewFacade.refresh();

                allReivew();
                detailbranch();
            }
        } catch (Exception e) {
        }
    }

    public List<BranchReviewComment> convert(Collection<BranchReviewComment> co) {
        List<BranchReviewComment> list = new ArrayList<BranchReviewComment>(co);
        return list;
    }

    public BranchReviewFacadeLocal getBranchReviewFacade() {
        return branchReviewFacade;
    }

    public void setBranchReviewFacade(BranchReviewFacadeLocal branchReviewFacade) {
        this.branchReviewFacade = branchReviewFacade;
    }

    public List<BranchReview> getList() {
        return list;
    }

    public void setList(List<BranchReview> list) {
        this.list = list;
    }
    public static Comparator<BranchReview> comparator = new Comparator<BranchReview>() {
        DateFormat f = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        @Override
        public int compare(BranchReview o1, BranchReview o2) {
            try {
                return f.parse(o2.getTimePost()).compareTo(f.parse(o1.getTimePost()));

            } catch (ParseException ex) {
                Logger.getLogger(detailBranch.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
            return 0;
        }

    };
    private BranchCommentMark branchCMMark = new BranchCommentMark();
    private BranchCommentMarkPK branchCMMrakPk = new BranchCommentMarkPK();
    private String commentRV;

    public BranchCommentMark getBranchCMMark() {
        return branchCMMark;
    }

    public void setBranchCMMark(BranchCommentMark branchCMMark) {
        this.branchCMMark = branchCMMark;
    }

    public BranchCommentMarkPK getBranchCMMrakPk() {
        return branchCMMrakPk;
    }

    public void setBranchCMMrakPk(BranchCommentMarkPK branchCMMrakPk) {
        this.branchCMMrakPk = branchCMMrakPk;
    }

    public String getCommentRV() {
        return commentRV;
    }

    public void setCommentRV(String commentRV) {
        this.commentRV = commentRV;
    }
    private boolean isratting = false;

    public boolean isIsratting() {
        return isratting;
    }

    public void setIsratting(boolean isratting) {
        this.isratting = isratting;
    }
    private String commentReview;

    public String getCommentReview() {
        return commentReview;
    }

    public void setCommentReview(String commentReview) {
        this.commentReview = commentReview;
    }

//    public void insertcommentBR() {
//        ExternalContext ec = FacesContext.getCurrentInstance()
//                .getExternalContext();
//        String dateFormat = "dd/MM/yyyy HH:mm:ss";
//        Calendar cal = Calendar.getInstance();
//        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
//
//        branchReviewFacade.refresh();
//        branchReviewCommentFacade.refresh();
//        List<BranchCommentMark> listBranchCoomentMark = new ArrayList<>();
//        listBranchCoomentMark = branchCommentMarkFacade.findAll();
//        try {
//            customer = new Customer();
//            branchReview = new BranchReview();
//            customer = (Customer) ec.getSessionMap().get("LOGGED_CUSTOMER");
//            if (customer == null) {
//                ec.redirect(ec.getRequestContextPath()
//                        + "/faces/index.xhtml");
//            } else {
//                customer = customerFacade.find(customer.getCustomerID());
//                System.out.println(lastId);
//                branchReview = branchReviewFacade.find(lastId);
//                String datePost = sdf.format(cal.getTime());
//                int cusID = customer.getCustomerID();
//                int bRId = branchReview.getBrid();
//
//                branchComment.setBrcid(0);
//                branchComment.setCustomerID(customer);
//                branchComment.setBrid(branchReview);
//                branchComment.setDatePost(datePost);
//                branchComment.setComments(commentRV);
//                branchReviewCommentFacade.create(branchComment);
//                branchComment = new BranchReviewComment();
//
//                customerFacade.refresh();
//                branchReviewFacade.refresh();
//                branchReviewCommentFacade.refresh();
//                allReivew();
//            }
//        } catch (Exception e) {
//        }
//    }
//    public void insertReviewMark(int brRID) {
//        ExternalContext ec = FacesContext.getCurrentInstance()
//                .getExternalContext();
//        branchReviewFacade.refresh();
//        branchReviewCommentFacade.refresh();
//        branchCommentMarkFacade.refresh();
//        try {
//            customer = new Customer();
//            branchReview = new BranchReview();
//            customer = (Customer) ec.getSessionMap().get("LOGGED_CUSTOMER");
//            if (customer == null) {
//                ec.redirect(ec.getRequestContextPath()
//                        + "/faces/index.xhtml");
//            } else {
//                customer = customerFacade.find(customer.getCustomerID());
//                System.out.println(brRID);
//                branchReview = branchReviewFacade.find(brRID);
//                int cusID = customer.getCustomerID();
//                int bRId = branchReview.getBrid();
//                branchCMMrakPk = new BranchCommentMarkPK();
//                branchCMMrakPk.setBrid(bRId);
//                branchCMMrakPk.setCustomerID(customer.getCustomerID());
//                branchCMMark.setBranchCommentMarkPK(branchCMMrakPk);//run
//                branchCommentMarkFacade.create(branchCMMark);
//                branchReviewFacade.refresh();
//                branchReviewCommentFacade.refresh();
//                allReivew();
//
//            }
//        } catch (Exception e) {
//        }
//    }
//    private int lastId;
//
//    public int getLastId() {
//        return lastId;
//    }
//
//    public void setLastId(int lastId) {
//        this.lastId = lastId;
//    }
//
//    public void idListener(int brid) {
//        lastId = brid;
//        System.out.println("lastID: " + lastId);
//    }
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

            allReivew();
        } catch (Exception e) {
        }
    }
//    private List<BranchReviewComment> listCMRV;

//    public List<BranchReviewComment> showComment(int brid) {
//
//        int frists;
//        int ends;
//        int recordPerPages;
//        int total;
//        List<BranchReviewComment> listCMB = new ArrayList<>();
//        branchReview = new BranchReview();
//        listCMRV = new ArrayList<>();
//        branchReview = branchReviewFacade.find(brid);
//        listCMRV = (List<BranchReviewComment>) branchReview.getBranchReviewCommentCollection();
//        if (listCMRV.size() <= 3) {
//            isLinkMore = false;
//            listRVCM = new ArrayList<>();
//            for (int i = 0; i < listCMRV.size(); i++) {
//                if (i >= 0 && i < 3) {
//                    listRVCM.add(listCMRV.get(i));
//                }
//
//            }
//        } else {
//            isLinkMore = true;
//            listRVCM = new ArrayList<>();
//            for (int i = 0; i < listCMRV.size(); i++) {
//                if (i >= 0 && i < 3) {
//                    listRVCM.add(listCMRV.get(i));
//                }
//
//            }
//        }
//        return listRVCM;
//    }
//    int totalrecord;
//    int recordLoad;
//    int frist;
//    int end;
//    boolean flag = true;
//
// 
}
