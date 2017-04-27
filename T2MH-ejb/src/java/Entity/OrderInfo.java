/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import java.io.Serializable;
import java.util.HashMap;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.xpath.XPathConstants;
import org.w3c.dom.NodeList;

/**
 *
 * @author 19319
 */
@Entity
@Table(name = "OrderInfo", catalog = "PROJECTSEM4F21506S0_T2MH", schema = "dbo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OrderInfo.findAll", query = "SELECT o FROM OrderInfo o")
    , @NamedQuery(name = "OrderInfo.findByOrderID", query = "SELECT o FROM OrderInfo o WHERE o.orderInfoPK.orderID = :orderID")
    , @NamedQuery(name = "OrderInfo.findByProductID", query = "SELECT o FROM OrderInfo o WHERE o.orderInfoPK.productID = :productID")
    , @NamedQuery(name = "OrderInfo.findByQuantity", query = "SELECT o FROM OrderInfo o WHERE o.quantity = :quantity")
    , @NamedQuery(name = "OrderInfo.findByDescriptions", query = "SELECT o FROM OrderInfo o WHERE o.descriptions = :descriptions")})
public class OrderInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected OrderInfoPK orderInfoPK;
    @Column(name = "Quantity")
    private Integer quantity;
    @Size(max = 1073741823)
    @Column(name = "Descriptions", length = 1073741823)
    private String descriptions;
    @JoinColumn(name = "OrderID", referencedColumnName = "OrderID", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Orders orders;
    @JoinColumn(name = "ProductID", referencedColumnName = "ProductID", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Product product;

    public OrderInfo() {
    }
//Code Insert
    @Transient
    public int OID;
    @Transient
    public int PID;
    @Transient
    public String TimeOrder;

    public String getTimeOrder() {
        return orders.getTimeOrder();
    }

    public void setTimeOrder(String TimeOrder) {
        this.TimeOrder = orders.getTimeOrder();
    }

    public int getOID() {
        return orders.getOrderID();
    }

    public int getPID() {
        return product.getProductID();
    }

    public void setOID(int oID) {
        this.OID = orders.getOrderID();
    }

    public void setPID(int pID) {
        this.PID = product.getProductID();
    }

    //end insertCode
    public OrderInfo(OrderInfoPK orderInfoPK) {
        this.orderInfoPK = orderInfoPK;
    }

    public OrderInfo(int orderID, int productID) {
        this.orderInfoPK = new OrderInfoPK(orderID, productID);
    }

    public OrderInfoPK getOrderInfoPK() {
        return orderInfoPK;
    }

    public void setOrderInfoPK(OrderInfoPK orderInfoPK) {
        this.orderInfoPK = orderInfoPK;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }

    public Orders getOrders() {
        return orders;
    }

    public void setOrders(Orders orders) {
        this.orders = orders;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (orderInfoPK != null ? orderInfoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OrderInfo)) {
            return false;
        }
        OrderInfo other = (OrderInfo) object;
        if ((this.orderInfoPK == null && other.orderInfoPK != null) || (this.orderInfoPK != null && !this.orderInfoPK.equals(other.orderInfoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.OrderInfo[ orderInfoPK=" + orderInfoPK + " ]";
    }

}
