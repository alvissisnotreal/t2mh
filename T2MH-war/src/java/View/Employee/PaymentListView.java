/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View.Employee;

import Entity.Branch;
import Entity.Customer;
import Entity.CustomerInfo;
import Entity.Payment;
import SessionBean.CustomerFacadeLocal;
import SessionBean.CustomerInfoFacadeLocal;
import SessionBean.PaymentFacadeLocal;
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
@Named(value = "paymentListView")
@ManagedBean
@ViewScoped
public class PaymentListView implements Serializable {

    @EJB
    private PaymentFacadeLocal paymentF;

    private LazyDataModel<Payment> lazyModel;

    private List<Payment> data;

    public List<Payment> getData() {
        return data;
    }

    public void setData(List<Payment> data) {
        this.data = data;
    }

    @PostConstruct
    public void init() {
        lazyModel = new LazyPaymentDataModel(paymentF.findAll());
    }

    public LazyDataModel<Payment> getLazyModel() {
        return lazyModel;
    }

    public void setLazyModel(LazyDataModel<Payment> lazyModel) {
        this.lazyModel = lazyModel;
    }

    public class LazyPaymentDataModel extends LazyDataModel<Payment> {

        private List<Payment> datasource;

        public LazyPaymentDataModel(List<Payment> datasource) {
            this.datasource = datasource;
        }

        @Override
        public Payment getRowData(String rowKey) {
            for (Payment perObject : datasource) {
                if (perObject.getPaymentID().toString().equals(rowKey)) {
                    return perObject;
                }
            }
            return null;
        }

        @Override
        public Object getRowKey(Payment object) {
            return object.getPaymentID();
        }

        @Override
        public List<Payment> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
            data = new ArrayList<Payment>();

            //refresh datasource 
            paymentF.refresh();
            for (int i = 0; i < datasource.size(); i++) {
                Payment perObject = datasource.get(i);
                if (perObject != null) {
                    datasource.set(i, paymentF.find(perObject.getPaymentID()));
                }
            }

            //filter
            for (Payment perObject : datasource) {
                boolean match = true;
                if (filters != null) {
                    String filterProperty = "";
                    for (Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {
                        try {
                            filterProperty = it.next();
                            Object filterValue = filters.get(filterProperty);
                            if (filterProperty.equalsIgnoreCase("branchID")) {
                                //perObject.getInverseField Class
                                Field field = perObject.getBranchID().getClass().getDeclaredField(filterProperty);
                                field.setAccessible(true);
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
                    Collections.sort(data, new PaymentListView.LazySorter(sortField, sortOrder));
                } catch (Exception e) {
                    // catch when class do not have field to sort, sortField is in InverseField
                    List<Branch> B = new ArrayList<Branch>();
                    for (Payment payment : data) {
                        boolean isExistsPayment = false;
                        for (Branch branch : B) {
                            if (payment.getBranchID().getBranchID() == branch.getBranchID()) {
                                isExistsPayment = true;
                            }
                            if (isExistsPayment) {
                                break;
                            }
                        }
                        if (isExistsPayment != true) {
                            B.add(payment.getBranchID());
                        }
                    }

                    Collections.sort(B, new PaymentListView.LazySorterInverseFields(sortField, sortOrder));
                    List<Payment> newData = new ArrayList<Payment>();
                    for (Branch branch : B) {
                        List<Payment> listPayment = (List<Payment>) branch.getPaymentCollection();
                        for (Iterator<Payment> iterator = listPayment.iterator(); iterator.hasNext();) {
                            Payment next = iterator.next();
                            for (Payment payment : data) {
                                if (next.equals(payment)) {
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

    public class LazySorter implements Comparator<Payment> {

        private String sortField;

        private SortOrder sortOrder;

        public LazySorter(String sortField, SortOrder sortOrder) {
            this.sortField = sortField;
            this.sortOrder = sortOrder;
        }

        public int compare(Payment obj1, Payment obj2) {
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
