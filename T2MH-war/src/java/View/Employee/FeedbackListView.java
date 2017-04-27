/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View.Employee;

import Entity.Branch;
import Entity.Customer;
import Entity.CustomerInfo;
import Entity.Employee;
import Entity.Feedback;
import Entity.Payment;
import SessionBean.CustomerFacadeLocal;
import SessionBean.CustomerInfoFacadeLocal;
import SessionBean.FeedbackFacadeLocal;
import SessionBean.PaymentFacadeLocal;
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
@Named(value = "feedbackListView")
@ManagedBean
@ViewScoped
public class FeedbackListView implements Serializable {

    @EJB
    private FeedbackFacadeLocal feedbackF;

    private LazyDataModel<Feedback> lazyModel;

    private List<Feedback> data;

    public List<Feedback> getData() {
        return data;
    }

    public void setData(List<Feedback> data) {
        this.data = data;
    }

    @PostConstruct
    public void init() {
        lazyModel = new LazyFeedbackDataModel(feedbackF.findAll());
    }

    public LazyDataModel<Feedback> getLazyModel() {
        return lazyModel;
    }

    public void setLazyModel(LazyDataModel<Feedback> lazyModel) {
        this.lazyModel = lazyModel;
    }

    public class LazyFeedbackDataModel extends LazyDataModel<Feedback> {

        private List<Feedback> datasource;

        public LazyFeedbackDataModel(List<Feedback> datasource) {
            this.datasource = datasource;
        }

        @Override
        public Feedback getRowData(String rowKey) {
            for (Feedback perObject : datasource) {
                if (perObject.getComplainID().toString().equals(rowKey)) {
                    return perObject;
                }
            }
            return null;
        }

        @Override
        public Object getRowKey(Feedback object) {
            return object.getComplainID();
        }

        @Override
        public List<Feedback> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
            data = new ArrayList<Feedback>();

            //refresh datasource 
            feedbackF.refresh();
            for (int i = 0; i < datasource.size(); i++) {
                Feedback perObject = datasource.get(i);
                if (perObject != null) {
                    datasource.set(i, feedbackF.find(perObject.getComplainID()));
                }
            }

