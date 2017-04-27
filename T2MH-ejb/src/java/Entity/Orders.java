/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author 19319
 */
@Entity
@Table(name = "Orders", catalog = "PROJECTSEM4F21506S0_T2MH", schema = "dbo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Orders.findAll", query = "SELECT o FROM Orders o")
    , @NamedQuery(name = "Orders.findByOrderID", query = "SELECT o FROM Orders o WHERE o.orderID = :orderID")
    , @NamedQuery(name = "Orders.findByTimeOrder", query = "SELECT o FROM Orders o WHERE o.timeOrder = :timeOrder")
    , @NamedQuery(name = "Orders.findByTimeDelivery", query = "SELECT o FROM Orders o WHERE o.timeDelivery = :timeDelivery")
    , @NamedQuery(name = "Orders.findBySender", query = "SELECT o FROM Orders o WHERE o.sender = :sender")
    , @NamedQuery(name = "Orders.findBySenderPhone", query = "SELECT o FROM Orders o WHERE o.senderPhone = :senderPhone")
    , @NamedQuery(name = "Orders.findByReceiver", query = "SELECT o FROM Orders o WHERE o.receiver = :receiver")
    , @NamedQuery(name = "Orders.findByReceiverPhone", query = "SELECT o FROM Orders o WHERE o.receiverPhone = :receiverPhone")
    , @NamedQuery(name = "Orders.findByReceiverAddress", query = "SELECT o FROM Orders o WHERE o.receiverAddress = :receiverAddress")
    , @NamedQuery(name = "Orders.findByPriceTotal", query = "SELECT o FROM Orders o WHERE o.priceTotal = :priceTotal")
    , @NamedQuery(name = "Orders.findByDescriptions", query = "SELECT o FROM Orders o WHERE o.descriptions = :descriptions")})
public class Orders implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OrderID", nullable = false)
    private Integer orderID;
    @Size(max = 50)
    @Column(name = "TimeOrder", length = 50)
    private String timeOrder;
    @Size(max = 50)
    @Column(name = "TimeDelivery", length = 50)
    private String timeDelivery;
    @Size(max = 100)
    @Column(name = "Sender", length = 100)
    private String sender;
    @Size(max = 50)
    @Column(name = "SenderPhone", length = 50)
    private String senderPhone;
    @Size(max = 100)
    @Column(name = "Receiver", length = 100)
    private String receiver;
    @Size(max = 50)
    @Column(name = "ReceiverPhone", length = 50)
    private String receiverPhone;
    @Size(max = 2000)
    @Column(name = "ReceiverAddress", length = 2000)
    private String receiverAddress;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "PriceTotal", precision = 53)
    private Double priceTotal;
    @Size(max = 1073741823)
    @Column(name = "Descriptions", length = 1073741823)
    private String descriptions;
    @JoinColumn(name = "CustomerID", referencedColumnName = "CustomerID")
    @ManyToOne
    private Customer customerID;
    @JoinColumn(name = "OrderStatusID", referencedColumnName = "OrderStatusID")
    @ManyToOne
    private OrderStatus orderStatusID;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "orders")
    private Collection<OrderInfo> orderInfoCollection;

    public Orders() {
    }
//Code Insert
    @Transient
    public int cusID;
    @Transient
    public String OrderStatusName;

    public int getCusID() {
        return customerID.getCustomerID();
    }

    public String getOrderStatusName() {
        return orderStatusID.getOrderStatusName();
    }

    //end insertCode
    public Orders(Integer orderID) {
        this.orderID = orderID;
    }

    public Integer getOrderID() {
        return orderID;
    }

    public void setOrderID(Integer orderID) {
        this.orderID = orderID;
    }

    public String getTimeOrder() {
        return timeOrder;
    }

    public void setTimeOrder(String timeOrder) {
        this.timeOrder = timeOrder;
    }

    public String getTimeDelivery() {
        return timeDelivery;
    }

    public void setTimeDelivery(String timeDelivery) {
        this.timeDelivery = timeDelivery;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSenderPhone() {
        return senderPhone;
    }

    public void setSenderPhone(String senderPhone) {
        this.senderPhone = senderPhone;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public Double getPriceTotal() {
        return priceTotal;
    }

    public void setPriceTotal(Double priceTotal) {
        this.priceTotal = priceTotal;
    }

    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }

    public Customer getCustomerID() {
        return customerID;
    }

    public void setCustomerID(Customer customerID) {
        this.customerID = customerID;
    }

    public OrderStatus getOrderStatusID() {
        return orderStatusID;
    }

    public void setOrderStatusID(OrderStatus orderStatusID) {
        this.orderStatusID = orderStatusID;
    }

    @XmlTransient
    public Collection<OrderInfo> getOrderInfoCollection() {
        return orderInfoCollection;
    }

    public void setOrderInfoCollection(Collection<OrderInfo> orderInfoCollection) {
        this.orderInfoCollection = orderInfoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (orderID != null ? orderID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Orders)) {
            return false;
        }
        Orders other = (Orders) object;
        if ((this.orderID == null && other.orderID != null) || (this.orderID != null && !this.orderID.equals(other.orderID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.Orders[ orderID=" + orderID + " ]";
    }

}
