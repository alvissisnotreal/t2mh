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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author 19319
 */
@Entity
@Table(name = "Specifics", catalog = "PROJECTSEM4F21506S0_T2MH", schema = "dbo", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"SpecName"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Specifics.findAll", query = "SELECT s FROM Specifics s")
    , @NamedQuery(name = "Specifics.findBySpecID", query = "SELECT s FROM Specifics s WHERE s.specID = :specID")
    , @NamedQuery(name = "Specifics.findBySpecName", query = "SELECT s FROM Specifics s WHERE s.specName = :specName")
    , @NamedQuery(name = "Specifics.findBySpecStatus", query = "SELECT s FROM Specifics s WHERE s.specStatus = :specStatus")
    , @NamedQuery(name = "Specifics.findBySpecDescriptions", query = "SELECT s FROM Specifics s WHERE s.specDescriptions = :specDescriptions")})
public class Specifics implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column(name = "SpecID", nullable = false)
    private Integer specID;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "SpecName", nullable = false, length = 200)
    private String specName;
    @Column(name = "SpecStatus")
    private Integer specStatus;
    @Size(max = 1073741823)
    @Column(name = "SpecDescriptions", length = 1073741823)
    private String specDescriptions;

    public Specifics() {
    }

    public Specifics(Integer specID) {
        this.specID = specID;
    }

    public Specifics(Integer specID, String specName) {
        this.specID = specID;
        this.specName = specName;
    }

    public Integer getSpecID() {
        return specID;
    }

    public void setSpecID(Integer specID) {
        this.specID = specID;
    }

    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
    }

    public Integer getSpecStatus() {
        return specStatus;
    }

    public void setSpecStatus(Integer specStatus) {
        this.specStatus = specStatus;
    }

    public String getSpecDescriptions() {
        return specDescriptions;
    }

    public void setSpecDescriptions(String specDescriptions) {
        this.specDescriptions = specDescriptions;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (specID != null ? specID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Specifics)) {
            return false;
        }
        Specifics other = (Specifics) object;
        if ((this.specID == null && other.specID != null) || (this.specID != null && !this.specID.equals(other.specID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.Specifics[ specID=" + specID + " ]";
    }
    
}
