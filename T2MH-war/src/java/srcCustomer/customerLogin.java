/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package srcCustomer;

import Entity.BranchManager;
import Entity.Customer;
import Entity.CustomerInfo;
import Entity.Orders;
import SessionBean.BranchManagerFacadeLocal;
import SessionBean.CustomerFacadeLocal;
import SessionBean.EmployeeFacadeLocal;
import SessionBean.EmployeeInfoFacadeLocal;
import SessionBean.ProductFacadeLocal;
import XMLAccess.XmlAccessDescriptions;
import java.io.IOException;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.inject.Named;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import org.primefaces.context.RequestContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author 19319
 */
@Named(value = "customerLogin")

@SessionScoped
//@RequestScoped
@ManagedBean
public class customerLogin implements Serializable {

    @EJB
    private ProductFacadeLocal productF;

    @EJB
    private CustomerFacadeLocal customerF;

    private String password;
    private String messageLogin;
    private String originalURL;
    private String baseURL;
    private String webURL;
    private String realPath;
    private Customer customer;
    private javax.xml.xpath.XPath xp;

    @ManagedProperty(value = "#{customerOrder}")
    private customerOrder customerOrderBean;
    // +setter

    public customerOrder getCustomerOrderBean() {
        return customerOrderBean;
    }

    public void setCustomerOrderBean(customerOrder customerOrderBean) {
        this.customerOrderBean = customerOrderBean;
    }

    public String getWebURL() {
        return webURL;
    }

    //Getter and Setter
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getRealPath() {
        return realPath;
    }

    public void setRealPath(String realPath) {
        this.realPath = realPath;
    }

    public void setWebURL(String webURL) {
        this.webURL = webURL;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMessageLogin() {
        return messageLogin;
    }

    public void setMessageLogin(String messageLogin) {
        this.messageLogin = messageLogin;
    }

    public String getBaseURL() {
        return baseURL;
    }

    public void setBaseURL(String baseURL) {
        this.baseURL = baseURL;
    }

    public String reWriteUrl(String path, String nameProject) {
        Pattern pat = Pattern.compile("(\\bdist\\b.*?war_war)");
        Matcher mat = pat.matcher(path);
        String url = mat.replaceAll(nameProject + "-war\\\\web");
        return url;
    }

    //End of Getter and Setter
    @PostConstruct
    public void init() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        originalURL = (String) externalContext.getRequestMap().get(RequestDispatcher.FORWARD_REQUEST_URI);
        baseURL = externalContext.getRequestContextPath();
        customer = new Customer();
        webURL = reWriteUrl(FacesContext.getCurrentInstance().getExternalContext().getRealPath(""), "T2MH");
        xp = javax.xml.xpath.XPathFactory.newInstance().newXPath();
        if (originalURL == null) {
            originalURL = externalContext.getRequestContextPath() + "/index.xhtml";
        } else {
            String originalQuery = (String) externalContext.getRequestMap().get(RequestDispatcher.FORWARD_QUERY_STRING);

            if (originalQuery != null) {
                originalURL += "?" + originalQuery;
            }
        }
    }

    public void reloadLogin() {
        customer = new Customer();
        messageLogin = new String();
    }

