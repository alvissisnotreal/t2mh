/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SessionBean;

import Entity.Category;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author 19319
 */
@Local
public interface CategoryFacadeLocal {

    void create(Category category);

    void edit(Category category);

    void remove(Category category);

    Category find(Object id);

    List<Category> findAll();

    List<Category> findRange(int[] range);

    int count();

    void refresh();

    boolean isNameExists(String cateName);

    boolean isNameExistsForUpdate(String cateID, String cateName);

    Category getCateByName(String cateName);
    
    HashMap<Integer,Long> getHotCategory(int[] listChooseCategory,String typeReport,int branchID,Date dateStart,Date dateEnd);
    
    HashMap<Integer,Long> getReport(int[] listChooseCategory,int[] listChooseGroups,String typeReport,String typeOutput,String typeCaculator,int branchID,Date dateStart,Date dateEnd);
    
    List<Category> findAllCateActive();
    
}
