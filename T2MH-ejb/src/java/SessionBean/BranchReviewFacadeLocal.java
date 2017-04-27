/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SessionBean;

import Entity.BranchReview;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author 19319
 */
@Local
public interface BranchReviewFacadeLocal {

    void create(BranchReview branchReview);

    void edit(BranchReview branchReview);

    void remove(BranchReview branchReview);

    BranchReview find(Object id);

    List<BranchReview> findAll();

    List<BranchReview> findRange(int[] range);

    int count();
    
    void refresh();
}
