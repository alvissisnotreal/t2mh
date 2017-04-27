/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SessionBean;

import Entity.Orders;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author 19319
 */
@Local
public interface OrdersFacadeLocal {

    void create(Orders orders);

    void edit(Orders orders);

    void remove(Orders orders);

    Orders find(Object id);

    List<Orders> findAll();

    List<Orders> findRange(int[] range);

    int count();

    void refresh();

    List<Orders> getAllOrderWating();

    List<Orders> getOrdersByCustomerID(int customerID);
    
    List<Orders> getOrdersDeliveredLastMonth();
    
    Integer countOrdering(int customerID);
}
