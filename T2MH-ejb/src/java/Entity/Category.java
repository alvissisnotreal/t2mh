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
@Table(name = "Category", catalog = "PROJECTSEM4F21506S0_T2MH", schema = "dbo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Category.findAll", query = "SELECT c FROM Category c")
    , @NamedQuery(name = "Category.findByCateID", query = "SELECT c FROM Category c WHERE c.cateID = :cateID")
    , @NamedQuery(name = "Category.findByCateName", query = "SELECT c FROM Category c WHERE c.cateName = :cateName")
    , @NamedQuery(name = "Category.findByCateStatus", query = "SELECT c FROM Category c WHERE c.cateStatus = :cateStatus")
    , @NamedQuery(name = "Category.findByCateDescriptions", query = "SELECT c FROM Category c WHERE c.cateDescriptions = :cateDescriptions")})
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column(name = "CateID", nullable = false)
    private Integer cateID;
    @Size(max = 1000)
    @Column(name = "CateName", length = 1000)
    private String cateName;
    @Column(name = "CateStatus")
    private Integer cateStatus;
    @Size(max = 1073741823)
    @Column(name = "CateDescriptions", length = 1073741823)
    private String cateDescriptions;
    @OneToMany(mappedBy = "cateID")
    private Collection<Groups> groupsCollection;

    public Category() {
    }

    public Category(Integer cateID) {
        this.cateID = cateID;
    }

    public Integer getCateID() {
        return cateID;
    }

    public void setCateID(Integer cateID) {
        this.cateID = cateID;
    }

    public String getCateName() {
        return cateName;
    }

    public void setCateName(String cateName) {
        this.cateName = cateName;
    }

    public Integer getCateStatus() {
        return cateStatus;
    }

    public void setCateStatus(Integer cateStatus) {
        this.cateStatus = cateStatus;
    }

    public String getCateDescriptions() {
        return cateDescriptions;
    }

    public void setCateDescriptions(String cateDescriptions) {
        this.cateDescriptions = cateDescriptions;
    }

    @XmlTransient
    public Collection<Groups> getGroupsCollection() {
        return groupsCollection;
    }

    public void setGroupsCollection(Collection<Groups> groupsCollection) {
        this.groupsCollection = groupsCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cateID != null ? cateID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Category)) {
            return false;
        }
        Category other = (Category) object;
        if ((this.cateID == null && other.cateID != null) || (this.cateID != null && !this.cateID.equals(other.cateID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.Category[ cateID=" + cateID + " ]";
    }
    
}
