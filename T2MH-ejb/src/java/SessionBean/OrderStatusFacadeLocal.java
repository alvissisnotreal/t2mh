/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SessionBean;

import Entity.OrderStatus;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author 19319
 */
@Local
public interface OrderStatusFacadeLocal {

    void create(OrderStatus orderStatus);

    void edit(OrderStatus orderStatus);

    void remove(OrderStatus orderStatus);

    OrderStatus find(Object id);

    List<OrderStatus> findAll();

    List<OrderStatus> findRange(int[] range);

    int count();
    
}
