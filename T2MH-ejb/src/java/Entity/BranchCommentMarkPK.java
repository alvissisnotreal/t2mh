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
public class BranchCommentMarkPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "BRID", nullable = false)
    private int brid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CustomerID", nullable = false)
    private int customerID;

    public BranchCommentMarkPK() {
    }

    public BranchCommentMarkPK(int brid, int customerID) {
        this.brid = brid;
        this.customerID = customerID;
    }

    public int getBrid() {
        return brid;
    }

    public void setBrid(int brid) {
        this.brid = brid;
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
        hash += (int) brid;
        hash += (int) customerID;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BranchCommentMarkPK)) {
            return false;
        }
        BranchCommentMarkPK other = (BranchCommentMarkPK) object;
        if (this.brid != other.brid) {
            return false;
        }
        if (this.customerID != other.customerID) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.BranchCommentMarkPK[ brid=" + brid + ", customerID=" + customerID + " ]";
    }
    
}
