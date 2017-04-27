/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package srcAdmin;

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
@Named(value = "aboutBranch")

@SessionScoped
@ManagedBean
public class aboutBranch {

    private String baseURL = "";
    @EJB
    private BranchFacadeLocal BF;
    @EJB
    private ProductFacadeLocal PF;
    @EJB
    private EmployeeFacadeLocal EF;
    @EJB
    private BranchManagerFacadeLocal BMF;

    private Branch branch;
    private String originalURL = "";
    private boolean isAllowed = false;
    private boolean isNameExists = false;
    private boolean isValidEmail = true;
    private boolean isValidAddress = true;
    private boolean isValidTaxInput = false;
    private boolean isUploadedImage = false;
    private boolean isEnable = true;

    private HashMap<String, String> hmDes;
    private List<FileUploadEvent> fileList;
    private XmlAccessDescriptions XAD;
    //getter and setter

    public HashMap<String, String> getHmDes() {
        return hmDes;
    }

    public void setHmDes(HashMap<String, String> hmDes) {
        this.hmDes = hmDes;
    }

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

    //end getter and setter
    public aboutBranch() {
    }

    @PostConstruct
    public void init() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        originalURL = (String) externalContext.getRequestMap().get(RequestDispatcher.FORWARD_REQUEST_URI);
        baseURL = externalContext.getRequestContextPath();
        branch = new Branch();
        fileList = new ArrayList<>();
        hmDes = new HashMap<>();
        XAD = new XmlAccessDescriptions();
        if (originalURL == null) {
            originalURL = externalContext.getRequestContextPath() + "/Admin/Branch/Index.xhtml";
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
            Employee emp = (Employee) externalContext.getSessionMap().get("LOGGED_ADMIN");
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
                externalContext.redirect(baseURL + "/Admin/Error.xhtml");
            }
        } catch (Exception e) {
            try {
                e.printStackTrace();
                externalContext.redirect(baseURL + "/Admin/Error.xhtml");
            } catch (Exception ex) {
            }
        }
    }

    public void chooseTypeListener(ValueChangeEvent event) {
        if (event.getNewValue().toString().equalsIgnoreCase("Ad Valorem")) {
            hmDes.put("labelTypeOfTax", "%");

        } else {
            hmDes.put("labelTypeOfTax", "VND");

        }
    }

    public void checkTaxInput() {
        try {
            if (hmDes.get("typeTax") == null) {
                isValidTaxInput = false;
                FacesContext.getCurrentInstance().addMessage("messagesUpdate", new FacesMessage(FacesMessage.SEVERITY_WARN, "Info", "Type of Tax is required"));
                return;
            }
            if (hmDes.get("typeTax").equalsIgnoreCase("Ad Valorem")) {
                if (Double.valueOf(hmDes.get("valueTax")) >= 50) {
                    isValidTaxInput = false;
                    FacesContext.getCurrentInstance().addMessage("messagesUpdate", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Tax's Ad Valorem must less than 50"));
                    return;
                }
            } else {
                if (Double.valueOf(hmDes.get("valueTax")) > 100000000) {
                    isValidTaxInput = false;
                    FacesContext.getCurrentInstance().addMessage("messagesUpdate", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Tax's Excises must less than or equal 100,000,000"));
                    return;
                } else if (Double.valueOf(hmDes.get("valueTax")) < 1000000) {
                    isValidTaxInput = false;
                    FacesContext.getCurrentInstance().addMessage("messagesUpdate", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Tax's Excises must greater than or equal 1,000,000"));
                    return;
                }
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage("messagesUpdate", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Check Tax Input Fail"));
            isValidTaxInput = false;
            return;
        }
        isValidTaxInput = true;
    }

    public void checkName() {
        if (BF.isNameExists(branch.getBranchName()) && branch.getBranchName().length() >= 5) {
            isNameExists = true;
            FacesContext.getCurrentInstance().addMessage("messagesUpdate", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Branch Name Is Exists"));
        } else {
            isNameExists = false;
        }
    }

    public void checkNameForUpdate() {
        isAllowed("Role01");
        if (BF.isNameExistsForUpdate(branch.getBranchID().toString(), branch.getBranchName())) {
            isNameExists = true;
            FacesContext.getCurrentInstance().addMessage("messagesUpdate", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Branch Name Is Exists"));
        } else {
            isNameExists = false;
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

    public String writeFile(int BranchID) {
        String content = "/Images/Branch/";
        deleteAvatarFile();
        for (int i = 0; i < this.fileList.size(); i++) {
            try {
                String fileName = fileList.get(i).getFile().getFileName();
                InputStream fileContentDeploy = fileList.get(i).getFile().getInputstream();
                String root = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/Images/Branch/");
                File path = new File(root);
                if (!path.exists()) {
                    boolean status = path.mkdirs();
                }

                String extension = "";
                int index = fileName.lastIndexOf('.');
                if (index > 0) {
                    extension = fileName.substring(index);
                }

                File uploadFile = new File(path + "/" + BranchID + extension);
                content += BranchID + extension;//
                Files.copy(fileContentDeploy, uploadFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception e) {
                return content;
            }
        }
        return content;
    }

    public void prepareCreate() {
        branch = new Branch();
        fileList = new ArrayList<>();
        hmDes = new HashMap<>();
    }

    public void createBranch() throws IOException {
        try {
            isAllowed("Role01");
            checkAddress();
            checkEmail();
            checkName();
            checkFileUpload();
            checkTaxInput();
            if (isNameExists == true || isValidAddress != true || isValidEmail != true || isValidTaxInput != true || isUploadedImage != true) {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Some Field Has Error"));
                return;
            }
            BF.create(branch);
            int branchID = BF.getBranchByName(branch.getBranchName()).getBranchID();
            //edit branch Descriptions
            XmlAccessDescriptions XAD = new XmlAccessDescriptions();
            hmDes.put("Avatar", writeFile(branchID));
            branch.setBranchDescriptions(XAD.createDescriptions(hmDes));
            if (isEnable) {
                branch.setBranchStatus(1);
            } else {
                branch.setBranchStatus(0);
            }
            BF.edit(branch);

            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Create Success"));
            externalContext.getSessionMap().put("details_Branch", branchID);
            externalContext.redirect(baseURL + "/Admin/Branch/detailsBranch.xhtml");
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage("messagesUpdate", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Create Fail"));
        }
    }

    //////////////////////////////////////////////////////////////////////////////
    public void loadDetails() {
        isAllowed("Role01");
        try {
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            if (externalContext.getRequestParameterMap().get("idBranch") != null) {
                branch = BF.find(Integer.valueOf(externalContext.getRequestParameterMap().get("idBranch")));
                externalContext.getSessionMap().put("details_Branch", branch.getBranchID());
            } else {
                branch = BF.find(Integer.valueOf(externalContext.getSessionMap().get("details_Branch").toString()));
            }
            isEnable = branch.getBranchStatus() == 1;
            //load hashmap of tag from Descriptions
            hmDes = XAD.convertDocumentToHashMap(XAD.stringToDocument(branch.getBranchDescriptions()));
            if (hmDes.get("typeTax").equalsIgnoreCase("Excises")) {
                hmDes.put("labelTypeOfTax", "VND");
            } else {
                hmDes.put("labelTypeOfTax", "%");
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage("messagesUpdate", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Cannot Load Info"));
        }
    }

    public void loadDetails(int branchID) {
        try {
            branch = BF.find(branchID);
            isEnable = branch.getBranchStatus() == 1;
            //load hashmap of tag from Descriptions
            hmDes = XAD.convertDocumentToHashMap(XAD.stringToDocument(branch.getBranchDescriptions()));
            if (hmDes.get("typeTax").equalsIgnoreCase("Excises")) {
                hmDes.put("labelTypeOfTax", "VND");
            } else {
                hmDes.put("labelTypeOfTax", "%");
            }
        } catch (Exception e) {
        }
    }

    public String getAvatar(String id) {
        try {
            return XAD.getAvatar(BF.find(Integer.valueOf(id)).getBranchDescriptions());
        } catch (Exception e) {
            return "";
        }
    }

    public void updateBranch() throws IOException {
        isAllowed("Role01");
        checkNameForUpdate();
        if (isNameExists == true || isValidAddress != true || isValidEmail != true) {
            return;
        }
        //edit branch Descriptions
        try {
            XmlAccessDescriptions XAD = new XmlAccessDescriptions();
            if (fileList.isEmpty() == false) {
                hmDes.put("Avatar", writeFile(branch.getBranchID()));
            }
            branch.setBranchDescriptions(XAD.createDescriptions(hmDes));

            if (isEnable) {
                branch.setBranchStatus(1);
            } else {
                branch.setBranchStatus(0);
            }
            BF.edit(branch);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage("messagesUpdate", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Update Fail"));
        }
        FacesContext.getCurrentInstance().addMessage("messagesUpdate", new FacesMessage(FacesMessage.SEVERITY_INFO, "Error", "Update Success"));
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        externalContext.getSessionMap().put("details_Branch", branch.getBranchID());
    }

    public void clickStatus(String id) {
        isAllowed("Role00");
        try {
            branch = BF.find(Integer.valueOf(id));
            if (branch.getBranchStatus() != 1) {
                branch.setBranchStatus(1);
            } else {
                branch.setBranchStatus(0);
            }
            BF.edit(branch);
            FacesContext.getCurrentInstance().addMessage("messagesUpdate", new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Update Success"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage("messagesUpdate", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Update Fail"));
        }
    }

    public void goCreateBM(String branchID) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("details_Branch", branchID);
        FacesContext.getCurrentInstance().getExternalContext().redirect(baseURL + "/Admin/BranchManager/createBM.xhtml");
    }
}
