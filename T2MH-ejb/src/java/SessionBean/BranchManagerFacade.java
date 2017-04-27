/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SessionBean;

import Entity.Branch;
import Entity.BranchManager;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author 19319
 */
@Stateless
public class BranchManagerFacade extends AbstractFacade<BranchManager> implements BranchManagerFacadeLocal {

    @PersistenceContext(unitName = "T2MH-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public BranchManagerFacade() {
        super(BranchManager.class);
    }

    @Override
    public void refresh() {
        em.getEntityManagerFactory().getCache().evictAll();
    }

    @Override
    public boolean isNameExists(String bmUsername) {
        this.refresh();
        Query query = em.createNamedQuery("BranchManager.findByBmUsername");
        query.setParameter("bmUsername", bmUsername);
        List<BranchManager> listBM = new ArrayList<BranchManager>();
        listBM = query.getResultList();
        if (listBM.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isNameExistsForUpdate(String bmID, String bmUsername) {
        this.refresh();
        Query query = em.createNamedQuery("BranchManager.findByBmUsername");
        query.setParameter("bmUsername", bmUsername);
        List<BranchManager> listBM = new ArrayList<BranchManager>();
        listBM = query.getResultList();
        if (listBM.size() > 0) {
            BranchManager bm = (BranchManager) listBM.get(0);
            if (bm.getBranchID().toString().equals(bmID)) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
}
