package srcCustomer;

import Entity.Branch;
import Entity.Customer;
import Entity.CustomerInfo;
import Entity.OrderInfo;
import Entity.OrderInfoPK;
import Entity.Orders;
import Entity.Product;
import SessionBean.CustomerFacadeLocal;
import SessionBean.CustomerInfoFacadeLocal;
import SessionBean.OrderStatusFacadeLocal;
import SessionBean.OrdersFacadeLocal;
import SessionBean.ProductFacadeLocal;
import XMLAccess.XmlAccessDescriptions;
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
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;
import javax.servlet.RequestDispatcher;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import org.primefaces.context.RequestContext;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@Named(value = "customerOrder")

@SessionScoped
@ManagedBean
public class customerOrder implements Serializable {

    @EJB
    private OrderStatusFacadeLocal orderStatusF;

    @EJB
    private OrdersFacadeLocal ordersF;

    @EJB
    private ProductFacadeLocal productF;

    @EJB
    private CustomerInfoFacadeLocal CIF;

    @EJB
    private CustomerFacadeLocal customerF;

    private final int maxOrder = 10;//orderItems
    private final int maximumOrdering = 5;//maximum order of customer on processing
    private final double maxTotal = Double.valueOf("10000000000");//value of order
    private double outputTotal;
    private String originalURL;
    private String baseURL;
    private Document cartDocument;
    private int spinnerValueChange;
    private HashMap<Integer, Integer> hmOrder;
    private XmlAccessDescriptions XAD;
    private Customer customer;
    private CustomerInfo customerInfo;
    HashMap<String, String> hmDes;
    HashMap<String, String> hmDesOfOrders;
    private Orders order;
    private OrderInfo orderInfo;
    private List<Orders> listOrder;
    private javax.xml.xpath.XPath xp;
    private String des;

    private final int maximumQuantity = 100;

