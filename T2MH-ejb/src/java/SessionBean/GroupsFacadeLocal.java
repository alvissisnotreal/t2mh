/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SessionBean;

import Entity.Groups;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author 19319
 */
@Local
public interface GroupsFacadeLocal {

    void create(Groups groups);

    void edit(Groups groups);

    void remove(Groups groups);

    Groups find(Object id);

    List<Groups> findAll();

    List<Groups> findRange(int[] range);

    int count();
    
    void refresh();
    
    boolean isNameExists(int CateID, String groupName);
    
    boolean isNameExistsForUpdate(int CateID,int groupID, String groupName);
    
    Groups getGroupByName(String groupName);
}
