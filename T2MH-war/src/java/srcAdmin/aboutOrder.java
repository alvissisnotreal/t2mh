/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package srcAdmin;

import Entity.Employee;
import Entity.AdminRole;
import Entity.Branch;
import Entity.OrderInfo;
import Entity.OrderInfoPK;
import Entity.OrderStatus;
import Entity.Orders;
import Entity.Payment;
import Entity.Product;
import SessionBean.AdminRoleFacadeLocal;
import SessionBean.BranchFacadeLocal;
import SessionBean.EmployeeFacadeLocal;
import SessionBean.EmployeeInfoFacadeLocal;
import SessionBean.OrderInfoFacadeLocal;
import SessionBean.OrderStatusFacadeLocal;
import SessionBean.OrdersFacadeLocal;
import SessionBean.PaymentFacadeLocal;
import SessionBean.ProductFacadeLocal;
import View.Employee.OrderInfoListView;
import XMLAccess.XmlAccessDescriptions;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.RequestDispatcher;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import org.primefaces.context.RequestContext;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author 19319
 */
@Named(value = "aboutOrder")

@SessionScoped
@ManagedBean
public class aboutOrder implements Serializable {

    @EJB
    private PaymentFacadeLocal paymentF;

    @EJB
    private BranchFacadeLocal branchF;

    @EJB
    private ProductFacadeLocal productF;

    @EJB
    private OrderInfoFacadeLocal orderInfoF;

    @EJB
    private OrdersFacadeLocal ordersF;

    @EJB
    private OrderStatusFacadeLocal orderStatusF;

    @EJB
    private AdminRoleFacadeLocal ARF;

    @EJB
    private EmployeeFacadeLocal EF;

    @EJB
    private EmployeeInfoFacadeLocal EIF;

    private String baseURL = "";
    private String originalURL = "";
    private boolean isAllowed;
    private List<OrderStatus> allOrderStatus;
    private Orders order;
    private List<OrderInfo> listOrderInfo;
    private XmlAccessDescriptions XAD;
    private boolean allAlready;
    private String recentOrderInfoStatus;
    private HashMap<Integer, String> hmOrderInfoStatus;
    private String newValue;
    private String oldvalue;
    private HashMap<String, String> hmDes;
    private Employee emp;

    //end about bean properties
    //getter and setter
    public HashMap<String, String> getHmDes() {
        return hmDes;
    }

    public void setHmDes(HashMap<String, String> hmDes) {
        this.hmDes = hmDes;
    }

    public HashMap<Integer, String> getHmOrderInfoStatus() {
        return hmOrderInfoStatus;
    }

    public void setHmOrderInfoStatus(HashMap<Integer, String> hmOrderInfoStatus) {
        this.hmOrderInfoStatus = hmOrderInfoStatus;
    }

    public String getRecentOrderInfoStatus() {
        return recentOrderInfoStatus;
    }

    public void setRecentOrderInfoStatus(String recentOrderInfoStatus) {
        this.recentOrderInfoStatus = recentOrderInfoStatus;
    }

    public boolean isAllAlready() {
        return allAlready;
    }

    public void setAllAlready(boolean allAlready) {
        this.allAlready = allAlready;
    }

    public List<OrderInfo> getListOrderInfo() {
        return listOrderInfo;
    }

    public void setListOrderInfo(List<OrderInfo> listOrderInfo) {
        this.listOrderInfo = listOrderInfo;
    }

    public Orders getOrder() {
        return order;
    }

    public void setOrder(Orders order) {
        this.order = order;
    }

    public String getBaseURL() {
        return baseURL;
    }

    public void setBaseURL(String baseURL) {
        this.baseURL = baseURL;
    }

    public List<OrderStatus> getAllOrderStatus() {
        return allOrderStatus;
    }

    public void setAllOrderStatus(List<OrderStatus> allOrderStatus) {
        this.allOrderStatus = allOrderStatus;
    }