    ////////////////////////////getter  setter
    public int getMaximumQuantity() {
        return maximumQuantity;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public List<Orders> getListOrder() {
        return listOrder;
    }

    public void setListOrder(List<Orders> listOrder) {
        this.listOrder = listOrder;
    }

    public Orders getOrder() {
        return order;
    }

    public void setOrder(Orders order) {
        this.order = order;
    }

    public OrderInfo getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(OrderInfo orderInfo) {
        this.orderInfo = orderInfo;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Document getCartDocument() {
        return cartDocument;
    }

    public void setCartDocument(Document cartDocument) {
        this.cartDocument = cartDocument;
    }

    public int getSpinnerValueChange() {
        return spinnerValueChange;
    }

    public void setSpinnerValueChange(int spinnerValueChange) {
        this.spinnerValueChange = spinnerValueChange;
    }

    public double getOutputTotal() throws XPathExpressionException {
        outputTotal = 0;
        for (Map.Entry<Integer, Integer> entry : hmOrder.entrySet()) {
            Integer key = entry.getKey();
            Integer value = entry.getValue();
            Product product = productF.find(key);
            outputTotal += (XAD.getPrice(product.getDescriptions()) * value);
        }
        return outputTotal;
    }

    public void setOutputTotal(double outputTotal) {
        this.outputTotal = outputTotal;
    }

    public HashMap<Integer, Integer> getHmOrder() {
        return hmOrder;
    }

    public void setHmOrder(HashMap<Integer, Integer> hmOrder) {
        this.hmOrder = hmOrder;
    }

    ////////////////////////  end  getter      setter
    @PostConstruct
    public void init() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        customer = new Customer();
        customerInfo = new CustomerInfo();
        XAD = new XMLAccess.XmlAccessDescriptions();
        //test customer logged?
        hmDes = new HashMap<>();
        hmDesOfOrders = new HashMap<>();
        hmOrder = new HashMap<>();
        outputTotal = 0;
        order = new Orders();
        listOrder = new ArrayList<>();
        if (externalContext.getSessionMap().get("LOGGED_CUSTOMER") == null) {
            cartDocument = XAD.stringToDocument("<CartOrder></CartOrder>");
        } else {
            customer = (Customer) externalContext.getSessionMap().get("LOGGED_CUSTOMER");
            customer = customerF.find(customer.getCustomerID());
            Document doc = XAD.stringToDocument(customer.getCustomerInfo().getDescriptions());
            hmDes = XAD.convertDocumentToHashMap(doc);
            if (hmDes.get("CartOrder") != null) {
                cartDocument = XAD.stringToDocument(hmDes.get("CartOrder"));
            } else {
                cartDocument = XAD.stringToDocument("<CartOrder></CartOrder>");
            }
        }
        originalURL = (String) externalContext.getRequestMap().get(RequestDispatcher.FORWARD_REQUEST_URI);
        baseURL = externalContext.getRequestContextPath();
        if (originalURL == null) {
            originalURL = externalContext.getRequestContextPath() + "/Admin/Customer/Index.xhtml";
        } else {
            String originalQuery = (String) externalContext.getRequestMap().get(RequestDispatcher.FORWARD_QUERY_STRING);

            if (originalQuery != null) {
                originalURL += "?" + originalQuery;
            }
        }
    }

    public List<Integer> convertHashMapToList(HashMap<Integer, Integer> hm, String typeOutput) {
        if (typeOutput.equalsIgnoreCase("Key")) {
            List<Integer> listKey = new ArrayList<>();
            for (Map.Entry<Integer, Integer> entry : hm.entrySet()) {
                int key = entry.getKey();
                listKey.add(key);
            }
            return listKey;
        } else {
            List<Integer> listKey = new ArrayList<>();
            for (Map.Entry<Integer, Integer> entry : hm.entrySet()) {
                Integer value = entry.getValue();
                listKey.add(value);
            }
            return listKey;
        }
    }

    public Product getProductByID(Integer productID) {
        return productF.find(productID);
    }

    public String getAvatarByID(Integer productID) throws XPathExpressionException {
        return XAD.getAvatar(productF.find(productID).getDescriptions());
    }

    public long getPriceByProductID(Integer productID) throws XPathExpressionException {
        return XAD.getPrice(productF.find(productID).getDescriptions());
    }

    public long getInventoryByProductID(Integer productID) {
        return productF.find(productID).getInventory();
    }

    public HashMap<Integer, Integer> getInforOrder() throws XPathExpressionException {
        xp = javax.xml.xpath.XPathFactory.newInstance().newXPath();
        hmOrder = new HashMap<>();
        productF.refresh();
        String xquery = "//CartOrder/OrderProduct";
        NodeList nodeList = (NodeList) xp.evaluate(xquery, cartDocument, XPathConstants.NODESET);
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element ele = (Element) nodeList.item(i);
            Product product = productF.find(Integer.valueOf(ele.getElementsByTagName("ProductID").item(0).getTextContent()));
            if (product != null && productF.productIsActive(product.getProductID())) {
                int quantity = Integer.valueOf(ele.getElementsByTagName("Quantity").item(0).getTextContent());
                hmOrder.put(product.getProductID(), quantity);
                //srcImage= first node of <Images/>
            }
        }
        return hmOrder;
    }

