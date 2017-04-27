/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View.Employee;

import Entity.Branch;
import Entity.Customer;
import Entity.OrderStatus;
import Entity.Orders;
import SessionBean.OrderInfoFacadeLocal;
import SessionBean.OrdersFacadeLocal;
import SessionBean.ProductFacadeLocal;
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
@Named(value = "orderListView")
@ManagedBean
@ViewScoped
public class OrderListView implements Serializable {

    @EJB
    private OrderInfoFacadeLocal orderInfoF;

    @EJB
    private OrdersFacadeLocal ordersF;

    @EJB
    private ProductFacadeLocal PF;

    private LazyDataModel<Orders> lazyModel;

    @PostConstruct
    public void init() {
        lazyModel = new LazyOrderDataModel(ordersF.findAll());
    }

    public LazyDataModel<Orders> getLazyModel() {
        return lazyModel;
    }

    public void setLazyModel(LazyDataModel<Orders> lazyModel) {
        this.lazyModel = lazyModel;
    }

    public class LazyOrderDataModel extends LazyDataModel<Orders> {

        private List<Orders> datasource;

        public LazyOrderDataModel(List<Orders> datasource) {
            this.datasource = datasource;
        }

        @Override
        public Orders getRowData(String rowKey) {
            for (Orders order : datasource) {
                if (order.getOrderID().equals(rowKey)) {
                    return order;
                }
            }
            return null;
        }

        @Override
        public Object getRowKey(Orders order) {
            return order.getOrderID();
        }

        @Override
        public List<Orders> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
            List<Orders> data = new ArrayList<Orders>();
            ordersF.refresh();
            for (int i = 0; i < datasource.size(); i++) {
                Orders order = datasource.get(i);
                if (order != null) {
                    datasource.set(i, ordersF.find(order.getOrderID()));
                }
            }
            //filter
            for (Orders order : datasource) {
                boolean match = true;

                if (filters != null) {
                    String filterProperty = "";
                    for (Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {
                        try {
                            filterProperty = it.next();
                            Object filterValue = filters.get(filterProperty);
                            if (filterProperty.equalsIgnoreCase("orderStatusName")) {
//                                field = class.getOtherClassID
                                Field field = order.getOrderStatusID().getClass().getDeclaredField(filterProperty);
                                field.setAccessible(true);
                                //field.get(class.getOtherClassID)
                                String fieldValue = String.valueOf(field.get(order.getOrderStatusID())).toLowerCase();
                                if (filterValue == null || fieldValue.startsWith(filterValue.toString().toLowerCase())) {
                                    match = true;
                                } else {
                                    match = false;
                                    break;
                                }
                            } else if (filterProperty.equalsIgnoreCase("customerID")) {
                                Field field = order.getCustomerID().getClass().getDeclaredField(filterProperty);
                                field.setAccessible(true);
                                //field.get(class.getOtherClassID)
                                String fieldValue = String.valueOf(field.get(order.getCustomerID())).toLowerCase();
                                if (filterValue == null || fieldValue.startsWith(filterValue.toString().toLowerCase())) {
                                    match = true;
                                } else {
                                    match = false;
                                    break;
                                }
                            } else {
                                Field field = order.getClass().getDeclaredField(filterProperty);
                                field.setAccessible(true);
                                String fieldValue;
                                if (filterProperty.equalsIgnoreCase("priceTotal"))//convert number to double string
                                {
                                    fieldValue = String.valueOf(Math.round((Double) field.get(order)));
                                } else {
                                    fieldValue = String.valueOf(field.get(order)).toLowerCase();
                                }
                                if (filterValue == null || fieldValue.startsWith(filterValue.toString().toLowerCase())) {
                                    match = true;
                                } else {
                                    match = false;
                                    break;
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            match = false;
                        }
                    }
                }

                if (match) {
                    data.add(order);
                }
            }

            //sort
            if (sortField != null) {
                try {
                    //for sort Time
                    if (sortField.equalsIgnoreCase("timeOrder")) {
                        Collections.sort(data, new OrderListView.DateTimeLazySorter(sortField, sortOrder));
                    } else {
                        Collections.sort(data, new OrderListView.LazySorter(sortField, sortOrder));
                    }
                } catch (Exception e) {
                    // catch when class do not have field to sort, sortField is in InverseField
                    if (sortField.equalsIgnoreCase("customerID")) {
                        List<Customer> customer = new ArrayList<Customer>();
                        for (Orders order : data) {
                            boolean isExists = false;
                            for (Customer perCustomer : customer) {
                                //class.getOtherClass
                                if (order.getCustomerID().getCustomerID() == perCustomer.getCustomerID()) {
                                    isExists = true;
                                }
                                if (isExists) {
                                    break;
                                }
                            }
                            if (isExists != true) {
                                customer.add(order.getCustomerID());
                            }
                        }

                        Collections.sort(customer, new OrderListView.LazySorterCusInverseFields(sortField, sortOrder));
                        List<Orders> newData = new ArrayList<Orders>();
                        for (Customer perCustomer : customer) {
                            List<Orders> listObject = (List<Orders>) perCustomer.getOrdersCollection();
                            for (Iterator<Orders> iterator = listObject.iterator(); iterator.hasNext();) {
                                Orders next = iterator.next();
                                for (Orders order : data) {
                                    if (next.equals(order)) {
                                        newData.add(next);
                                    }
                                }
                            }
                        }
                        data = newData;
                    } else {
                        List<OrderStatus> OS = new ArrayList<OrderStatus>();
                        for (Orders order : data) {
                            boolean isExists = false;
                            for (OrderStatus orderStatus : OS) {
                                //class.getOtherClass
                                if (order.getOrderStatusID().getOrderStatusID() == orderStatus.getOrderStatusID()) {
                                    isExists = true;
                                }
                                if (isExists) {
                                    break;
                                }
                            }
                            if (isExists != true) {
                                OS.add(order.getOrderStatusID());
                            }
                        }

                        Collections.sort(OS, new OrderListView.LazySorterInverseFields(sortField, sortOrder));
                        List<Orders> newData = new ArrayList<Orders>();
                        for (OrderStatus orderStatus : OS) {
                            List<Orders> listObject = (List<Orders>) orderStatus.getOrdersCollection();
                            for (Iterator<Orders> iterator = listObject.iterator(); iterator.hasNext();) {
                                Orders next = iterator.next();
                                for (Orders order : data) {
                                    if (next.equals(order)) {
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

    public class LazySorter implements Comparator<Orders> {

        private String sortField;

        private SortOrder sortOrder;

        public LazySorter(String sortField, SortOrder sortOrder) {
            this.sortField = sortField;
            this.sortOrder = sortOrder;
        }

        public int compare(Orders obj1, Orders obj2) {
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

    public class LazySorterInverseFields implements Comparator<OrderStatus> {

        private String sortField;

        private SortOrder sortOrder;

        public LazySorterInverseFields(String sortField, SortOrder sortOrder) {
            this.sortField = sortField;
            this.sortOrder = sortOrder;
        }

        public int compare(OrderStatus obj1, OrderStatus obj2) {
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

    public class DateTimeLazySorter implements Comparator<Orders> {

        private String sortField;

        private SortOrder sortOrder;

        public DateTimeLazySorter(String sortField, SortOrder sortOrder) {
            this.sortField = sortField;
            this.sortOrder = sortOrder;
        }

        public int compare(Orders obj1, Orders obj2) {
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

    public class LazySorterCusInverseFields implements Comparator<Customer> {

        private String sortField;

        private SortOrder sortOrder;

        public LazySorterCusInverseFields(String sortField, SortOrder sortOrder) {
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
