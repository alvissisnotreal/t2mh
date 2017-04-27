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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
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
//@Table(name = "Branch", catalog = "PROJECTSEM4F21506S0_T2MH", schema = "dbo", uniqueConstraints = {
//    @UniqueConstraint(columnNames = {"BranchName"})})
@Table(name = "Branch", catalog = "PROJECTSEM4F21506S0_T2MH", schema = "dbo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Branch.findAll", query = "SELECT b FROM Branch b")
    , @NamedQuery(name = "Branch.findByBranchID", query = "SELECT b FROM Branch b WHERE b.branchID = :branchID")
    , @NamedQuery(name = "Branch.findByBranchName", query = "SELECT b FROM Branch b WHERE b.branchName = :branchName")
    , @NamedQuery(name = "Branch.findByBranchDescriptions", query = "SELECT b FROM Branch b WHERE b.branchDescriptions = :branchDescriptions")
    , @NamedQuery(name = "Branch.findByBranchStatus", query = "SELECT b FROM Branch b WHERE b.branchStatus = :branchStatus")})
public class Branch implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column(name = "BranchID", nullable = false , updatable = true)
    private Integer branchID;
    @Size(max = 450)
    @Column(name = "BranchName", length = 450)
    private String branchName;
    @Size(max = 1073741823)
    @Column(name = "BranchDescriptions", length = 1073741823)
    private String branchDescriptions;
    @Column(name = "BranchStatus")
    private Integer branchStatus;
    @OneToMany(mappedBy = "branchID")
    private Collection<BranchManager> branchManagerCollection;
    @OneToMany(mappedBy = "branchID")
    private Collection<Product> productCollection;
    @OneToMany(mappedBy = "branchID")
    private Collection<BranchReview> branchReviewCollection;
    @OneToMany(mappedBy = "branchID")
    private Collection<Payment> paymentCollection;

    public Branch() {
        branchID=0;
    }

    public Branch(Integer branchID) {
        this.branchID = branchID;
    }

    public Integer getBranchID() {
        return branchID;
    }

    public void setBranchID(Integer branchID) {
        this.branchID = branchID;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getBranchDescriptions() {
        return branchDescriptions;
    }

    public void setBranchDescriptions(String branchDescriptions) {
        this.branchDescriptions = branchDescriptions;
    }

    public Integer getBranchStatus() {
        return branchStatus;
    }

    public void setBranchStatus(Integer branchStatus) {
        this.branchStatus = branchStatus;
    }

    @XmlTransient
    public Collection<BranchManager> getBranchManagerCollection() {
        return branchManagerCollection;
    }

    public void setBranchManagerCollection(Collection<BranchManager> branchManagerCollection) {
        this.branchManagerCollection = branchManagerCollection;
    }

    @XmlTransient
    public Collection<Product> getProductCollection() {
        return productCollection;
    }

    public void setProductCollection(Collection<Product> productCollection) {
        this.productCollection = productCollection;
    }

    @XmlTransient
    public Collection<BranchReview> getBranchReviewCollection() {
        return branchReviewCollection;
    }

    public void setBranchReviewCollection(Collection<BranchReview> branchReviewCollection) {
        this.branchReviewCollection = branchReviewCollection;
    }

    @XmlTransient
    public Collection<Payment> getPaymentCollection() {
        return paymentCollection;
    }

    public void setPaymentCollection(Collection<Payment> paymentCollection) {
        this.paymentCollection = paymentCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (branchID != null ? branchID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Branch)) {
            return false;
        }
        Branch other = (Branch) object;
        if ((this.branchID == null && other.branchID != null) || (this.branchID != null && !this.branchID.equals(other.branchID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.Branch[ branchID=" + branchID + " ]";
    }
    
}
