/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View.Employee;

import Entity.Branch;
import Entity.BranchManager;
import Entity.Product;
import SessionBean.BranchFacadeLocal;
import SessionBean.BranchManagerFacadeLocal;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author 19319
 */
@Named(value = "BMListView")
@ManagedBean
@ViewScoped
public class BMListView implements Serializable {

    @EJB
    private BranchFacadeLocal BF;

    @EJB
    private BranchManagerFacadeLocal BMF;

    private LazyDataModel<BranchManager> lazyModel;

    private List<BranchManager> data;

    public List<BranchManager> getData() {
        return data;
    }

    public void setData(List<BranchManager> data) {
        this.data = data;
    }

    @PostConstruct
    public void init() {
        lazyModel = new LazyBMDataModel(BMF.findAll());
    }

    public LazyDataModel<BranchManager> getLazyModel() {
        return lazyModel;
    }

    public void setLazyModel(LazyDataModel<BranchManager> lazyModel) {
        this.lazyModel = lazyModel;
    }

    public class LazyBMDataModel extends LazyDataModel<BranchManager> {

        private List<BranchManager> datasource;

        public LazyBMDataModel(List<BranchManager> datasource) {
            this.datasource = datasource;
        }

        @Override
        public BranchManager getRowData(String rowKey) {
            for (BranchManager bm : datasource) {
                if (bm.getBmUsername().equals(rowKey)) {
                    return bm;
                }
            }
            return null;
        }

        @Override
        public Object getRowKey(BranchManager bm) {
            return bm.getBmUsername();
        }

        @Override
        public List<BranchManager> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
            data = new ArrayList<BranchManager>();
            BMF.refresh();
            for (int i = 0; i < datasource.size(); i++) {
                BranchManager bm = datasource.get(i);
                if (bm != null) {
                    datasource.set(i, BMF.find(bm.getBmUsername()));
                }

            }

            //filter
            for (BranchManager bm : datasource) {
                boolean match = true;

                if (filters != null) {
                    String filterProperty = "";
                    for (Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {
                        try {
                            filterProperty = it.next();
                            Object filterValue = filters.get(filterProperty);
                            if (filterProperty.equalsIgnoreCase("branchID") || filterProperty.equalsIgnoreCase("branchName")) {
                                Field field = bm.getBranchID().getClass().getDeclaredField(filterProperty);
                                field.setAccessible(true);
                                String fieldValue = String.valueOf(field.get(bm.getBranchID())).toLowerCase();
                                if (filterValue == null || fieldValue.startsWith(filterValue.toString().toLowerCase())) {
                                    match = true;
                                } else {
                                    match = false;
                                    break;
                                }
                            } else {
                                Field field = bm.getClass().getDeclaredField(filterProperty);
                                field.setAccessible(true);
                                String fieldValue = String.valueOf(field.get(bm)).toLowerCase();
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
                    data.add(bm);
                }
            }

            //sort
            if (sortField != null) {
                try {
                    Collections.sort(data, new BMListView.LazySorter(sortField, sortOrder));
                } catch (Exception e) {
                    // catch when class do not have field to sort, sortField is in InverseField
                    List<Branch> B = new ArrayList<Branch>();
                    for (BranchManager bm : data) {
                        boolean isExistsBM = false;
                        for (Branch branch : B) {
                            if (bm.getBranchID().getBranchID() == branch.getBranchID()) {
                                isExistsBM = true;
                            }
                            if (isExistsBM) {
                                break;
                            }
                        }
                        if (isExistsBM != true) {
                            B.add(bm.getBranchID());
                        }
                    }

                    Collections.sort(B, new BMListView.LazySorterInverseFields(sortField, sortOrder));
                    List<BranchManager> newData = new ArrayList<BranchManager>();
                    for (Branch branch : B) {
                        List<BranchManager> listBM = (List<BranchManager>) branch.getBranchManagerCollection();
                        for (Iterator<BranchManager> iterator = listBM.iterator(); iterator.hasNext();) {
                            BranchManager next = iterator.next();
                            for (BranchManager branchManager : data) {
                                if (next.equals(branchManager)) {
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

    public class LazySorter implements Comparator<BranchManager> {

        private String sortField;

        private SortOrder sortOrder;

        public LazySorter(String sortField, SortOrder sortOrder) {
            this.sortField = sortField;
            this.sortOrder = sortOrder;
        }

        public int compare(BranchManager bm1, BranchManager bm2) {
            try {
                Field field1 = bm1.getClass().getDeclaredField(this.sortField);
                field1.setAccessible(true);
                Field field2 = bm2.getClass().getDeclaredField(this.sortField);
                field2.setAccessible(true);
                Object value1 = field1.get(bm1);
                Object value2 = field2.get(bm2);
                int value = ((Comparable) value1).compareTo(value2);
                return SortOrder.ASCENDING.equals(sortOrder) ? value : -1 * value;
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException();
            }
        }
    }

    public class LazySorterInverseFields implements Comparator<Branch> {

        private String sortField;

        private SortOrder sortOrder;

        public LazySorterInverseFields(String sortField, SortOrder sortOrder) {
            this.sortField = sortField;
            this.sortOrder = sortOrder;
        }

        public int compare(Branch obj1, Branch obj2) {
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
