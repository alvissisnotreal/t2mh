/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SessionBean;

import Entity.Product;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author 19319
 */
@Local
public interface ProductFacadeLocal {

    void create(Product product);

    void edit(Product product);

    void remove(Product product);

    Product find(Object id);

    List<Product> findAll();

    List<Product> findRange(int[] range);

    int count();

    public void refresh();

    Product findProductAfterCreate(int branchID, String generateCode);

    Product getProductNeedApprova();

    List<Product> findAllProductActive();

    boolean productIsActive(int productID);
    
    List<Product> findProductByNameLike(String searchString);
    
    List<Product> findProductByTag(String searchString);
}
