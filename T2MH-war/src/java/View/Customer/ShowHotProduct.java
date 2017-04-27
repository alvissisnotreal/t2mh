/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View.Customer;

import Entity.Category;
import Entity.Groups;
import Entity.Product;
import SessionBean.CategoryFacadeLocal;
import SessionBean.GroupsFacadeLocal;
import SessionBean.ProductFacadeLocal;
import XMLAccess.XmlAccessDescriptions;

import java.io.File;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import java.util.List;
import javafx.print.Collation;

import javax.ejb.EJB;
import javax.xml.xpath.XPathExpressionException;

/**
 *
 * @author Huy-PC
 */
@Named(value = "showHotProduct")
@SessionScoped
public class ShowHotProduct implements Serializable {

    @EJB
    private GroupsFacadeLocal groupsFacade;

    @EJB
    private ProductFacadeLocal productFacade;

    @EJB
    private CategoryFacadeLocal categoryFacade;

    List<productToShow> list;
    List<Category> listCate;
    List<Product> listproduct;
    List<Groups> listgroups;
    List<productImage> listImage;
    XmlAccessDescriptions XAD = new XmlAccessDescriptions();

    public List<productImage> getListImage() {
        return listImage;
    }

    public void showAll() throws XPathExpressionException {
        listCate = new ArrayList<Category>();
        listgroups = new ArrayList<Groups>();
        listproduct = new ArrayList<Product>();
        list = new ArrayList<productToShow>();
        listImage = new ArrayList<productImage>();
        listCate = categoryFacade.findAll();
        for (int i = 0; i < listCate.size(); i++) {
            if (listCate.get(i).getCateStatus() == 1) {
                listgroups = (List<Groups>) categoryFacade.find(listCate.get(i).getCateID()).getGroupsCollection();
                String cateName = listCate.get(i).getCateName();
                String cateID = listCate.get(i).getCateID().toString();
                for (int j = 0; j < listgroups.size(); j++) {
                    listproduct = (List<Product>) groupsFacade.find(listgroups.get(j).getGroupID()).getProductCollection();
                    for (Product pro : listproduct) {
                        if (pro.getbStatus() == 1) {
                            int id = pro.getProductID();
                            String description = pro.getDescriptions();
                            String avatar = XAD.getAvatar(description);
                            int count = XAD.getCount(description);
                            list.add(new productToShow(id, count, avatar));
                        }

                    }
                }
                Collections.sort(list, comparator);
                if (list.size() >= 4) {
                    String img1 = list.get(0).getProductAvatar();
                    String img2 = list.get(1).getProductAvatar();
                    String img3 = list.get(2).getProductAvatar();
                    String img4 = list.get(3).getProductAvatar();
                    String id1 = String.valueOf(list.get(0).getId());
                    String id2 = String.valueOf(list.get(1).getId());
                    String id3 = String.valueOf(list.get(2).getId());
                    String id4 = String.valueOf(list.get(3).getId());
                    listImage.add(new productImage(img1, img2, img3, img4, id1, id2, id3, id4, cateName, cateID));
                }
                list.removeAll(list);
            }
        }

    }
    public static Comparator<productToShow> comparator = new Comparator<productToShow>() {
        @Override
        public int compare(productToShow o1, productToShow o2) {
            return o2.getCount() - o1.getCount();
        }

    };
    public static Comparator<productToShow> comparator1 = new Comparator<productToShow>() {
        @Override
        public int compare(productToShow o1, productToShow o2) {
            return o2.getCount() - o1.getCount();
        }

    };

}
