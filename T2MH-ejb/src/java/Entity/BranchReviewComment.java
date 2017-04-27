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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author 19319
 */
@Entity
@Table(name = "BranchReviewComment", catalog = "PROJECTSEM4F21506S0_T2MH", schema = "dbo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BranchReviewComment.findAll", query = "SELECT b FROM BranchReviewComment b")
    , @NamedQuery(name = "BranchReviewComment.findByDatePost", query = "SELECT b FROM BranchReviewComment b WHERE b.datePost = :datePost")
    , @NamedQuery(name = "BranchReviewComment.findByComments", query = "SELECT b FROM BranchReviewComment b WHERE b.comments = :comments")
    , @NamedQuery(name = "BranchReviewComment.findByBrcid", query = "SELECT b FROM BranchReviewComment b WHERE b.brcid = :brcid")})
public class BranchReviewComment implements Serializable {

    private static final long serialVersionUID = 1L;
    @Size(max = 50)
    @Column(name = "DatePost", length = 50)
    private String datePost;
    @Size(max = 2000)
    @Column(name = "Comments", length = 2000)
    private String comments;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column(name = "BRCID", nullable = false)
    private Integer brcid;
    @JoinColumn(name = "BRID", referencedColumnName = "BRID")
    @ManyToOne
    private BranchReview brid;
    @JoinColumn(name = "CustomerID", referencedColumnName = "CustomerID")
    @ManyToOne
    private Customer customerID;

    public BranchReviewComment() {
    }

    public BranchReviewComment(Integer brcid) {
        this.brcid = brcid;
    }

    public String getDatePost() {
        return datePost;
    }

    public void setDatePost(String datePost) {
        this.datePost = datePost;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Integer getBrcid() {
        return brcid;
    }

    public void setBrcid(Integer brcid) {
        this.brcid = brcid;
    }

    public BranchReview getBrid() {
        return brid;
    }

    public void setBrid(BranchReview brid) {
        this.brid = brid;
    }

    public Customer getCustomerID() {
        return customerID;
    }

    public void setCustomerID(Customer customerID) {
        this.customerID = customerID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (brcid != null ? brcid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BranchReviewComment)) {
            return false;
        }
        BranchReviewComment other = (BranchReviewComment) object;
        if ((this.brcid == null && other.brcid != null) || (this.brcid != null && !this.brcid.equals(other.brcid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.BranchReviewComment[ brcid=" + brcid + " ]";
    }
    
}
