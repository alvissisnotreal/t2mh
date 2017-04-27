/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SessionBean;

import Entity.Branch;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author 19319
 */
@Local
public interface BranchFacadeLocal {

    void create(Branch branch);

    void edit(Branch branch);

    void remove(Branch branch);

    Branch find(Object id);

    List<Branch> findAll();

    List<Branch> findRange(int[] range);

    int count();

    void refresh();

    boolean isNameExists(String branchName);

    Branch getBranchByName(String branchName);

    boolean isNameExistsForUpdate(String branchID, String branchName);

    List<Branch> getBranchListBySearchLikeName(String keySearch);

    boolean updateBranch(Branch branch);

    List<Branch> getBranchActive();
}
