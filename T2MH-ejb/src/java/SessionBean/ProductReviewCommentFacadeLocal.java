/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SessionBean;

import Entity.ProductReviewComment;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author 19319
 */
@Local
public interface ProductReviewCommentFacadeLocal {

    void create(ProductReviewComment productReviewComment);

    void edit(ProductReviewComment productReviewComment);

    void remove(ProductReviewComment productReviewComment);

    ProductReviewComment find(Object id);

    List<ProductReviewComment> findAll();

    List<ProductReviewComment> findRange(int[] range);

    int count();
    
    void refresh();
    
}
