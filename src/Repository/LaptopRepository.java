package Repository;

import model.Laptop;
import util.XMLHelper;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LaptopRepository {

    private static final String FILE_PATH = "xml/laptop.xml";

    public List<Laptop> getAllLaptops() {
        List<Laptop> laptops = new ArrayList<>();
        File file = XMLHelper.resolveFile(FILE_PATH);

        try {
            if (!file.exists() || file.length() == 0) {
                createEmptyXmlFile();
                return laptops;
            }

            Document document = loadDocument();
            NodeList nodeList = document.getElementsByTagName("laptop");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    Laptop laptop = new Laptop(
                            getTagValue("id", element),
                            getTagValue("customerId", element),
                            getTagValue("merk", element),
                            getTagValue("tipe", element),
                            getTagValue("serialNumber", element),
                            getTagValue("keluhan", element)
                    );

                    laptops.add(laptop);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return laptops;
    }

    public void addLaptop(Laptop laptop) {
        try {
            File file = XMLHelper.resolveFile(FILE_PATH);
            if (!file.exists() || file.length() == 0) {
                createEmptyXmlFile();
            }

            Document document = loadDocument();
            Element root = document.getDocumentElement();

            Element laptopElement = document.createElement("laptop");

            appendChild(document, laptopElement, "id", laptop.getId());
            appendChild(document, laptopElement, "customerId", laptop.getCustomerId());
            appendChild(document, laptopElement, "merk", laptop.getMerk());
            appendChild(document, laptopElement, "tipe", laptop.getTipe());
            appendChild(document, laptopElement, "serialNumber", laptop.getSerialNumber());
            appendChild(document, laptopElement, "keluhan", laptop.getKeluhan());

            root.appendChild(laptopElement);
            saveDocument(document);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateLaptop(Laptop updatedLaptop) {
        try {
            File file = XMLHelper.resolveFile(FILE_PATH);
            if (!file.exists() || file.length() == 0) {
                createEmptyXmlFile();
            }

            Document document = loadDocument();
            NodeList nodeList = document.getElementsByTagName("laptop");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String id = getTagValue("id", element);

                    if (id != null && id.equals(updatedLaptop.getId())) {
                        setTagValue("customerId", updatedLaptop.getCustomerId(), element);
                        setTagValue("merk", updatedLaptop.getMerk(), element);
                        setTagValue("tipe", updatedLaptop.getTipe(), element);
                        setTagValue("serialNumber", updatedLaptop.getSerialNumber(), element);
                        setTagValue("keluhan", updatedLaptop.getKeluhan(), element);
                        break;
                    }
                }
            }

            saveDocument(document);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteLaptop(String id) {
        try {
            File file = XMLHelper.resolveFile(FILE_PATH);
            if (!file.exists() || file.length() == 0) {
                createEmptyXmlFile();
                return;
            }

            Document document = loadDocument();
            NodeList nodeList = document.getElementsByTagName("laptop");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String laptopId = getTagValue("id", element);

                    if (laptopId != null && laptopId.equals(id)) {
                        element.getParentNode().removeChild(element);
                        break;
                    }
                }
            }

            saveDocument(document);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Laptop getLaptopById(String id) {
        List<Laptop> laptops = getAllLaptops();

        for (Laptop laptop : laptops) {
            if (laptop.getId() != null && laptop.getId().equals(id)) {
                return laptop;
            }
        }

        return null;
    }

    private Document loadDocument() throws Exception {
        File file = XMLHelper.resolveFile(FILE_PATH);

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(file);
        document.getDocumentElement().normalize();

        return document;
    }

    private void saveDocument(Document document) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();

        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(XMLHelper.resolveFile(FILE_PATH));
        transformer.transform(source, result);
    }

    private void createEmptyXmlFile() {
        try {
            File directory = XMLHelper.resolveFile("xml");
            if (!directory.exists()) {
                directory.mkdirs();
            }

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();

            Element root = document.createElement("laptops");
            document.appendChild(root);

            saveDocument(document);
        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }
    }

    private void appendChild(Document document, Element parent, String tagName, String value) {
        Element child = document.createElement(tagName);
        child.appendChild(document.createTextNode(value != null ? value : ""));
        parent.appendChild(child);
    }

    private String getTagValue(String tagName, Element parent) {
        NodeList nodeList = parent.getElementsByTagName(tagName);

        if (nodeList.getLength() > 0 && nodeList.item(0).getFirstChild() != null) {
            return nodeList.item(0).getFirstChild().getNodeValue();
        }

        return "";
    }

    private void setTagValue(String tagName, String value, Element parent) {
        NodeList nodeList = parent.getElementsByTagName(tagName);

        if (nodeList.getLength() > 0) {
            Node node = nodeList.item(0);
            node.setTextContent(value != null ? value : "");
        } else {
            Document document = parent.getOwnerDocument();
            appendChild(document, parent, tagName, value);
        }
    }
}
