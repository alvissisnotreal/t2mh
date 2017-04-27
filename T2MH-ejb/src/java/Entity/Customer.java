/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import java.io.Serializable;
import java.util.Collection;
import javax.annotation.PostConstruct;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author 19319
 */
@Entity
@Table(name = "Customer", catalog = "PROJECTSEM4F21506S0_T2MH", schema = "dbo", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"CustomerUsername"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Customer.findAll", query = "SELECT c FROM Customer c")
    , @NamedQuery(name = "Customer.findByCustomerID", query = "SELECT c FROM Customer c WHERE c.customerID = :customerID")
    , @NamedQuery(name = "Customer.findByCustomerUsername", query = "SELECT c FROM Customer c WHERE c.customerUsername = :customerUsername")
    , @NamedQuery(name = "Customer.findByCustomerPassword", query = "SELECT c FROM Customer c WHERE c.customerPassword = :customerPassword")
    , @NamedQuery(name = "Customer.findByCustomerStatus", query = "SELECT c FROM Customer c WHERE c.customerStatus = :customerStatus")})
public class Customer implements Serializable {

    @OneToMany(mappedBy = "customerID")
    private Collection<BranchReviewComment> branchReviewCommentCollection;
    @OneToMany(mappedBy = "customerID")
    private Collection<ProductReviewComment> productReviewCommentCollection;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column(name = "CustomerID", nullable = false)
    private Integer customerID;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "CustomerUsername", nullable = false, length = 50)
    private String customerUsername;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "CustomerPassword", nullable = false, length = 200)
    private String customerPassword;
    @Column(name = "CustomerStatus")
    private Integer customerStatus;
    @OneToMany(mappedBy = "customerID")
    private Collection<Orders> ordersCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customer")
    private Collection<ProductCommentMark> productCommentMarkCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customer")
    private Collection<BranchCommentMark> branchCommentMarkCollection;
    @OneToMany(mappedBy = "customerID")
    private Collection<BranchReview> branchReviewCollection;
    @OneToMany(mappedBy = "customerID")
    private Collection<ProductReview> productReviewCollection;
    @OneToMany(mappedBy = "customerID")
    private Collection<Feedback> feedbackCollection;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "customer")
    private CustomerInfo customerInfo;
    //Code Insert
    @Transient
    public String name;
    @Transient
    public String phone;
    @Transient
    public String email;

    public String getName() {
        try {
            return customerInfo.getCustomerName();
        } catch (Exception e) {
        }
        return null;
    }

    public void setName(String name) {
        try {
            this.name = customerInfo.getCustomerName();
        } catch (Exception e) {
        }
        this.name = "";
    }

    public String getPhone() {
        try {
           return customerInfo.getCustomerPhone();
        } catch (Exception e) {
        }
        return null;
    }

    public void setPhone(String phone) {
        try {
            this.phone = customerInfo.getCustomerPhone();
        } catch (Exception e) {
        }
        this.name = "";
    }

    public String getEmail() {
         try {
            return customerInfo.getCustomerEmail();
        } catch (Exception e) {
        }
        return null;
    }

    public void setEmail(String email) {
        try {
            this.name = customerInfo.getCustomerEmail();
        } catch (Exception e) {
        }
        this.name = "";
    }

    //
    public Customer() {
    }

    public Customer(Integer customerID) {
        this.customerID = customerID;
    }

    public Customer(Integer customerID, String customerUsername, String customerPassword) {
        this.customerID = customerID;
        this.customerUsername = customerUsername;
        this.customerPassword = customerPassword;
    }

    public Integer getCustomerID() {
        return customerID;
    }

    public void setCustomerID(Integer customerID) {
        this.customerID = customerID;
    }

    public String getCustomerUsername() {
        return customerUsername;
    }

    public void setCustomerUsername(String customerUsername) {
        this.customerUsername = customerUsername;
    }

    public String getCustomerPassword() {
        return customerPassword;
    }

    public void setCustomerPassword(String customerPassword) {
        this.customerPassword = customerPassword;
    }

    public Integer getCustomerStatus() {
        return customerStatus;
    }

    public void setCustomerStatus(Integer customerStatus) {
        this.customerStatus = customerStatus;
    }

    @XmlTransient
    public Collection<Orders> getOrdersCollection() {
        return ordersCollection;
    }

    public void setOrdersCollection(Collection<Orders> ordersCollection) {
        this.ordersCollection = ordersCollection;
    }

    @XmlTransient
    public Collection<ProductCommentMark> getProductCommentMarkCollection() {
        return productCommentMarkCollection;
    }

    public void setProductCommentMarkCollection(Collection<ProductCommentMark> productCommentMarkCollection) {
        this.productCommentMarkCollection = productCommentMarkCollection;
    }

    @XmlTransient
    public Collection<BranchCommentMark> getBranchCommentMarkCollection() {
        return branchCommentMarkCollection;
    }

    public void setBranchCommentMarkCollection(Collection<BranchCommentMark> branchCommentMarkCollection) {
        this.branchCommentMarkCollection = branchCommentMarkCollection;
    }

    @XmlTransient
    public Collection<BranchReview> getBranchReviewCollection() {
        return branchReviewCollection;
    }

    public void setBranchReviewCollection(Collection<BranchReview> branchReviewCollection) {
        this.branchReviewCollection = branchReviewCollection;
    }

    @XmlTransient
    public Collection<ProductReview> getProductReviewCollection() {
        return productReviewCollection;
    }

    public void setProductReviewCollection(Collection<ProductReview> productReviewCollection) {
        this.productReviewCollection = productReviewCollection;
    }

    @XmlTransient
    public Collection<Feedback> getFeedbackCollection() {
        return feedbackCollection;
    }

    public void setFeedbackCollection(Collection<Feedback> feedbackCollection) {
        this.feedbackCollection = feedbackCollection;
    }

    public CustomerInfo getCustomerInfo() {
        return customerInfo;
    }

    public void setCustomerInfo(CustomerInfo customerInfo) {
        this.customerInfo = customerInfo;
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
        if (!(object instanceof Customer)) {
            return false;
        }
        Customer other = (Customer) object;
        if ((this.customerID == null && other.customerID != null) || (this.customerID != null && !this.customerID.equals(other.customerID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.Customer[ customerID=" + customerID + " ]";
    }

    @XmlTransient
    public Collection<BranchReviewComment> getBranchReviewCommentCollection() {
        return branchReviewCommentCollection;
    }

    public void setBranchReviewCommentCollection(Collection<BranchReviewComment> branchReviewCommentCollection) {
        this.branchReviewCommentCollection = branchReviewCommentCollection;
    }

    @XmlTransient
    public Collection<ProductReviewComment> getProductReviewCommentCollection() {
        return productReviewCommentCollection;
    }

    public void setProductReviewCommentCollection(Collection<ProductReviewComment> productReviewCommentCollection) {
        this.productReviewCommentCollection = productReviewCommentCollection;
    }

}
