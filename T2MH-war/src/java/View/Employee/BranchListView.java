/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View.Employee;

import Entity.Branch;
import SessionBean.BranchFacadeLocal;
import SessionBean.ProductFacadeLocal;
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
@Named(value = "branchListView")
@ManagedBean
@ViewScoped
public class BranchListView implements Serializable {

    @EJB
    private BranchFacadeLocal BF;

    private LazyDataModel<Branch> lazyModel;

    @PostConstruct
    public void init() {
        lazyModel = new LazyBranchDataModel(BF.findAll());
    }

    public LazyDataModel<Branch> getLazyModel() {
        return lazyModel;
    }

    public void setLazyModel(LazyDataModel<Branch> lazyModel) {
        this.lazyModel = lazyModel;
    }

    

    public class LazyBranchDataModel extends LazyDataModel<Branch> {

        private List<Branch> datasource;

        public LazyBranchDataModel(List<Branch> datasource) {
            this.datasource = datasource;
        }

        @Override
        public Branch getRowData(String rowKey) {
            for (Branch branch : datasource) {
                if (branch.getBranchID().equals(rowKey)) {
                    return branch;
                }
            }
            return null;
        }

        @Override
        public Object getRowKey(Branch branch) {
            return branch.getBranchID();
        }

        @Override
        public List<Branch> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
            List<Branch> data = new ArrayList<Branch>();
            BF.refresh();
            for (int i = 0; i < datasource.size(); i++) {
                Branch branch = datasource.get(i);
                if(branch!=null)
                {
                    datasource.set(i, BF.find(branch.getBranchID()));
                }
                
            }
            //filter
            for (Branch branch : datasource) {
                boolean match = true;

                if (filters != null) {
                    for (Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {
                        try {
                            String filterProperty = it.next();
                            Object filterValue = filters.get(filterProperty);
                            Field field = branch.getClass().getDeclaredField(filterProperty);
                            field.setAccessible(true);
                            String fieldValue = String.valueOf(field.get(branch));
                            if (filterValue == null || fieldValue.startsWith(filterValue.toString())) {
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
                    data.add(branch);
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

    public class LazySorter implements Comparator<Branch> {

        private String sortField;

        private SortOrder sortOrder;

        public LazySorter(String sortField, SortOrder sortOrder) {
            this.sortField = sortField;
            this.sortOrder = sortOrder;
        }

        public int compare(Branch branch1, Branch branch2) {
            try {
                Field field1 = branch1.getClass().getDeclaredField(this.sortField);
                field1.setAccessible(true);
                Field field2 = branch2.getClass().getDeclaredField(this.sortField);
                field2.setAccessible(true);
                Object value1 = field1.get(branch1);
                Object value2 = field2.get(branch2);
                int value = ((Comparable) value1).compareTo(value2);
                return SortOrder.ASCENDING.equals(sortOrder) ? value : -1 * value;
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException();
            }
        }
    }
}