    //end getter and setter//
    @PostConstruct
    public void init() {
        String currentURL = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        originalURL = (String) externalContext.getRequestMap().get(RequestDispatcher.FORWARD_REQUEST_URI);
        baseURL = externalContext.getRequestContextPath();
        allOrderStatus = orderStatusF.findAll();
        order = new Orders();
        XAD = new XmlAccessDescriptions();
        hmOrderInfoStatus = new HashMap<>();
        hmDes = new HashMap();
        if (originalURL == null) {
            originalURL = externalContext.getRequestContextPath() + "/Admin/Employee/Index.xhtml";
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
            emp = (Employee) externalContext.getSessionMap().get("LOGGED_ADMIN");
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

    public void loadDetails() throws IOException {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        //test

        //end test
        try {
            if (externalContext.getRequestParameterMap().get("orderID") != null) {
                order = ordersF.find(Integer.valueOf(externalContext.getRequestParameterMap().get("orderID")));
                externalContext.getSessionMap().put("details_Order", order.getOrderID());
            } else {
                order = ordersF.find(Integer.valueOf(externalContext.getSessionMap().get("details_Order").toString()));
            }
            listOrderInfo = (List<OrderInfo>) order.getOrderInfoCollection();
            allAlready = true;
            for (OrderInfo orderInfo : listOrderInfo) {
                if (orderInfo.getDescriptions().equalsIgnoreCase("Already Delivery") == false) {
                    allAlready = false;
                    break;
                }
            }
            hmDes = XAD.convertDocumentToHashMap(XAD.stringToDocument(order.getDescriptions()));
            getOrderInfoStatus();
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Can't Load Info"));
        }
    }

    public void getOrderInfoStatus() {
        //orderid via session
        hmOrderInfoStatus = new HashMap<>();
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        List<OrderInfo> listOrderInfo = (List<OrderInfo>) order.getOrderInfoCollection();
        for (OrderInfo orderInfo : listOrderInfo) {
            hmOrderInfoStatus.put(orderInfo.getProduct().getProductID(), orderInfo.getDescriptions());
        }
    }

    public void recentOrderInfoStatus(ValueChangeEvent event) {
        recentOrderInfoStatus = event.getNewValue().toString();
    }

    public void changeOrderInfoStatus(int productID) {
        try {
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            OrderInfoPK OIPK = new OrderInfoPK(order.getOrderID(), productID);
            OrderInfo orderInfo = orderInfoF.findByFK(OIPK);
            orderInfo.setDescriptions(recentOrderInfoStatus);
            orderInfoF.edit(orderInfo);
            listOrderInfo = (List<OrderInfo>) order.getOrderInfoCollection();
            allAlready = true;
            for (OrderInfo aOrderInfo : listOrderInfo) {
                if (aOrderInfo.getDescriptions().equalsIgnoreCase("Already Delivery") == false) {
                    allAlready = false;
                    break;
                }
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Change Status Success"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Some Thing Error, Try Again"));
        }

    }

    public void acceptChangeStatus() throws XPathExpressionException {
        System.out.println("acceptChangeStatus");
        productF.refresh();
        if (newValue.equalsIgnoreCase("1") || newValue.equalsIgnoreCase("5")) {
            for (OrderInfo orderInfo : listOrderInfo) {
                Product product = orderInfo.getProduct();
                product.setInventory(product.getInventory() + orderInfo.getQuantity());
                productF.edit(product);
            }

            //update order and orderinfo
            order.setOrderStatusID(orderStatusF.find(Integer.valueOf(newValue)));
            hmDes = XAD.convertDocumentToHashMap(XAD.stringToDocument(order.getDescriptions()));
            hmDes.put("EditBy", String.valueOf(emp.getEmployeeID()));
            if (newValue.equalsIgnoreCase("1")) {
                hmDes.put("EditReason", "Change OrderStatus To " + newValue + " [Cancel]");
            } else {
                hmDes.put("EditReason", "Change OrderStatus To " + newValue + " [Refuse]");
            }
            order.setDescriptions(XAD.createDescriptions(hmDes));
            ordersF.edit(order);

            for (OrderInfo orderInfo : listOrderInfo) {
                if (newValue.equalsIgnoreCase("1")) {
                    orderInfo.setDescriptions("Admin Cancel");
                } else {
                    orderInfo.setDescriptions("Refuse");
                }
                orderInfoF.edit(orderInfo);
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Order Complete. Save Success"));
        } else if (newValue.equalsIgnoreCase("4")) {
            order.setOrderStatusID(orderStatusF.find(Integer.valueOf(newValue)));
            hmDes.put("EditBy", String.valueOf(emp.getEmployeeID()));
            hmDes.put("EditReason", "Change OrderStatus To " + newValue + "[Delivered]");

            order.setDescriptions(XAD.createDescriptions(hmDes));
            Date date = new Date();
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            order.setTimeDelivery(formatter.format(date));
            ordersF.edit(order);

            for (OrderInfo orderInfo : listOrderInfo) {
                orderInfo.setDescriptions("Successful Transaction");
                orderInfoF.edit(orderInfo);
            }
            updatePayment();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Order Complete. Save Success"));
        }

        RequestContext contextRequest = RequestContext.getCurrentInstance();
        contextRequest.execute("PF('dialogConfirm').hide();");
        return;
    }

    public void rollBackOrderStatus() {
        order.setOrderStatusID(orderStatusF.find(Integer.valueOf(oldvalue)));
        ordersF.edit(order);
        newValue = oldvalue;
    }

    public void orderChangeStatus(ValueChangeEvent event) {
        newValue = event.getNewValue().toString();
        oldvalue = event.getOldValue().toString();
        if (newValue.equalsIgnoreCase("5") || newValue.equalsIgnoreCase("1") || newValue.equalsIgnoreCase("4")) {
            RequestContext contextRequest = RequestContext.getCurrentInstance();
            contextRequest.execute("PF('dialogConfirm').show();");
            return;
        }
    }

    public void updatePayment() throws XPathExpressionException {
        ordersF.refresh();
        branchF.refresh();
        paymentF.refresh();

        order = ordersF.find(order.getOrderID());

        Document doc;
        XmlAccessDescriptions XAD = new XmlAccessDescriptions();
        DateFormat formatterMY = new SimpleDateFormat("MM/yyyy");
        DateFormat formatterDMY = new SimpleDateFormat("dd/MM/yyyy");

        Date currentDate = new Date();
        String dateCreate = formatterDMY.format(currentDate);

        Calendar calendarStart = Calendar.getInstance();
        calendarStart.set(Calendar.DAY_OF_MONTH, 1);
        calendarStart.set(Calendar.HOUR_OF_DAY, 0);
        calendarStart.set(Calendar.MINUTE, 0);
        calendarStart.set(Calendar.SECOND, 0);
        Date startDate = calendarStart.getTime();
        String monthPayment = formatterMY.format(startDate);
        String stringStartDate = formatterDMY.format(startDate);

        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.add(Calendar.MONTH, +1);
        calendarEnd.set(Calendar.DAY_OF_MONTH, 1);
        calendarEnd.add(Calendar.DATE, -1);
        calendarEnd.set(Calendar.HOUR_OF_DAY, 23);
        calendarEnd.set(Calendar.MINUTE, 59);
        calendarEnd.set(Calendar.SECOND, 59);
        Date endDate = calendarEnd.getTime();
        String stringEndDate = formatterDMY.format(endDate);

        Payment paymentcre = new Payment();

        HashMap<Integer, Long> hmPaymentUpdate = new HashMap<>();//key=branchID,Value=new Amount

        {//code block get info from order to hashmap
            System.out.println(XAD.getSpecAsIndiv(order.getDescriptions(), "Payments"));
            doc = XAD.stringToDocument("<Payments>" + XAD.getSpecAsIndiv(order.getDescriptions(), "Payments") + "</Payments>");
            javax.xml.xpath.XPath xp = javax.xml.xpath.XPathFactory.newInstance().newXPath();
            String xquery = "//Payments/Payment";
            NodeList nodePayments = (NodeList) xp.evaluate(xquery, doc, XPathConstants.NODESET);
            System.out.println("Number of nodePayment: " + nodePayments.getLength());
            for (int i = 0; i < nodePayments.getLength(); i++) {
                try {
                    Node aNodePayment = nodePayments.item(i);
                    int branchID = Integer.valueOf(((Element) aNodePayment.getChildNodes()).getElementsByTagName("BranchID").item(0).getTextContent());
                    System.out.println("branchID: " + branchID);
                    Long values = Long.valueOf(((Element) aNodePayment.getChildNodes()).getElementsByTagName("Values").item(0).getTextContent());
                    System.out.println(values);
                    hmPaymentUpdate.put(branchID, values);
                } catch (Exception e) {
                }
            }
        }

        //create new Payment
        for (Map.Entry<Integer, Long> entry : hmPaymentUpdate.entrySet()) {

            Integer key = entry.getKey();//key=branchID
            Long value = entry.getValue();//value=money will receiver of a orders(tax not yet)
            Branch branch = branchF.find(key);

            HashMap<String, String> hmDesBranch;
            hmDesBranch = XAD.convertDocumentToHashMap(XAD.stringToDocument(branch.getBranchDescriptions()));
            String typeTax = hmDesBranch.get("typeTax");
            double valueTax = Double.valueOf(hmDesBranch.get("valueTax"));
            double paymentAmount = 0;

            try {
                paymentcre.setPaymentID(key + "-" + monthPayment);
                //test exists Payment
                if (paymentF.find(paymentcre.getPaymentID()) == null) {
                    if (typeTax.equalsIgnoreCase("Excises")) {
                        paymentAmount = value - valueTax;
                    } else {
                        //Ad valorem
                        paymentAmount = value;
                    }
                    paymentcre.setBranchID(branch);
                    paymentcre.setTimeCreate(dateCreate);
                    paymentcre.setStartDate(stringStartDate);
                    paymentcre.setEndDate(stringEndDate);
                    paymentcre.setDateDebt("");
                    paymentcre.setAmount(paymentAmount);
                    paymentcre.setPaid(0.00);
                    paymentcre.setDebt(paymentAmount);
                    paymentF.create(paymentcre);
                } else {
                    paymentAmount = value;//value will calculated when customer order
                    Payment paymentUpdate = paymentF.find(paymentcre.getPaymentID());
                    paymentUpdate.setDateDebt("");
                    paymentUpdate.setAmount(paymentUpdate.getAmount() + paymentAmount);
                    paymentUpdate.setDebt(paymentUpdate.getAmount() - paymentUpdate.getPaid());
                    paymentF.edit(paymentUpdate);
                }
            } catch (Exception e) {
            }
        }
    }
    public void goPrint(int orderID)
    {
        ExternalContext externalContext=FacesContext.getCurrentInstance().getExternalContext();
        externalContext.getSessionMap().put("orderIDBill", orderID);
    }
    public void goPrint()
    {
        ExternalContext externalContext=FacesContext.getCurrentInstance().getExternalContext();
        externalContext.getSessionMap().put("orderIDBill", order.getOrderID());
    }
}
