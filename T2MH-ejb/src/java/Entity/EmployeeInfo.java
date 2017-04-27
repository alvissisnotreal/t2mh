/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author 19319
 */
@Entity
@Table(name = "EmployeeInfo", catalog = "PROJECTSEM4F21506S0_T2MH", schema = "dbo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EmployeeInfo.findAll", query = "SELECT e FROM EmployeeInfo e")
    , @NamedQuery(name = "EmployeeInfo.findByEmployeeID", query = "SELECT e FROM EmployeeInfo e WHERE e.employeeID = :employeeID")
    , @NamedQuery(name = "EmployeeInfo.findByEmployeeName", query = "SELECT e FROM EmployeeInfo e WHERE e.employeeName = :employeeName")
    , @NamedQuery(name = "EmployeeInfo.findByEmployeePhone", query = "SELECT e FROM EmployeeInfo e WHERE e.employeePhone = :employeePhone")
    , @NamedQuery(name = "EmployeeInfo.findByEmployeeEmail", query = "SELECT e FROM EmployeeInfo e WHERE e.employeeEmail = :employeeEmail")
    , @NamedQuery(name = "EmployeeInfo.findByEmployeeDescriptions", query = "SELECT e FROM EmployeeInfo e WHERE e.employeeDescriptions = :employeeDescriptions")})
public class EmployeeInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "EmployeeID", nullable = false)
    private Integer employeeID;
    @Size(max = 100)
    @Column(name = "EmployeeName", length = 100)
    private String employeeName;
    @Size(max = 50)
    @Column(name = "EmployeePhone", length = 50)
    private String employeePhone;
    @Size(max = 100)
    @Column(name = "EmployeeEmail", length = 100)
    private String employeeEmail;
    @Size(max = 1073741823)
    @Column(name = "EmployeeDescriptions", length = 1073741823)
    private String employeeDescriptions;
    @JoinColumn(name = "EmployeeID", referencedColumnName = "EmployeeID", nullable = false, insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Employee employee;

    public EmployeeInfo() {
    }

    public EmployeeInfo(Integer employeeID) {
        this.employeeID = employeeID;
    }

    public Integer getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(Integer employeeID) {
        this.employeeID = employeeID;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeePhone() {
        return employeePhone;
    }

    public void setEmployeePhone(String employeePhone) {
        this.employeePhone = employeePhone;
    }

    public String getEmployeeEmail() {
        return employeeEmail;
    }

    public void setEmployeeEmail(String employeeEmail) {
        this.employeeEmail = employeeEmail;
    }

    public String getEmployeeDescriptions() {
        return employeeDescriptions;
    }

    public void setEmployeeDescriptions(String employeeDescriptions) {
        this.employeeDescriptions = employeeDescriptions;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
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
        if (!(object instanceof EmployeeInfo)) {
            return false;
        }
        EmployeeInfo other = (EmployeeInfo) object;
        if ((this.employeeID == null && other.employeeID != null) || (this.employeeID != null && !this.employeeID.equals(other.employeeID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.EmployeeInfo[ employeeID=" + employeeID + " ]";
    }
    
}
