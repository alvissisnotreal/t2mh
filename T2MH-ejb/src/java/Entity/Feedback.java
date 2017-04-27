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
@Table(name = "Feedback", catalog = "PROJECTSEM4F21506S0_T2MH", schema = "dbo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Feedback.findAll", query = "SELECT f FROM Feedback f")
    , @NamedQuery(name = "Feedback.findByComplainID", query = "SELECT f FROM Feedback f WHERE f.complainID = :complainID")
    , @NamedQuery(name = "Feedback.findByTitle", query = "SELECT f FROM Feedback f WHERE f.title = :title")
    , @NamedQuery(name = "Feedback.findByTimePost", query = "SELECT f FROM Feedback f WHERE f.timePost = :timePost")
    , @NamedQuery(name = "Feedback.findByContent", query = "SELECT f FROM Feedback f WHERE f.content = :content")
    , @NamedQuery(name = "Feedback.findByTimeReply", query = "SELECT f FROM Feedback f WHERE f.timeReply = :timeReply")
    , @NamedQuery(name = "Feedback.findByReplyContent", query = "SELECT f FROM Feedback f WHERE f.replyContent = :replyContent")})
public class Feedback implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ComplainID", nullable = false)
    private Integer complainID;
    @Size(max = 1000)
    @Column(name = "Title", length = 1000)
    private String title;
    @Size(max = 50)
    @Column(name = "TimePost", length = 50)
    private String timePost;
    @Size(max = 4000)
    @Column(name = "Content", length = 4000)
    private String content;
    @Size(max = 50)
    @Column(name = "TimeReply", length = 50)
    private String timeReply;
    @Size(max = 4000)
    @Column(name = "ReplyContent", length = 4000)
    private String replyContent;
    @JoinColumn(name = "CustomerID", referencedColumnName = "CustomerID")
    @ManyToOne
    private Customer customerID;
    @JoinColumn(name = "EmployeeID", referencedColumnName = "EmployeeID")
    @ManyToOne
    private Employee employeeID;

    public Feedback() {
    }

    //code Insert
    @Transient
    public int CID;
    @Transient
    public int EID;

    public int getCID() {
        return customerID.getCustomerID();
    }

    public int getEID() {
        try {
            return employeeID.getEmployeeID();
        } catch (Exception e) {
        }
        return 0;
    }

    //end code insert
    public Feedback(Integer complainID) {
        this.complainID = complainID;
    }

    public Integer getComplainID() {
        return complainID;
    }

    public void setComplainID(Integer complainID) {
        this.complainID = complainID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTimePost() {
        return timePost;
    }

    public void setTimePost(String timePost) {
        this.timePost = timePost;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimeReply() {
        return timeReply;
    }

    public void setTimeReply(String timeReply) {
        this.timeReply = timeReply;
    }

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }

    public Customer getCustomerID() {
        return customerID;
    }

    public void setCustomerID(Customer customerID) {
        this.customerID = customerID;
    }

    public Employee getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(Employee employeeID) {
        this.employeeID = employeeID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (complainID != null ? complainID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Feedback)) {
            return false;
        }
        Feedback other = (Feedback) object;
        if ((this.complainID == null && other.complainID != null) || (this.complainID != null && !this.complainID.equals(other.complainID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.Feedback[ complainID=" + complainID + " ]";
    }

}
