/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View.Employee;

import Entity.Branch;
import Entity.BranchReview;
import Entity.Customer;
import SessionBean.BranchReviewFacadeLocal;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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
@Named(value = "customerListView")
@ManagedBean
@ViewScoped
public class BRListView implements Serializable {

    @EJB
    private BranchReviewFacadeLocal BRF;

    private LazyDataModel<BranchReview> lazyModel;

    private List<BranchReview> data;

    public List<BranchReview> getData() {
        return data;
    }

    public void setData(List<BranchReview> data) {
        this.data = data;
    }

    @PostConstruct
    public void init() {
        lazyModel = new LazyBRDataModel(BRF.findAll());
    }

    public LazyDataModel<BranchReview> getLazyModel() {
        return lazyModel;
    }

    public void setLazyModel(LazyDataModel<BranchReview> lazyModel) {
        this.lazyModel = lazyModel;
    }

    public class LazyBRDataModel extends LazyDataModel<BranchReview> {

        private List<BranchReview> datasource;

        public LazyBRDataModel(List<BranchReview> datasource) {
            this.datasource = datasource;
        }

        @Override
        public BranchReview getRowData(String rowKey) {
            for (BranchReview perObject : datasource) {
                if (perObject.getBrid().toString().equals(rowKey)) {
                    return perObject;
                }
            }
            return null;
        }

        @Override
        public Object getRowKey(BranchReview object) {
            return object.getBrid();
        }

        @Override
        public List<BranchReview> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
            data = new ArrayList<BranchReview>();

            //refresh datasource 
            BRF.refresh();
            for (int i = 0; i < datasource.size(); i++) {
                BranchReview perObject = datasource.get(i);
                if(perObject!=null)
                {
                    datasource.set(i, BRF.find(perObject.getBrid()));
                }                
            }

