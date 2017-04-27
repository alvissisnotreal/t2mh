/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author 19319
 */
@Entity
@Table(name = "BranchCommentMark", catalog = "PROJECTSEM4F21506S0_T2MH", schema = "dbo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BranchCommentMark.findAll", query = "SELECT b FROM BranchCommentMark b")
    , @NamedQuery(name = "BranchCommentMark.findByBrid", query = "SELECT b FROM BranchCommentMark b WHERE b.branchCommentMarkPK.brid = :brid")
    , @NamedQuery(name = "BranchCommentMark.findByCustomerID", query = "SELECT b FROM BranchCommentMark b WHERE b.branchCommentMarkPK.customerID = :customerID")
    , @NamedQuery(name = "BranchCommentMark.findByBCMStars", query = "SELECT b FROM BranchCommentMark b WHERE b.bCMStars = :bCMStars")})
public class BranchCommentMark implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected BranchCommentMarkPK branchCommentMarkPK;
    @Column(name = "BCMStars")
    private Integer bCMStars;
    @JoinColumn(name = "BRID", referencedColumnName = "BRID", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private BranchReview branchReview;
    @JoinColumn(name = "CustomerID", referencedColumnName = "CustomerID", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Customer customer;

    public BranchCommentMark() {
    }

    public BranchCommentMark(BranchCommentMarkPK branchCommentMarkPK) {
        this.branchCommentMarkPK = branchCommentMarkPK;
    }

    public BranchCommentMark(int brid, int customerID) {
        this.branchCommentMarkPK = new BranchCommentMarkPK(brid, customerID);
    }

    public BranchCommentMarkPK getBranchCommentMarkPK() {
        return branchCommentMarkPK;
    }

    public void setBranchCommentMarkPK(BranchCommentMarkPK branchCommentMarkPK) {
        this.branchCommentMarkPK = branchCommentMarkPK;
    }

    public Integer getBCMStars() {
        return bCMStars;
    }

    public void setBCMStars(Integer bCMStars) {
        this.bCMStars = bCMStars;
    }

    public BranchReview getBranchReview() {
        return branchReview;
    }

    public void setBranchReview(BranchReview branchReview) {
        this.branchReview = branchReview;
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
        hash += (branchCommentMarkPK != null ? branchCommentMarkPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BranchCommentMark)) {
            return false;
        }
        BranchCommentMark other = (BranchCommentMark) object;
        if ((this.branchCommentMarkPK == null && other.branchCommentMarkPK != null) || (this.branchCommentMarkPK != null && !this.branchCommentMarkPK.equals(other.branchCommentMarkPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.BranchCommentMark[ branchCommentMarkPK=" + branchCommentMarkPK + " ]";
    }
    
}
