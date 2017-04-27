/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SessionBean;

import Entity.Customer;
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
public class CustomerFacade extends AbstractFacade<Customer> implements CustomerFacadeLocal {

    @PersistenceContext(unitName = "T2MH-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CustomerFacade() {
        super(Customer.class);
    }
     @Override
    public void refresh() {
        em.getEntityManagerFactory().getCache().evictAll();
    }
    @Override
    public Customer findCustomerByUsername(String Username) {
        Customer customer=new Customer();
        this.refresh();
        Query query=em.createNamedQuery("Customer.findByCustomerUsername");
        query.setParameter("customerUsername", Username);
        List<Customer> listCustomer= new ArrayList<Customer>();
        listCustomer=query.getResultList();
        if(listCustomer.size()==1)
        {
            customer=listCustomer.get(0);
            return customer;
        }
        else 
        {
            return null;
        }
    }
    @Override
    public boolean isNameExistsForUpdate(String ID, String Name) {
        this.refresh();
        Query query = em.createNamedQuery("Customer.findByCustomerUsername");
        query.setParameter("customerUsername", Name);
        List<Customer> listObj = new ArrayList<Customer>();
        listObj = query.getResultList();
        if (listObj.size() > 0) {
            Customer obj = (Customer) listObj.get(0);
            if (obj.getCustomerID().toString().equals(ID)) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
}
