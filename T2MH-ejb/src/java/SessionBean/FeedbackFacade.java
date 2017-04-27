/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SessionBean;

import Entity.Feedback;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author 19319
 */
@Stateless
public class FeedbackFacade extends AbstractFacade<Feedback> implements FeedbackFacadeLocal {

    @PersistenceContext(unitName = "T2MH-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public FeedbackFacade() {
        super(Feedback.class);
    }
    @Override
    public void refresh() {
        em.getEntityManagerFactory().getCache().evictAll();
    }
    @Override
    public int countFeedbackEmptyEmpID(int CustomerID)
    {
        this.refresh();
        Query query = em.createQuery("SELECT F FROM Feedback AS F WHERE F.customerID.customerID = :CustomerID AND F.employeeID = Null");
        query.setParameter("CustomerID", CustomerID);
        return query.getResultList().size();
    }
}
