package View.Customer;

import Entity.Category;
import Entity.Customer;
import Entity.Groups;
import Entity.Product;
import SessionBean.CategoryFacadeLocal;
import SessionBean.GroupsFacadeLocal;
import SessionBean.ProductFacadeLocal;
import static View.Customer.ShowHotProduct.comparator;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.xml.xpath.XPathExpressionException;
import org.w3c.dom.Document;

@Named(value = "showProduct")
@SessionScoped
public class showProduct implements Serializable {

    @EJB
    private ProductFacadeLocal productFacade;

    @EJB
    private CategoryFacadeLocal categoryFacade;

    @EJB
    private GroupsFacadeLocal groupsFacade;

    List<Product> listProduct;
    List<Groups> listGroup;
    List<productToShow> list;
    List<productToShow> listp;
    List<Integer> listpages;
    String cateName, groupName, cateN;
    int pageCurent;
    int pageTotal;

    public int getPageCurent() {
        return pageCurent;
    }

    public void setPageCurent(int pageCurent) {
        this.pageCurent = pageCurent;
    }

    public int getPageTotal() {
        return pageTotal;
    }

    public void setPageTotal(int pageTotal) {
        this.pageTotal = pageTotal;
    }
    
    public String getCateN() {
        return cateN;
    }

    public void setCateN(String cateN) {
        this.cateN = cateN;
    }

    public String getCateName() {
        return cateName;
    }

    public void setCateName(String cateName) {
        this.cateName = cateName;
    }

    public List<Integer> getListpages() {
        return listpages;
    }

    public void setListpages(List<Integer> listpages) {
        this.listpages = listpages;
    }
    int recordPerPage, totalRecord, totalPage, fristRecord, endRecord;

    public List<productToShow> getListp() {
        return listp;
    }

    public void setListp(List<productToShow> listp) {
        this.listp = listp;
    }

    public CategoryFacadeLocal getCategoryFacade() {
        return categoryFacade;
    }

    public void setCategoryFacade(CategoryFacadeLocal categoryFacade) {
        this.categoryFacade = categoryFacade;
    }

    public List<productToShow> getList() {
        return list;
    }

    public void setList(List<productToShow> list) {
        this.list = list;
    }

    public GroupsFacadeLocal getGroupsFacade() {
        return groupsFacade;
    }

    public void setGroupsFacade(GroupsFacadeLocal groupsFacade) {
        this.groupsFacade = groupsFacade;
    }

    public List<Product> getListProduct() {
        return listProduct;
    }