    public boolean handleMaxTotal(Document doc) {
        try {
            HashMap<Integer, Integer> hmOrderTest = new HashMap<>();
            xp = javax.xml.xpath.XPathFactory.newInstance().newXPath();
            productF.refresh();
            String xquery = "//CartOrder/OrderProduct";
            NodeList nodeList = (NodeList) xp.evaluate(xquery, doc, XPathConstants.NODESET);
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element ele = (Element) nodeList.item(i);
                Product product = productF.find(Integer.valueOf(ele.getElementsByTagName("ProductID").item(0).getTextContent()));
                if (product != null && product.getProductStatus() == 1) {
                    int quantity = Integer.valueOf(ele.getElementsByTagName("Quantity").item(0).getTextContent());
                    hmOrderTest.put(product.getProductID(), quantity);
                    //srcImage= first node of <Images/>
                }
            }
            double outputTotalTest = 0;
            for (Map.Entry<Integer, Integer> entry : hmOrderTest.entrySet()) {
                Integer key = entry.getKey();
                Integer value = entry.getValue();
                Product product = productF.find(key);
                outputTotalTest += (XAD.getPrice(product.getDescriptions()) * value);
            }
            if (outputTotalTest > maxTotal) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public void removeCartOfCustomer() {
        customerF.refresh();
        CIF.refresh();
        customerInfo = CIF.find(customer.getCustomerID());
        Document doc = XAD.stringToDocument(customerInfo.getDescriptions());
        if (doc.getElementsByTagName("CartOrder").getLength() != 0) {
            for (int i = 0; i < doc.getElementsByTagName("CartOrder").getLength(); i++) {
                doc.getElementsByTagName("CartOrder").item(i).getParentNode().removeChild(doc.getElementsByTagName("CartOrder").item(i));
            }
        }
        customerInfo.setDescriptions(XAD.documentToString(cartDocument));
        CIF.edit(customerInfo);
    }

    public void saveCartToCustomer() {
        customerF.refresh();
        CIF.refresh();
        customerInfo = CIF.find(customer.getCustomerID());
        Document doc = XAD.stringToDocument(customerInfo.getDescriptions());
        if (doc.getElementsByTagName("CartOrder").getLength() != 0) {
            for (int i = 0; i < doc.getElementsByTagName("CartOrder").getLength(); i++) {
                doc.getElementsByTagName("CartOrder").item(i).getParentNode().removeChild(doc.getElementsByTagName("CartOrder").item(i));
            }
        }
        Element eleroot = doc.getDocumentElement();
        eleroot.appendChild(doc.createTextNode(XAD.documentToString(cartDocument).trim()));
        //converC
        customerInfo.setDescriptions(XAD.documentToString(doc));
        CIF.edit(customerInfo);
    }

    public void addProduct(int productID) throws XPathExpressionException {
        productF.refresh();
        if (productF.productIsActive(productID) != true) {
            FacesContext.getCurrentInstance().addMessage("messagesUpdate", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Can't Add This Product"));
            return;
        }

        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        Document backupCart = (Document) cartDocument.cloneNode(true);

        if (externalContext.getSessionMap().get("LOGGED_CUSTOMER") == null) {
            if (cartDocument == null) {
                cartDocument = XAD.stringToDocument("<CartOrder></CartOrder>");
            }
        } else {
            customer = (Customer) externalContext.getSessionMap().get("LOGGED_CUSTOMER");
            customer = customerF.find(customer.getCustomerID());
            Document doc = XAD.stringToDocument(customer.getCustomerInfo().getDescriptions());
            hmDes = XAD.convertDocumentToHashMap(doc);
            if (hmDes.get("CartOrder") != null) {
                cartDocument = XAD.stringToDocument("<CartOrder>" + hmDes.get("CartOrder") + "</CartOrder>");
            } else {
                cartDocument = XAD.stringToDocument("<CartOrder></CartOrder>");
            }
        }
        xp = javax.xml.xpath.XPathFactory.newInstance().newXPath();
        String xquery = "//CartOrder/OrderProduct[ProductID='" + productID + "']";
        Node node = (Node) xp.evaluate(xquery, cartDocument, XPathConstants.NODE);
        if (node != null) {
            Element ele = (Element) node;
            int updateQuantity = Integer.valueOf(ele.getElementsByTagName("Quantity").item(0).getTextContent()) + 1;
            int currentInventory = productF.find(productID).getInventory();
            if (updateQuantity > currentInventory) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn", "Lack Of Stock,<br/> Maximum is " + currentInventory));
                return;
            }
            ele.getElementsByTagName("Quantity").item(0).setTextContent(String.valueOf(updateQuantity));
            if (!handleMaxTotal(cartDocument)) {
                cartDocument = backupCart;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Maximum Of Cart Is " + Math.round(maxTotal)));
                return;
            }
        } else//product do not exists on cartDocument
        {
            Product product = productF.find(productID);
            if (product.getInventory() <= 0) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Out Of Stock"));
                return;
            }
            if (cartDocument.getElementsByTagName("ProductID").getLength() >= maxOrder) {
                saveCartToCustomer();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Maximum Product Of Cart Is <br/>" + maxOrder + " Product"));
                return;
            }
            try {
                Element eleRoot = cartDocument.getDocumentElement();
                Element nodeProduct = cartDocument.createElement("OrderProduct");

                //create element
                Element nodeID = cartDocument.createElement("ProductID");
                nodeID.appendChild(cartDocument.createCDATASection(String.valueOf(productID)));
                nodeProduct.appendChild(nodeID);

                Element nodeQuantity = cartDocument.createElement("Quantity");
                nodeQuantity.appendChild(cartDocument.createCDATASection("1"));
                nodeProduct.appendChild(nodeQuantity);

                eleRoot.appendChild(nodeProduct);
            } catch (Exception e) {
            }
            if (!handleMaxTotal(cartDocument)) {
                cartDocument = backupCart;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Maximum Of Cart Is<br/> " + Math.round(maxTotal) + " VND"));
                return;
            }
        }
        if (externalContext.getSessionMap().get("LOGGED_CUSTOMER") != null) {
            saveCartToCustomer();
        }
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Add To Cart Success"));
    }

