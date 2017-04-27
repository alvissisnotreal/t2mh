/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SessionBean;

import Entity.OrderInfo;
import Entity.OrderInfoPK;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author 19319
 */
@Stateless
public class OrderInfoFacade extends AbstractFacade<OrderInfo> implements OrderInfoFacadeLocal {

    @PersistenceContext(unitName = "T2MH-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public OrderInfoFacade() {
        super(OrderInfo.class);
    }

    @Override
    public void refresh() {
        em.getEntityManagerFactory().getCache().evictAll();
    }
    @Override
    public OrderInfo findByFK(OrderInfoPK orderInfoPK)
    {
        this.refresh();
        Query query = em.createQuery("SELECT oi FROM OrderInfo AS oi WHERE OI.orderInfoPK = :orderInfoPK");
        query.setParameter("orderInfoPK", orderInfoPK);
        return (OrderInfo) query.getSingleResult();
    }
}