            //filter
            for (BranchReview perObject : datasource) {
                boolean match = true;
                if (filters != null) {
                    String filterProperty = "";
                    for (Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {
                        try {
                            filterProperty = it.next();
                            Object filterValue = filters.get(filterProperty);
                            if (filterProperty.equalsIgnoreCase("customerID")) {
                                //perObject.getInverseField Class
                                Field field = perObject.getCustomerID().getClass().getDeclaredField(filterProperty);
                                field.setAccessible(true);                              //getInverseField
                                String fieldValue = String.valueOf(field.get(perObject.getCustomerID())).toLowerCase();
                                if (filterValue == null || fieldValue.startsWith(filterValue.toString().toLowerCase())) {
                                    match = true;
                                } else {
                                    match = false;
                                    break;
                                }
                            } else if (filterProperty.equalsIgnoreCase("branchID")) {
                                //perObject.getInverseField Class
                                Field field = perObject.getBranchID().getClass().getDeclaredField(filterProperty);
                                field.setAccessible(true);                              //getInverseField
                                String fieldValue = String.valueOf(field.get(perObject.getBranchID())).toLowerCase();
                                if (filterValue == null || fieldValue.startsWith(filterValue.toString().toLowerCase())) {
                                    match = true;
                                } else {
                                    match = false;
                                    break;
                                }
                            } else {
                                Field field = perObject.getClass().getDeclaredField(filterProperty);
                                field.setAccessible(true);
                                String fieldValue = String.valueOf(field.get(perObject)).toLowerCase();
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
                    data.add(perObject);
                }
            }

            //sort
            if (sortField != null) {
                try {
                    //for sort Time
                    if (sortField.equalsIgnoreCase("timePost")) {
                        Collections.sort(data, new BRListView.DateTimeLazySorter(sortField, sortOrder));
                    } else {
                        Collections.sort(data, new BRListView.LazySorter(sortField, sortOrder));
                    }

                } catch (Exception e) {
                    // catch when class do not have field to sort, sortField is in InverseField

                    //for Branch sortField
                    if (sortField.equalsIgnoreCase("branchID")) {
                        List<Branch> inverseList = new ArrayList<Branch>();
                        //for mainClass
                        for (BranchReview perObject : data) {
                            boolean isExistsObject = false;
                            //for inverserClass
                            for (Branch perInverseObject : inverseList) {
                                try {               //get InverserClass.InverserIDClass                 //getInverserID
                                    if (perObject.getBranchID().getBranchID().equals(perInverseObject.getBranchID())) {
                                        isExistsObject = true;
                                    }
                                    if (isExistsObject) {
                                        break;
                                    }
                                } catch (Exception ex) {
                                }
                            }
                            if (isExistsObject != true) {//get InverserClass
                                inverseList.add(perObject.getBranchID());
                            }
                        }
                        //                                          //InverserClassLazySorter
                        Collections.sort(inverseList, new BRListView.BranchLazySorterInverseFields(sortField, sortOrder));
                        //mainClass
                        List<BranchReview> newData = new ArrayList<BranchReview>();
                        //InverseClass
                        for (Branch inverse : inverseList) {
                            //mainClass                                                 //getMainClassCollection
                            List<BranchReview> listMainClass = (List<BranchReview>) inverse.getBranchReviewCollection();
                            //mainClass
                            for (Iterator<BranchReview> iterator = listMainClass.iterator(); iterator.hasNext();) {
                                //mainClass
                                BranchReview next = iterator.next();
                                for (BranchReview mainClass : data) {
                                    if (next.equals(mainClass)) {
                                        newData.add(next);
                                    }
                                }
                            }
                        }
                        data = newData;
                    }

                    //for Customer Sort Field
                    if (sortField.equalsIgnoreCase("customerID")) {
                        //inverseClass
                        List<Customer> inverseList = new ArrayList<Customer>();
                        //for mainClass
                        for (BranchReview perObject : data) {
                            boolean isExistsObject = false;
                            //for inverserClass
                            for (Customer perInverseObject : inverseList) {
                                try {               //get InverserClass.InverserIDClass                 //getInverserID
                                    if (perObject.getCustomerID().getCustomerID().equals(perInverseObject.getCustomerID())) {
                                        isExistsObject = true;
                                    }
                                    if (isExistsObject) {
                                        break;
                                    }
                                } catch (Exception ex) {
                                }
                            }
                            if (isExistsObject != true) {//get InverserClass
                                inverseList.add(perObject.getCustomerID());
                            }
                        }
                        //                                          //InverserClassLazySorter
                        Collections.sort(inverseList, new BRListView.CustomerLazySorterInverseFields(sortField, sortOrder));
                        //mainClass
                        List<BranchReview> newData = new ArrayList<BranchReview>();
                        //InverseClass
                        for (Customer inverse : inverseList) {
                            //mainClass                                                 //getMainClassCollection
                            List<BranchReview> listMainClass = (List<BranchReview>) inverse.getBranchReviewCollection();
                            //mainClass
                            for (Iterator<BranchReview> iterator = listMainClass.iterator(); iterator.hasNext();) {
                                //mainClass
                                BranchReview next = iterator.next();
                                for (BranchReview mainClass : data) {
                                    if (next.equals(mainClass)) {
                                        newData.add(next);
                                    }
                                }
                            }
                        }
                        data = newData;
                    }
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

    public class DateTimeLazySorter implements Comparator<BranchReview> {

        private String sortField;

        private SortOrder sortOrder;

        public DateTimeLazySorter(String sortField, SortOrder sortOrder) {
            this.sortField = sortField;
            this.sortOrder = sortOrder;
        }

        public int compare(BranchReview obj1, BranchReview obj2) {
            try {
                DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Field field1 = obj1.getClass().getDeclaredField(this.sortField);
                field1.setAccessible(true);
                Field field2 = obj2.getClass().getDeclaredField(this.sortField);
                field2.setAccessible(true);
                Date value1 = (Date) formatter.parse(field1.get(obj1).toString());
                Date value2 = (Date) formatter.parse(field2.get(obj2).toString());
                int value = ((Comparable) value1).compareTo(value2);
                return SortOrder.ASCENDING.equals(sortOrder) ? value : -1 * value;
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException();
            }
        }
    }

    public class LazySorter implements Comparator<BranchReview> {

        private String sortField;

        private SortOrder sortOrder;

        public LazySorter(String sortField, SortOrder sortOrder) {
            this.sortField = sortField;
            this.sortOrder = sortOrder;
        }

        public int compare(BranchReview obj1, BranchReview obj2) {
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

    public class CustomerLazySorterInverseFields implements Comparator<Customer> {

        private String sortField;

        private SortOrder sortOrder;

        public CustomerLazySorterInverseFields(String sortField, SortOrder sortOrder) {
            this.sortField = sortField;
            this.sortOrder = sortOrder;
        }

        public int compare(Customer obj1, Customer obj2) {
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
                if (value1 == null && value2 == null) {
                    return 0;
                }
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

    public class BranchLazySorterInverseFields implements Comparator<Branch> {

        private String sortField;

        private SortOrder sortOrder;

        public BranchLazySorterInverseFields(String sortField, SortOrder sortOrder) {
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
                if (value1 == null && value2 == null) {
                    return 0;
                }
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