    public void setListProduct(List<Product> listProduct) {
        this.listProduct = listProduct;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void showByCate() throws XPathExpressionException {
        ExternalContext ec = FacesContext.getCurrentInstance()
                .getExternalContext();
        String cateID = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("catID");
        String groupID = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("groupID");
        productFacade.refresh();
        listProduct = new ArrayList<Product>();
        listGroup = new ArrayList<Groups>();
        list = new ArrayList<productToShow>();
        listp = new ArrayList<productToShow>();
        listpages = new ArrayList<Integer>();
        Groups group = new Groups();
        Category cate = new Category();
        XMLAccess.XmlAccessDescriptions xml = new XMLAccess.XmlAccessDescriptions();

        Document doc;
        if (cateID != null) {
            listGroup = (List<Groups>) categoryFacade.find(Integer.valueOf(cateID)).getGroupsCollection();
            cate = categoryFacade.find(Integer.valueOf(cateID));
            cateName = cate.getCateName();
            groupName = "";
            for (int j = 0; j < listGroup.size(); j++) {
                listProduct = (List<Product>) listGroup.get(j).getProductCollection();
                for (int i = 0; i < listProduct.size(); i++) {
                    if (listProduct.get(i).getProductStatus() == 1) {
                        int id = listProduct.get(i).getProductID();
                        String pname = listProduct.get(i).getProductName();
                        String description = listProduct.get(i).getDescriptions();
                        doc = xml.stringToDocument(description);
                        double price = xml.getPrice(description);
                        String avatar = xml.getAvatar(description);
                        int count = xml.getCount(description);
                        int inventory = listProduct.get(i).getInventory();
                        list.add(new productToShow(id, count, pname, avatar, price, inventory));
                    }
                }

            }
            Collections.sort(list, comparator);
            ec.getSessionMap().put("cate", cate.getCateID());
            ec.getSessionMap().remove("group");
            loadData();
        } else if (groupID != null) {
            listProduct = (List<Product>) groupsFacade.find(Integer.valueOf(groupID)).getProductCollection();
            group = groupsFacade.find(Integer.valueOf(groupID));
            groupName = group.getGroupName();
            cateName = group.getCateID().getCateName();
            for (int i = 0; i < listProduct.size(); i++) {
                if (listProduct.get(i).getProductStatus() == 1) {
                    int id = listProduct.get(i).getProductID();
                    String pname = listProduct.get(i).getProductName();
                    String description = listProduct.get(i).getDescriptions();
                    doc = xml.stringToDocument(description);
                    double price = xml.getPrice(description);
                    String avatar = xml.getAvatar(description);
                    int count = xml.getCount(description);
                    int inventory = listProduct.get(i).getInventory();
                    list.add(new productToShow(id, count, pname, avatar, price, inventory));
                }
            }
            Collections.sort(list, comparator);
            ec.getSessionMap().put("group", group.getGroupID());
            ec.getSessionMap().remove("cate");
            loadData();
        } else if (ec.getSessionMap().get("group") == null && cateID == null ) {
            String cateid = ec.getSessionMap().get("cate").toString();
            listGroup = (List<Groups>) categoryFacade.find(Integer.valueOf(cateid)).getGroupsCollection();
            cate = categoryFacade.find(Integer.valueOf(cateid));
            cateName = cate.getCateName();
            groupName = "";
            for (int j = 0; j < listGroup.size(); j++) {
                listProduct = (List<Product>) listGroup.get(j).getProductCollection();
                for (int i = 0; i < listProduct.size(); i++) {
                    if (listProduct.get(i).getProductStatus() == 1) {
                        int id = listProduct.get(i).getProductID();
                        String pname = listProduct.get(i).getProductName();
                        String description = listProduct.get(i).getDescriptions();
                        doc = xml.stringToDocument(description);
                        double price = xml.getPrice(description);
                        String avatar = xml.getAvatar(description);
                        int count = xml.getCount(description);
                        int inventory = listProduct.get(i).getInventory();
                        list.add(new productToShow(id, count, pname, avatar, price, inventory));
                    }
                }

            }
            Collections.sort(list, comparator);
           
            loadData();
        } else if (groupID == null && ec.getSessionMap().get("cate") == null) {
            String groupid = ec.getSessionMap().get("group").toString();
            listProduct = (List<Product>) groupsFacade.find(Integer.valueOf(groupid)).getProductCollection();
            group = groupsFacade.find(Integer.valueOf(groupid));
            groupName = group.getGroupName();
            cateName = group.getCateID().getCateName();
            for (int i = 0; i < listProduct.size(); i++) {
                if (listProduct.get(i).getProductStatus() == 1) {
                    int id = listProduct.get(i).getProductID();
                    String pname = listProduct.get(i).getProductName();
                    String description = listProduct.get(i).getDescriptions();
                    doc = xml.stringToDocument(description);
                    double price = xml.getPrice(description);
                    String avatar = xml.getAvatar(description);
                    int count = xml.getCount(description);
                    int inventory = listProduct.get(i).getInventory();
                    list.add(new productToShow(id, count, pname, avatar, price, inventory));
                }
            }
            Collections.sort(list, comparator);
            loadData();
        }
       

    }
    public static Comparator<productToShow> comparator = new Comparator<productToShow>() {
        @Override
        public int compare(productToShow o1, productToShow o2) {
            return o2.getCount() - o1.getCount();
        }

    };

    public List<productToShow> loadData() {
        ExternalContext ec = FacesContext.getCurrentInstance()
                .getExternalContext();
        int id;
//        if ( ec.getSessionMap().get("LOGGED_CUSTOMER") != null && ec.getSessionMap().get("idPage") != null) {
//            id = Integer.valueOf(ec.getSessionMap().get("idPage").toString())
//        }
        if (ec.getRequestParameterMap().get("id") !=null) {
            id = Integer.valueOf(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id"));
        }
        else
        {
            id =1 ;
        }   
        recordPerPage = 16;
        System.out.println(id);
        listp = new ArrayList<productToShow>();
        totalRecord = list.size();

        if (id == 1) {
            if (totalRecord > 16) {
                fristRecord = 0;
                endRecord = recordPerPage;
            } else {

                fristRecord = 0;
                endRecord = totalRecord;
            }
        } else {

            fristRecord = (recordPerPage * (id - 1));
            endRecord = fristRecord + recordPerPage;
        }
        for (int i = 0; i < list.size(); i++) {
            if (i >= fristRecord && i < endRecord) {
                listp.add(list.get(i));
                Collections.sort(listp, comparator);
            }
        }
        fristRecord = 0;
        endRecord = 0;
        loadPageTotal(id);
//        ec.getSessionMap().put("idPage", id);
        return listp;
    }

    public List<Integer> loadPageTotal(int id) {
        listpages = new ArrayList<Integer>();
        totalRecord = list.size();
        pageTotal = 0;
        pageCurent= 0;
        if ((totalRecord % recordPerPage) == 0) {
            totalPage = totalRecord / recordPerPage;
        } else {
            totalPage = (totalRecord / recordPerPage) + 1;
        }
        for (int i = 1; i <= totalPage; i++) {
            listpages.add(i);
        }
        pageTotal = listpages.size();
        int currentPage = id;
        if (totalPage < 3) {
            if (currentPage == 1) {
                listpages.removeAll(listpages);
                for (int j = 1; j <= totalPage; j++) {
                    listpages.add(j);
                }
            } else {
                listpages.removeAll(listpages);
                for (int j = 1; j <= totalPage; j++) {
                    listpages.add(j);
                }
            }
        } else {
            if (currentPage == 1) {

                listpages.removeAll(listpages);
                for (int j = 1; j <= (currentPage + 2); j++) {
                    listpages.add(j);
                }
            } else {
                if (currentPage == totalPage) {

                    listpages.removeAll(listpages);
                    for (int j = currentPage - 2; j <= currentPage; j++) {
                        listpages.add(j);
                    }

                } else {
                    listpages.removeAll(listpages);
                    for (int j = currentPage - 1; j <= (currentPage + 1); j++) {
                        listpages.add(j);
                    }
                }
            }
            pageCurent = id;
        }

        return listpages;
    }
}
