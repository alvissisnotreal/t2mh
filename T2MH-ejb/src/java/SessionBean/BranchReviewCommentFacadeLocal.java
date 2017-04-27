/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SessionBean;

import Entity.BranchReviewComment;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author 19319
 */
@Local
public interface BranchReviewCommentFacadeLocal {

    void create(BranchReviewComment branchReviewComment);

    void edit(BranchReviewComment branchReviewComment);

    void remove(BranchReviewComment branchReviewComment);

    BranchReviewComment find(Object id);

    List<BranchReviewComment> findAll();

    List<BranchReviewComment> findRange(int[] range);

    int count();
    
    void refresh();
}
