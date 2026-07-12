package Repository;

import java.util.ArrayList;
import java.util.List;

import model.Customer;
import util.XMLHelper;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class CustomerRepository {

    private static final String FILE_PATH = "xml/customer.xml";
    private static final String ROOT = "customers";

    // READ ALL
    public List<Customer> getAllCustomers() {

        List<Customer> customers = new ArrayList<>();

        Document document = XMLHelper.loadDocument(FILE_PATH);

        if (document == null) {
            return customers;
        }

        Element root = XMLHelper.getRoot(document);

        if (root == null) {
            return customers;
        }

        NodeList nodeList = root.getElementsByTagName("customer");

        for (int i = 0; i < nodeList.getLength(); i++) {

            Node node = nodeList.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {

                Element element = (Element) node;

                Customer customer = new Customer();

                customer.setId(
                        element.getElementsByTagName("id")
                                .item(0).getTextContent());

                customer.setNama(
                        element.getElementsByTagName("nama")
                                .item(0).getTextContent());

                customer.setNoHP(
                        element.getElementsByTagName("noHP")
                                .item(0).getTextContent());

                customer.setAlamat(
                        element.getElementsByTagName("alamat")
                                .item(0).getTextContent());

                customers.add(customer);
            }
        }

        return customers;
    }

    // CREATE
    public void addCustomer(Customer customer) {

        Document document = XMLHelper.loadDocument(FILE_PATH);

        Element root = XMLHelper.getRoot(document);

        if (root == null) {
            root = XMLHelper.createRoot(document, ROOT);
        }

        Element customerElement = document.createElement("customer");

        Element id = document.createElement("id");
        id.setTextContent(customer.getId());

        Element nama = document.createElement("nama");
        nama.setTextContent(customer.getNama());

        Element noHP = document.createElement("noHP");
        noHP.setTextContent(customer.getNoHP());

        Element alamat = document.createElement("alamat");
        alamat.setTextContent(customer.getAlamat());

        customerElement.appendChild(id);
        customerElement.appendChild(nama);
        customerElement.appendChild(noHP);
        customerElement.appendChild(alamat);

        root.appendChild(customerElement);

        XMLHelper.saveDocument(document, FILE_PATH);
    }

    // UPDATE
    public void updateCustomer(Customer customer) {

        Document document = XMLHelper.loadDocument(FILE_PATH);

        Element root = XMLHelper.getRoot(document);

        if (root == null) {
            return;
        }

        NodeList nodeList = root.getElementsByTagName("customer");

        for (int i = 0; i < nodeList.getLength(); i++) {

            Element element = (Element) nodeList.item(i);

            String id = element.getElementsByTagName("id")
                    .item(0).getTextContent();

            if (id.equals(customer.getId())) {

                element.getElementsByTagName("nama")
                        .item(0)
                        .setTextContent(customer.getNama());

                element.getElementsByTagName("noHP")
                        .item(0)
                        .setTextContent(customer.getNoHP());

                element.getElementsByTagName("alamat")
                        .item(0)
                        .setTextContent(customer.getAlamat());

                break;
            }
        }

        XMLHelper.saveDocument(document, FILE_PATH);
    }

    // DELETE
    public void deleteCustomer(String idCustomer) {

        Document document = XMLHelper.loadDocument(FILE_PATH);

        Element root = XMLHelper.getRoot(document);

        if (root == null) {
            return;
        }

        NodeList nodeList = root.getElementsByTagName("customer");

        for (int i = 0; i < nodeList.getLength(); i++) {

            Element element = (Element) nodeList.item(i);

            String id = element.getElementsByTagName("id")
                    .item(0).getTextContent();

            if (id.equals(idCustomer)) {

                root.removeChild(element);

                break;
            }
        }

        XMLHelper.saveDocument(document, FILE_PATH);
    }

    // SEARCH BY ID
    public Customer getCustomerById(String idCustomer) {

        List<Customer> customers = getAllCustomers();

        for (Customer customer : customers) {

            if (customer.getId().equals(idCustomer)) {

                return customer;

            }

        }

        return null;
    }

}
