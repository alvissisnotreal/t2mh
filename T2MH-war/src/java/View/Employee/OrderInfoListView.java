/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View.Employee;

import View.Employee.*;
import Entity.Branch;
import Entity.BranchManager;
import Entity.OrderInfo;
import Entity.OrderInfoPK;
import Entity.Orders;
import Entity.Product;
import SessionBean.BranchFacadeLocal;
import SessionBean.OrderInfoFacade;
import SessionBean.OrderInfoFacadeLocal;
import SessionBean.OrdersFacade;
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
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author 19319
 */
@Named(value = "empOrderInfoListView")
@ManagedBean
@ViewScoped
public class OrderInfoListView implements Serializable {

    @EJB
    private OrdersFacadeLocal ordersF;

    @EJB
    private OrderInfoFacadeLocal orderInfoF;

    private LazyDataModel<OrderInfo> lazyModel;

    @PostConstruct
    public void init() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        if (ordersF == null) {
            ordersF = new OrdersFacade();
        }
        if (orderInfoF == null) {
            orderInfoF = new OrderInfoFacade();
        }
        ordersF.refresh();
        Orders order = ordersF.find(Integer.valueOf(externalContext.getSessionMap().get("details_Order").toString()));
        if (order != null) {
            lazyModel = new LazyOrderInfoDataModel((List<OrderInfo>) order.getOrderInfoCollection());
        }
    }

    public LazyDataModel<OrderInfo> getLazyModel() {
        return lazyModel;
    }

    public void setLazyModel(LazyDataModel<OrderInfo> lazyModel) {
        this.lazyModel = lazyModel;
    }

    public class LazyOrderInfoDataModel extends LazyDataModel<OrderInfo> {

        private List<OrderInfo> datasource;

        public LazyOrderInfoDataModel(List<OrderInfo> datasource) {
            this.datasource = datasource;
        }

        @Override
        public OrderInfo getRowData(String rowKey) {
            List<OrderInfo> oi = (List<OrderInfo>) getWrappedData();
            for (OrderInfo orderInfo : datasource) {
                if (orderInfo.getOrderInfoPK().equals(rowKey)) {
                    return orderInfo;
                }
            }
            return null;
        }

        @Override
        public Object getRowKey(OrderInfo orderInfo) {
            return orderInfo.getOrderInfoPK();
        }

        @Override
        public List<OrderInfo> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
            List<OrderInfo> data = new ArrayList<OrderInfo>();
            orderInfoF.refresh();
            for (int i = 0; i < datasource.size(); i++) {
                OrderInfo orderInfo = datasource.get(i);
                if (orderInfo != null) {
                    datasource.set(i, orderInfoF.find(orderInfo.getOrderInfoPK()));
                }
            }
            //filter
            for (OrderInfo orderInfo : datasource) {
                boolean match = true;

                if (filters != null) {
                    String filterProperty = "";
                    for (Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {
                        try {
                            filterProperty = it.next();
                            Object filterValue = filters.get(filterProperty);
                            if (filterProperty.equalsIgnoreCase("productID") || filterProperty.equalsIgnoreCase("orderID")) {
                                //field = class.getOtherClassID
                                Field field = orderInfo.getOrderInfoPK().getClass().getDeclaredField(filterProperty);
                                field.setAccessible(true);
                                //field.get(class.getOtherClassID)
                                String fieldValue = String.valueOf(field.get(orderInfo.getOrderInfoPK())).toLowerCase();
                                if (filterValue == null || fieldValue.startsWith(filterValue.toString().toLowerCase())) {
                                    match = true;
                                } else {
                                    match = false;
                                    break;
                                }
                            } else if (filterProperty.equalsIgnoreCase("descriptions")) {
                                Field field = orderInfo.getClass().getDeclaredField("descriptions");
                                field.setAccessible(true);
                                String fieldValue = String.valueOf(field.get(orderInfo)).toLowerCase();
                                if (filterValue == null || fieldValue.startsWith(filterValue.toString().toLowerCase())) {
                                    match = true;
                                } else {
                                    match = false;
                                    break;
                                }
                            } else if (filterProperty.equalsIgnoreCase("timeOrder")) {
                                Field field = orderInfo.getOrders().getClass().getDeclaredField("descriptions");
                                field.setAccessible(true);
                                String fieldValue = String.valueOf(field.get(orderInfo.getOrders())).toLowerCase();
                                if (filterValue == null || fieldValue.startsWith(filterValue.toString().toLowerCase())) {
                                    match = true;
                                } else {
                                    match = false;
                                    break;
                                }
                            } else {
                                Field field = orderInfo.getClass().getDeclaredField(filterProperty);
                                field.setAccessible(true);
                                String fieldValue = String.valueOf(field.get(orderInfo)).toLowerCase();
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
                    data.add(orderInfo);
                }
            }

            //sort
            if (sortField != null) {
                try {
                    if (sortField.equalsIgnoreCase("timeOrder")) {
                        Collections.sort(data, new OrderInfoListView.DateTimeLazySorter(sortField, sortOrder));
                    } else {
                        Collections.sort(data, new OrderInfoListView.LazySorter(sortField, sortOrder));
                    }

                } catch (Exception e) {
                    // catch when class do not have field to sort, sortField is in InverseField
                    if (sortField.equalsIgnoreCase("productID") || sortField.equalsIgnoreCase("orderId")) {//get inner class=pk
                        List<OrderInfoPK> listOIPK = new ArrayList<OrderInfoPK>();
                        for (OrderInfo orderInfo : data) {
                            boolean isExists = false;
                            for (OrderInfoPK oipk : listOIPK) {
                                //class.getOtherClass
                                if (sortField.equalsIgnoreCase("productID")) {
                                    if (orderInfo.getOrderInfoPK().getProductID() == oipk.getProductID()) {
                                        isExists = true;
                                    }
                                    if (isExists) {
                                        break;
                                    }
                                } else if (sortField.equalsIgnoreCase("orderId")) {
                                    if (orderInfo.getOrderInfoPK().getOrderID() == oipk.getOrderID()) {
                                        isExists = true;
                                    }
                                    if (isExists) {
                                        break;
                                    }
                                }
                            }
                            if (isExists != true) {
                                listOIPK.add(orderInfo.getOrderInfoPK());
                            }
                        }

                        Collections.sort(listOIPK, new OrderInfoListView.LazySorterInverseFields(sortField, sortOrder));
                        List<OrderInfo> newData = new ArrayList<OrderInfo>();
                        for (OrderInfoPK oipk : listOIPK) {
                            OrderInfo order = orderInfoF.findByFK(oipk);
                            if (order != null) {
                                newData.add(order);
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

    public class LazySorter implements Comparator<OrderInfo> {

        private String sortField;

        private SortOrder sortOrder;

        public LazySorter(String sortField, SortOrder sortOrder) {
            this.sortField = sortField;
            this.sortOrder = sortOrder;
        }

        public int compare(OrderInfo obj1, OrderInfo obj2) {
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

    public class LazySorterInverseFields implements Comparator<OrderInfoPK> {

        private String sortField;

        private SortOrder sortOrder;

        public LazySorterInverseFields(String sortField, SortOrder sortOrder) {
            this.sortField = sortField;
            this.sortOrder = sortOrder;
        }

        public int compare(OrderInfoPK obj1, OrderInfoPK obj2) {
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

    public class DateTimeLazySorter implements Comparator<OrderInfo> {

        private String sortField;

        private SortOrder sortOrder;

        public DateTimeLazySorter(String sortField, SortOrder sortOrder) {
            this.sortField = sortField;
            this.sortOrder = sortOrder;
        }

        public int compare(OrderInfo obj1, OrderInfo obj2) {
            try {
                DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Field field1 = obj1.getOrders().getClass().getDeclaredField(this.sortField);
                field1.setAccessible(true);
                Field field2 = obj2.getOrders().getClass().getDeclaredField(this.sortField);
                field2.setAccessible(true);
                Date value1 = (Date) formatter.parse(field1.get(obj1.getOrders()).toString());
                Date value2 = (Date) formatter.parse(field2.get(obj2.getOrders()).toString());
                int value = ((Comparable) value1).compareTo(value2);
                return SortOrder.ASCENDING.equals(sortOrder) ? value : -1 * value;
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException();
            }
        }
    }
}
