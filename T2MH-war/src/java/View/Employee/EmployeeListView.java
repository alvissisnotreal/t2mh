/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View.Employee;

import Entity.Employee;
import SessionBean.EmployeeFacadeLocal;
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
@Named(value = "employeeListView")
@ManagedBean
@ViewScoped
public class EmployeeListView implements Serializable {

    @EJB
    private EmployeeFacadeLocal EF;

    private LazyDataModel<Employee> lazyModel;

    @PostConstruct
    public void init() {
        lazyModel = new LazyEmpDataModel(EF.findAll());
    }

    public LazyDataModel<Employee> getLazyModel() {
        return lazyModel;
    }

    public void setLazyModel(LazyDataModel<Employee> lazyModel) {
        this.lazyModel = lazyModel;
    }

    public class LazyEmpDataModel extends LazyDataModel<Employee> {

        private List<Employee> datasource;

        public LazyEmpDataModel(List<Employee> datasource) {
            this.datasource = datasource;
        }

        @Override
        public Employee getRowData(String rowKey) {
            for (Employee emp : datasource) {
                if (emp.getEmployeeID().equals(rowKey)) {
                    return emp;
                }
            }
            return null;
        }

        @Override
        public Object getRowKey(Employee emp) {
            return emp.getEmployeeID();
        }

        @Override
        public List<Employee> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
            List<Employee> data = new ArrayList<Employee>();
            EF.refresh();
            for (int i = 0; i < datasource.size(); i++) {
                Employee emp = datasource.get(i);
                if (emp != null) {
                    datasource.set(i, EF.find(emp.getEmployeeID()));
                }

            }
            //filter
            for (Employee emp : datasource) {
                boolean match = true;

                if (filters != null) {
                    for (Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {
                        try {
                            String filterProperty = it.next();
                            Object filterValue = filters.get(filterProperty);
                            Field field = emp.getClass().getDeclaredField(filterProperty);
                            field.setAccessible(true);
                            String fieldValue = String.valueOf(field.get(emp)).toLowerCase();
                            if (filterValue == null || fieldValue.startsWith(filterValue.toString().toLowerCase())) {
                                match = true;
                            } else {
                                match = false;
                                break;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            match = false;
                        }
                    }
                }

                if (match) {
                    data.add(emp);
                }
            }

            //sort
            if (sortField != null) {
                Collections.sort(data, new LazySorter(sortField, sortOrder));
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

    public class LazySorter implements Comparator<Employee> {

        private String sortField;

        private SortOrder sortOrder;

        public LazySorter(String sortField, SortOrder sortOrder) {
            this.sortField = sortField;
            this.sortOrder = sortOrder;
        }

        public int compare(Employee emp1, Employee emp2) {
            try {
                Field field1 = emp1.getClass().getDeclaredField(this.sortField);
                field1.setAccessible(true);
                Field field2 = emp2.getClass().getDeclaredField(this.sortField);
                field2.setAccessible(true);
                Object value1 = field1.get(emp1);
                Object value2 = field2.get(emp2);
                int value = ((Comparable) value1).compareTo(value2);
                return SortOrder.ASCENDING.equals(sortOrder) ? value : -1 * value;
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException();
            }
        }
    }
}
