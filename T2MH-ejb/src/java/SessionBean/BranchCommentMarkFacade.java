/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SessionBean;

import Entity.BranchCommentMark;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author 19319
 */
@Stateless
public class BranchCommentMarkFacade extends AbstractFacade<BranchCommentMark> implements BranchCommentMarkFacadeLocal {

    @PersistenceContext(unitName = "T2MH-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public BranchCommentMarkFacade() {
        super(BranchCommentMark.class);
    }
    @Override
    public void refresh() {
        em.getEntityManagerFactory().getCache().evictAll();
    }
}
