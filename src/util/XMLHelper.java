package util;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XMLHelper {

    /**
     * Membuat document XML baru.
     */
    public static Document createDocument() {

        try {

            DocumentBuilderFactory factory =
                    DocumentBuilderFactory.newInstance();

            DocumentBuilder builder =
                    factory.newDocumentBuilder();

            return builder.newDocument();

        } catch (Exception e) {

            e.printStackTrace();

        }

        return null;

    }

    /**
     * Membaca file XML.
     */
    public static Document loadDocument(String filePath) {

        try {

            File file = new File(filePath);

            if (!file.exists()) {

                return createDocument();

            }

            DocumentBuilderFactory factory =
                    DocumentBuilderFactory.newInstance();

            DocumentBuilder builder =
                    factory.newDocumentBuilder();

            Document document =
                    builder.parse(file);

            document.getDocumentElement().normalize();

            return document;

        } catch (Exception e) {

            e.printStackTrace();

        }

        return null;

    }
        /**
     * Menyimpan Document ke file XML.
     */
    public static void saveDocument(Document document, String filePath) {

        try {

            TransformerFactory transformerFactory =
                    TransformerFactory.newInstance();

            Transformer transformer =
                    transformerFactory.newTransformer();

            transformer.setOutputProperty(
                    OutputKeys.INDENT, "yes");

            transformer.setOutputProperty(
                    "{http://xml.apache.org/xslt}indent-amount", "4");

            DOMSource source =
                    new DOMSource(document);

            StreamResult result =
                    new StreamResult(new File(filePath));

            transformer.transform(source, result);

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    /**
     * Membuat root element apabila belum ada.
     */
    public static Element createRoot(Document document, String rootName) {

        Element root = document.createElement(rootName);

        document.appendChild(root);

        return root;

    }

    /**
     * Mengambil root element.
     */
    public static Element getRoot(Document document) {

        return document.getDocumentElement();

    }
        /**
     * Membuat element baru.
     */
    public static Element createElement(Document document, String elementName) {

        return document.createElement(elementName);

    }

    /**
     * Menambahkan child ke parent.
     */
    public static void appendChild(Element parent, Element child) {

        parent.appendChild(child);

    }

}