            //filter
            for (Feedback perObject : datasource) {
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
                                field.setAccessible(true);
                                String fieldValue = String.valueOf(field.get(perObject.getCustomerID())).toLowerCase();
                                if (filterValue == null || fieldValue.startsWith(filterValue.toString().toLowerCase())) {
                                    match = true;
                                } else {
                                    match = false;
                                    break;
                                }
                            } else if (filterProperty.equalsIgnoreCase("employeeID")) {
                                //perObject.getInverseField Class
                                Field field = perObject.getEmployeeID().getClass().getDeclaredField(filterProperty);
                                field.setAccessible(true);
                                String fieldValue = String.valueOf(field.get(perObject.getEmployeeID())).toLowerCase();
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
                    if (sortField.equalsIgnoreCase("timePost")) {
                        Collections.sort(data, new FeedbackListView.DateTimeLazySorter(sortField, sortOrder));
                    } else {
                        Collections.sort(data, new FeedbackListView.LazySorter(sortField, sortOrder));
                    }
                } catch (Exception e) {
                    // catch when class do not have field to sort, sortField is in InverseField
                    if (sortField.equalsIgnoreCase("customerID")) {
                        List<Customer> C = new ArrayList<Customer>();
                        for (Feedback feedback : data) {
                            boolean isExistsFeedback = false;
                            for (Customer customer : C) {
                                if (feedback.getCustomerID().getCustomerID() == customer.getCustomerID()) {
                                    isExistsFeedback = true;
                                }
                                if (isExistsFeedback) {
                                    break;
                                }
                            }
                            if (isExistsFeedback != true) {
                                C.add(feedback.getCustomerID());
                            }
                        }

                        Collections.sort(C, new FeedbackListView.LazySorterCustomerInverseFields(sortField, sortOrder));
                        List<Feedback> newData = new ArrayList<Feedback>();
                        for (Customer customer : C) {
                            List<Feedback> listFeedback = (List<Feedback>) customer.getFeedbackCollection();
                            for (Iterator<Feedback> iterator = listFeedback.iterator(); iterator.hasNext();) {
                                Feedback next = iterator.next();
                                for (Feedback feedback : data) {
                                    if (next.equals(feedback)) {
                                        newData.add(next);
                                    }
                                }
                            }
                        }
                        data = newData;
                    } else if (sortField.equalsIgnoreCase("employeeID")) {
                        List<Employee> E = new ArrayList<Employee>();
                        for (Feedback feedback : data) {
                            boolean isExistsFeedback = false;
                            for (Employee employee : E) {
                                try {
                                    if (feedback.getEmployeeID().getEmployeeID() == employee.getEmployeeID()) {
                                        isExistsFeedback = true;
                                    }
                                } catch (Exception ex) {
                                }
                                if (isExistsFeedback) {
                                    break;
                                }
                            }
                            if (isExistsFeedback != true) {
                                if (E.contains(feedback.getEmployeeID()) == false) {
                                    E.add(feedback.getEmployeeID());
                                }
                            }
                        }

                        Collections.sort(E, new FeedbackListView.LazySorterEmployeeInverseFields(sortField, sortOrder));
                        List<Feedback> newData = new ArrayList<Feedback>();
                        for (Employee employee : E) {
                            try {
                                List<Feedback> listFeedback = (List<Feedback>) employee.getFeedbackCollection();
                                for (Iterator<Feedback> iterator = listFeedback.iterator(); iterator.hasNext();) {
                                    Feedback next = iterator.next();
                                    if (data.contains(next)) {
                                        newData.add(next);
                                    }
                                }
                            } catch (Exception ex) {
                                //catch when employeeID null
                                for (Feedback feedback : data) {
                                    if (feedback.getEmployeeID() == null) {
                                        newData.add(feedback);
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

    public class LazySorter implements Comparator<Feedback> {

        private String sortField;

        private SortOrder sortOrder;

        public LazySorter(String sortField, SortOrder sortOrder) {
            this.sortField = sortField;
            this.sortOrder = sortOrder;
        }

        public int compare(Feedback obj1, Feedback obj2) {
            try {
                Field field1 = obj1.getClass().getDeclaredField(this.sortField);
                field1.setAccessible(true);
                Field field2 = obj2.getClass().getDeclaredField(this.sortField);
                field2.setAccessible(true);

                Object value1 = field1.get(obj1);
                Object value2 = field2.get(obj2);
                if (value1 == null && value2 == null) {
                    return 1;
                }
                if (value1 == null) {
                    return SortOrder.ASCENDING.equals(sortOrder) ? 1 : 1 * 1;
                }
                if (value2 == null) {
                    return SortOrder.ASCENDING.equals(sortOrder) ? -1 : -1 * 1;
                }
                int value = ((Comparable) value1).compareTo(value2);
                return SortOrder.ASCENDING.equals(sortOrder) ? value : -1 * value;
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException();
            }
        }
    }

    public class LazySorterCustomerInverseFields implements Comparator<Customer> {

        private String sortField;

        private SortOrder sortOrder;

        public LazySorterCustomerInverseFields(String sortField, SortOrder sortOrder) {
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

    public class LazySorterEmployeeInverseFields implements Comparator<Employee> {

        private String sortField;

        private SortOrder sortOrder;

        public LazySorterEmployeeInverseFields(String sortField, SortOrder sortOrder) {
            this.sortField = sortField;
            this.sortOrder = sortOrder;
        }

        public int compare(Employee obj1, Employee obj2) {
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

    public class DateTimeLazySorter implements Comparator<Feedback> {

        private String sortField;

        private SortOrder sortOrder;

        public DateTimeLazySorter(String sortField, SortOrder sortOrder) {
            this.sortField = sortField;
            this.sortOrder = sortOrder;
        }

        public int compare(Feedback obj1, Feedback obj2) {
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
}
