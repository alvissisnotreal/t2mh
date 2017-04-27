/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package srcAdmin;

import Entity.Employee;
import Entity.AdminRole;
import Entity.Category;
import Entity.Groups;
import Entity.OrderStatus;
import Entity.Product;
import Entity.Specifics;
import SessionBean.AdminRoleFacadeLocal;
import SessionBean.CategoryFacadeLocal;
import SessionBean.EmployeeFacadeLocal;
import SessionBean.EmployeeInfoFacadeLocal;
import SessionBean.GroupsFacadeLocal;
import SessionBean.OrderStatusFacadeLocal;
import SessionBean.ProductFacadeLocal;
import SessionBean.SpecificsFacadeLocal;
import XMLAccess.XmlAccessDescriptions;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.RequestDispatcher;
import javax.xml.xpath.XPathExpressionException;
import org.primefaces.event.FileUploadEvent;
import org.w3c.dom.Document;

/**
 *
 * @author 19319
 */
@Named(value = "aboutProduct")

@SessionScoped
@ManagedBean
public class aboutProduct implements Serializable {

    @EJB
    private SpecificsFacadeLocal specF;

    @EJB
    private GroupsFacadeLocal groupsF;

    @EJB
    private CategoryFacadeLocal cateF;

    @EJB
    private ProductFacadeLocal productF;

    @EJB
    private OrderStatusFacadeLocal OSF;

    @EJB
    private AdminRoleFacadeLocal ARF;

    @EJB
    private EmployeeFacadeLocal EF;

    @EJB
    private EmployeeInfoFacadeLocal EIF;

    private String baseURL = "";
    private List<AdminRole> listAllRole;
    private List<AdminRole> roleList;
    private String[] empRole;
    private String originalURL = "";
    private boolean isAllowed;
    private List<OrderStatus> allOrderStatus;
    private String description;
    private List<Category> allCate;
    private List<Groups> listGroups;
    private List<Specifics> allSpec;
    private Product product;
    private Category category;
    private Groups groups;
    private Specifics spec;
    private List<FileUploadEvent> fileAvatar;
    private String fileAvatarString;
    private List<FileUploadEvent> fileListImages;
    private int fileLimit;
    private List<String> fileListImagesString;
    private List<String> fileListDeleteString;
    private HashMap<String, String> hmSpec;
    private HashMap<String, String> hmDes;
    private List<String> tagList;
    private String tag;
    private String lastInputTag;
    private int specID;
    private XmlAccessDescriptions XAD;
    private Employee emp;

    //getter and setter//
    public int getFileLimit() {
        return fileLimit;
    }

