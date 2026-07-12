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
     * Mengarahkan path relatif XML ke root project LaptopCare, bukan ke folder
     * tempat Java kebetulan dijalankan.
     */
    public static File resolveFile(String filePath) {

        File directFile = new File(filePath);

        if (directFile.isAbsolute()) {
            return directFile;
        }

        File existingFile = findExistingFile(filePath);
        if (existingFile != null) {
            return existingFile;
        }

        File codeProjectRoot = findProjectRootFromCodeSource();
        if (codeProjectRoot != null) {
            return new File(codeProjectRoot, filePath);
        }

        File workingDirectory = new File(System.getProperty("user.dir"))
                .getAbsoluteFile();

        File workingProjectRoot = findProjectRootFromWorkingDirectory(workingDirectory);
        if (workingProjectRoot != null) {
            return new File(workingProjectRoot, filePath);
        }

        File nestedProjectRoot = findProjectRootInChildren(workingDirectory, 4);
        if (nestedProjectRoot != null) {
            return new File(nestedProjectRoot, filePath);
        }

        return new File(workingDirectory, filePath);

    }

    private static File findExistingFile(String filePath) {
        File workingDirectory = new File(System.getProperty("user.dir"))
                .getAbsoluteFile();

        File file = findFileInAncestors(workingDirectory, filePath);
        if (file != null) {
            return file;
        }

        File codeProjectRoot = findProjectRootFromCodeSource();
        if (codeProjectRoot != null) {
            File candidate = new File(codeProjectRoot, filePath);
            if (candidate.exists()) {
                return candidate;
            }
        }

        return null;
    }

    private static File findFileInAncestors(File directory, String filePath) {
        File current = directory;

        while (current != null) {
            File candidate = new File(current, filePath);
            if (candidate.exists()) {
                return candidate;
            }
            current = current.getParentFile();
        }

        return null;
    }

    private static File findProjectRootFromWorkingDirectory(File directory) {
        File current = directory;

        while (current != null) {
            if (isProjectRoot(current)) {
                return current;
            }
            current = current.getParentFile();
        }

        return null;
    }

    private static File findProjectRootInChildren(File directory, int depth) {
        if (directory == null || depth < 0) {
            return null;
        }

        if (isProjectRoot(directory)) {
            return directory;
        }

        File[] children = directory.listFiles(File::isDirectory);
        if (children == null) {
            return null;
        }

        for (File child : children) {
            if (isProjectRoot(child)) {
                return child;
            }
        }

        for (File child : children) {
            File found = findProjectRootInChildren(child, depth - 1);
            if (found != null) {
                return found;
            }
        }

        return null;
    }

    private static File findProjectRootFromCodeSource() {

        try {

            File location = new File(XMLHelper.class
                    .getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .toURI());

            File base = location.isFile()
                    ? location.getParentFile()
                    : location;

            if (isProjectRoot(base)) {
                return base;
            }

            File parent = base.getParentFile();

            if (isProjectRoot(parent)) {
                return parent;
            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        return null;

    }

    private static boolean isProjectRoot(File directory) {

        return directory != null
                && directory.isDirectory()
                && new File(directory, "src").isDirectory();

    }

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

            File file = resolveFile(filePath);

            if (!file.exists() || file.length() == 0) {

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

            File file = resolveFile(filePath);
            File parent = file.getParentFile();

            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }

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
                    new StreamResult(file);

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
