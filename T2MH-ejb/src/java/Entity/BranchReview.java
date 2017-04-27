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
@Table(name = "BranchReview", catalog = "PROJECTSEM4F21506S0_T2MH", schema = "dbo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BranchReview.findAll", query = "SELECT b FROM BranchReview b")
    , @NamedQuery(name = "BranchReview.findByBrid", query = "SELECT b FROM BranchReview b WHERE b.brid = :brid")
    , @NamedQuery(name = "BranchReview.findByTimePost", query = "SELECT b FROM BranchReview b WHERE b.timePost = :timePost")
    , @NamedQuery(name = "BranchReview.findByBRStars", query = "SELECT b FROM BranchReview b WHERE b.bRStars = :bRStars")
    , @NamedQuery(name = "BranchReview.findByComments", query = "SELECT b FROM BranchReview b WHERE b.comments = :comments")})
public class BranchReview implements Serializable {

    @OneToMany(mappedBy = "brid")
    private Collection<BranchReviewComment> branchReviewCommentCollection;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BRID", nullable = false)
    private Integer brid;
    @Size(max = 50)
    @Column(name = "TimePost", length = 50)
    private String timePost;
    @Column(name = "BRStars")
    private Integer bRStars;
    @Size(max = 2000)
    @Column(name = "Comments", length = 2000)
    private String comments;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "branchReview")
    private Collection<BranchCommentMark> branchCommentMarkCollection;
    @JoinColumn(name = "BranchID", referencedColumnName = "BranchID")
    @ManyToOne
    private Branch branchID;
    @JoinColumn(name = "CustomerID", referencedColumnName = "CustomerID")
    @ManyToOne
    private Customer customerID;

    //Code Insert
    @Transient
    public int bID;
    @Transient
    public int cID;

    public int getbID() {
        return this.branchID.getBranchID();
    }

    public void setbID(int bID) {
        this.bID = this.branchID.getBranchID();
    }

    public int getcID() {
        return this.customerID.getCustomerID();
    }

    public void setcID(int cID) {
        this.cID = this.customerID.getCustomerID();
    }

    //
    public BranchReview() {
    }

    public Integer getbRStars() {
        return bRStars;
    }

    public void setbRStars(Integer bRStars) {
        this.bRStars = bRStars;
    }

    public BranchReview(Integer brid) {
        this.brid = brid;
    }

    public Integer getBrid() {
        return brid;
    }

    public void setBrid(Integer brid) {
        this.brid = brid;
    }

    public String getTimePost() {
        return timePost;
    }

    public void setTimePost(String timePost) {
        this.timePost = timePost;
    }

    public Integer getBRStars() {
        return bRStars;
    }

    public void setBRStars(Integer bRStars) {
        this.bRStars = bRStars;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @XmlTransient
    public Collection<BranchCommentMark> getBranchCommentMarkCollection() {
        return branchCommentMarkCollection;
    }

    public void setBranchCommentMarkCollection(Collection<BranchCommentMark> branchCommentMarkCollection) {
        this.branchCommentMarkCollection = branchCommentMarkCollection;
    }

    public Branch getBranchID() {
        return branchID;
    }

    public void setBranchID(Branch branchID) {
        this.branchID = branchID;
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
        hash += (brid != null ? brid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BranchReview)) {
            return false;
        }
        BranchReview other = (BranchReview) object;
        if ((this.brid == null && other.brid != null) || (this.brid != null && !this.brid.equals(other.brid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.BranchReview[ brid=" + brid + " ]";
    }

    @XmlTransient
    public Collection<BranchReviewComment> getBranchReviewCommentCollection() {
        return branchReviewCommentCollection;
    }

    public void setBranchReviewCommentCollection(Collection<BranchReviewComment> branchReviewCommentCollection) {
        this.branchReviewCommentCollection = branchReviewCommentCollection;
    }

}
