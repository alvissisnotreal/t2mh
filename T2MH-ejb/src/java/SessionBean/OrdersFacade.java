/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SessionBean;

import Entity.Orders;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author 19319
 */
@Stateless
public class OrdersFacade extends AbstractFacade<Orders> implements OrdersFacadeLocal {

    @PersistenceContext(unitName = "T2MH-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public OrdersFacade() {
        super(Orders.class);
    }

    @Override
    public void refresh() {
        em.getEntityManagerFactory().getCache().evictAll();
    }

    @Override
    public List<Orders> getAllOrderWating() {
        this.refresh();
        Query query = em.createQuery("SELECT o FROM Orders AS o WHERE o.orderStatusID.orderStatusID = :orderStatusID");
        query.setParameter("orderStatusID", Integer.valueOf("2"));
        List<Orders> list = new ArrayList<Orders>();
        list = query.getResultList();
        return list;
    }

    @Override
    public List<Orders> getOrdersByCustomerID(int customerID) {
        this.refresh();
        Query query = em.createQuery("SELECT o FROM Orders AS o WHERE o.customerID = :customerID");
        query.setParameter("customerID", customerID);
        List<Orders> list = new ArrayList<Orders>();
        list = query.getResultList();
        return list;
    }

    @Override
    public List<Orders> getOrdersDeliveredLastMonth() {
        this.refresh();
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Calendar calendarStart = Calendar.getInstance();

        calendarStart.add(Calendar.MONTH, -1);
        calendarStart.set(Calendar.DAY_OF_MONTH, 1);
        calendarStart.set(Calendar.HOUR_OF_DAY, 0);
        calendarStart.set(Calendar.MINUTE, 0);
        calendarStart.set(Calendar.SECOND, 0);
        Date startDate = calendarStart.getTime();

        Calendar calendarEnd = Calendar.getInstance();

        calendarEnd.set(Calendar.DAY_OF_MONTH, 1);
        calendarEnd.set(Calendar.DATE, -1);
        calendarEnd.set(Calendar.HOUR_OF_DAY, 23);
        calendarEnd.set(Calendar.MINUTE, 59);
        calendarEnd.set(Calendar.SECOND, 59);
        Date endDate = calendarEnd.getTime();

        Query query = em.createQuery("SELECT o FROM Orders AS o WHERE o.orderStatusID.orderStatusID = 4 ORDER BY O.orderID DESC");
        final int resultsPerGet = 500;
        int resultPage = 0;
        boolean stillGet = true;

        List<Orders> listOrderSuccess = new ArrayList<>();
        while (stillGet) {
            List<Orders> listOrderGet = new ArrayList<>();
            query.setFirstResult(resultPage * resultsPerGet);
            query.setMaxResults(resultsPerGet);
            listOrderGet.addAll(query.getResultList());
            for (Iterator<Orders> iterator = query.getResultList().iterator(); iterator.hasNext();) {
                Orders next = iterator.next();
                try {
                    Date date = formatter.parse(next.getTimeDelivery());
                    if ((date.after(startDate) || date.equals(startDate)) && (date.before(endDate) || date.equals(endDate))) {
                        listOrderSuccess.add(next);
                    }
                } catch (ParseException ex) {
                }
            }
            if (listOrderGet.size() < resultsPerGet) {
                stillGet = false;
            }
            resultPage++;
        }
        return listOrderSuccess;
    }

    @Override
    public Integer countOrdering(int customerID) {
        this.refresh();
        try {
            Query query = em.createQuery("SELECT o FROM Orders AS o WHERE o.orderStatusID.orderStatusID = 2 OR o.orderStatusID.orderStatusID = 3");
            return query.getResultList().size();
        } catch (Exception e) {
            return 0;
        }

    }
}
