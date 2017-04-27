/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package srcBranchManager;

import Entity.AdminRole;
import Entity.Branch;
import Entity.BranchManager;
import Entity.Employee;
import Entity.Product;
import SessionBean.BranchFacadeLocal;
import SessionBean.BranchManagerFacadeLocal;
import SessionBean.EmployeeFacadeLocal;
import SessionBean.ProductFacadeLocal;
import XMLAccess.XmlAccessDescriptions;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.Part;
import org.primefaces.event.FileUploadEvent;
import org.w3c.dom.Document;

/**
 *
 * @author 19319
 */
@Named(value = "bmBranch")

@SessionScoped
@ManagedBean
public class bmBranch {

    
    @EJB
    private BranchManagerFacadeLocal BMF;

    @EJB
    private BranchFacadeLocal BF;
    @EJB
    private ProductFacadeLocal PF;
    @EJB
    private EmployeeFacadeLocal EF;
    private Branch branch;
    private BranchManager bm;
    private String originalURL = "";
    private String baseURL = "";
    private boolean isAllowed = false;
    private boolean isNameExists = false;
    private boolean isValidEmail = true;
    private boolean isValidAddress = true;
    private boolean isValidTaxInput = false;
    private boolean isUploadedImage = false;
    private boolean isEnable = true;

//    private Part file;
    private HashMap<String, String> hmDes;
    private List<FileUploadEvent> fileList;
    private XmlAccessDescriptions XAD;
    //getter and setter

//    }
    public boolean isIsUploadedImage() {
        return isUploadedImage;
    }

    public void setIsUploadedImage(boolean isUploadedImage) {
        this.isUploadedImage = isUploadedImage;
    }

    public List<FileUploadEvent> getFileList() {
        return fileList;
    }

    public void setFileList(List<FileUploadEvent> fileList) {
        this.fileList = fileList;
    }

    public boolean isIsEnable() {
        return isEnable;
    }

    public void setIsEnable(boolean isEnable) {
        this.isEnable = isEnable;
    }

    public boolean isIsNameExists() {
        return isNameExists;
    }


    public boolean isIsValidEmail() {
        return isValidEmail;
    }

    public void setIsValidEmail(boolean isValidEmail) {
        this.isValidEmail = isValidEmail;
    }

