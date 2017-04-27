/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SessionBean;

import Entity.Branch;
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
public class BranchFacade extends AbstractFacade<Branch> implements BranchFacadeLocal {

    @PersistenceContext(unitName = "T2MH-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public BranchFacade() {
        super(Branch.class);
    }

    @Override
    public void refresh() {
        em.getEntityManagerFactory().getCache().evictAll();
    }

    @Override
    public boolean isNameExists(String branchName) {
        this.refresh();
        Query query = em.createNamedQuery("Branch.findByBranchName");
        query.setParameter("branchName", branchName);
        List<Branch> listBranch = new ArrayList<Branch>();
        listBranch = query.getResultList();
        if (listBranch.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isNameExistsForUpdate(String branchID, String branchName) {
        this.refresh();
        Query query = em.createNamedQuery("Branch.findByBranchName");
        query.setParameter("branchName", branchName);
        List<Branch> listBranch = new ArrayList<Branch>();
        listBranch = query.getResultList();
        if (listBranch.size() > 0) {
            Branch branch = (Branch) listBranch.get(0);
            if (branch.getBranchID().toString().equals(branchID)) {
                return false;
            } else {
                return true;
            }

        } else {
            return false;
        }
    }

    @Override
    public Branch getBranchByName(String branchName) {
        this.refresh();
        Query query = em.createNamedQuery("Branch.findByBranchName");
        query.setParameter("branchName", branchName);
        List<Branch> listBranch = new ArrayList<Branch>();
        listBranch = query.getResultList();
        if (listBranch.size() > 0) {
            return listBranch.get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<Branch> getBranchListBySearchLikeName(String keySearch) {
        List<Branch> listBranch = new ArrayList<Branch>();
        Query query = em.createQuery("SELECT b FROM Branch b WHERE b.branchName LIKE :branchName");
        query.setParameter("branchName", keySearch);
        listBranch = (List<Branch>) query.getResultList();
        return listBranch;
    }

    @Override
    public boolean updateBranch(Branch branch) {
        refresh();
        Query query = em.createQuery("UPDATE Branch SET branchName = :branchName, branchStatus = :branchStatus, branchDescriptions = :branchDes WHERE branchID = :branchID");
        query.setParameter("branchName", branch.getBranchName());
        query.setParameter("branchStatus", branch.getBranchStatus());
        query.setParameter("branchDes", branch.getBranchDescriptions());
        query.setParameter("branchID", branch.getBranchID());
        if (query.executeUpdate() > 0) {
            return true;
        } else {
            return false;
        }
    }
    @Override
    public List<Branch> getBranchActive() {
        this.refresh();
        Query query = em.createNamedQuery("Branch.findByBranchStatus");
        query.setParameter("branchStatus", 1);
        List<Branch> listBranch = new ArrayList<Branch>();
        listBranch = query.getResultList();
        return listBranch;
    }
}
