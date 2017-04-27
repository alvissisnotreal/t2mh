/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SessionBean;

import Entity.BranchCommentMark;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author 19319
 */
@Local
public interface BranchCommentMarkFacadeLocal {

    void create(BranchCommentMark branchCommentMark);

    void edit(BranchCommentMark branchCommentMark);

    void remove(BranchCommentMark branchCommentMark);

    BranchCommentMark find(Object id);

    List<BranchCommentMark> findAll();

    List<BranchCommentMark> findRange(int[] range);

    int count();

    void refresh();
}
