/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View.Employee;

import Entity.Orders;
import SessionBean.OrdersFacadeLocal;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 *
 * @author 19319
 */
@Named(value = "ORDERBILL")
@ManagedBean
@ViewScoped
public class OrderBill implements Serializable {

    @EJB
    private OrdersFacadeLocal ordersF;

    private Orders order;
    private String dateCreate;

    //Getter and setter
    public Orders getOrder() {
        return order;
    }

    public void setOrder(Orders order) {
        this.order = order;
    }

    public String getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(String dateCreate) {
        this.dateCreate = dateCreate;
    }
    //End getter and setter

    @PostConstruct
    public void init() {
        ordersF.refresh();
        order = ordersF.find((int)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("orderIDBill"));
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        dateCreate = formatter.format(date);
    }
    
}
