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
@Table(name = "Product", catalog = "PROJECTSEM4F21506S0_T2MH", schema = "dbo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Product.findAll", query = "SELECT p FROM Product p")
    , @NamedQuery(name = "Product.findByProductID", query = "SELECT p FROM Product p WHERE p.productID = :productID")
    , @NamedQuery(name = "Product.findByDateCreate", query = "SELECT p FROM Product p WHERE p.dateCreate = :dateCreate")
    , @NamedQuery(name = "Product.findByProductStatus", query = "SELECT p FROM Product p WHERE p.productStatus = :productStatus")
    , @NamedQuery(name = "Product.findByProductName", query = "SELECT p FROM Product p WHERE p.productName = :productName")
    , @NamedQuery(name = "Product.findByInventory", query = "SELECT p FROM Product p WHERE p.inventory = :inventory")
    , @NamedQuery(name = "Product.findByDescriptions", query = "SELECT p FROM Product p WHERE p.descriptions = :descriptions")})
public class Product implements Serializable {


    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ProductID", nullable = false)
    private Integer productID;
    @Size(max = 50)
    @Column(name = "DateCreate", length = 50)
    private String dateCreate;
    @Column(name = "ProductStatus")
    private Integer productStatus;
    @Size(max = 1000)
    @Column(name = "ProductName", length = 1000)
    private String productName;
    @Column(name = "Inventory")
    private Integer inventory;
    @Size(max = 1073741823)
    @Column(name = "Descriptions", length = 1073741823)
    private String descriptions;
    @JoinColumn(name = "BranchID", referencedColumnName = "BranchID")
    @ManyToOne
    private Branch branchID;
    @JoinColumn(name = "GroupID", referencedColumnName = "GroupID")
    @ManyToOne
    private Groups groupID;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product")
    private Collection<OrderInfo> orderInfoCollection;
    @OneToMany(mappedBy = "productID")
    private Collection<ProductReview> productReviewCollection;

    public Product() {
    }
//Code Insert
    @Transient
    public int bID;
    @Transient
    public String bName;
    @Transient
    public int bStatus;
    
    public int getbID() {
        return this.branchID.getBranchID();
    }

    public void setbID(int bID) {
        this.bID = this.branchID.getBranchID();
    }

    public String getbName() {
        return this.branchID.getBranchName();
    }

    public void setbName(String bName) {
        this.bName = this.branchID.getBranchName();
    }

    public int getbStatus() {
        return this.branchID.getBranchStatus();
    }

    public void setbStatus(int bStatus) {
        this.bStatus = this.branchID.getBranchStatus();
    }

    //end insert code
    public Product(Integer productID) {
        this.productID = productID;
    }

    public Integer getProductID() {
        return productID;
    }

    public void setProductID(Integer productID) {
        this.productID = productID;
    }

    public String getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(String dateCreate) {
        this.dateCreate = dateCreate;
    }

    public Integer getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(Integer productStatus) {
        this.productStatus = productStatus;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getInventory() {
        return inventory;
    }

    public void setInventory(Integer inventory) {
        this.inventory = inventory;
    }

    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }

    public Branch getBranchID() {
        return branchID;
    }

    public void setBranchID(Branch branchID) {
        this.branchID = branchID;
    }

    public Groups getGroupID() {
        return groupID;
    }

    public void setGroupID(Groups groupID) {
        this.groupID = groupID;
    }

    @XmlTransient
    public Collection<OrderInfo> getOrderInfoCollection() {
        return orderInfoCollection;
    }

    public void setOrderInfoCollection(Collection<OrderInfo> orderInfoCollection) {
        this.orderInfoCollection = orderInfoCollection;
    }

    @XmlTransient
    public Collection<ProductReview> getProductReviewCollection() {
        return productReviewCollection;
    }

    public void setProductReviewCollection(Collection<ProductReview> productReviewCollection) {
        this.productReviewCollection = productReviewCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (productID != null ? productID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Product)) {
            return false;
        }
        Product other = (Product) object;
        if ((this.productID == null && other.productID != null) || (this.productID != null && !this.productID.equals(other.productID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.Product[ productID=" + productID + " ]";
    }

}
