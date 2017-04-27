/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import java.io.Serializable;
import java.util.Collection;
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
@Table(name = "Groups", catalog = "PROJECTSEM4F21506S0_T2MH", schema = "dbo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Groups.findAll", query = "SELECT g FROM Groups g")
    , @NamedQuery(name = "Groups.findByGroupID", query = "SELECT g FROM Groups g WHERE g.groupID = :groupID")
    , @NamedQuery(name = "Groups.findByGroupName", query = "SELECT g FROM Groups g WHERE g.groupName = :groupName")
    , @NamedQuery(name = "Groups.findByGroupStatus", query = "SELECT g FROM Groups g WHERE g.groupStatus = :groupStatus")
    , @NamedQuery(name = "Groups.findByGroupDescriptions", query = "SELECT g FROM Groups g WHERE g.groupDescriptions = :groupDescriptions")})
public class Groups implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column(name = "GroupID", nullable = false)
    private Integer groupID;
    @Size(max = 200)
    @Column(name = "GroupName", length = 200)
    private String groupName;
    @Column(name = "GroupStatus")
    private Integer groupStatus;
    @Size(max = 1073741823)
    @Column(name = "GroupDescriptions", length = 1073741823)
    private String groupDescriptions;
    @OneToMany(mappedBy = "groupID")
    private Collection<Product> productCollection;
    @JoinColumn(name = "CateID", referencedColumnName = "CateID")
    @ManyToOne
    private Category cateID;

    //code Insert
    @Transient
    public int cID;
    @Transient
    public String cName;

    public int getcID() {
        return cID=this.cateID.getCateID();
    }

    public void setcID(int cID) {
        this.cID = this.cateID.getCateID();
    }

    

    public String getcName() {
        return cName=this.cateID.getCateName();
    }

    public void setcName(String cName) {
        this.cName = this.cateID.getCateName();
    }

    //end code insert
    public Groups() {
    }

    public Groups(Integer groupID) {
        this.groupID = groupID;
    }

    public Integer getGroupID() {
        return groupID;
    }

    public void setGroupID(Integer groupID) {
        this.groupID = groupID;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Integer getGroupStatus() {
        return groupStatus;
    }

    public void setGroupStatus(Integer groupStatus) {
        this.groupStatus = groupStatus;
    }

    public String getGroupDescriptions() {
        return groupDescriptions;
    }

    public void setGroupDescriptions(String groupDescriptions) {
        this.groupDescriptions = groupDescriptions;
    }

    @XmlTransient
    public Collection<Product> getProductCollection() {
        return productCollection;
    }

    public void setProductCollection(Collection<Product> productCollection) {
        this.productCollection = productCollection;
    }

    public Category getCateID() {
        return cateID;
    }

    public void setCateID(Category cateID) {
        this.cateID = cateID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (groupID != null ? groupID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Groups)) {
            return false;
        }
        Groups other = (Groups) object;
        if ((this.groupID == null && other.groupID != null) || (this.groupID != null && !this.groupID.equals(other.groupID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.Groups[ groupID=" + groupID + " ]";
    }

}