    public void removeProduct(int productID) throws XPathExpressionException {
        javax.xml.xpath.XPath xp = javax.xml.xpath.XPathFactory.newInstance().newXPath();
        String xquery = "//CartOrder/OrderProduct[ProductID='" + productID + "']";
        Node node = (Node) xp.evaluate(xquery, cartDocument, XPathConstants.NODE);
        if (node != null) {
            String b = node.getTextContent();
            cartDocument.getDocumentElement().removeChild(node);
        }
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        if (externalContext.getSessionMap().get("LOGGED_CUSTOMER") != null) {
            saveCartToCustomer();
        }
    }

    public void updateSpinner(int productID) throws XPathExpressionException {
        Document backupCart = (Document) cartDocument.cloneNode(true);
        FacesContext faceContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = faceContext.getExternalContext();
        productF.refresh();
        int currentInventory = productF.find(productID).getInventory();
        if (spinnerValueChange > currentInventory) {
            faceContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn", "Lack Of Stock.<br/>Maximum = " + currentInventory));
            spinnerValueChange = currentInventory;
        }
        if (spinnerValueChange > maximumQuantity) {
            faceContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error", "Maximum Of Quantity Is " + maximumQuantity));
            spinnerValueChange = maximumQuantity;
        }
        {
            xp = javax.xml.xpath.XPathFactory.newInstance().newXPath();
            String xquery = "//CartOrder/OrderProduct[ProductID='" + productID + "']";
            Node node = (Node) xp.evaluate(xquery, cartDocument, XPathConstants.NODE);
            if (node != null) {
                Element ele = (Element) node;
                ele.getElementsByTagName("Quantity").item(0).setTextContent(String.valueOf(spinnerValueChange));
            }
            if (!handleMaxTotal(cartDocument)) {
                cartDocument = backupCart;
                faceContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Maximum Of Cart Is<br/> " + Math.round(maxTotal) + " VND"));
                return;
            }
            if (externalContext.getSessionMap().get("LOGGED_CUSTOMER") != null) {
                saveCartToCustomer();
            }
        }
    }

