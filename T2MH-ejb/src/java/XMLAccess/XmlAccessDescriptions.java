/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package XMLAccess;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;

public class XmlAccessDescriptions {

    public String replaceXMLSpecialToString(String aString) {
        aString = aString.replaceAll(">\\s*<", "><");
        aString = aString.replaceAll("&#13;", "");
        aString = aString.replaceAll("\r\r\n", "\r\n");//for new line + 1 space line
        aString = aString.replaceAll("&lt;", "<");
        aString = aString.replaceAll("&gt;", ">");
        return aString;
    }

    public String documentToString(Document document) {
        String content = "";
        try {
            StringWriter sw = new StringWriter();
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
//            transformer.setOutputProperty(OutputKeys.CDATA_SECTION_ELEMENTS, "Descriptions");
            transformer.transform(new DOMSource(document), new StreamResult(sw));
            content = sw.toString();
            content = replaceXMLSpecialToString(content);
        } catch (Exception e) {
            return null;
        }
        return content;
    }

    public Document stringToDocument(String content) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(content)));
//            Document document = builder.newDocument();
//            TransformerFactory tf = TransformerFactory.newInstance();
//            Transformer transformer = tf.newTransformer();
//
//            document.createCDATASection(content);
            return document;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String createDescriptions(HashMap<String, String> content) {
        try {
            Document doc;
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
//            Document document = builder.parse(new InputSource(new StringReader(content)));
            doc = stringToDocument("<Descriptions></Descriptions>");
            Element eleRoot = doc.getDocumentElement();
            System.out.println("create element of root");
            for (Iterator<Map.Entry<String, String>> iterator = content.entrySet().iterator(); iterator.hasNext();) {

                Map.Entry<String, String> objectTag = iterator.next();
                Element ele = doc.createElement(objectTag.getKey());
                if (objectTag.getValue() == null) {
                    objectTag.setValue("");
                }
                String valueOfTag = objectTag.getValue();
                System.out.println(valueOfTag);
                ele.appendChild(doc.createCDATASection(valueOfTag));
                eleRoot.appendChild(ele);
            }
            return documentToString(doc);
        } catch (Exception e) {
            e.printStackTrace();;
            return null;
        }
    }

    public String getAvatar(String StringDescriptions) throws XPathExpressionException {
        Document doc = stringToDocument(StringDescriptions);
        javax.xml.xpath.XPath xp = javax.xml.xpath.XPathFactory.newInstance().newXPath();
        String xquery = "//Descriptions/Avatar";

        Node node = (Node) xp.evaluate(xquery, doc, XPathConstants.NODE);
        if (node != null) {
            System.out.println("TextContent Avatar: " + node.getTextContent());
            return node.getTextContent();
        } else {
            return null;
        }
    }

    public long getPrice(String StringDescriptions) throws XPathExpressionException {
        Document doc = stringToDocument(StringDescriptions);
        javax.xml.xpath.XPath xp = javax.xml.xpath.XPathFactory.newInstance().newXPath();
        String xquery = "//Descriptions/Price";
        Node node = (Node) xp.evaluate(xquery, doc, XPathConstants.NODE);
        if (node != null) {
            return Long.valueOf(node.getTextContent());
        } else {
            return 0;
        }
    }

    public int getCount(String StringDescriptions) throws XPathExpressionException {
        Document doc = stringToDocument(StringDescriptions);
        javax.xml.xpath.XPath xp = javax.xml.xpath.XPathFactory.newInstance().newXPath();
        String xquery = "//Descriptions/Count";
        Node node = (Node) xp.evaluate(xquery, doc, XPathConstants.NODE);
        if (node != null) {
            return Integer.valueOf(node.getTextContent());
        } else {
            return 0;
        }
    }

    public List<String> getImagesList(String StringDescriptions) throws XPathExpressionException {
        List<String> listImage = new ArrayList<String>();
        Document doc = stringToDocument(StringDescriptions);
        javax.xml.xpath.XPath xp = javax.xml.xpath.XPathFactory.newInstance().newXPath();
        String xquery = "//Descriptions/Images/Image";
        NodeList nodeList = (NodeList) xp.evaluate(xquery, doc, XPathConstants.NODESET);
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node childNode = nodeList.item(i);
            String imageSrc = "";
            if (childNode instanceof CharacterData) {
                CharacterData cd = (CharacterData) childNode;
                imageSrc += cd.getData();
            } else {
                imageSrc += nodeList.item(i).getTextContent();
            }
//                String imageSrc = nodeList.item(i).getTextContent();
            listImage.add(imageSrc);
        }

        return listImage;
    }

    public String getSpecAsIndiv(String DescriptionsString, String specTag) throws XPathExpressionException {
        try {
            Document doc = stringToDocument(DescriptionsString);
            javax.xml.xpath.XPath xp = javax.xml.xpath.XPathFactory.newInstance().newXPath();
            String xquery = "//Descriptions/" + specTag;

            Node node = (Node) xp.evaluate(xquery, doc, XPathConstants.NODE);
            if (node.hasChildNodes()) {
                return node.getTextContent();
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public List<String> getListContent(String DescriptionsString, String specTag) throws XPathExpressionException {
        List<String> results = new ArrayList<String>();
        Document doc = stringToDocument(DescriptionsString);
        javax.xml.xpath.XPath xp = javax.xml.xpath.XPathFactory.newInstance().newXPath();
        try {
            String xquery = "//Descriptions/" + specTag;
            Node node = (Node) xp.evaluate(xquery, doc, XPathConstants.NODE);
            if (node != null) {
                NodeList nodeList = node.getChildNodes();
                System.out.println("nodeList Length: " + nodeList.getLength());
                for (int i = 0; i < nodeList.getLength(); i++) {
                    if (nodeList.item(i) instanceof CharacterData) {
                        String textContent = nodeList.item(i).getTextContent();
                        System.out.println("textContet Of CDATA tag " + specTag + " :" + textContent);
                        Document docTextContent = stringToDocument("<" + specTag + ">" + textContent + "</" + specTag + ">");
                        Element ele = docTextContent.getDocumentElement();
                        NodeList nodeListTextContent = ele.getChildNodes();
                        System.out.println("NodeListTetContent Length: " + nodeListTextContent.getLength());
                        for (int j = 0; j < nodeListTextContent.getLength(); j++) {
                            Node childTextNode = nodeListTextContent.item(j);
                            if (childTextNode.hasChildNodes()) {
                                results.add(childTextNode.getTextContent());
                            }
                        }
                    } else {
                        results.add(nodeList.item(i).getTextContent());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    public HashMap<String, String> getListSpecOfProductByStringSpec(String specString) {
        HashMap<String, String> hmSpecProduct = new HashMap<>();
        Document specDocument = stringToDocument("<Specs>" + specString + "</Specs>");
        javax.xml.xpath.XPath xp = javax.xml.xpath.XPathFactory.newInstance().newXPath();
        String xquery = "//Specs/Spec";
        try {
            NodeList nodeListSpec = (NodeList) xp.evaluate(xquery, specDocument, XPathConstants.NODESET);
            for (int i = 0; i < nodeListSpec.getLength(); i++) {
                Node nodeSpec = nodeListSpec.item(i);
                if (nodeSpec.hasChildNodes()) {
                    Element eleSpec = (Element) nodeSpec;
                    String specID = eleSpec.getElementsByTagName("SpecID").item(0).getTextContent();
                    String specValue = eleSpec.getElementsByTagName("SpecValue").item(0).getTextContent();
                    hmSpecProduct.put(specID, specValue);
                }
            }
        } catch (Exception e) {
        }
        return hmSpecProduct;
    }

    public void childStringOfDocument(Document doc, String rootNode) {
        Element ele = doc.getDocumentElement();
        System.out.println(ele.toString());
    }

    public HashMap<String, String> getHashMapTagName(String descriptionS, String urlTag) throws XPathExpressionException {
        Document doc = stringToDocument(descriptionS);
        HashMap<String, String> hmListTag = new HashMap();
        //query~ Specs
        String xquery = "//Descriptions/" + urlTag;
        javax.xml.xpath.XPath xp = javax.xml.xpath.XPathFactory.newInstance().newXPath();
        NodeList nodeList = (NodeList) xp.evaluate(xquery, doc, XPathConstants.NODESET);
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.hasChildNodes()) {
                NodeList childNodeList = node.getChildNodes();
                for (int j = 0; j < childNodeList.getLength(); j++) {
                    Node nodeChild = childNodeList.item(j);
                    hmListTag.put(nodeChild.getNodeName(), nodeChild.getTextContent());
                }
            }
        }
        return hmListTag;
    }

    public String convertHashMapToString(HashMap<String, String> hmContent) {
        String content = "";
        for (Map.Entry<String, String> entry : hmContent.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            content += "<" + key + ">" + value + "</" + key + ">";
        }
        return content;
    }

    public String convertListToString(String tagForPerElement, List<String> list) {
        String content = "";
        for (Iterator iterator = list.iterator(); iterator.hasNext();) {
            String value = (String) iterator.next();
            content += "<" + tagForPerElement + ">" + value + "</" + tagForPerElement + ">";
        }
        return content;
    }

    public HashMap<String, String> convertDocumentToHashMap(Document descriptionsDocument) {
        HashMap<String, String> hmDes = new HashMap<String, String>();
        try {
            Element eleRoot = descriptionsDocument.getDocumentElement();
            NodeList nodeList = eleRoot.getChildNodes();
            System.out.println("NodeList size: "+nodeList.getLength());
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.hasChildNodes()) {
                    Element element = (Element) node;
                    if (element.hasChildNodes()) {
                        String output = new String();
                        Document document = node.getOwnerDocument();
                        DOMImplementationLS domImplLS = (DOMImplementationLS) document
                                .getImplementation();
                        LSSerializer serializer = domImplLS.createLSSerializer();
                        serializer.getDomConfig().setParameter("xml-declaration", false);
                        NodeList nodeListOutput = element.getChildNodes();
                        for (int j = 0; j < nodeListOutput.getLength(); j++) {
                            Node childNode = nodeListOutput.item(j);
                            if (childNode instanceof CharacterData) {
                                CharacterData cd = (CharacterData) childNode;
                                output += cd.getData();
                            } else {
                                output += serializer.writeToString(nodeListOutput.item(j));
                            }
                        }
                        hmDes.put(node.getNodeName(), replaceXMLSpecialToString(output));
                    } else {
                        String output = new String();
                        hmDes.put(element.getNodeName(), replaceXMLSpecialToString(element.getTextContent()));
                    }
                }
            }
            return hmDes;
        } catch (Exception e) {
            e.printStackTrace();
            return hmDes;
        }
    }
}
