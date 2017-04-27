/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View.Employee;

import Entity.ProductReview;
import Entity.Customer;
import Entity.Product;
import SessionBean.ProductReviewFacadeLocal;
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
@Named(value = "pRListView")
@ManagedBean
@ViewScoped
public class PRListView implements Serializable {

    @EJB
    private ProductReviewFacadeLocal PRF;

    private LazyDataModel<ProductReview> lazyModel;

    private List<ProductReview> data;

    public List<ProductReview> getData() {
        return data;
    }

    public void setData(List<ProductReview> data) {
        this.data = data;
    }

    @PostConstruct
    public void init() {
        lazyModel = new LazyPRDataModel(PRF.findAll());
    }

    public LazyDataModel<ProductReview> getLazyModel() {
        return lazyModel;
    }

    public void setLazyModel(LazyDataModel<ProductReview> lazyModel) {
        this.lazyModel = lazyModel;
    }

    public class LazyPRDataModel extends LazyDataModel<ProductReview> {

        private List<ProductReview> datasource;

        public LazyPRDataModel(List<ProductReview> datasource) {
            this.datasource = datasource;
        }

        @Override
        public ProductReview getRowData(String rowKey) {
            for (ProductReview perObject : datasource) {
                if (perObject.getPrid().toString().equals(rowKey)) {
                    return perObject;
                }
            }
            return null;
        }

        @Override
        public Object getRowKey(ProductReview object) {
            return object.getPrid();
        }

        @Override
        public List<ProductReview> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
            data = new ArrayList<ProductReview>();

            //refresh datasource 
            PRF.refresh();
            for (int i = 0; i < datasource.size(); i++) {
                ProductReview perObject = datasource.get(i);
                if (perObject != null) {
                    datasource.set(i, PRF.find(perObject.getPrid()));
                }
            }

            //filter
            for (ProductReview perObject : datasource) {
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
                            } else if (filterProperty.equalsIgnoreCase("productID")) {
                                //perObject.getInverseField Class
                                Field field = perObject.getProductID().getClass().getDeclaredField(filterProperty);
                                field.setAccessible(true);                              //getInverseField
                                String fieldValue = String.valueOf(field.get(perObject.getProductID())).toLowerCase();
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
                        Collections.sort(data, new PRListView.DateTimeLazySorter(sortField, sortOrder));
                    } else {
                        Collections.sort(data, new PRListView.LazySorter(sortField, sortOrder));
                    }

                } catch (Exception e) {
                    // catch when class do not have field to sort, sortField is in InverseField

                    //for Product sortField
                    if (sortField.equalsIgnoreCase("productID")) {
                        List<Product> inverseList = new ArrayList<Product>();
                        //for mainClass
                        for (ProductReview perObject : data) {
                            boolean isExistsObject = false;
                            //for inverserClass
                            for (Product perInverseObject : inverseList) {
                                try {               //get InverserClass.InverserIDClass                 //getInverserID
                                    if (perObject.getProductID().getProductID().equals(perInverseObject.getProductID())) {
                                        isExistsObject = true;
                                    }
                                    if (isExistsObject) {
                                        break;
                                    }
                                } catch (Exception ex) {
                                }
                            }
                            if (isExistsObject != true) {//get InverserClass
                                inverseList.add(perObject.getProductID());
                            }
                        }
                        //                                          //InverserClassLazySorter
                        Collections.sort(inverseList, new PRListView.ProductLazySorterInverseFields(sortField, sortOrder));
                        //mainClass
                        List<ProductReview> newData = new ArrayList<ProductReview>();
                        //InverseClass
                        for (Product inverse : inverseList) {
                            //mainClass                                                 //getMainClassCollection
                            List<ProductReview> listMainClass = (List<ProductReview>) inverse.getProductReviewCollection();
                            //mainClass
                            for (Iterator<ProductReview> iterator = listMainClass.iterator(); iterator.hasNext();) {
                                //mainClass
                                ProductReview next = iterator.next();
                                for (ProductReview mainClass : data) {
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
                        for (ProductReview perObject : data) {
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
                        Collections.sort(inverseList, new PRListView.CustomerLazySorterInverseFields(sortField, sortOrder));
                        //mainClass
                        List<ProductReview> newData = new ArrayList<ProductReview>();
                        //InverseClass
                        for (Customer inverse : inverseList) {
                            //mainClass                                                 //getMainClassCollection
                            List<ProductReview> listMainClass = (List<ProductReview>) inverse.getProductReviewCollection();
                            //mainClass
                            for (Iterator<ProductReview> iterator = listMainClass.iterator(); iterator.hasNext();) {
                                //mainClass
                                ProductReview next = iterator.next();
                                for (ProductReview mainClass : data) {
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

    public class DateTimeLazySorter implements Comparator<ProductReview> {

        private String sortField;

        private SortOrder sortOrder;

        public DateTimeLazySorter(String sortField, SortOrder sortOrder) {
            this.sortField = sortField;
            this.sortOrder = sortOrder;
        }

        public int compare(ProductReview obj1, ProductReview obj2) {
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

    public class LazySorter implements Comparator<ProductReview> {

        private String sortField;

        private SortOrder sortOrder;

        public LazySorter(String sortField, SortOrder sortOrder) {
            this.sortField = sortField;
            this.sortOrder = sortOrder;
        }

        public int compare(ProductReview obj1, ProductReview obj2) {
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

    public class ProductLazySorterInverseFields implements Comparator<Product> {

        private String sortField;

        private SortOrder sortOrder;

        public ProductLazySorterInverseFields(String sortField, SortOrder sortOrder) {
            this.sortField = sortField;
            this.sortOrder = sortOrder;
        }

        public int compare(Product obj1, Product obj2) {
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
