/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View.Employee;

import Entity.Branch;
import Entity.BranchManager;
import Entity.Category;
import Entity.Groups;
import SessionBean.CategoryFacadeLocal;
import SessionBean.GroupsFacadeLocal;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author 19319
 */
@Named(value = "groupsListView")
@ManagedBean
@ViewScoped
public class GroupsListView implements Serializable {

    @EJB
    private CategoryFacadeLocal CF;

    @EJB
    private GroupsFacadeLocal GF;

    private LazyDataModel<Groups> lazyModel;

    private List<Groups> data;

    public List<Groups> getData() {
        return data;
    }

    public void setData(List<Groups> data) {
        this.data = data;
    }

    @PostConstruct
    public void init() {
        lazyModel = new LazyGroupsDataModel(GF.findAll());
    }

    public LazyDataModel<Groups> getLazyModel() {
        return lazyModel;
    }

    public void setLazyModel(LazyDataModel<Groups> lazyModel) {
        this.lazyModel = lazyModel;
    }

    public class LazyGroupsDataModel extends LazyDataModel<Groups> {

        private List<Groups> datasource;

        public LazyGroupsDataModel(List<Groups> datasource) {
            this.datasource = datasource;
        }

        @Override
        public Groups getRowData(String rowKey) {
            for (Groups group : datasource) {
                if (group.getGroupID().equals(rowKey)) {
                    return group;
                }
            }
            return null;
        }

        @Override
        public Object getRowKey(Groups groups) {
            return groups.getGroupID();
        }

        @Override
        public List<Groups> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
            data = new ArrayList<Groups>();
            GF.refresh();
            for (int i = 0; i < datasource.size(); i++) {
                Groups group = datasource.get(i);
                if (group != null) {
                    datasource.set(i, GF.find(group.getGroupID()));
                }

            }

            //filter
            for (Groups group : datasource) {
                boolean match = true;

                if (filters != null) {
                    String filterProperty = "";
                    for (Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {
                        try {
                            filterProperty = it.next();
                            Object filterValue = filters.get(filterProperty);
                            if (filterProperty.equalsIgnoreCase("cateName")) {
                                //field = class.getOtherClassID
                                Field field = group.getCateID().getClass().getDeclaredField(filterProperty);
                                field.setAccessible(true);
                                //field.get(class.getOtherClassID)
                                String fieldValue = String.valueOf(field.get(group.getCateID())).toLowerCase();
                                if (filterValue == null || fieldValue.startsWith(filterValue.toString().toLowerCase())) {
                                    match = true;
                                } else {
                                    match = false;
                                    break;
                                }
                            } else {
                                Field field = group.getClass().getDeclaredField(filterProperty);
                                field.setAccessible(true);
                                String fieldValue = String.valueOf(field.get(group)).toLowerCase();
                                if (filterValue == null || fieldValue.startsWith(filterValue.toString().toLowerCase())) {
                                    match = true;
                                } else {
                                    match = false;
                                    break;
                                }
                            }

                        } catch (Exception e) {
                            match = false;
                        }
                    }
                }

                if (match) {
                    data.add(group);
                }
            }

            //sort
            if (sortField != null) {
                try {
                    Collections.sort(data, new GroupsListView.LazySorter(sortField, sortOrder));
                } catch (Exception e) {
                    // catch when class do not have field to sort, sortField is in InverseField
                    List<Category> C = new ArrayList<Category>();
                    for (Groups group : data) {
                        boolean isExistsGroups = false;
                        for (Category cate : C) {
                            if (group.getCateID().getCateID() == cate.getCateID()) {
                                isExistsGroups = true;
                            }
                            if (isExistsGroups) {
                                break;
                            }
                        }
                        if (isExistsGroups != true) {
                            C.add(group.getCateID());
                        }
                    }

                    Collections.sort(C, new GroupsListView.LazySorterInverseFields(sortField, sortOrder));
                    List<Groups> newData = new ArrayList<Groups>();
                    for (Category cate : C) {
                        List<Groups> listGroups = (List<Groups>) cate.getGroupsCollection();
                        for (Iterator<Groups> iterator = listGroups.iterator(); iterator.hasNext();) {
                            Groups next = iterator.next();
                            for (Groups group : data) {
                                if (next.equals(group)) {
                                    newData.add(next);
                                }
                            }
                        }
                    }
                    data = newData;
                }
            }

            //rowCount
            int dataSize = data.size();
            this.setRowCount(dataSize);

            //paginate
            if (dataSize > pageSize) {
                try {
                    return data.subList(first, first + pageSize);
                } catch (IndexOutOfBoundsException e) {
                    return data.subList(first, first + (dataSize % pageSize));
                }
            } else {
                return data;
            }
        }
    }

    public class LazySorter implements Comparator<Groups> {

        private String sortField;

        private SortOrder sortOrder;

        public LazySorter(String sortField, SortOrder sortOrder) {
            this.sortField = sortField;
            this.sortOrder = sortOrder;
        }

        public int compare(Groups object1, Groups object2) {
            try {
                Field field1 = object1.getClass().getDeclaredField(this.sortField);
                field1.setAccessible(true);
                Field field2 = object2.getClass().getDeclaredField(this.sortField);
                field2.setAccessible(true);
                Object value1 = field1.get(object1);
                Object value2 = field2.get(object2);
                int value = ((Comparable) value1).compareTo(value2);
                return SortOrder.ASCENDING.equals(sortOrder) ? value : -1 * value;
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException();
            }
        }
    }

    public class LazySorterInverseFields implements Comparator<Category> {

        private String sortField;

        private SortOrder sortOrder;

        public LazySorterInverseFields(String sortField, SortOrder sortOrder) {
            this.sortField = sortField;
            this.sortOrder = sortOrder;
        }

        public int compare(Category obj1, Category obj2) {
            try {
                if (obj1 == null && obj2 == null) {
                    return 0;
                }
                if (obj1 == null) {
                    return SortOrder.ASCENDING.equals(sortOrder) ? 1 : -1;
                } else if (obj2 == null) {
                    return SortOrder.ASCENDING.equals(sortOrder) ? -1 : 1;
                }
                Field field1 = obj1.getClass().getDeclaredField(this.sortField);
                field1.setAccessible(true);
                Field field2 = obj2.getClass().getDeclaredField(this.sortField);
                field2.setAccessible(true);
                Object value1 = field1.get(obj1);
                Object value2 = field2.get(obj2);
                if (value1 == null) {
                    return SortOrder.ASCENDING.equals(sortOrder) ? 1 : -1;
                } else if (value2 == null) {
                    return SortOrder.ASCENDING.equals(sortOrder) ? -1 : 1;
                }
                int value = ((Comparable) value1).compareTo(value2);
                return SortOrder.ASCENDING.equals(sortOrder) ? value : -1 * value;
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException();
            }
        }
    }
}
