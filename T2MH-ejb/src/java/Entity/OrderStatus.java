/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author 19319
 */
@Entity
@Table(name = "OrderStatus", catalog = "PROJECTSEM4F21506S0_T2MH", schema = "dbo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OrderStatus.findAll", query = "SELECT o FROM OrderStatus o")
    , @NamedQuery(name = "OrderStatus.findByOrderStatusID", query = "SELECT o FROM OrderStatus o WHERE o.orderStatusID = :orderStatusID")
    , @NamedQuery(name = "OrderStatus.findByOrderStatusName", query = "SELECT o FROM OrderStatus o WHERE o.orderStatusName = :orderStatusName")
    , @NamedQuery(name = "OrderStatus.findByDescriptions", query = "SELECT o FROM OrderStatus o WHERE o.descriptions = :descriptions")})
public class OrderStatus implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "OrderStatusID", nullable = false)
    private Integer orderStatusID;
    @Size(max = 4000)
    @Column(name = "OrderStatusName", length = 4000)
    private String orderStatusName;
    @Size(max = 1073741823)
    @Column(name = "Descriptions", length = 1073741823)
    private String descriptions;
    @OneToMany(mappedBy = "orderStatusID")
    private Collection<Orders> ordersCollection;

    public OrderStatus() {
    }

    public OrderStatus(Integer orderStatusID) {
        this.orderStatusID = orderStatusID;
    }

    public Integer getOrderStatusID() {
        return orderStatusID;
    }

    public void setOrderStatusID(Integer orderStatusID) {
        this.orderStatusID = orderStatusID;
    }

    public String getOrderStatusName() {
        return orderStatusName;
    }

    public void setOrderStatusName(String orderStatusName) {
        this.orderStatusName = orderStatusName;
    }

    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }

    @XmlTransient
    public Collection<Orders> getOrdersCollection() {
        return ordersCollection;
    }

    public void setOrdersCollection(Collection<Orders> ordersCollection) {
        this.ordersCollection = ordersCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (orderStatusID != null ? orderStatusID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OrderStatus)) {
            return false;
        }
        OrderStatus other = (OrderStatus) object;
        if ((this.orderStatusID == null && other.orderStatusID != null) || (this.orderStatusID != null && !this.orderStatusID.equals(other.orderStatusID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.OrderStatus[ orderStatusID=" + orderStatusID + " ]";
    }
    
}
