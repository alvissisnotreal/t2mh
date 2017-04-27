/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author 19319
 */
@Embeddable
public class ProductCommentMarkPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "PRID", nullable = false)
    private int prid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CustomerID", nullable = false)
    private int customerID;

    public ProductCommentMarkPK() {
    }

    public ProductCommentMarkPK(int prid, int customerID) {
        this.prid = prid;
        this.customerID = customerID;
    }

    public int getPrid() {
        return prid;
    }

    public void setPrid(int prid) {
        this.prid = prid;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) prid;
        hash += (int) customerID;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProductCommentMarkPK)) {
            return false;
        }
        ProductCommentMarkPK other = (ProductCommentMarkPK) object;
        if (this.prid != other.prid) {
            return false;
        }
        if (this.customerID != other.customerID) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.ProductCommentMarkPK[ prid=" + prid + ", customerID=" + customerID + " ]";
    }
    
}