    public void spinnerValueListener(ValueChangeEvent event) {
        int newValue = Integer.valueOf(event.getNewValue().toString());
        if (newValue > maximumQuantity) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Maximum Of Quantity Is " + maximumQuantity));
            spinnerValueChange = maximumQuantity;
        } else {
            spinnerValueChange = newValue;
        }

    }

    public void orderNow() {
        productF.refresh();
        loadInfoCustomer();
        try {
            for (Map.Entry<Integer, Integer> entry : hmOrder.entrySet()) {
                Integer key = entry.getKey();
                Integer value = entry.getValue();
                Product product = productF.find(key);
                if (product.getInventory() < value) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Product " + product.getProductID() + " Is Out Of Stock"));
                    return;
                }
                if (productF.productIsActive(product.getProductID()) == false) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Cannot Order Product " + product.getProductID() + " Now"));
                    return;
                }
            }
            if (hmOrder.size() > 10) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Maximum Of Cart Is " + maxOrder + " Product.<br/>Remove Some Product Before Order!"));
                return;
            }
            if (getOutputTotal() > maxTotal) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Maximum Of Cart Is<br/> " + Math.round(maxTotal) + " VND"));
                return;
            }
            //test login? if not, call logged
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            if (externalContext.getSessionMap().get("LOGGED_CUSTOMER") == null) {
                RequestContext contextRequest = RequestContext.getCurrentInstance();
                contextRequest.execute("PF('dialogLogin').show();");
                return;
            } else {
                //logged

                //set auto info
                RequestContext contextRequest = RequestContext.getCurrentInstance();
                contextRequest.execute("PF('dialogOrderInfo').show();");
                return;
            }
        } catch (Exception e) {
//            cartDocument = backupCart;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Sorry, Cannot Order Now. <br/>Try Again Or Contact Admin"));
        }
    }

    public void confirmOrder() throws XPathExpressionException {
        //count order of customer. Max=5
        if (ordersF.countOrdering(customer.getCustomerID()) >= maximumOrdering) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Sorry, Cannot Order Now. "
                    + "                                                                                                 <br/>You Have 5 Orders Processing."
                    + "                                                                                                 <br/>Waiting For At Least 1 Of Those Finish"));
            RequestContext contextRequest = RequestContext.getCurrentInstance();
            contextRequest.execute("PF('dialogOrderInfo').show();");
            return;
        }
        Document backupCart = (Document) cartDocument.cloneNode(true);
        //insert
        try {
            Calendar calendar = Calendar.getInstance();
            Date date = calendar.getTime();
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            System.out.println("Date Insert: " + formatter.format(date));
            String timeOrder = formatter.format(date);

            order.setOrderID(0);
            order.setCustomerID(customerF.find(customer.getCustomerID()));
            order.setOrderStatusID(orderStatusF.find(2));

            List<OrderInfo> listOrder = new ArrayList<>();

            //prepare OrderInfo, test Info for Accept
            for (Map.Entry<Integer, Integer> hmOrder : hmOrder.entrySet()) {
                Integer key = hmOrder.getKey();
                Integer value = hmOrder.getValue();

                Product productTest = productF.find(key);
                //test value is accept?(Quantity)
                if (value > productTest.getInventory()) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "ProductID: " + key + "<br/>Maximum Quantity Is " + productTest.getInventory()));
                    return;
                }
                if (value > maximumQuantity) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "ProductID: " + key + "<br/>Maximum Quantity Is " + maximumQuantity));
                    return;
                }

                if (value > maximumQuantity) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Maximum Of Quantity Is " + maximumQuantity));
                    return;
                }
            }

            ordersF.create(order);
            for (Map.Entry<Integer, Integer> hmOrder : hmOrder.entrySet()) {
                Integer key = hmOrder.getKey();
                Integer value = hmOrder.getValue();

                Product productTest = productF.find(key);
                orderInfo = new OrderInfo();
                OrderInfoPK oiPK = new OrderInfoPK(order.getOrderID(), key);
                orderInfo.setOrderInfoPK(oiPK);
                orderInfo.setQuantity(value);
                orderInfo.setDescriptions("Wait For Confirm");
                listOrder.add(orderInfo);
            }
            order.setOrderInfoCollection(listOrder);
            order.setPriceTotal(getOutputTotal());
            order.setTimeOrder(timeOrder);
            ordersF.edit(order);
            //update inventory after order+update orderInfo 
            {
                for (Iterator<OrderInfo> iterator = listOrder.iterator(); iterator.hasNext();) {
                    OrderInfo orderInfo = iterator.next();
                    Product product = productF.find(orderInfo.getOrderInfoPK().getProductID());
                    product.setInventory(product.getInventory() - orderInfo.getQuantity());
                    productF.edit(product);
                }
            }
            ordersF.refresh();
            order = ordersF.find(order.getOrderID());
            listOrder = (List<OrderInfo>) order.getOrderInfoCollection();
            HashMap<Integer, Double> hmValuesOrder = new HashMap<Integer, Double>();//key = branchID, Value=amount will plus in payment
            HashMap<Integer, Long> hmValuesProduct = new HashMap<>();//key =productID,value=price of Product at the time Orders Is Create

            //hmValuesOrder use for Orders
            //hmValuesProduct use for OrderInfo
            for (OrderInfo aOrderInfo : listOrder) {
                //write description for orders
                //get branchID to write
                Branch branch = aOrderInfo.getProduct().getBranchID();
                System.out.println("branch id: " + branch.getBranchID());
                int branchID = aOrderInfo.getProduct().getBranchID().getBranchID();
                //calculated paymentInput 
                long priceValue = Math.round(getPriceByProductID(aOrderInfo.getProduct().getProductID()));
                HashMap<String, String> hmDesBranch;
                hmDesBranch = XAD.convertDocumentToHashMap(XAD.stringToDocument(branch.getBranchDescriptions()));
                String typeTax = hmDesBranch.get("typeTax");
                double paymentAmount = 0;
                if (typeTax.equalsIgnoreCase("Excises")) {
                    paymentAmount = priceValue * aOrderInfo.getQuantity();
                } else {
                    //calculator Amount of branch will receive
                    double valueTax = Double.valueOf(hmDesBranch.get("valueTax"));
                    paymentAmount = priceValue * aOrderInfo.getQuantity() * (100 - valueTax) / 100;
                }
                if (hmValuesOrder.get(branchID) == null) {
                    hmValuesOrder.put(branchID, paymentAmount);
                } else {
                    hmValuesOrder.put(branchID, hmValuesOrder.get(branchID) + paymentAmount);
                }
                Product product = aOrderInfo.getProduct();
                if (hmValuesProduct.get(product.getProductID()) == null) {
                    hmValuesProduct.put(product.getProductID(), XAD.getPrice(aOrderInfo.getProduct().getDescriptions()));
                }
            }

            Document doc = XAD.stringToDocument("<Descriptions></Descriptions>");
            Element eleRoot = doc.getDocumentElement();

            Element nodePayments = doc.createElement("Payments");
            for (Map.Entry<Integer, Double> entry : hmValuesOrder.entrySet()) {
                Integer branchIDKey = entry.getKey();
                Long ValuesKey = Math.round(entry.getValue());

                Element nodePayment = doc.createElement("Payment");

                //create element
                Element nodeID = doc.createElement("BranchID");
                nodeID.appendChild(doc.createTextNode(branchIDKey.toString()));
                nodePayment.appendChild(nodeID);

                Element nodeValues = doc.createElement("Values");
                nodeValues.appendChild(doc.createTextNode(ValuesKey.toString()));
                nodePayment.appendChild(nodeValues);

                nodePayments.appendChild(nodePayment);
            }
            eleRoot.appendChild(nodePayments);

            Element nodeProducts = doc.createElement("Products");
            for (Map.Entry<Integer, Long> entry : hmValuesProduct.entrySet()) {
                Integer productIDKey = entry.getKey();
                long ValuesKey = entry.getValue();

                Element nodeProduct = doc.createElement("Product");

                //create element
                Element productID = doc.createElement("ProductID");
                productID.appendChild(doc.createTextNode(productIDKey.toString()));
                nodeProduct.appendChild(productID);

                Element nodeValues = doc.createElement("ValuesProduct");
                nodeValues.appendChild(doc.createTextNode(String.valueOf(ValuesKey)));
                nodeProduct.appendChild(nodeValues);

                nodeProducts.appendChild(nodeProduct);

            }
            eleRoot.appendChild(nodeProducts);

            //create element description node
            Element nodeDescription = doc.createElement("Description");
            if (des != null) {
                nodeDescription.appendChild(doc.createCDATASection(des));
            } else {
                nodeDescription.appendChild(doc.createCDATASection(""));
            }
            eleRoot.appendChild(nodeDescription);

            order.setDescriptions(XAD.documentToString(doc));
            ordersF.edit(order);
            order = new Orders();
            this.cartDocument = XAD.stringToDocument("<CartOrder></CartOrder>");
            saveCartToCustomer();
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            externalContext.redirect(baseURL + "//Customer/OrderHistory.xhtml");
        } catch (Exception e) {
            e.printStackTrace();
            cartDocument = backupCart;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Sorry, Cannot Order Now. <br/>Try Again Or Contact Admin"));
        }
    }

    public void getAllOrderOfCustomer() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        if (externalContext.getSessionMap().get("LOGGED_CUSTOMER") == null) {
            return;
        } else {
            customer = (Customer) externalContext.getSessionMap().get("LOGGED_CUSTOMER");
            customerF.refresh();
            customer = customerF.find(customer.getCustomerID());
            listOrder = (List<Orders>) customer.getOrdersCollection();
            List<Orders> output = new ArrayList<Orders>();
            for (int i = listOrder.size(); i > 0; i--) {
                output.add(listOrder.get(i - 1));
            }
            listOrder = output;
        }
    }

    public void loadInfoCustomer() {

        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("LOGGED_CUSTOMER") != null) {//loading info for orderInfo dialog
            customerF.refresh();
            customer = (Customer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("LOGGED_CUSTOMER");
            customer = customerF.find(customer.getCustomerID());
            customerInfo = customer.getCustomerInfo();
            order.setSender(customerInfo.getCustomerName());
            order.setSenderPhone(customerInfo.getCustomerPhone());
            order.setReceiver(customerInfo.getCustomerName());
            order.setReceiverPhone(customerInfo.getCustomerPhone());
            order.setReceiverAddress(customerInfo.getCustomerAddress());
        }

    }
}
