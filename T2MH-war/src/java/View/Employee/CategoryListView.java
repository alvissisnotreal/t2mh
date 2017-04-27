/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View.Employee;

import Entity.Category;
import SessionBean.CategoryFacadeLocal;
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
@Named(value = "CategoryListView")
@ManagedBean
@ViewScoped
public class CategoryListView implements Serializable {

    @EJB
    private CategoryFacadeLocal CF;

    private LazyDataModel<Category> lazyModel;

    private List<Category> data;

    public List<Category> getData() {
        return data;
    }

    public void setData(List<Category> data) {
        this.data = data;
    }

    @PostConstruct
    public void init() {
        lazyModel = new LazyCategoryDataModel(CF.findAll());
    }

    public LazyDataModel<Category> getLazyModel() {
        return lazyModel;
    }

    public void setLazyModel(LazyDataModel<Category> lazyModel) {
        this.lazyModel = lazyModel;
    }

    public class LazyCategoryDataModel extends LazyDataModel<Category> {

        private List<Category> datasource;

        public LazyCategoryDataModel(List<Category> datasource) {
            this.datasource = datasource;
        }

        @Override
        public Category getRowData(String rowKey) {
            for (Category perObject : datasource) {
                if (perObject.getCateID().toString().equals(rowKey)) {
                    return perObject;
                }
            }
            return null;
        }

        @Override
        public Object getRowKey(Category object) {
            return object.getCateID();
        }

        @Override
        public List<Category> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
            data = new ArrayList<Category>();

            //refresh datasource 
            CF.refresh();
            for (int i = 0; i < datasource.size(); i++) {
                Category perObject = datasource.get(i);
                if (perObject != null) {
                    datasource.set(i, CF.find(perObject.getCateID()));
                }

            }

            //filter
            for (Category perObject : datasource) {
                boolean match = true;
                if (filters != null) {
                    String filterProperty = "";
                    for (Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {
                        try {
                            filterProperty = it.next();
                            Object filterValue = filters.get(filterProperty);
                            Field field = perObject.getClass().getDeclaredField(filterProperty);
                            field.setAccessible(true);
                            String fieldValue = String.valueOf(field.get(perObject)).toLowerCase();
                            if (filterValue == null || fieldValue.startsWith(filterValue.toString().toLowerCase())) {
                                match = true;
                            } else {
                                match = false;
                                break;
                            }
                        } catch (Exception e) {
                            match = false;
                        }
                    }
                }

                if (match) {
                    data.add(perObject);
                }
            }

            //sort
            if (sortField != null) {
                try {
                    Collections.sort(data, new CategoryListView.LazySorter(sortField, sortOrder));
                } catch (Exception e) {

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

    public class LazySorter implements Comparator<Category> {

        private String sortField;

        private SortOrder sortOrder;

        public LazySorter(String sortField, SortOrder sortOrder) {
            this.sortField = sortField;
            this.sortOrder = sortOrder;
        }

        public int compare(Category obj1, Category obj2) {
            try {
                Field field1 = obj1.getClass().getDeclaredField(this.sortField);
                field1.setAccessible(true);
                Field field2 = obj2.getClass().getDeclaredField(this.sortField);
                field2.setAccessible(true);
                Object value1 = field1.get(obj1);
                Object value2 = field2.get(obj2);
                int value = ((Comparable) value1).compareTo(value2);
                return SortOrder.ASCENDING.equals(sortOrder) ? value : -1 * value;
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException();
            }
        }
    }
}