    public void setIsNameExists(boolean isNameExists) {
        this.isNameExists = isNameExists;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public boolean isIsValidAddress() {
        return isValidAddress;
    }

    public void setIsValidAddress(boolean isValidAddress) {
        this.isValidAddress = isValidAddress;
    }

    public HashMap<String, String> getHmDes() {
        return hmDes;
    }

    public void setHmDes(HashMap<String, String> hmDes) {
        this.hmDes = hmDes;
    }

    //end getter and setter
    public bmBranch() {
    }

    @PostConstruct
    public void init() {
        String currentURL = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        hmDes = new HashMap<String, String>();
        XAD=new XmlAccessDescriptions();
        bm = new BranchManager();
        fileList = new ArrayList<FileUploadEvent>();
        branch = new Branch();
        originalURL = (String) externalContext.getRequestMap().get(RequestDispatcher.FORWARD_REQUEST_URI);
        baseURL = externalContext.getRequestContextPath();
        if (originalURL == null) {
            originalURL = externalContext.getRequestContextPath() + "/BranchManager/Branch/Index.xhtml";
        } else {
            String originalQuery = (String) externalContext.getRequestMap().get(RequestDispatcher.FORWARD_QUERY_STRING);

            if (originalQuery != null) {
                originalURL += "?" + originalQuery;
            }
        }
    }

    public void isAllowed() throws IOException {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        try {
            bm = (BranchManager) externalContext.getSessionMap().get("LOGGED_BM");
            BMF.refresh();
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

    public void checkFileUpload() {
        try {
            if (fileList == null || fileList.isEmpty()) {
                FacesContext.getCurrentInstance().addMessage("messagesUpdate", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Need upload a Avatar Image"));
                isUploadedImage = false;
                return;
            } else {
                isUploadedImage = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage("messagesUpdate", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Need upload a Avatar Image"));
            isUploadedImage = false;
            return;
        }
    }

    public void handleFileUpload(FileUploadEvent event) throws IOException {
        fileList = new ArrayList<FileUploadEvent>();
        fileList.add(event);
        FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
        FacesContext.getCurrentInstance().addMessage("messagesUpdate", message);
    }

    public String reWriteUrl(String path, String nameProject) {
        Pattern pat = Pattern.compile("(\\bdist\\b.*?war_war)");
        Matcher mat = pat.matcher(path);
        String url = mat.replaceAll(nameProject + "-war\\\\web");
        return url;
    }

    public String writeFile(int BranchID) {
        String content = "/Images/Branch/";
        for (int i = 0; i < this.fileList.size(); i++) {
            try {
                String fileName = fileList.get(i).getFile().getFileName();
                InputStream fileContent = fileList.get(i).getFile().getInputstream();

                //write file in t2mh-war/web/
                String root = reWriteUrl(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"), "T2MH") + "/Images/Branch";
                File path = new File(root);
                if (!path.exists()) {
                    boolean status = path.mkdirs();
                }
                String extension = "";
                int index = fileName.lastIndexOf('.');
                if (index > 0) {
                    extension = fileName.substring(index);
                }
                File uploadFile = new File(path + "/" + String.valueOf(BranchID) + extension);
                content += BranchID + extension;
                Files.copy(fileContent, uploadFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                //write file in dist/deploy/t2mh-war_war/
                InputStream fileContentDeploy = fileList.get(i).getFile().getInputstream();
                root = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/Images/Branch");
                path = new File(root);
                if (!path.exists()) {
                    boolean status = path.mkdirs();
                }
                uploadFile = new File(path + "/" + String.valueOf(BranchID) + extension);
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
            File fileDelete = new File(root + XAD.getAvatar(branch.getBranchDescriptions()));
            if (fileDelete.delete()) {
                System.out.println(fileDelete.getName() + " is deleted!");
            } else {
                System.out.println("Delete operation is failed.");
            }
        } catch (Exception e) {
        }
    }
    //////////////////////////////////////////////////////////////////////////////
    public void loadDetails() {
        try {
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            branch = bm.getBranchID();
            //load hashmap of tag from Descriptions
            hmDes = XAD.convertDocumentToHashMap(XAD.stringToDocument(branch.getBranchDescriptions()));
            if(hmDes.get("typeTax").equalsIgnoreCase("Excises"))
            {
                hmDes.put("labelTypeOfTax", "VND");
            }
            else
            {
                hmDes.put("labelTypeOfTax", "%");
            }
        } catch (Exception e) {
        }
    }

    public String getAvatar(String id) {
        try {
            return XAD.getAvatar(BF.find(Integer.valueOf(id)).getBranchDescriptions());
        } catch (Exception e) {
            return null;
        }
    }

    public void checkEmail() {
        String branchEmail = hmDes.get("Email");
        if (branchEmail.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")) {
            isValidEmail = true;
        } else {
            FacesContext.getCurrentInstance().addMessage("messagesUpdate", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Email Is Incorrect"));
            isValidEmail = false;
        }
    }

    public void checkAddress() {
        String branchAddress = hmDes.get("Address");
        if (branchAddress.length() < 20) {
            isValidAddress = false;
            FacesContext.getCurrentInstance().addMessage("messagesUpdate", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Address Too Short"));
        } else {
            isValidAddress = true;
        }
    }

    public void updateBranch() throws IOException {
        checkEmail();
        checkAddress();
        if (isValidAddress != true || isValidEmail != true) {
            return;
        }
        //edit branch Descriptions
        try {
            if (fileList.isEmpty() == false) {
                hmDes.put("Avatar", writeFile(branch.getBranchID()));
            }
            branch.setBranchDescriptions(XAD.createDescriptions(hmDes));
            BF.edit(branch);

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage("messagesUpdate", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Update Fail"));
        }
        FacesContext.getCurrentInstance().addMessage("messagesUpdate", new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Update Success"));
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        externalContext.getSessionMap().put("details_Branch", branch.getBranchID());
//        searchString=branch.getBranchID().toString();
//        searchByString("ID");
    }

}
