/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View.Employee;

import Entity.Customer;
import Entity.CustomerInfo;
import SessionBean.CustomerFacadeLocal;
import SessionBean.CustomerInfoFacadeLocal;
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
@Named(value = "customerListView")
@ManagedBean
@ViewScoped
public class CustomerListView implements Serializable {

    @EJB
    private CustomerFacadeLocal CF;

    private LazyDataModel<Customer> lazyModel;

    private List<Customer> data;

    public List<Customer> getData() {
        return data;
    }

    public void setData(List<Customer> data) {
        this.data = data;
    }

    @PostConstruct
    public void init() {
        lazyModel = new LazyBMDataModel(CF.findAll());
    }

    public LazyDataModel<Customer> getLazyModel() {
        return lazyModel;
    }

    public void setLazyModel(LazyDataModel<Customer> lazyModel) {
        this.lazyModel = lazyModel;
    }

    public class LazyBMDataModel extends LazyDataModel<Customer> {

        private List<Customer> datasource;

        public LazyBMDataModel(List<Customer> datasource) {
            this.datasource = datasource;
        }

        @Override
        public Customer getRowData(String rowKey) {
            for (Customer perObject : datasource) {
                if (perObject.getCustomerID().toString().equals(rowKey)) {
                    return perObject;
                }
            }
            return null;
        }

        @Override
        public Object getRowKey(Customer object) {
            return object.getCustomerID();
        }

        @Override
        public List<Customer> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
            data = new ArrayList<Customer>();

            //refresh datasource 
            CF.refresh();
            for (int i = 0; i < datasource.size(); i++) {
                Customer perObject = datasource.get(i);
                if(perObject!=null)
                {
                    datasource.set(i, CF.find(perObject.getCustomerID()));
                }
            }

            //filter
            for (Customer perObject : datasource) {
                boolean match = true;
                if (filters != null) {
                    String filterProperty = "";
                    for (Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {
                        try {
                            filterProperty = it.next();
                            Object filterValue = filters.get(filterProperty);
                            if (filterProperty.equalsIgnoreCase("customerName") || filterProperty.equalsIgnoreCase("customerPhone") || filterProperty.equalsIgnoreCase("customerEmail")) {
                                //perObject.getInverseField Class
                                Field field = perObject.getCustomerInfo().getClass().getDeclaredField(filterProperty);
                                field.setAccessible(true);
                                String fieldValue = String.valueOf(field.get(perObject.getCustomerInfo())).toLowerCase();
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
                    Collections.sort(data, new CustomerListView.LazySorter(sortField, sortOrder));
                } catch (Exception e) {
                    // catch when class do not have field to sort, sortField is in InverseField
                    List<CustomerInfo> inverseList = new ArrayList<CustomerInfo>();
                    for (Customer perObject : data) {
                        boolean isExistsObject = false;
                        for (CustomerInfo perInverseObject : inverseList) {
                            try {
                                if (perObject.getCustomerInfo().getCustomerID().equals(perInverseObject.getCustomerID())) {
                                    isExistsObject = true;
                                }
                                if (isExistsObject) {
                                    break;
                                }
                            } catch (Exception ex) {
                            }
                        }
                        if (isExistsObject != true) {
                            inverseList.add(perObject.getCustomerInfo());
                        }
                    }

                    Collections.sort(inverseList, new CustomerListView.LazySorterInverseFields(sortField, sortOrder));
//                    data=new ArrayList<>();
                    List<Customer> newData = new ArrayList<>();
                    for (int i = 0; i < inverseList.size(); i++) {
                        try {
                            if (inverseList != null) {
                                Customer tempObject = inverseList.get(i).getCustomer();
                                newData.add(tempObject);
                            }
                        } catch (Exception aE) {
                        }
                    }
                    List<Customer> test = data;
//                    List<Customer> nullAfterFilter = data;
                    data = new ArrayList<Customer>();

                    for (Customer tempObject : newData) {
                        boolean isExists = false;
                        if (tempObject != null) {
                            for (Customer tempPerOfData : data) {
                                if (tempPerOfData.equals(tempObject)) {
                                    isExists = true;
                                }
                            }
                            for (Iterator<Customer> iterator = test.iterator(); iterator.hasNext();) {
                                Customer tempIter=iterator.next();
                                if (tempIter.equals(tempObject)) {
                                    iterator.remove();
                                }
                            }
                            if (isExists == false) {
                                data.add(tempObject);
                            }
                        }
                    }
                    for (Customer objectNull : test) {
                        data.add(objectNull);
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

    public class LazySorter implements Comparator<Customer> {

        private String sortField;

        private SortOrder sortOrder;

        public LazySorter(String sortField, SortOrder sortOrder) {
            this.sortField = sortField;
            this.sortOrder = sortOrder;
        }

        public int compare(Customer obj1, Customer obj2) {
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

    public class LazySorterInverseFields implements Comparator<CustomerInfo> {

        private String sortField;

        private SortOrder sortOrder;

        public LazySorterInverseFields(String sortField, SortOrder sortOrder) {
            this.sortField = sortField;
            this.sortOrder = sortOrder;
        }

        public int compare(CustomerInfo obj1, CustomerInfo obj2) {
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
