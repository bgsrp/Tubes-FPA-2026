package Repository;

import model.Service;
import util.XMLHelper;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ServiceRepository {

    private static final String FILE_PATH = "xml/service.xml";
    private static final String ROOT = "services";

    public List<Service> getAllServices() {

        List<Service> services = new ArrayList<>();

        Document document = XMLHelper.loadDocument(FILE_PATH);

        if (document == null) {
            return services;
        }

        Element root = XMLHelper.getRoot(document);

        if (root == null) {
            return services;
        }

        NodeList nodeList = root.getElementsByTagName("service");

        for (int i = 0; i < nodeList.getLength(); i++) {

            Node node = nodeList.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {

                Element element = (Element) node;

                Service service = new Service(
                        getTagValue(element, "id"),
                        getTagValue(element, "laptopId"),
                        getTagValue(element, "tanggalMasuk"),
                        getTagValue(element, "status"),
                        parseBiaya(getTagValue(element, "biaya")),
                        getTagValue(element, "keterangan")
                );

                services.add(service);
            }
        }

        return services;
    }

    public void addService(Service service) {

        Document document = XMLHelper.loadDocument(FILE_PATH);

        Element root = XMLHelper.getRoot(document);

        if (root == null) {
            root = XMLHelper.createRoot(document, ROOT);
        }

        Element serviceElement = document.createElement("service");

        appendChild(document, serviceElement, "id", service.getId());
        appendChild(document, serviceElement, "laptopId", service.getLaptopId());
        appendChild(document, serviceElement, "tanggalMasuk", service.getTanggalMasuk());
        appendChild(document, serviceElement, "status", service.getStatus());
        appendChild(document, serviceElement, "biaya", String.valueOf(service.getBiaya()));
        appendChild(document, serviceElement, "keterangan", service.getKeterangan());

        root.appendChild(serviceElement);

        XMLHelper.saveDocument(document, FILE_PATH);
    }

    public void updateService(Service updatedService) {

        Document document = XMLHelper.loadDocument(FILE_PATH);

        Element root = XMLHelper.getRoot(document);

        if (root == null) {
            return;
        }

        NodeList nodeList = root.getElementsByTagName("service");

        for (int i = 0; i < nodeList.getLength(); i++) {

            Element element = (Element) nodeList.item(i);

            String id = getTagValue(element, "id");

            if (id.equals(updatedService.getId())) {

                setTagValue(element, "laptopId", updatedService.getLaptopId());
                setTagValue(element, "tanggalMasuk", updatedService.getTanggalMasuk());
                setTagValue(element, "status", updatedService.getStatus());
                setTagValue(element, "biaya", String.valueOf(updatedService.getBiaya()));
                setTagValue(element, "keterangan", updatedService.getKeterangan());

                break;
            }
        }

        XMLHelper.saveDocument(document, FILE_PATH);
    }

    public void deleteService(String id) {

        Document document = XMLHelper.loadDocument(FILE_PATH);

        Element root = XMLHelper.getRoot(document);

        if (root == null) {
            return;
        }

        NodeList nodeList = root.getElementsByTagName("service");

        for (int i = 0; i < nodeList.getLength(); i++) {

            Element element = (Element) nodeList.item(i);

            String serviceId = getTagValue(element, "id");

            if (serviceId.equals(id)) {

                root.removeChild(element);

                break;
            }
        }

        XMLHelper.saveDocument(document, FILE_PATH);
    }

    public Service findById(String id) {

        for (Service service : getAllServices()) {

            if (service.getId() != null && service.getId().equals(id)) {

                return service;
            }
        }

        return null;
    }

    private void appendChild(Document document, Element parent, String tagName, String value) {

        Element child = document.createElement(tagName);

        child.setTextContent(value != null ? value : "");

        parent.appendChild(child);
    }

    private String getTagValue(Element parent, String tagName) {

        NodeList nodeList = parent.getElementsByTagName(tagName);

        if (nodeList.getLength() > 0) {

            Node node = nodeList.item(0);

            return node.getTextContent();
        }

        return "";
    }

    private void setTagValue(Element parent, String tagName, String value) {

        NodeList nodeList = parent.getElementsByTagName(tagName);

        if (nodeList.getLength() > 0) {

            nodeList.item(0).setTextContent(value != null ? value : "");

        } else {

            appendChild(parent.getOwnerDocument(), parent, tagName, value);
        }
    }

    private double parseBiaya(String value) {

        try {

            return Double.parseDouble(value);

        } catch (NumberFormatException e) {

            return 0;
        }
    }

}
