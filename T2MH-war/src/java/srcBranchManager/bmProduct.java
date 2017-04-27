/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package srcBranchManager;

import Entity.BranchManager;
import Entity.Category;
import Entity.Groups;
import Entity.Product;
import Entity.Specifics;
import SessionBean.BranchManagerFacadeLocal;
import SessionBean.CategoryFacadeLocal;
import SessionBean.GroupsFacadeLocal;
import SessionBean.ProductFacadeLocal;
import SessionBean.SpecificsFacadeLocal;
import XMLAccess.XmlAccessDescriptions;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import org.primefaces.event.FileUploadEvent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author 19319
 */
@Named(value = "bmProduct")
@ManagedBean
@SessionScoped
public class bmProduct implements Serializable {

    @EJB
    private ProductFacadeLocal productF;

    @EJB
    private SpecificsFacadeLocal specF;

    @EJB
    private GroupsFacadeLocal groupsF;

    @EJB
    private CategoryFacadeLocal cateF;

    @EJB
    private BranchManagerFacadeLocal BMF;

    private String baseURL = "";
    private String originalURL = "";
    private BranchManager bm;

    private boolean isAllowed;
//    private String description;
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

    //getter and setter//
    public int getFileLimit() {
        return fileLimit;
    }

    public void setFileLimit(int fileLimit) {
        this.fileLimit = fileLimit;
    }

    public List<String> getFileListDeleteString() {
        return fileListDeleteString;
    }

    public void setFileListDeleteString(List<String> fileListDeleteString) {
        this.fileListDeleteString = fileListDeleteString;
    }

    public String getFileAvatarString() {
        return fileAvatarString;
    }

    public void setFileAvatarString(String fileAvatarString) {
        this.fileAvatarString = fileAvatarString;
    }

    public List<String> getFileListImagesString() {
        return fileListImagesString;
    }

    public void setFileListImagesString(List<String> fileListImagesString) {
        this.fileListImagesString = fileListImagesString;
    }

    public int getSpecID() {
        return specID;
    }

    public void setSpecID(int specID) {
        this.specID = specID;
    }

    public HashMap<String, String> getHmDes() {
        return hmDes;
    }

    public void setHmDes(HashMap<String, String> hmDes) {
        this.hmDes = hmDes;
    }

    public String getLastInputTag() {
        return lastInputTag;
    }

