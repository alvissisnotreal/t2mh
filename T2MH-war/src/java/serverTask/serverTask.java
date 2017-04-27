/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverTask;

import Entity.Branch;
import Entity.OrderInfo;
import Entity.OrderStatus;
import Entity.Orders;
import Entity.Payment;
import SessionBean.BranchFacadeLocal;
import SessionBean.OrderStatusFacadeLocal;
import SessionBean.OrdersFacadeLocal;
import SessionBean.PaymentFacadeLocal;
import XMLAccess.XmlAccessDescriptions;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Named;

/**
 *
 * @author 19319
 */
@Named(value = "serverTask")
@Startup
@Singleton
public class serverTask {

    @EJB
    private PaymentFacadeLocal paymentF;

    @EJB
    private BranchFacadeLocal branchF;

    @EJB
    private OrderStatusFacadeLocal orderStatusF;

    @EJB
    private OrdersFacadeLocal ordersF;

    //end about bean properties
    public serverTask() {
    }

    @PostConstruct
    public void init() {
        try {
            updateCartStatus();
        } catch (Exception e) {
        }
        try {
            autoCreatePayment();
        } catch (Exception e) {
        }
        try {
            autoDisableBranch();
        } catch (Exception e) {
        }

    }

    @PreDestroy
    public void destroy() {
        System.out.println("server shutdown");
    }

    @Schedule(hour = "*/1", minute = "*/5", second = "0", persistent = false)
    public void updateCartStatus() throws ParseException {
        System.out.println("called task updateCartStatus");
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, -3);
        Date date = calendar.getTime();
        Date dateOrder;
        OrderStatus orderStatusConfirmed = orderStatusF.find(3);//3=confirmed
        List<Orders> listOrders = ordersF.getAllOrderWating();
        for (Orders objectOrders : listOrders) {
            dateOrder = formatter.parse(objectOrders.getTimeOrder());
            if (dateOrder.before(date)) {
                objectOrders.setOrderStatusID(orderStatusConfirmed);
                ordersF.edit(objectOrders);
                List<OrderInfo> listOrderInfo = (List<OrderInfo>) objectOrders.getOrderInfoCollection();
                for (Iterator<OrderInfo> iterator = listOrderInfo.iterator(); iterator.hasNext();) {
                    OrderInfo orderInfo = iterator.next();
                    if (orderInfo.getDescriptions().equalsIgnoreCase("Wait For Confirm")) {
                        orderInfo.setDescriptions("Confirmed");
                    }
                }
                System.out.println("Order ID: " + objectOrders + " Confirmed");
            }
        }
    }

    /*
    -test all enable branch and disable what branch have payment's debt < -1000 of 2 months ago
     */
    @Schedule(hour = "*/12", minute = "0", second = "0", persistent = false)
    public void autoDisableBranch() {

        //first=> caculated 2 month ago
        //second=> select all branch active
        //test payment of 2 month ago
        //disable if payment exists and payment.debt < -100000
        branchF.refresh();
        System.out.println("called task autoDisableBranch");
        DateFormat formatterMY = new SimpleDateFormat("MM/yyyy");
        Date currentDate = new Date();

        Calendar calendar2MonthsAgo = Calendar.getInstance();
        calendar2MonthsAgo.set(Calendar.DAY_OF_MONTH, 1);
        calendar2MonthsAgo.add(Calendar.MONTH, -2);
        calendar2MonthsAgo.set(Calendar.MINUTE, 0);
        calendar2MonthsAgo.set(Calendar.SECOND, 0);
        Date startDate = calendar2MonthsAgo.getTime();
        String monthPayment = formatterMY.format(startDate);

        List<Branch> listBranchActive = branchF.getBranchActive();
        Branch branch;
        paymentF.refresh();
        Payment paymentcre;
        for (Iterator<Branch> iterator = listBranchActive.iterator(); iterator.hasNext();) {
            try {
                branch = iterator.next();
                paymentcre = new Payment();
                paymentcre.setPaymentID(branch.getBranchID() + "-" + monthPayment);
                paymentcre = paymentF.find(paymentcre.getPaymentID());
                if (paymentcre != null) {
                    if (paymentcre.getDebt() < -1) {

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Schedule(hour = "*/1", minute = "0", second = "0", persistent = false)
    public void autoCreatePayment() {
        System.out.println("called task autoCreatePayment");

        ordersF.refresh();
        branchF.refresh();

        DateFormat formatterMY = new SimpleDateFormat("MM/yyyy");
        DateFormat formatterDMY = new SimpleDateFormat("dd/MM/yyyy");
        Date currentDate = new Date();
        String dateCreate = formatterDMY.format(currentDate);

        Calendar calendarStart = Calendar.getInstance();
        calendarStart.set(Calendar.DAY_OF_MONTH, 1);
        calendarStart.set(Calendar.HOUR_OF_DAY, 0);
        calendarStart.set(Calendar.MINUTE, 0);
        calendarStart.set(Calendar.SECOND, 0);
        Date startDate = calendarStart.getTime();
        String monthPayment = formatterMY.format(startDate);
        String stringStartDate = formatterDMY.format(startDate);

        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.add(Calendar.MONTH, 1);
        calendarEnd.set(Calendar.DAY_OF_MONTH, 1);
        calendarEnd.add(Calendar.DATE, -1);
        calendarEnd.set(Calendar.HOUR_OF_DAY, 23);
        calendarEnd.set(Calendar.MINUTE, 59);
        calendarEnd.set(Calendar.SECOND, 59);
        Date endDate = calendarEnd.getTime();
        String stringEndDate = formatterDMY.format(endDate);

        Payment paymentcre = new Payment();
        double paymentAmount = 0;

        XmlAccessDescriptions XAD = new XmlAccessDescriptions();
        HashMap<String, String> hmDes;
        //create payment for every exercis branch of new month(if not disable)

        //getactive
        List<Branch> listBranchActive = branchF.getBranchActive();
        Branch branch;
        paymentF.refresh();
        for (Iterator<Branch> iterator = listBranchActive.iterator(); iterator.hasNext();) {
            try {
                branch = iterator.next();
                hmDes = XAD.convertDocumentToHashMap(XAD.stringToDocument(branch.getBranchDescriptions()));
                String typeTax = hmDes.get("typeTax");
                if (typeTax.equalsIgnoreCase("Excises") == false) {
                    continue;
                }
                double valueTax = Double.valueOf(hmDes.get("valueTax"));
                paymentAmount = 0;
                paymentcre = new Payment();
                paymentcre.setPaymentID(branch.getBranchID() + "-" + monthPayment);
                //test exists Payment

                if (paymentF.find(paymentcre.getPaymentID()) == null) {
                    paymentAmount = -valueTax;
                    paymentcre.setBranchID(branch);
                    paymentcre.setTimeCreate(dateCreate);
                    paymentcre.setStartDate(stringStartDate);
                    paymentcre.setEndDate(stringEndDate);
                    paymentcre.setDateDebt("");
                    paymentcre.setAmount(paymentAmount);
                    paymentcre.setPaid(0.00);
                    paymentcre.setDebt(paymentAmount);
                    paymentF.create(paymentcre);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
