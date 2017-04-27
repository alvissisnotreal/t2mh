/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View.Customer;

/**
 *
 * @author Huy-PC
 */
public class reviewBranch {
    String customerName,timePort,commnets;
    int brStar,BRID,cusID;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getTimePort() {
        return timePort;
    }

    public void setTimePort(String timePort) {
        this.timePort = timePort;
    }

    public String getCommnets() {
        return commnets;
    }

    public void setCommnets(String commnets) {
        this.commnets = commnets;
    }

    public int getBrStar() {
        return brStar;
    }

    public void setBrStar(int brStar) {
        this.brStar = brStar;
    }

    public int getBRID() {
        return BRID;
    }

    public void setBRID(int BRID) {
        this.BRID = BRID;
    }

    public int getCusID() {
        return cusID;
    }

    public void setCusID(int cusID) {
        this.cusID = cusID;
    }

    public reviewBranch(String customerName, String timePort, String commnets, int brStar) {
        this.customerName = customerName;
        this.timePort = timePort;
        this.commnets = commnets;
        this.brStar = brStar;
    }

    public reviewBranch(String customerName, String timePort, String commnets, int brStar, int BRID) {
        this.customerName = customerName;
        this.timePort = timePort;
        this.commnets = commnets;
        this.brStar = brStar;
        this.BRID = BRID;
    }

    public reviewBranch(String customerName, String timePort, String commnets, int brStar, int BRID, int cusID) {
        this.customerName = customerName;
        this.timePort = timePort;
        this.commnets = commnets;
        this.brStar = brStar;
        this.BRID = BRID;
        this.cusID = cusID;
    }
    
    public reviewBranch() {
    }
    
}
