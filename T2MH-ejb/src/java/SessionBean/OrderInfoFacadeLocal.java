/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SessionBean;

import Entity.OrderInfo;
import Entity.OrderInfoPK;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author 19319
 */
@Local
public interface OrderInfoFacadeLocal {

    void create(OrderInfo orderInfo);

    void edit(OrderInfo orderInfo);

    void remove(OrderInfo orderInfo);

    OrderInfo find(Object id);

    List<OrderInfo> findAll();

    List<OrderInfo> findRange(int[] range);

    int count();
    
    void refresh();
    
    OrderInfo findByFK(OrderInfoPK orderInfoPK);
}
