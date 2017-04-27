/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SessionBean;

import Entity.Employee;
import Entity.EmployeeInfo;
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
public class EmployeeInfoFacade extends AbstractFacade<EmployeeInfo> implements EmployeeInfoFacadeLocal {

    @PersistenceContext(unitName = "T2MH-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EmployeeInfoFacade() {
        super(EmployeeInfo.class);
    }
    @Override
    public void refresh() {
        em.getEntityManagerFactory().getCache().evictAll();
    }
    
    
}
