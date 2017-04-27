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
@Table(name = "ProductReviewComment", catalog = "PROJECTSEM4F21506S0_T2MH", schema = "dbo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProductReviewComment.findAll", query = "SELECT p FROM ProductReviewComment p")
    , @NamedQuery(name = "ProductReviewComment.findByTimePost", query = "SELECT p FROM ProductReviewComment p WHERE p.timePost = :timePost")
    , @NamedQuery(name = "ProductReviewComment.findByComments", query = "SELECT p FROM ProductReviewComment p WHERE p.comments = :comments")
    , @NamedQuery(name = "ProductReviewComment.findByPrcid", query = "SELECT p FROM ProductReviewComment p WHERE p.prcid = :prcid")})
public class ProductReviewComment implements Serializable {

    private static final long serialVersionUID = 1L;
    @Size(max = 50)
    @Column(name = "TimePost", length = 50)
    private String timePost;
    @Size(max = 2000)
    @Column(name = "Comments", length = 2000)
    private String comments;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column(name = "PRCID", nullable = false)
    private Integer prcid;
    @JoinColumn(name = "CustomerID", referencedColumnName = "CustomerID")
    @ManyToOne
    private Customer customerID;
    @JoinColumn(name = "PRID", referencedColumnName = "PRID")
    @ManyToOne
    private ProductReview prid;

    public ProductReviewComment() {
    }

    public ProductReviewComment(Integer prcid) {
        this.prcid = prcid;
    }

    public String getTimePost() {
        return timePost;
    }

    public void setTimePost(String timePost) {
        this.timePost = timePost;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Integer getPrcid() {
        return prcid;
    }

    public void setPrcid(Integer prcid) {
        this.prcid = prcid;
    }

    public Customer getCustomerID() {
        return customerID;
    }

    public void setCustomerID(Customer customerID) {
        this.customerID = customerID;
    }

    public ProductReview getPrid() {
        return prid;
    }

    public void setPrid(ProductReview prid) {
        this.prid = prid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (prcid != null ? prcid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProductReviewComment)) {
            return false;
        }
        ProductReviewComment other = (ProductReviewComment) object;
        if ((this.prcid == null && other.prcid != null) || (this.prcid != null && !this.prcid.equals(other.prcid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.ProductReviewComment[ prcid=" + prcid + " ]";
    }
    
}
