/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SessionBean;

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
public class EmployeeFacade extends AbstractFacade<Employee> implements EmployeeFacadeLocal {

    @PersistenceContext(unitName = "T2MH-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EmployeeFacade() {
        super(Employee.class);
    }
    @Override
    public void refresh() {
        em.getEntityManagerFactory().getCache().evictAll();
    }
    
    @Override
    public Employee findEmpByUsername(String Username) {
        this.refresh();
        Query query=em.createNamedQuery("Employee.findByEmployeeUsername");
        query.setParameter("employeeUsername", Username);
        List<Employee> listEmp= new ArrayList<Employee>();
        listEmp=query.getResultList();
        if(listEmp.size()==1)
        {
            Employee employee=new Employee();
            employee=listEmp.get(0);
            return employee;
        }
        else 
        {
            return null;
        }
    }
    @Override
    public boolean isNameExistsForUpdate(String empID, String empName) {
        this.refresh();
        Query query = em.createNamedQuery("Employee.findByEmployeeUsername");
        query.setParameter("employeeUsername", empName);
        List<Employee> listEmp = new ArrayList<Employee>();
        listEmp = query.getResultList();
        if (listEmp.size() > 0) {
            Employee employee = (Employee) listEmp.get(0);
            if (employee.getEmployeeID().toString().equals(empID)) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
}
