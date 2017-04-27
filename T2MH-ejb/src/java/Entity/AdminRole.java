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
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author 19319
 */
@Entity
@Table(name = "AdminRole", catalog = "PROJECTSEM4F21506S0_T2MH", schema = "dbo", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"RoleName"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AdminRole.findAll", query = "SELECT a FROM AdminRole a")
    , @NamedQuery(name = "AdminRole.findByRoleID", query = "SELECT a FROM AdminRole a WHERE a.roleID = :roleID")
    , @NamedQuery(name = "AdminRole.findByRoleName", query = "SELECT a FROM AdminRole a WHERE a.roleName = :roleName")
    , @NamedQuery(name = "AdminRole.findByRoleDescriptions", query = "SELECT a FROM AdminRole a WHERE a.roleDescriptions = :roleDescriptions")})
public class AdminRole implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "RoleID", nullable = false, length = 50)
    private String roleID;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "RoleName", nullable = false, length = 100)
    private String roleName;
    @Size(max = 450)
    @Column(name = "RoleDescriptions", length = 450)
    private String roleDescriptions;
    @ManyToMany(mappedBy = "adminRoleCollection")
    private Collection<Employee> employeeCollection;

    public AdminRole() {
    }

    public AdminRole(String roleID) {
        this.roleID = roleID;
    }

    public AdminRole(String roleID, String roleName) {
        this.roleID = roleID;
        this.roleName = roleName;
    }

    public String getRoleID() {
        return roleID;
    }

    public void setRoleID(String roleID) {
        this.roleID = roleID;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleDescriptions() {
        return roleDescriptions;
    }

    public void setRoleDescriptions(String roleDescriptions) {
        this.roleDescriptions = roleDescriptions;
    }

    @XmlTransient
    public Collection<Employee> getEmployeeCollection() {
        return employeeCollection;
    }

    public void setEmployeeCollection(Collection<Employee> employeeCollection) {
        this.employeeCollection = employeeCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (roleID != null ? roleID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AdminRole)) {
            return false;
        }
        AdminRole other = (AdminRole) object;
        if ((this.roleID == null && other.roleID != null) || (this.roleID != null && !this.roleID.equals(other.roleID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.AdminRole[ roleID=" + roleID + " ]";
    }
    
}
