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
@Table(name = "Payment", catalog = "PROJECTSEM4F21506S0_T2MH", schema = "dbo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Payment.findAll", query = "SELECT p FROM Payment p")
    , @NamedQuery(name = "Payment.findByPaymentID", query = "SELECT p FROM Payment p WHERE p.paymentID = :paymentID")
    , @NamedQuery(name = "Payment.findByTimeCreate", query = "SELECT p FROM Payment p WHERE p.timeCreate = :timeCreate")
    , @NamedQuery(name = "Payment.findByStartDate", query = "SELECT p FROM Payment p WHERE p.startDate = :startDate")
    , @NamedQuery(name = "Payment.findByEndDate", query = "SELECT p FROM Payment p WHERE p.endDate = :endDate")
    , @NamedQuery(name = "Payment.findByDateDebt", query = "SELECT p FROM Payment p WHERE p.dateDebt = :dateDebt")
    , @NamedQuery(name = "Payment.findByAmount", query = "SELECT p FROM Payment p WHERE p.amount = :amount")
    , @NamedQuery(name = "Payment.findByPaid", query = "SELECT p FROM Payment p WHERE p.paid = :paid")
    , @NamedQuery(name = "Payment.findByDebt", query = "SELECT p FROM Payment p WHERE p.debt = :debt")})
public class Payment implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "PaymentID", nullable = false, length = 50)
    private String paymentID;
    @Size(max = 50)
    @Column(name = "TimeCreate", length = 50)
    private String timeCreate;
    @Size(max = 50)
    @Column(name = "StartDate", length = 50)
    private String startDate;
    @Size(max = 50)
    @Column(name = "EndDate", length = 50)
    private String endDate;
    @Size(max = 50)
    @Column(name = "DateDebt", length = 50)
    private String dateDebt;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "Amount", precision = 53)
    private Double amount;
    @Column(name = "Paid", precision = 53)
    private Double paid;
    @Column(name = "Debt", precision = 53)
    private Double debt;
    @JoinColumn(name = "BranchID", referencedColumnName = "BranchID")
    @ManyToOne
    private Branch branchID;

    public Payment() {
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

    //end insert code
    public Payment(String paymentID) {
        this.paymentID = paymentID;
    }

    public String getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(String paymentID) {
        this.paymentID = paymentID;
    }

    public String getTimeCreate() {
        return timeCreate;
    }

    public void setTimeCreate(String timeCreate) {
        this.timeCreate = timeCreate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getDateDebt() {
        return dateDebt;
    }

    public void setDateDebt(String dateDebt) {
        this.dateDebt = dateDebt;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getPaid() {
        return paid;
    }

    public void setPaid(Double paid) {
        this.paid = paid;
    }

    public Double getDebt() {
        return debt;
    }

    public void setDebt(Double debt) {
        this.debt = debt;
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
        hash += (paymentID != null ? paymentID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Payment)) {
            return false;
        }
        Payment other = (Payment) object;
        if ((this.paymentID == null && other.paymentID != null) || (this.paymentID != null && !this.paymentID.equals(other.paymentID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.Payment[ paymentID=" + paymentID + " ]";
    }

}
