/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SessionBean;

import Entity.Specifics;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author 19319
 */
@Local
public interface SpecificsFacadeLocal {

    void create(Specifics specifics);

    void edit(Specifics specifics);

    void remove(Specifics specifics);

    Specifics find(Object id);

    List<Specifics> findAll();

    List<Specifics> findRange(int[] range);

    int count();

    void refresh();

    boolean isNameExists(String specName);

    boolean isNameExistsForUpdate(String specID, String specName);

    Specifics getSpecByName(String specName);
}
