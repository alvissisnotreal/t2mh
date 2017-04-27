/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SessionBean;

import Entity.BranchManager;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author 19319
 */
@Local
public interface BranchManagerFacadeLocal {

    void create(BranchManager branchManager);

    void edit(BranchManager branchManager);

    void remove(BranchManager branchManager);

    BranchManager find(Object id);

    List<BranchManager> findAll();

    List<BranchManager> findRange(int[] range);

    int count();
    
    void refresh();
    
    boolean isNameExists(String bmUsername);
    
    boolean isNameExistsForUpdate(String bmID, String bmUsername);
    
   
}
