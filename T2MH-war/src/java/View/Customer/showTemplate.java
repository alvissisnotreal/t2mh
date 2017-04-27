/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View.Customer;

import Entity.Category;
import Entity.Groups;
import SessionBean.CategoryFacadeLocal;
import SessionBean.GroupsFacadeLocal;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.ejb.EJB;

/**
 *
 * @author Huy-PC
 */
@Named(value = "showTemplate")
@SessionScoped
public class showTemplate implements Serializable {

    @EJB
    private GroupsFacadeLocal groupsFacade;

    @EJB
    private CategoryFacadeLocal categoryFacade;

    List<Category> list;
    List<Groups> listGroup;
    private Groups groups;

    public Groups getGroups() {
        return groups;
    }

    public void setGroups(Groups groups) {
        this.groups = groups;
    }
    
    public showTemplate() {
    }

    public List<Groups> getListGroup() {
        return listGroup;
    }

    public void setListGroup(List<Groups> listGroup) {
        this.listGroup = listGroup;
    }

    public CategoryFacadeLocal getCategoryFacade() {
        return categoryFacade;
    }

    public void setCategoryFacade(CategoryFacadeLocal categoryFacade) {
        this.categoryFacade = categoryFacade;
    }

    public List<Category> getList() {
        return list;
    }

    public void setList(List<Category> list) {
        this.list = list;
    }

    public GroupsFacadeLocal getGroupsFacade() {
        return groupsFacade;
    }

    public void setGroupsFacade(GroupsFacadeLocal groupsFacade) {
        this.groupsFacade = groupsFacade;
    }


    public showTemplate(CategoryFacadeLocal categoryFacade, List<Category> list) {
        this.categoryFacade = categoryFacade;
        this.list = list;
    }
    List<String> listGroupName;

    public List<String> getListGroupName() {
        return listGroupName;
    }

    public void setListGroupName(List<String> listGroupName) {
        this.listGroupName = listGroupName;
    }

    public void showCate() {

        list = new ArrayList<Category>();
        listGroup = new ArrayList<Groups>();
        listGroupName = new ArrayList<String>();
        list = categoryFacade.findAllCateActive();
        for (int i = 0; i < list.size(); i++) {
            listGroup = (List<Groups>) categoryFacade.find(list.get(i).getCateID()).getGroupsCollection();
            
        }
    }

    public List<Groups> convert(Collection<Groups> co) {
       List<Groups> list = new ArrayList<Groups>(co);
       return list;
    }
}
