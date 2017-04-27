/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View.BM;

import View.Employee.*;
import Entity.Branch;
import Entity.BranchManager;
import Entity.Product;
import SessionBean.BranchFacadeLocal;
import SessionBean.ProductFacadeLocal;
import XMLAccess.XmlAccessDescriptions;
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
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.xml.xpath.XPathExpressionException;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author 19319
 */
@Named(value = "bMProductListView")
@ManagedBean
@ViewScoped
public class BMProductListView implements Serializable {

    @EJB
    private BranchFacadeLocal branchF;

    @EJB
    private ProductFacadeLocal PF;

    private LazyDataModel<Product> lazyModel;
    private BranchManager bm;
    private XmlAccessDescriptions XAD;

    @PostConstruct
    public void init() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        bm = (BranchManager) externalContext.getSessionMap().get("LOGGED_BM");
        XAD = new XmlAccessDescriptions();
        branchF.refresh();
        lazyModel = new LazyProductDataModel((List<Product>) branchF.find(bm.getBranchID().getBranchID()).getProductCollection());
    }

    public LazyDataModel<Product> getLazyModel() {
        return lazyModel;
    }

    public void setLazyModel(LazyDataModel<Product> lazyModel) {
        this.lazyModel = lazyModel;
    }

    public class LazyProductDataModel extends LazyDataModel<Product> {

        private List<Product> datasource;

        public LazyProductDataModel(List<Product> datasource) {
            this.datasource = datasource;
        }

        @Override
        public Product getRowData(String rowKey) {
            for (Product product : datasource) {
                if (product.getProductID().equals(rowKey)) {
                    return product;
                }
            }
            return null;
        }

        @Override
        public Object getRowKey(Product product) {
            return product.getProductID();
        }

        @Override
        public List<Product> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
            List<Product> data = new ArrayList<Product>();
            PF.refresh();
            for (int i = 0; i < datasource.size(); i++) {
                Product product = datasource.get(i);
                if (product != null) {
                    datasource.set(i, PF.find(product.getProductID()));
                }
            }

            //filter
            for (Product product : datasource) {
                boolean match = true;
                if (filters != null) {
                    String filterProperty = "";
                    for (Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {
                        try {
                            filterProperty = it.next();
                            Object filterValue = filters.get(filterProperty);
                            if (filterProperty.equalsIgnoreCase("branchID")) {
                                //field = class.getOtherClassID
                                Field field = product.getBranchID().getClass().getDeclaredField(filterProperty);
                                field.setAccessible(true);
                                //field.get(class.getOtherClassID)
                                String fieldValue = String.valueOf(field.get(product.getBranchID())).toLowerCase();
                                if (filterValue == null || fieldValue.startsWith(filterValue.toString().toLowerCase())) {
                                    match = true;
                                } else {
                                    match = false;
                                    break;
                                }
                            } else if (filterProperty.equalsIgnoreCase("price")) {
                                String fieldValue = String.valueOf(XAD.getPrice(product.getDescriptions()));
                                if (filterValue == null || fieldValue.startsWith(filterValue.toString().toLowerCase())) {
                                    match = true;
                                } else {
                                    match = false;
                                    break;
                                }
                            } else {
                                Field field = product.getClass().getDeclaredField(filterProperty);
                                field.setAccessible(true);
                                String fieldValue = String.valueOf(field.get(product)).toLowerCase();
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
                    data.add(product);
                }
            }

            //sort
            if (sortField != null) {
                try {
                    if (sortField.equalsIgnoreCase("price")) {
                        //
                        //create List<Product> => contain Product of DataSource
                        //create HashMap<Integer,Long> => key=id product, Long=price of product
                        //sort hashmap, change index of List
                        //
                        List<Product> listProductToSort = new ArrayList<>();
                        HashMap<Integer, Long> hmListProduct = new HashMap<>();
                        for (Product product : data) {
                            listProductToSort.add(product);
                            hmListProduct.put(product.getProductID(), XAD.getPrice(product.getDescriptions()));
                        }
                        if (sortOrder.toString().equalsIgnoreCase("ASCENDING")) {
                            for (int i = 0; i < listProductToSort.size(); i++) {
                                for (int j = i + 1; j < listProductToSort.size(); j++) {
                                    if (hmListProduct.get(listProductToSort.get(i).getProductID()) > hmListProduct.get(listProductToSort.get(j).getProductID())) {
                                        //index of smaller
                                        Product greaterValue = listProductToSort.get(i);
                                        Product smallerValue = listProductToSort.get(j);

                                        listProductToSort.set(i, smallerValue);
                                        listProductToSort.set(j, greaterValue);
                                    }
                                }
                            }
                        } else {
                            for (int i = 0; i < listProductToSort.size(); i++) {
                                for (int j = i + 1; j < listProductToSort.size(); j++) {
                                    if (hmListProduct.get(listProductToSort.get(i).getProductID()) < hmListProduct.get(listProductToSort.get(j).getProductID())) {
                                        //index of smaller
                                        Product smallerValue = listProductToSort.get(i);
                                        Product greaterValue = listProductToSort.get(j);

                                        listProductToSort.set(i, greaterValue);
                                        listProductToSort.set(j, smallerValue);
                                    }
                                }
                            }
                        }
                        data = listProductToSort;
                    } else {
                        Collections.sort(data, new BMProductListView.LazySorter(sortField, sortOrder));
                    }
                } catch (Exception e) {
                    // catch when class do not have field to sort, sortField is in InverseField
                    List<Branch> B = new ArrayList<Branch>();
                    for (Product product : data) {
                        boolean isExistsProduct = false;
                        for (Branch branch : B) {
                            //class.getOtherClass
                            if (product.getBranchID().getBranchID() == branch.getBranchID()) {
                                isExistsProduct = true;
                            }
                            if (isExistsProduct) {
                                break;
                            }
                        }
                        if (isExistsProduct != true) {
                            B.add(product.getBranchID());
                        }
                    }

                    Collections.sort(B, new BMProductListView.LazySorterInverseFields(sortField, sortOrder));
                    List<Product> newData = new ArrayList<Product>();
                    for (Branch branch : B) {
                        List<Product> listProduct = (List<Product>) branch.getProductCollection();
                        for (Iterator<Product> iterator = listProduct.iterator(); iterator.hasNext();) {
                            Product next = iterator.next();
                            for (Product product : data) {
                                if (next.equals(product)) {
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

    public class LazySorter implements Comparator<Product> {

        private String sortField;

        private SortOrder sortOrder;

        public LazySorter(String sortField, SortOrder sortOrder) {
            this.sortField = sortField;
            this.sortOrder = sortOrder;
        }

        public int compare(Product product1, Product product2) {
            try {
                Field field1 = product1.getClass().getDeclaredField(this.sortField);
                field1.setAccessible(true);
                Field field2 = product2.getClass().getDeclaredField(this.sortField);
                field2.setAccessible(true);
                Object value1 = field1.get(product1);
                Object value2 = field2.get(product2);
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
