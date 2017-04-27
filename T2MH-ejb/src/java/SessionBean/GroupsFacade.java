/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SessionBean;

import Entity.Groups;
import java.util.ArrayList;
import java.util.Iterator;
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
public class GroupsFacade extends AbstractFacade<Groups> implements GroupsFacadeLocal {

    @PersistenceContext(unitName = "T2MH-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public GroupsFacade() {
        super(Groups.class);
    }

    @Override
    public void refresh() {
        em.getEntityManagerFactory().getCache().evictAll();
    }

    @Override
    public boolean isNameExists(int CateID, String groupName) {
        this.refresh();
        Query query = em.createNamedQuery("Groups.findByGroupName");
        query.setParameter("groupName", groupName);
        List<Groups> listObject = new ArrayList<Groups>();
        listObject = query.getResultList();
        boolean isExists = false;
        for (Iterator<Groups> iterator = listObject.iterator(); iterator.hasNext();) {
            Groups next = iterator.next();
            if (next.getCateID().getCateID() == CateID) {
                isExists = true;
            }
        }
        return isExists;
    }

    @Override
    public boolean isNameExistsForUpdate(int CateID, int groupID, String groupName) {
        this.refresh();
        Query query = em.createNamedQuery("Groups.findByGroupName");
        query.setParameter("groupName", groupName);
        List<Groups> listObject = new ArrayList<Groups>();
        listObject = query.getResultList();
        boolean isExists = false;
        for (Iterator<Groups> iterator = listObject.iterator(); iterator.hasNext();) {
            Groups next = iterator.next();
            if (next.getCateID().getCateID() == CateID && next.getGroupID() != groupID) {
                isExists = true;
                break;
            }
        }
        return isExists;

    }

    @Override
    public Groups getGroupByName(String groupName) {
        this.refresh();
        Query query = em.createNamedQuery("Groups.findByGroupName");
        query.setParameter("groupName", groupName);
        List<Groups> listObject = new ArrayList<Groups>();
        listObject = query.getResultList();
        if (listObject.size() > 0) {
            return listObject.get(0);
        } else {
            return null;
        }
    }
}
