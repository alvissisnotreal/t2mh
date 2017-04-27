/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author 19319
 */
@Entity
@Table(name = "BranchManager", catalog = "PROJECTSEM4F21506S0_T2MH", schema = "dbo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BranchManager.findAll", query = "SELECT b FROM BranchManager b")
    , @NamedQuery(name = "BranchManager.findByBmUsername", query = "SELECT b FROM BranchManager b WHERE b.bmUsername = :bmUsername")
    , @NamedQuery(name = "BranchManager.findByBmPassword", query = "SELECT b FROM BranchManager b WHERE b.bmPassword = :bmPassword")
    , @NamedQuery(name = "BranchManager.findByBMStatus", query = "SELECT b FROM BranchManager b WHERE b.bMStatus = :bMStatus")})
public class BranchManager implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "BmUsername", nullable = false, length = 50)
    private String bmUsername;
    @Size(max = 200)
    @Column(name = "BmPassword", length = 200)
    private String bmPassword;
    @Column(name = "BMStatus")
    private Integer bMStatus;
    @JoinColumn(name = "BranchID", referencedColumnName = "BranchID")
    @ManyToOne
    private Branch branchID;

    public BranchManager() {
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

//    @PostConstruct
//    public void init() {
//        this.branchID.getBranchID();
//        this.branchID.getBranchName();
//        branchID.getBranchStatus();
//    }

    //
    public BranchManager(String bmUsername) {
        this.bmUsername = bmUsername;
    }

    public String getBmUsername() {
        return bmUsername;
    }

    public void setBmUsername(String bmUsername) {
        this.bmUsername = bmUsername;
    }

    public String getBmPassword() {
        return bmPassword;
    }

    public void setBmPassword(String bmPassword) {
        this.bmPassword = bmPassword;
    }

    public Integer getBMStatus() {
        return bMStatus;
    }

    public void setBMStatus(Integer bMStatus) {
        this.bMStatus = bMStatus;
    }

    public Branch getBranchID() {
        return branchID;
    }

    public void setBranchID(Branch branchID) {
        this.branchID = branchID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (bmUsername != null ? bmUsername.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BranchManager)) {
            return false;
        }
        BranchManager other = (BranchManager) object;
        if ((this.bmUsername == null && other.bmUsername != null) || (this.bmUsername != null && !this.bmUsername.equals(other.bmUsername))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.BranchManager[ bmUsername=" + bmUsername + " ]";
    }

}
