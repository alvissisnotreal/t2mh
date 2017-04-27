/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SessionBean;

import Entity.AdminRole;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author 19319
 */
@Local
public interface AdminRoleFacadeLocal {

    void create(AdminRole adminRole);

    void edit(AdminRole adminRole);

    void remove(AdminRole adminRole);

    AdminRole find(Object id);

    List<AdminRole> findAll();

    List<AdminRole> findRange(int[] range);

    int count();
    
    void refresh();
    
    AdminRole findByRoleID(String id);
}
