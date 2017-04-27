/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SessionBean;

import Entity.ProductCommentMark;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author 19319
 */
@Local
public interface ProductCommentMarkFacadeLocal {

    void create(ProductCommentMark productCommentMark);

    void edit(ProductCommentMark productCommentMark);

    void remove(ProductCommentMark productCommentMark);

    ProductCommentMark find(Object id);

    List<ProductCommentMark> findAll();

    List<ProductCommentMark> findRange(int[] range);

    int count();
    
    void refresh();
}
