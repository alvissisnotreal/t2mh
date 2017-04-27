/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SessionBean;

import Entity.CustomerInfo;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author 19319
 */
@Local
public interface CustomerInfoFacadeLocal {

    void create(CustomerInfo customerInfo);

    void edit(CustomerInfo customerInfo);

    void remove(CustomerInfo customerInfo);

    CustomerInfo find(Object id);

    List<CustomerInfo> findAll();

    List<CustomerInfo> findRange(int[] range);

    int count();
    
    void refresh();
}
