/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SessionBean;

import Entity.ProductReview;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author 19319
 */
@Stateless
public class ProductReviewFacade extends AbstractFacade<ProductReview> implements ProductReviewFacadeLocal {

    @PersistenceContext(unitName = "T2MH-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProductReviewFacade() {
        super(ProductReview.class);
    }

    @Override
    public void refresh() {
        em.getEntityManagerFactory().getCache().evictAll();
    }
}
