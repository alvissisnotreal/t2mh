/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package srcCustomer;

import Entity.Customer;
import Entity.OrderInfo;
import Entity.OrderStatus;
import Entity.Orders;
import Entity.Product;
import SessionBean.CustomerFacadeLocal;
import SessionBean.OrderInfoFacadeLocal;
import SessionBean.OrderStatusFacadeLocal;
import SessionBean.OrdersFacadeLocal;
import SessionBean.ProductFacadeLocal;
import XMLAccess.XmlAccessDescriptions;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.application.FacesMessage;
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
@Named(value = "cusOrder")
@SessionScoped
@ManagedBean
public class cusOrder {

    @EJB
    private OrderInfoFacadeLocal orderInfoF;

    @EJB
    private OrderStatusFacadeLocal orderStatusF;

    @EJB
    private CustomerFacadeLocal customerF;

    @EJB
    private ProductFacadeLocal productF;

    @EJB
    private OrdersFacadeLocal ordersF;

    private XmlAccessDescriptions XAD;
    private List<Product> listProduct;
    private Orders order;
    private HashMap<Integer, Integer> hmQuantity;//key=productid,value=quantity of orderInfo
    private double outputTotal;

    //getter and setter
    public double getOutputTotal() {
        return outputTotal;
    }

    public void setOutputTotal(double outputTotal) {
        this.outputTotal = outputTotal;
    }

    public HashMap<Integer, Integer> getHmQuantity() {
        return hmQuantity;
    }

    public void setHmQuantity(HashMap<Integer, Integer> hmQuantity) {
        this.hmQuantity = hmQuantity;
    }

    public Orders getOrder() {
        return order;
    }

    public void setOrder(Orders order) {
        this.order = order;
    }

    public List<Product> getListProduct() {
        return listProduct;
    }

    public void setListProduct(List<Product> listProduct) {
        this.listProduct = listProduct;
    }

    //end getter and setter
    public cusOrder() {
    }

    @PostConstruct
    public void init() {
        order = new Orders();
        listProduct = new ArrayList<>();
        XAD = new XmlAccessDescriptions();
        hmQuantity = new HashMap<>();
        outputTotal = 0;
    }

    public String getAvatarByID(Integer productID) throws XPathExpressionException {
        return XAD.getAvatar(productF.find(productID).getDescriptions());
    }

    public long getPriceByProductID(Integer productID) throws XPathExpressionException {
        return XAD.getPrice(productF.find(productID).getDescriptions());
    }

    public boolean requestAllow() {
        customerF.refresh();
        ordersF.refresh();
        productF.refresh();
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        Customer customer = (Customer) externalContext.getSessionMap().get("LOGGED_CUSTOMER");
        
        if (externalContext.getRequestParameterMap().get("orderID") != null) {
            order = ordersF.find(Integer.valueOf(externalContext.getRequestParameterMap().get("orderID")));
            externalContext.getSessionMap().put("orderDetails", order.getOrderID());
        } else {
            order = ordersF.find(Integer.valueOf(externalContext.getSessionMap().get("orderDetails").toString()));
        }
        if (customer == null) {
            return false;
        }
        customer = customerF.find(customer.getCustomerID());
        if (order.getCustomerID().getCustomerID().intValue() != customer.getCustomerID().intValue()) {
            order = new Orders();
            FacesContext.getCurrentInstance().addMessage("messagesUpdate", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Sorry, You Cannot View This Order"));
            return false;
        }
        return true;
    }

    public void loadOrderView() {
        listProduct=new ArrayList<>();
        hmQuantity=new HashMap<>();
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        try {
            if (requestAllow() != true) {
                return;
            }
            for (Iterator<OrderInfo> iterator = order.getOrderInfoCollection().iterator(); iterator.hasNext();) {
                OrderInfo next = iterator.next();
                listProduct.add(next.getProduct());
                hmQuantity.put(next.getProduct().getProductID(), next.getQuantity());
            }
            outputTotal = 0;
            for (Map.Entry<Integer, Integer> entry : hmQuantity.entrySet()) {
                Integer key = entry.getKey();
                Integer value = entry.getValue();
                outputTotal += getPriceByProductID(key) * value;
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage("messagesUpdate", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Sorry, Cannot Load Info Of This Order"));
        }
    }

    public void cancelOrder() {
        //cancel order;
        System.out.println("Cancel Order");
        orderInfoF.refresh();
        // set order status
        order = ordersF.find(order.getOrderID());
        //cancel only work when orderStatus at 2 3
        if (order.getOrderStatusID().getOrderStatusID() != 2 && order.getOrderStatusID().getOrderStatusID() != 3) {
            FacesContext.getCurrentInstance().addMessage("messagesUpdate", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Sorry, Cannot Do This Task."));
            return;
        }
        OrderStatus orderStautsCancel = orderStatusF.find(1);//1=cancel
        order.setOrderStatusID(orderStautsCancel);
        ordersF.edit(order);

        //set orderInfo Status // roll back inventory
        List<OrderInfo> listOrderInfo = (List<OrderInfo>) order.getOrderInfoCollection();
        for (Iterator<OrderInfo> iterator = listOrderInfo.iterator(); iterator.hasNext();) {
            OrderInfo next = iterator.next();
            next.setDescriptions("Customer Cancel");
            orderInfoF.edit(next);
            Product product = next.getProduct();
            product.setInventory(product.getInventory() + next.getQuantity());
            productF.edit(product);
        }
        FacesContext.getCurrentInstance().addMessage("messagesUpdate", new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Cancel Sucess"));
    }

    @PreDestroy
    public void destroyRequestBean() {
        System.out.println("Complete Request cusOrder bean");
    }
}