    private String encryptPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update((password).getBytes());
        byte byteData[] = md.digest();
        //convert the byte to hex format method 1
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    public void fastLogin() throws IOException, NoSuchAlgorithmException, XPathExpressionException {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        //  HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
        String password = customer.getCustomerPassword();
        String username = customer.getCustomerUsername();
        RequestContext contextRequest = RequestContext.getCurrentInstance();
        customerF.refresh();
        customer = customerF.findCustomerByUsername(username);

        if (customer == null) {
            this.messageLogin = "Username is not exists, try again";
            return;
        } else {
            if (customer.getCustomerStatus() != 1) {
                this.messageLogin = "Account has been locked. You cannot login";
                return;
            }
            if (customer.getCustomerStatus() == 1) {
                if (!encryptPassword(password).equals(customer.getCustomerPassword())) {
                    this.messageLogin = "Password incorrect, try again";
                    return;
                }
                if (encryptPassword(password).equals(customer.getCustomerPassword())) {
                    externalContext.getSessionMap().put("LOGGED_CUSTOMER", customer);
                    XmlAccessDescriptions XAD = new XmlAccessDescriptions();
                    Document desDoc = XAD.stringToDocument(customer.getCustomerInfo().getDescriptions());
                    //add current cart to account
                    if (customerOrderBean.getCartDocument().getElementsByTagName("OrderProduct").getLength() > 0) {
                        Document doc = (Document) customerOrderBean.getCartDocument();

                        //join
                        String xquery = "//CartOrder/OrderProduct";
                        NodeList listCurrentProduct = (NodeList) xp.evaluate(xquery, doc, XPathConstants.NODESET);

                        //write all node from new to saved, if exists, update entity
                        for (int i = 0; i < listCurrentProduct.getLength(); i++) {
                            //node Product in listOrder
                            Node nodeProductTest = listCurrentProduct.item(i);
                            String productID = ((Element) nodeProductTest.getChildNodes()).getElementsByTagName("ProductID").item(0).getTextContent();
                            int quantity = Integer.valueOf(((Element) nodeProductTest.getChildNodes()).getElementsByTagName("Quantity").item(0).getTextContent());

                            //test if product is active(Active mean cate group, branch and product is active)
                            boolean isActive = productF.productIsActive(Integer.valueOf(productID));

                            xquery = "//Descriptions/CartOrder/OrderProduct[ProductID='" + productID + "']";
                            Node nodeProductFromDes = (Node) xp.evaluate(xquery, desDoc, XPathConstants.NODE);
                            if (nodeProductFromDes != null)//exists, update entity
                            {
                                if (isActive) {
                                    ((Element) nodeProductFromDes.getChildNodes()).getElementsByTagName("Quantity").item(0).setTextContent(String.valueOf(quantity));
                                } else {
                                    nodeProductFromDes.getParentNode().removeChild(nodeProductFromDes);
//                                    desDoc.removeChild(nodeProductFromDes.getParentNode());
                                }

                            } else//write new
                            {
                                if (isActive) {
                                    Element eleroot = desDoc.getDocumentElement();
                                    Node nodeCartOrder;
                                    if (desDoc.getElementsByTagName("CartOrder").getLength() == 0) {
                                        nodeCartOrder = desDoc.createElement("CartOrder");
                                        eleroot.appendChild(nodeCartOrder);
                                    } else {
                                        nodeCartOrder = desDoc.getElementsByTagName("CartOrder").item(0);
                                    }
                                    Element eleOrder = (Element) nodeCartOrder;

                                    Element nodeProduct = desDoc.createElement("OrderProduct");

                                    //create element
                                    Element nodeID = desDoc.createElement("ProductID");
                                    nodeID.appendChild(desDoc.createTextNode(String.valueOf(productID)));
                                    nodeProduct.appendChild(nodeID);

                                    Element nodeQuantity = desDoc.createElement("Quantity");
                                    nodeQuantity.appendChild(desDoc.createTextNode(String.valueOf(quantity)));
                                    nodeProduct.appendChild(nodeQuantity);

                                    eleOrder.appendChild(nodeProduct);
                                } else {

                                }
                            }

                        }
                        //write in account and set in session
                        customer.getCustomerInfo().setDescriptions(XAD.documentToString(desDoc));
                        customerF.edit(customer);
                        //XAD.convertHashMapToString(XAD.convertDocumentToHashMap(desDoc))
                        customerOrderBean.setCartDocument(XAD.stringToDocument("<CartOrder>" + XAD.convertDocumentToHashMap(desDoc).get("CartOrder") + "</CartOrder>"));
                        //
                    } else {
                        String xquery = "//Descriptions/CartOrder/OrderProduct";
                        NodeList listProductOfCartAccount = (NodeList) xp.evaluate(xquery, desDoc, XPathConstants.NODESET);

                        for (int i = 0; i < listProductOfCartAccount.getLength(); i++) {
                            //node Product in cartCustomer
                            Node nodeProductTest = listProductOfCartAccount.item(i);
                            String productID = ((Element) nodeProductTest.getChildNodes()).getElementsByTagName("ProductID").item(0).getTextContent();

                            //test if product is active(Active mean cate group, branch and product is active)
                            boolean isActive = productF.productIsActive(Integer.valueOf(productID));
                            if (!isActive) {
                                nodeProductTest.getParentNode().removeChild(nodeProductTest);
                            }
                        }
                        customer.getCustomerInfo().setDescriptions(XAD.documentToString(desDoc));
                        customerF.edit(customer);
                        customerOrderBean.setCartDocument(XAD.stringToDocument("<CartOrder>" + XAD.convertDocumentToHashMap(desDoc).get("CartOrder") + "</CartOrder>"));
                    }
//                    contextRequest.execute("PF('dialogLoginForm').hide();");
                    System.out.println("logged");
                    customerOrderBean.setCustomer(customer);
                    CustomerInfo customerInfo = customer.getCustomerInfo();
                    Orders order = new Orders();
                    order.setSender(customerInfo.getCustomerName());
                    order.setSenderPhone(customerInfo.getCustomerPhone());
                    order.setReceiver(customerInfo.getCustomerName());
                    order.setReceiverPhone(customerInfo.getCustomerPhone());
                    order.setReceiverAddress(customerInfo.getCustomerAddress());
                    customerOrderBean.setOrder(order);
                    externalContext.redirect(((HttpServletRequest) externalContext.getRequest()).getRequestURI());
                }
            }
        }
    }

    public void logout() throws IOException {
//        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
//        isLoggedCustomer=false;
//        externalContext.invalidateSession();
//        externalContext.redirect(externalContext.getRequestContextPath() + "/Admin/Login.xhtml");

        //code below only remove logged_
        System.out.println("Logout");
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
        externalContext.getSessionMap().remove("LOGGED_CUSTOMER");
        externalContext.redirect(((HttpServletRequest) externalContext.getRequest()).getRequestURI());
//        externalContext.redirect(externalContext.getRequestContextPath() + "/Customer/Login.xhtml");
    }

}
