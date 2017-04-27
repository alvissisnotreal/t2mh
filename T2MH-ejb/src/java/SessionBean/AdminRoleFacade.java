/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SessionBean;

import Entity.AdminRole;
import Entity.Employee;
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
public class AdminRoleFacade extends AbstractFacade<AdminRole> implements AdminRoleFacadeLocal {

    @PersistenceContext(unitName = "T2MH-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AdminRoleFacade() {
        super(AdminRole.class);
    }
     @Override
    public void refresh() {
        em.getEntityManagerFactory().getCache().evictAll();
    }
    @Override
    public AdminRole findByRoleID(String id)
    {
        this.refresh();
        Query query=em.createNamedQuery("AdminRole.findByRoleID");
        query.setParameter("roleID", id);
        List<AdminRole> listEmp= new ArrayList<AdminRole>();
        listEmp=query.getResultList();
        if(listEmp.size()==1)
        {
            AdminRole adminRole=new AdminRole();
            adminRole=listEmp.get(0);
            return adminRole;
        }
        else 
        {
            return null;
        }
    }
}
