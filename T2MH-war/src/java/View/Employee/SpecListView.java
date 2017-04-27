/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View.Employee;

import Entity.Specifics;
import SessionBean.SpecificsFacadeLocal;
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
@Named(value = "specListView")
@ManagedBean
@ViewScoped
public class SpecListView implements Serializable {

    @EJB
    private SpecificsFacadeLocal specF;

    private LazyDataModel<Specifics> lazyModel;

    private List<Specifics> data;

    public List<Specifics> getData() {
        return data;
    }

    public void setData(List<Specifics> data) {
        this.data = data;
    }

    @PostConstruct
    public void init() {
        lazyModel = new LazySpecDataModel(specF.findAll());
    }

    public LazyDataModel<Specifics> getLazyModel() {
        return lazyModel;
    }

    public void setLazyModel(LazyDataModel<Specifics> lazyModel) {
        this.lazyModel = lazyModel;
    }

    public class LazySpecDataModel extends LazyDataModel<Specifics> {

        private List<Specifics> datasource;

        public LazySpecDataModel(List<Specifics> datasource) {
            this.datasource = datasource;
        }

        @Override
        public Specifics getRowData(String rowKey) {
            for (Specifics perObject : datasource) {
                if (perObject.getSpecID().toString().equals(rowKey)) {
                    return perObject;
                }
            }
            return null;
        }

        @Override
        public Object getRowKey(Specifics object) {
            return object.getSpecID();
        }

        @Override
        public List<Specifics> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
            data = new ArrayList<Specifics>();

            //refresh datasource 
            specF.refresh();
            for (int i = 0; i < datasource.size(); i++) {
                Specifics perObject = datasource.get(i);
                if (perObject != null) {
                    datasource.set(i, specF.find(perObject.getSpecID()));
                }
            }

            //filter
            for (Specifics perObject : datasource) {
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
                    Collections.sort(data, new SpecListView.LazySorter(sortField, sortOrder));
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

    public class LazySorter implements Comparator<Specifics> {

        private String sortField;

        private SortOrder sortOrder;

        public LazySorter(String sortField, SortOrder sortOrder) {
            this.sortField = sortField;
            this.sortOrder = sortOrder;
        }

        public int compare(Specifics obj1, Specifics obj2) {
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
