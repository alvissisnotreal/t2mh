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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
@Table(name = "Employee", catalog = "PROJECTSEM4F21506S0_T2MH", schema = "dbo", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"EmployeeUsername"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Employee.findAll", query = "SELECT e FROM Employee e")
    , @NamedQuery(name = "Employee.findByEmployeeID", query = "SELECT e FROM Employee e WHERE e.employeeID = :employeeID")
    , @NamedQuery(name = "Employee.findByEmployeeUsername", query = "SELECT e FROM Employee e WHERE e.employeeUsername = :employeeUsername")
    , @NamedQuery(name = "Employee.findByEmployeePassword", query = "SELECT e FROM Employee e WHERE e.employeePassword = :employeePassword")
    , @NamedQuery(name = "Employee.findByEmployeeStatus", query = "SELECT e FROM Employee e WHERE e.employeeStatus = :employeeStatus")})
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column(name = "EmployeeID", nullable = false)
    private Integer employeeID;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "EmployeeUsername", nullable = false, length = 200)
    private String employeeUsername;
    @Size(max = 200)
    @Column(name = "EmployeePassword", length = 200)
    private String employeePassword;
    @Column(name = "EmployeeStatus")
    private Integer employeeStatus;
    @JoinTable(name = "EmployeeRole", joinColumns = {
        @JoinColumn(name = "EmployeeID", referencedColumnName = "EmployeeID", nullable = false)}, inverseJoinColumns = {
        @JoinColumn(name = "RoleID", referencedColumnName = "RoleID", nullable = false)})
    @ManyToMany
    private Collection<AdminRole> adminRoleCollection;
    @OneToMany(mappedBy = "employeeID")
    private Collection<Feedback> feedbackCollection;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "employee")
    private EmployeeInfo employeeInfo;

    public Employee() {
    }

    public Employee(Integer employeeID) {
        this.employeeID = employeeID;
    }

    public Employee(Integer employeeID, String employeeUsername) {
        this.employeeID = employeeID;
        this.employeeUsername = employeeUsername;
    }

    public Integer getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(Integer employeeID) {
        this.employeeID = employeeID;
    }

    public String getEmployeeUsername() {
        return employeeUsername;
    }

    public void setEmployeeUsername(String employeeUsername) {
        this.employeeUsername = employeeUsername;
    }

    public String getEmployeePassword() {
        return employeePassword;
    }

    public void setEmployeePassword(String employeePassword) {
        this.employeePassword = employeePassword;
    }

    public Integer getEmployeeStatus() {
        return employeeStatus;
    }

    public void setEmployeeStatus(Integer employeeStatus) {
        this.employeeStatus = employeeStatus;
    }

    @XmlTransient
    public Collection<AdminRole> getAdminRoleCollection() {
        return adminRoleCollection;
    }

    public void setAdminRoleCollection(Collection<AdminRole> adminRoleCollection) {
        this.adminRoleCollection = adminRoleCollection;
    }

    @XmlTransient
    public Collection<Feedback> getFeedbackCollection() {
        return feedbackCollection;
    }

    public void setFeedbackCollection(Collection<Feedback> feedbackCollection) {
        this.feedbackCollection = feedbackCollection;
    }

    public EmployeeInfo getEmployeeInfo() {
        return employeeInfo;
    }

    public void setEmployeeInfo(EmployeeInfo employeeInfo) {
        this.employeeInfo = employeeInfo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (employeeID != null ? employeeID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Employee)) {
            return false;
        }
        Employee other = (Employee) object;
        if ((this.employeeID == null && other.employeeID != null) || (this.employeeID != null && !this.employeeID.equals(other.employeeID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.Employee[ employeeID=" + employeeID + " ]";
    }
    
}
