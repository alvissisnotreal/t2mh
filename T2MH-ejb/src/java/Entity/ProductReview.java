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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author 19319
 */
@Entity
@Table(name = "ProductReview", catalog = "PROJECTSEM4F21506S0_T2MH", schema = "dbo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProductReview.findAll", query = "SELECT p FROM ProductReview p")
    , @NamedQuery(name = "ProductReview.findByPrid", query = "SELECT p FROM ProductReview p WHERE p.prid = :prid")
    , @NamedQuery(name = "ProductReview.findByTimePost", query = "SELECT p FROM ProductReview p WHERE p.timePost = :timePost")
    , @NamedQuery(name = "ProductReview.findByPRStars", query = "SELECT p FROM ProductReview p WHERE p.pRStars = :pRStars")
    , @NamedQuery(name = "ProductReview.findByComments", query = "SELECT p FROM ProductReview p WHERE p.comments = :comments")})
public class ProductReview implements Serializable {

    @OneToMany(mappedBy = "prid")
    private Collection<ProductReviewComment> productReviewCommentCollection;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column(name = "PRID", nullable = false)
    private Integer prid;
    @Size(max = 50)
    @Column(name = "TimePost", length = 50)
    private String timePost;
    @Column(name = "PRStars")
    private Integer pRStars;
    @Size(max = 2000)
    @Column(name = "Comments", length = 2000)
    private String comments;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "productReview")
    private Collection<ProductCommentMark> productCommentMarkCollection;
    @JoinColumn(name = "CustomerID", referencedColumnName = "CustomerID")
    @ManyToOne
    private Customer customerID;
    @JoinColumn(name = "ProductID", referencedColumnName = "ProductID")
    @ManyToOne
    private Product productID;

    public ProductReview() {
    }

    //code insert
    public Integer getpRStars() {
        return pRStars;
    }

    public void setpRStars(Integer pRStars) {
        this.pRStars = pRStars;
    }

    //
    public ProductReview(Integer prid) {
        this.prid = prid;
    }

    public Integer getPrid() {
        return prid;
    }

    public void setPrid(Integer prid) {
        this.prid = prid;
    }

    public String getTimePost() {
        return timePost;
    }

    public void setTimePost(String timePost) {
        this.timePost = timePost;
    }

    public Integer getPRStars() {
        return pRStars;
    }

    public void setPRStars(Integer pRStars) {
        this.pRStars = pRStars;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @XmlTransient
    public Collection<ProductCommentMark> getProductCommentMarkCollection() {
        return productCommentMarkCollection;
    }

    public void setProductCommentMarkCollection(Collection<ProductCommentMark> productCommentMarkCollection) {
        this.productCommentMarkCollection = productCommentMarkCollection;
    }

    public Customer getCustomerID() {
        return customerID;
    }

    public void setCustomerID(Customer customerID) {
        this.customerID = customerID;
    }

    public Product getProductID() {
        return productID;
    }

    public void setProductID(Product productID) {
        this.productID = productID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (prid != null ? prid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProductReview)) {
            return false;
        }
        ProductReview other = (ProductReview) object;
        if ((this.prid == null && other.prid != null) || (this.prid != null && !this.prid.equals(other.prid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.ProductReview[ prid=" + prid + " ]";
    }

    @XmlTransient
    public Collection<ProductReviewComment> getProductReviewCommentCollection() {
        return productReviewCommentCollection;
    }

    public void setProductReviewCommentCollection(Collection<ProductReviewComment> productReviewCommentCollection) {
        this.productReviewCommentCollection = productReviewCommentCollection;
    }

}
