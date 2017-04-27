/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author 19319
 */
@Entity
@Table(name = "CustomerInfo", catalog = "PROJECTSEM4F21506S0_T2MH", schema = "dbo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CustomerInfo.findAll", query = "SELECT c FROM CustomerInfo c")
    , @NamedQuery(name = "CustomerInfo.findByCustomerID", query = "SELECT c FROM CustomerInfo c WHERE c.customerID = :customerID")
    , @NamedQuery(name = "CustomerInfo.findByCustomerName", query = "SELECT c FROM CustomerInfo c WHERE c.customerName = :customerName")
    , @NamedQuery(name = "CustomerInfo.findByCustomerPhone", query = "SELECT c FROM CustomerInfo c WHERE c.customerPhone = :customerPhone")
    , @NamedQuery(name = "CustomerInfo.findByCustomerEmail", query = "SELECT c FROM CustomerInfo c WHERE c.customerEmail = :customerEmail")
    , @NamedQuery(name = "CustomerInfo.findByCustomerAddress", query = "SELECT c FROM CustomerInfo c WHERE c.customerAddress = :customerAddress")
    , @NamedQuery(name = "CustomerInfo.findByDescriptions", query = "SELECT c FROM CustomerInfo c WHERE c.descriptions = :descriptions")})
public class CustomerInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "CustomerID", nullable = false)
    private Integer customerID;
    @Size(max = 100)
    @Column(name = "CustomerName", length = 100)
    private String customerName;
    @Size(max = 50)
    @Column(name = "CustomerPhone", length = 50)
    private String customerPhone;
    @Size(max = 100)
    @Column(name = "CustomerEmail", length = 100)
    private String customerEmail;
    @Size(max = 2000)
    @Column(name = "CustomerAddress", length = 2000)
    private String customerAddress;
    @Size(max = 1073741823)
    @Column(name = "Descriptions", length = 1073741823)
    private String descriptions;
    @JoinColumn(name = "CustomerID", referencedColumnName = "CustomerID", nullable = false, insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Customer customer;

    public CustomerInfo() {
    }

    public CustomerInfo(Integer customerID) {
        this.customerID = customerID;
    }

    public Integer getCustomerID() {
        return customerID;
    }

    public void setCustomerID(Integer customerID) {
        this.customerID = customerID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (customerID != null ? customerID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CustomerInfo)) {
            return false;
        }
        CustomerInfo other = (CustomerInfo) object;
        if ((this.customerID == null && other.customerID != null) || (this.customerID != null && !this.customerID.equals(other.customerID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.CustomerInfo[ customerID=" + customerID + " ]";
    }
    
}