    public void setLastInputTag(String lastInputTag) {
        this.lastInputTag = lastInputTag;
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

    public HashMap<String, String> getHmSpec() {
        return hmSpec;
    }

    public Specifics getSpec() {
        return spec;
    }

    public void setSpec(Specifics spec) {
        this.spec = spec;
    }

    public List<Groups> getListGroups() {
        return listGroups;
    }

    public void setListGroups(List<Groups> listGroups) {
        this.listGroups = listGroups;
    }

//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }

    public void setHmSpec(HashMap<String, String> hmSpec) {
        this.hmSpec = hmSpec;
    }

    public List<Specifics> getAllSpec() {
        return allSpec;
    }

    public void setAllSpec(List<Specifics> allSpec) {
        this.allSpec = allSpec;
    }

    public List<FileUploadEvent> getFileAvatar() {
        return fileAvatar;
    }

    public void setFileAvatar(List<FileUploadEvent> fileAvatar) {
        this.fileAvatar = fileAvatar;
    }

    public List<FileUploadEvent> getFileListImages() {
        return fileListImages;
    }

    public void setFileListImages(List<FileUploadEvent> fileListImages) {
        this.fileListImages = fileListImages;
    }

    public List<Category> getAllCate() {
        return allCate;
    }

    public void setAllCate(List<Category> allCate) {
        this.allCate = allCate;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
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

    public String getBaseURL() {
        return baseURL;
    }

    public void setBaseURL(String baseURL) {
        this.baseURL = baseURL;
    }

    //end getter and setter//
    @PostConstruct
    public void init() {
        String currentURL = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        originalURL = (String) externalContext.getRequestMap().get(RequestDispatcher.FORWARD_REQUEST_URI);
        baseURL = externalContext.getRequestContextPath();
        //Init Properties
        bm = new BranchManager();
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
            originalURL = externalContext.getRequestContextPath() + "/BranchManager/Index.xhtml";
        } else {
            String originalQuery = (String) externalContext.getRequestMap().get(RequestDispatcher.FORWARD_QUERY_STRING);

            if (originalQuery != null) {
                originalURL += "?" + originalQuery;
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

    public void isAllowed() throws IOException {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        try {
            BMF.refresh();
            bm = (BranchManager) externalContext.getSessionMap().get("LOGGED_BM");
            BranchManager currentInfo = BMF.find(bm.getBmUsername());

            if (currentInfo.getBMStatus() != 1 || currentInfo.getBmPassword().equals(bm.getBmPassword()) == false) {
                externalContext.redirect(baseURL + "/BranchManager/Error.xhtml");
            } else {
                isAllowed = true;
            }
        } catch (Exception e) {
            externalContext.redirect(baseURL + "/BranchManager/Error.xhtml");
        }
    }

    public void clickStatus(int productID, int productStatus) throws IOException {
        productF.refresh();
        isAllowed();
        try {
            System.out.println("ID Click: " + productID);
            product = productF.find(productID);
            if (product.getProductStatus() != 1 && product.getProductStatus() != 00) {
                FacesContext.getCurrentInstance().addMessage("messagesUpdate", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Update Fail"));
                return;
            }
            product.setProductStatus(productStatus);
            productF.edit(product);
            FacesContext.getCurrentInstance().addMessage("messagesUpdate", new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Update Success"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage("messagesUpdate", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Update Fail"));
        }
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

    public void handleFileAvatarUpload(FileUploadEvent event) throws IOException {
        fileAvatar = new ArrayList<>();
        fileAvatar.add(event);
        System.out.println("upload a avatar");
        FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
        FacesContext.getCurrentInstance().addMessage(null, message);
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
        FacesContext.getCurrentInstance().addMessage(null, message);
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
        fileLimit++;
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

    public void deleteAvatarFile() {
        try {
            String root = FacesContext.getCurrentInstance().getExternalContext().getRealPath("");
            File fileDelete = new File(root + XAD.getAvatar(product.getDescriptions()));
            if (fileDelete.delete()) {
                System.out.println(fileDelete.getName() + " is deleted!");
            } else {
                System.out.println("Delete operation is failed.");
            }
        } catch (Exception e) {
        }
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
        System.out.println("Content Return: "+contentReturn);
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

    public void cateChange() {
        try {
            category = cateF.find(category.getCateID());
            getGroupsActive(category.getCateID());
            groups = new Groups();
        } catch (Exception e) {
        }
    }

    public void groupsChange() {
        groups = groupsF.find(groups.getGroupID());
    }

    public void specChange() throws Exception {
        spec = specF.find(specID);
        hmSpec.put(String.valueOf(spec.getSpecID()), "");
        allSpec.remove(spec);
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

    public String getNameBySpecID(String specID) {
        return specF.find(Integer.valueOf(specID)).getSpecName();
    }

    public void addNewTag() {
        //handle tags Size <=10
        if (tagList.size() >= 10) {
            FacesContext.getCurrentInstance().addMessage("tagMessagesUpdate", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Can't Add More Tag,<br/> Size <= 10 tag"));
            lastInputTag = "";
            return;
        }

        int lenthOfString = 0;
        //handle if lastInput Tag is exists

        for (Iterator<String> iterator = tagList.iterator(); iterator.hasNext();) {
            String next = iterator.next();
            if (next.equalsIgnoreCase(lastInputTag)) {
                FacesContext.getCurrentInstance().addMessage("tagMessagesUpdate", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Tag Is Exists"));
                lastInputTag = "";
                return;
            }
            lenthOfString += next.length();
        }
        //handle max length of String//max=500

        if (lenthOfString + lastInputTag.length() > 500) {
            //output facesmessage for max length
            FacesContext.getCurrentInstance().addMessage("tagMessagesUpdate", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Can't Add More Tag,<br/> Length <= 500 Characters"));
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

    public void clickToRemoveSpec(String idOfSpec) {
        specF.refresh();
        try {
            spec = specF.find(Integer.valueOf(idOfSpec));
            //remove inputtext
            if (hmSpec.containsKey(idOfSpec)) {
                hmSpec.remove(idOfSpec);
            }
            //remove selected on SelectOneListBox
            if (hmSpec.isEmpty()) {
                specID = -1;
            }
            specID = -1;
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
        } catch (Exception e) {
        }
    }

    private String generateCodeDescription(String datetime, int branchID, String bmUsername) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update((datetime + branchID + bmUsername).getBytes());
        byte byteData[] = md.digest();
        //convert the byte to hex format method 1
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    public void createProduct() throws NoSuchAlgorithmException, IOException {
        //test
        isAllowed();

        if (fileAvatar.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Need Upload An Avatar Image"));
            return;
        }
        if (fileListImages.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Need Upload At Least An Images"));
            return;
        }

        //end test
        productF.refresh();
        groupsF.refresh();

        Date date = new Date();
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        product.setProductID(0);
        product.setBranchID(bm.getBranchID());
        product.setDateCreate(formatter.format(date));
        product.setProductStatus(-1);
        product.setGroupID(groupsF.find(groups.getGroupID()));
        //productName
        //Inventory

        //a String for test when create=> get =>edit
        String generateCode = generateCodeDescription(date.toString(), product.getBranchID().getBranchID(), bm.getBmUsername());
        product.setDescriptions(generateCode);
        productF.create(product);

        product = productF.findProductAfterCreate(bm.getBranchID().getBranchID(), generateCode);
        if (product == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Create Fail"));
            return;
        }
        //end get Product

        //update Product Description---     Avatar      ImagesList(Images)      SpecsList(Specs)       Price         Tags
        //Price Done
        //Description Done
        hmDes.put("Avatar", writeAvatarFile(product.getProductID()));
        hmDes.put("Images", writeImagesFile(product.getProductID()));
        hmDes.put("CreateBy", bm.getBmUsername());
        //convert hmSpec to String
        String specString = "";
        for (Map.Entry<String, String> entry : hmSpec.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            specString += "<Spec><SpecID>" + key + "</SpecID><SpecValue><![CDATA[" + value + "]]></SpecValue></Spec>";
        }
        hmDes.put("Specs", specString);

        hmDes.put("Tags", XAD.convertListToString("Tag", tagList));
        hmDes.put("Count", "0");

        String stringDes = XAD.createDescriptions(hmDes);
        product.setDescriptions(XAD.createDescriptions(hmDes));

        productF.edit(product);
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        externalContext.getSessionMap().put("details_Product", product.getProductID());
        externalContext.redirect(baseURL + "/BranchManager/Product/detailsProduct.xhtml");
    }

    public void loadDetails() throws IOException {
//        reloadPage();
        System.out.println("Start Load Product Details");
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        //test
        isAllowed();
        //testBM.id

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
            if (bm.getBranchID().getBranchID().equals(product.getBranchID().getBranchID()) == false) {
                product = null;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Can't Load Info From Product Of Other Branch"));
                return;
            }
            groups = product.getGroupID();
            category = groups.getCateID();
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
            fileLimit = 10 - fileListImagesString.size() - fileListImages.size();
            for (Iterator<Specifics> iterator = allSpec.iterator(); iterator.hasNext();) {
                Specifics spec = iterator.next();
                for (Map.Entry<String, String> entry : hmSpec.entrySet()) {
                    String key = entry.getKey();
                    if (spec.getSpecName().equalsIgnoreCase(key)) {
                        iterator.remove();
                        break;
                    }
                }
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Can't Load Info"));
        }
    }

    public void updateProduct() throws IOException, XPathExpressionException {
        try {
            //test
            isAllowed();
            if (bm.getBranchID().getBranchID().equals(product.getBranchID().getBranchID()) == false) {
                product = null;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Can't Update Info From Product Of Other Branch"));
                return;
            }
            if (fileListImages.isEmpty() && fileListImagesString.isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Need Upload At Least An Images"));
                return;
            }
            //end test

            productF.refresh();
            groupsF.refresh();

            product.setGroupID(groupsF.find(groups.getGroupID()));
            //productName
            //Inventory

            //update Product Description---     Avatar      ImagesList(Images)      SpecsList(Specs)       Price         Tags
            Date date = new Date();
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

            String specString = "";
            for (Map.Entry<String, String> entry : hmSpec.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                specString += "<Spec><SpecID>" + key + "</SpecID><SpecValue>" + value + "</SpecValue></Spec>";
            }
            hmDes.put("Specs", specString);

            hmDes.put("Tags", XAD.convertListToString("Tag", tagList));
            hmDes.put("LastEdit", formatter.format(date));
            hmDes.put("EditBy", bm.getBmUsername());
            //save, delete file image, write file image
            {
                if (fileAvatar.isEmpty()) {
                    //do nothing(do not upload image)
                } else {
                    //delete file
                    deleteAvatarFile();
                    //write file

                    hmDes.put("Avatar", writeAvatarFile(product.getProductID()));
                }
                //update Images Content
                {
                    //delete file
                    deleteImagesFile();
                    //write file
                    hmDes.put("Images", writeImagesFile(product.getProductID()) + XAD.convertListToString("Image", fileListImagesString));
                }
                System.out.println("XAD list to string of fileListImagesString: "+XAD.convertListToString("Image", fileListImagesString));
            }

            product.setDescriptions(XAD.createDescriptions(hmDes));
            productF.edit(product);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Update Success"));
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            externalContext.getSessionMap().put("details_Product", product.getProductID());
//            externalContext.redirect(baseURL + "/BranchManager/Product/detailsProduct.xhtml");

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Update Fail"));
        }

    }

    public String getAvatar(int ProductID) throws XPathExpressionException {
        return XAD.getAvatar(productF.find(ProductID).getDescriptions());
    }

    public long getPriceByProductID(Integer productID) throws XPathExpressionException {
        return XAD.getPrice(productF.find(productID).getDescriptions());
    }
}