    public void setFileLimit(int fileLimit) {
        this.fileLimit = fileLimit;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public GroupsFacadeLocal getGroupsF() {
        return groupsF;
    }

    public void setGroupsF(GroupsFacadeLocal groupsF) {
        this.groupsF = groupsF;
    }

    public CategoryFacadeLocal getCateF() {
        return cateF;
    }

    public void setCateF(CategoryFacadeLocal cateF) {
        this.cateF = cateF;
    }

    public String[] getEmpRole() {
        return empRole;
    }

    public void setEmpRole(String[] empRole) {
        this.empRole = empRole;
    }

    public List<Category> getAllCate() {
        return allCate;
    }

    public void setAllCate(List<Category> allCate) {
        this.allCate = allCate;
    }

    public List<Groups> getListGroups() {
        return listGroups;
    }

    public void setListGroups(List<Groups> listGroups) {
        this.listGroups = listGroups;
    }

    public List<Specifics> getAllSpec() {
        return allSpec;
    }

    public void setAllSpec(List<Specifics> allSpec) {
        this.allSpec = allSpec;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Groups getGroups() {
        return groups;
    }

    public void setGroups(Groups groups) {
        this.groups = groups;
    }

    public Specifics getSpec() {
        return spec;
    }

    public void setSpec(Specifics spec) {
        this.spec = spec;
    }

    public List<FileUploadEvent> getFileAvatar() {
        return fileAvatar;
    }

    public void setFileAvatar(List<FileUploadEvent> fileAvatar) {
        this.fileAvatar = fileAvatar;
    }

    public String getFileAvatarString() {
        return fileAvatarString;
    }

    public void setFileAvatarString(String fileAvatarString) {
        this.fileAvatarString = fileAvatarString;
    }

    public List<FileUploadEvent> getFileListImages() {
        return fileListImages;
    }

    public void setFileListImages(List<FileUploadEvent> fileListImages) {
        this.fileListImages = fileListImages;
    }

    public List<String> getFileListImagesString() {
        return fileListImagesString;
    }

    public void setFileListImagesString(List<String> fileListImagesString) {
        this.fileListImagesString = fileListImagesString;
    }

    public List<String> getFileListDeleteString() {
        return fileListDeleteString;
    }

    public void setFileListDeleteString(List<String> fileListDeleteString) {
        this.fileListDeleteString = fileListDeleteString;
    }

    public HashMap<String, String> getHmSpec() {
        return hmSpec;
    }

    public void setHmSpec(HashMap<String, String> hmSpec) {
        this.hmSpec = hmSpec;
    }

    public HashMap<String, String> getHmDes() {
        return hmDes;
    }

    public void setHmDes(HashMap<String, String> hmDes) {
        this.hmDes = hmDes;
    }

    public List<String> getTagList() {
        return tagList;
    }

    public void setTagList(List<String> tagList) {
        this.tagList = tagList;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getLastInputTag() {
        return lastInputTag;
    }

    public void setLastInputTag(String lastInputTag) {
        this.lastInputTag = lastInputTag;
    }

    public int getSpecID() {
        return specID;
    }

    public void setSpecID(int specID) {
        this.specID = specID;
    }

    public String getBaseURL() {
        return baseURL;
    }

    public void setBaseURL(String baseURL) {
        this.baseURL = baseURL;
    }

    public List<OrderStatus> getAllOrderStatus() {
        return allOrderStatus;
    }

    public void setAllOrderStatus(List<OrderStatus> allOrderStatus) {
        this.allOrderStatus = allOrderStatus;
    }

    //end getter and setter//
    @PostConstruct
    public void init() {
        String currentURL = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        listAllRole = ARF.findAll();
        originalURL = (String) externalContext.getRequestMap().get(RequestDispatcher.FORWARD_REQUEST_URI);
        baseURL = externalContext.getRequestContextPath();
        allOrderStatus = OSF.findAll();
        emp = new Employee();
        XAD = new XmlAccessDescriptions();
        category = new Category();
        groups = new Groups();
        product = new Product();
        spec = new Specifics();
        getSpecActive();
        hmSpec = new HashMap<>();
        hmDes = new HashMap<>();
        fileListImages = new ArrayList<>();
        fileLimit = 10;
        fileListImagesString = new ArrayList<>();
        fileListDeleteString = new ArrayList<>();
        fileAvatar = new ArrayList<>();
        fileAvatarString = "";
        isAllowed = false;
        tag = "";
        lastInputTag = "";
        tagList = new ArrayList<>();
        specID = 0;
        if (originalURL == null) {
            originalURL = externalContext.getRequestContextPath() + "//Admin/Employee/Index.xhtml";
        } else {
            String originalQuery = (String) externalContext.getRequestMap().get(RequestDispatcher.FORWARD_QUERY_STRING);

            if (originalQuery != null) {
                originalURL += "?" + originalQuery;
            }
        }
    }

    public void isAllowed(String role) {
        isAllowed = false;
        List<AdminRole> listRole;
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        try {
            emp = (Employee) externalContext.getSessionMap().get("LOGGED_ADMIN");
            EF.refresh();
            emp = EF.find(emp.getEmployeeID());
            listRole = (List<AdminRole>) emp.getAdminRoleCollection();
            for (Iterator<AdminRole> iterator = listRole.iterator(); iterator.hasNext();) {
                AdminRole aRole = iterator.next();
                //Role00=Manage
                if (aRole.getRoleID().equals("Role00") || aRole.getRoleID().equals(role)) {
                    isAllowed = true;
                    break;
                }
            }
            if (isAllowed == false || emp.getEmployeeStatus() != 1) {
                externalContext.redirect(baseURL + "//Admin/Error.xhtml");
            }
        } catch (Exception e) {
            try {
                e.printStackTrace();
                externalContext.redirect(baseURL + "//Admin/Error.xhtml");
            } catch (Exception ex) {
            }
        }
    }

    public void getCateActive() {
        cateF.refresh();
        allCate = cateF.findAll();
        for (Iterator<Category> iterator = allCate.iterator(); iterator.hasNext();) {
            Category next = iterator.next();
            if (next.getCateStatus() != 1) {
                iterator.remove();
            }
        }
    }

    public void getGroupsActive(int cateID) {
        groupsF.refresh();
        listGroups = (List<Groups>) cateF.find(cateID).getGroupsCollection();
        for (Iterator<Groups> iterator = listGroups.iterator(); iterator.hasNext();) {
            Groups next = iterator.next();
            if (next.getGroupStatus() != 1) {
                iterator.remove();
            }
        }
    }

    public void handleFileAvatarUpload(FileUploadEvent event) throws IOException {
        fileAvatar = new ArrayList<>();
        fileAvatar.add(event);
        System.out.println("upload a avatar");
        FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
        FacesContext.getCurrentInstance().addMessage("messagesUpdate", message);
    }

    public void resetUploadAvatar() {
        fileListImages = new ArrayList<>();
        fileListDeleteString = new ArrayList<>();
        System.out.println("reset Avatar Array");
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Reset Avatar File Upload"));
    }

    public void resetUploadListImages() {
        fileListImages = new ArrayList<>();
        fileListDeleteString = new ArrayList<>();
        fileLimit = 10 - fileListImagesString.size() - fileListImages.size();
        System.out.println("reset List Image Array");
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Reset List Images Content File Upload"));
    }

    public void handleFileImagesUpload(FileUploadEvent event) throws IOException {
        fileListImages.add(event);
        fileLimit = 10 - fileListImagesString.size() - fileListImages.size();
        FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
        System.out.println("upload images List: " + event.getFile().getFileName() + "");
        FacesContext.getCurrentInstance().addMessage("messagesUpdate", message);
    }

    public List<String> convertHashMapToList(HashMap<String, String> hmString, String typeOutput) {
        if (typeOutput.equalsIgnoreCase("Key")) {
            List<String> listKeyString = new ArrayList<String>();
            for (Map.Entry<String, String> entry : hmString.entrySet()) {
                String key = entry.getKey();
                listKeyString.add(key);
            }
            return listKeyString;
        } else {
            List<String> listValueString = new ArrayList<String>();
            for (Map.Entry<String, String> entry : hmString.entrySet()) {
                String value = entry.getValue();
                listValueString.add(value);
            }
            return listValueString;
        }
    }

    public void specChange() throws Exception {
        spec = specF.find(specID);
        hmSpec.put(spec.getSpecName(), "");
        allSpec.remove(spec);
    }

    public void groupsChange() {
        groups = groupsF.find(groups.getGroupID());
    }

    public void cateChange() {
        try {
            category = cateF.find(category.getCateID());
            getGroupsActive(category.getCateID());
            groups = new Groups();
        } catch (Exception e) {
        }
    }

    public String getNameBySpecID(String specID) {
        return specF.find(Integer.valueOf(specID)).getSpecName();
    }

    public void addNewTag() {

        int lenthOfString = 0;
        //handle if lastInput Tag is exists

        for (Iterator<String> iterator = tagList.iterator(); iterator.hasNext();) {
            String next = iterator.next();
            if (next.equalsIgnoreCase(lastInputTag)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error", "Tag Is Exists"));
                lastInputTag = "";
                return;
            }
            lenthOfString += next.length();
        }
        //handle max length of String//max=500

        if (lenthOfString + lastInputTag.length() > 500) {
            //output facesmessage for max length
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error", "Can't Add More Tag"));
            lastInputTag = "";
            return;
        }
        tagList.add(lastInputTag);
        lastInputTag = "";
    }

    public void removeTag() {
        String tagValue = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("valueTag");
        tagList.remove(tagValue);
    }

    public void editTag() {
        String tagValue = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("valueTag");
        lastInputTag = tagValue;
        tagList.remove(tagValue);
    }

    public void addImageDelete() {
        String urlDelete = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("urlDelete");
        boolean exists = false;
        for (String url : fileListDeleteString) {
            if (url.equalsIgnoreCase(urlDelete)) {
                exists = true;
                break;
            }
        }
        if (exists == false) {
            fileListDeleteString.add(urlDelete);
            fileListImagesString.remove(urlDelete);
            System.out.println(urlDelete);
        }
    }

    public void deleteAvatarFile() {
        try {
            String root = FacesContext.getCurrentInstance().getExternalContext().getRealPath("");
            File fileDelete = new File(root + hmDes.get("Avatar"));
            if (fileDelete.delete()) {
                System.out.println(fileDelete.getName() + " is deleted!");
            } else {
                System.out.println("Delete operation is failed.");
            }
        } catch (Exception e) {
        }
    }

    public String writeAvatarFile(int productID) {
        String content = "/Images/Products/Avatar/";
        for (int i = 0; i < this.fileAvatar.size(); i++) {
            try {

                //write file in dist/deploy/t2mh-war_war/
                String fileName = fileAvatar.get(i).getFile().getFileName();
                InputStream fileContentDeploy = fileAvatar.get(i).getFile().getInputstream();
                String root = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/Images/Products/Avatar");
                File path = new File(root);
                if (!path.exists()) {
                    boolean status = path.mkdirs();
                }

                String extension = "";
                int index = fileName.lastIndexOf('.');
                if (index > 0) {
                    extension = fileName.substring(index);
                }

                File uploadFile = new File(path + "/" + String.valueOf(productID) + extension);
                content += productID + extension;//
                Files.copy(fileContentDeploy, uploadFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception e) {
                return content;
            }
        }
        return content;
    }

    public String writeImagesFile(int productID) {
        String rootLocalImage = "/Images/Products/Content/";
        List<String> listImages = new ArrayList<>();
        String contentReturn = "";
        String root = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/Images/Products/Content");
        Date date = new Date();
        DateFormat formatter = new SimpleDateFormat("ddMMyyyyhhmmss");
        File path = new File(root);
        if (!path.exists()) {
            boolean status = path.mkdirs();
        }
        for (int i = 0; i < this.fileListImages.size(); i++) {
            try {
                //write file in dist/deploy/t2mh-war_war/
                String fileName = fileListImages.get(i).getFile().getFileName();
                InputStream fileContentDeploy = fileListImages.get(i).getFile().getInputstream();

                String content = "";
                String extension = "";
                int index = fileName.lastIndexOf('.');
                if (index > 0) {
                    extension = fileName.substring(index);
                }

                String fileNameSaved = productID + "_" + i + "_" + formatter.format(date) + extension;
                File uploadFile = new File(path + "/" + fileNameSaved);
                Files.copy(fileContentDeploy, uploadFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                //edit hashmap
                content += rootLocalImage + fileNameSaved;
                listImages.add(content);
            } catch (Exception e) {
                return contentReturn;
            }
        }
        contentReturn = XAD.convertListToString("Image", listImages);
        return contentReturn;
    }

    public void deleteImagesFile() {
        String root = FacesContext.getCurrentInstance().getExternalContext().getRealPath("");
        File fileDelete;
        for (String fileUrl : fileListDeleteString) {
            try {
                fileDelete = new File(root + fileUrl);
                if (fileDelete.delete()) {
                    System.out.println(fileDelete.getName() + " is deleted!");
                } else {
                    System.out.println("Delete operation is failed.");
                }
            } catch (Exception e) {
            }
        }

    }

    public void reloadPage() {
        System.out.println("ReloadPage");
        category = new Category();
        groups = new Groups();
        product = new Product();
        spec = new Specifics();
        specID = 0;
        getCateActive();//allCate       reload list Category Active
        listGroups = new ArrayList<>();//clear Groups List Box
        getSpecActive();//allSpec       reload list Spec Active
        hmSpec = new HashMap<>();//
        hmDes = new HashMap<>();
        fileListImages = new ArrayList<>();
        fileLimit = 10;
        fileListImagesString = new ArrayList<>();
        fileListDeleteString = new ArrayList<>();
        fileAvatar = new ArrayList<>();
        fileAvatarString = "";
        tag = "";
        lastInputTag = "";
        tagList = new ArrayList<>();
    }

    public void clickToRemoveSpec(String specName) {
        specF.refresh();
        spec = specF.getSpecByName(specName);
        //remove inputtext
        if (hmSpec.containsKey(specName)) {
            hmSpec.remove(specName);
        }
        //remove selected on SelectOneListBox
        if (hmSpec.isEmpty()) {
            specID = 0;
        }

        if (allSpec.contains(spec) == false) {
            allSpec.add(spec);
        }
        List<Specifics> newSpecsList = specF.findAll();
        List<Specifics> tempSpecsList = new ArrayList<>();
        for (Iterator<Specifics> iterator = newSpecsList.iterator(); iterator.hasNext();) {
            Specifics next = iterator.next();
            for (Iterator<Specifics> currentIterator = allSpec.iterator(); currentIterator.hasNext();) {
                Specifics current = currentIterator.next();
                if (current.getSpecID().equals(next.getSpecID())) {
                    tempSpecsList.add(next);
                    currentIterator.remove();
                    iterator.remove();
                }
            }
        }
        allSpec = tempSpecsList;
    }

    public void getSpecActive() {
        specF.refresh();
        allSpec = specF.findAll();
        for (Iterator<Specifics> iterator = allSpec.iterator(); iterator.hasNext();) {
            Specifics next = iterator.next();
            if (next.getSpecStatus() != 1) {
                iterator.remove();
            }
        }
    }

    public void clickStatus(int productID, int productStatus) {
        isAllowed("Role03");

        try {
            System.out.println("ID Click: " + productID);
            System.out.println("status click: " + productStatus);
            product = productF.find(productID);
            product.setProductStatus(productStatus);
            Date date = new Date();
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String fullDescriptions = product.getDescriptions();

            //convert DescriptionsString to Document
            Document doc = XAD.stringToDocument(fullDescriptions);
            hmDes = XAD.convertDocumentToHashMap(doc);
            hmDes.put("AdminLastEdit", formatter.format(date));
            hmDes.put("AdminEditBy", String.valueOf(emp.getEmployeeID()));
            switch (product.getProductStatus()) {
                case -2:
                    hmDes.put("AdminEditReason", "Set Status To " + product.getProductStatus() + " [Blocked]");
                    break;
                case 0:
                    hmDes.put("AdminEditReason", "Set Status To " + product.getProductStatus() + " [Disable]");
                    break;
                case 1:
                    hmDes.put("AdminEditReason", "Set Status To " + product.getProductStatus() + " [Enable]");
                    break;
            }

            product.setDescriptions(XAD.createDescriptions(hmDes));
            productF.edit(product);

            FacesContext.getCurrentInstance().addMessage("messagesUpdate", new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Update Success"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage("messagesUpdate", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Update Fail"));
        }
    }

    public void loadDetails() throws IOException {
        reloadPage();
        System.out.println("Start Load Product Details");
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        //test

        //end test
        try {
            productF.refresh();
            groupsF.refresh();
            specF.refresh();
            if (externalContext.getRequestParameterMap().get("productID") != null) {
                product = productF.find(Integer.valueOf(externalContext.getRequestParameterMap().get("productID")));
                externalContext.getSessionMap().put("details_Product", product.getProductID());
            } else {
                product = productF.find(Integer.valueOf(externalContext.getSessionMap().get("details_Product").toString()));
            }

            String fullDescriptions = product.getDescriptions();

            //convert DescriptionsString to Document
            Document doc = XAD.stringToDocument(fullDescriptions);
            hmDes = XAD.convertDocumentToHashMap(doc);

            hmSpec = XAD.getListSpecOfProductByStringSpec(hmDes.get("Specs"));
            //test spec enable or else and remove listBox Spec
            {
                for (Map.Entry<String, String> entry : hmSpec.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    Specifics spec = specF.find(Integer.valueOf(key));
                    if (spec.getSpecStatus() != 1) {
                        hmSpec.remove(key);
                    } else//remove it if spec enable
                    {
                        allSpec.remove(spec);
                    }
                }
            }
            tagList = XAD.getListContent(fullDescriptions, "Tags");
            fileAvatarString = hmDes.get("Avatar");
            fileListImagesString = XAD.getListContent(fullDescriptions, "Images");
            //productName
            //Inventory

            //Product Description---     Avatar      ImagesList(Images)      SpecsList(Specs)       Price         Tags
            //Price Done
            //Description Done
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Can't Load Info"));
        }
    }

    public void updateProduct() throws IOException, XPathExpressionException {
        isAllowed("Role03");
        Date date = new Date();
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        hmDes.put("AdminLastEdit", formatter.format(date));
        hmDes.put("AdminEditBy", String.valueOf(emp.getEmployeeID()));
        switch (product.getProductStatus()) {
            case -2:
                hmDes.put("AdminEditReason", "Set Status To " + product.getProductStatus() + " [Blocked]");
                break;
            case 0:
                hmDes.put("AdminEditReason", "Set Status To " + product.getProductStatus() + " [Disable]");
                break;
            case 1:
                hmDes.put("AdminEditReason", "Set Status To " + product.getProductStatus() + " [Enable]");
                break;
        }

        product.setDescriptions(XAD.createDescriptions(hmDes));
        productF.edit(product);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Update Success"));
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        externalContext.getSessionMap().put("details_Product", product.getProductID());
//        externalContext.redirect(baseURL + "//Admin/Product/detailsProduct.xhtml");

    }

    public void acceptProduct() throws IOException {
        product.setProductStatus(0);
        Date date = new Date();
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        hmDes.put("AdminLastEdit", formatter.format(date));
        hmDes.put("AdminEditBy", String.valueOf(emp.getEmployeeID()));
        hmDes.put("AdminEditReason", "Accept Product");
        product.setDescriptions(XAD.createDescriptions(hmDes));

        productF.edit(product);
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        externalContext.redirect(baseURL + "/Admin/Product/approvalProduct.xhtml");
    }

    public void declineProduct() throws IOException, XPathExpressionException {
        deleteAvatarFile();
        fileListDeleteString = XAD.getListContent(product.getDescriptions(), "Images");
        deleteImagesFile();
        productF.remove(product);

        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        externalContext.redirect(baseURL + "/Admin/Product/approvalProduct.xhtml");
    }

    public void ProductNeedApprova() throws IOException {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        if (externalContext.getRequestParameterMap().get("productID") != null) {
            product = productF.find(Integer.valueOf(externalContext.getRequestParameterMap().get("productID")));
            externalContext.getSessionMap().put("details_Product", product.getProductID());
        } else {
            product = productF.getProductNeedApprova();
            if (product == null) {
                externalContext.redirect(baseURL + "/Admin/Product/listProduct.xhtml");
            }
            externalContext.getSessionMap().put("details_Product", product.getProductID());
        }
    }

    public String getAvatar(int ProductID) throws XPathExpressionException {
        return XAD.getAvatar(productF.find(ProductID).getDescriptions());
    }
}
