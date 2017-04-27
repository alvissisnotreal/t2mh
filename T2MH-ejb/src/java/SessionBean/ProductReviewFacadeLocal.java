/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SessionBean;

import Entity.ProductReview;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author 19319
 */
@Local
public interface ProductReviewFacadeLocal {

    void create(ProductReview productReview);

    void edit(ProductReview productReview);

    void remove(ProductReview productReview);

    ProductReview find(Object id);

    List<ProductReview> findAll();

    List<ProductReview> findRange(int[] range);

    int count();
    
    void refresh();
    
}
