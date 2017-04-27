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
@Table(name = "ProductCommentMark", catalog = "PROJECTSEM4F21506S0_T2MH", schema = "dbo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProductCommentMark.findAll", query = "SELECT p FROM ProductCommentMark p")
    , @NamedQuery(name = "ProductCommentMark.findByPrid", query = "SELECT p FROM ProductCommentMark p WHERE p.productCommentMarkPK.prid = :prid")
    , @NamedQuery(name = "ProductCommentMark.findByCustomerID", query = "SELECT p FROM ProductCommentMark p WHERE p.productCommentMarkPK.customerID = :customerID")
    , @NamedQuery(name = "ProductCommentMark.findByStars", query = "SELECT p FROM ProductCommentMark p WHERE p.stars = :stars")})
public class ProductCommentMark implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ProductCommentMarkPK productCommentMarkPK;
    @Column(name = "Stars")
    private Integer stars;
    @JoinColumn(name = "CustomerID", referencedColumnName = "CustomerID", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Customer customer;
    @JoinColumn(name = "PRID", referencedColumnName = "PRID", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private ProductReview productReview;

    public ProductCommentMark() {
    }

    public ProductCommentMark(ProductCommentMarkPK productCommentMarkPK) {
        this.productCommentMarkPK = productCommentMarkPK;
    }

    public ProductCommentMark(int prid, int customerID) {
        this.productCommentMarkPK = new ProductCommentMarkPK(prid, customerID);
    }

    public ProductCommentMarkPK getProductCommentMarkPK() {
        return productCommentMarkPK;
    }

    public void setProductCommentMarkPK(ProductCommentMarkPK productCommentMarkPK) {
        this.productCommentMarkPK = productCommentMarkPK;
    }

    public Integer getStars() {
        return stars;
    }

    public void setStars(Integer stars) {
        this.stars = stars;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public ProductReview getProductReview() {
        return productReview;
    }

    public void setProductReview(ProductReview productReview) {
        this.productReview = productReview;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (productCommentMarkPK != null ? productCommentMarkPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProductCommentMark)) {
            return false;
        }
        ProductCommentMark other = (ProductCommentMark) object;
        if ((this.productCommentMarkPK == null && other.productCommentMarkPK != null) || (this.productCommentMarkPK != null && !this.productCommentMarkPK.equals(other.productCommentMarkPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.ProductCommentMark[ productCommentMarkPK=" + productCommentMarkPK + " ]";
    }
    
}
