/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SessionBean;

import Entity.Specifics;
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
public class SpecificsFacade extends AbstractFacade<Specifics> implements SpecificsFacadeLocal {

    @PersistenceContext(unitName = "T2MH-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SpecificsFacade() {
        super(Specifics.class);
    }

    @Override
    public void refresh() {
        em.getEntityManagerFactory().getCache().evictAll();
    }

    @Override
    public boolean isNameExists(String specName) {
        this.refresh();
        Query query = em.createNamedQuery("Specifics.findBySpecName");
        query.setParameter("specName", specName);
        List<Specifics> listSpec = new ArrayList<Specifics>();
        listSpec = query.getResultList();
        if (listSpec.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isNameExistsForUpdate(String specID, String specName) {
        this.refresh();
        Query query = em.createNamedQuery("Specifics.findBySpecName");
        query.setParameter("specName", specName);
        List<Specifics> listSpec = new ArrayList<Specifics>();
        listSpec = query.getResultList();
        if (listSpec.size() > 0) {
            Specifics branch = (Specifics) listSpec.get(0);
            if (branch.getSpecID().toString().equals(specID)) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    @Override
    public Specifics getSpecByName(String specName) {
        this.refresh();
        Query query = em.createNamedQuery("Specifics.findBySpecName");
        query.setParameter("specName", specName);
        List<Specifics> listSpec = new ArrayList<Specifics>();
        listSpec = query.getResultList();
        if (listSpec.size() > 0) {
            return listSpec.get(0);
        } else {
            return null;
        }
    }
}
