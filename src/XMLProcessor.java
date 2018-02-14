import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.FileWriter;
import java.io.IOException;

public class XMLProcessor {
    private String fileName;
    private String schemaName;

    XMLProcessor(String fileName, String schemaName) {
        this.fileName = fileName;
        this.schemaName = schemaName;
    }

    private Document readXML() throws SAXException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        //System.out.println("documentBuilderFactory = " + documentBuilderFactory);

        documentBuilderFactory.setNamespaceAware(false);
        documentBuilderFactory.setIgnoringElementContentWhitespace(true);

        //Source schemaSource = new StreamSource(schemaName);
        //SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI);
        //Schema schema = schemaFactory.newSchema(schemaSource);
        //documentBuilderFactory.setSchema(schema);

        DocumentBuilder documentBuilder = null;
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            return null;
        }

        try {
            return documentBuilder.parse(fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    void process() {
        try {
            Document document = readXML();
            findPrintFigures(document);
            pointCircleCentre(document);
            changeColor(document);
            saveXML(document);

        } catch (SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    private void findPrintFigures(Document document) {

        Element root = document.getDocumentElement();
        NodeList rectangles = root.getElementsByTagName("rect");
        NodeList circles = root.getElementsByTagName("circle");

        for (int i = 0; i < rectangles.getLength(); i++) {

            System.out.printf("Прямоугольник №%d: ", i + 1);
            NamedNodeMap attributes = rectangles.item(i).getAttributes();

            for (int j = 0; j < attributes.getLength(); j++) {
                Node attribute = attributes.item(j);
                System.out.print(attribute.getNodeName() + "=" + attribute.getNodeValue() + " ");
            }
            System.out.println();
        }
        for (int i = 0; i < circles.getLength(); i++) {

            System.out.printf("Окружность №%d: ", i + 1);
            NamedNodeMap attributes = circles.item(i).getAttributes();

            for (int j = 0; j < attributes.getLength(); j++) {
                Node attribute = attributes.item(j);
                System.out.print(attribute.getNodeName() + "=" + attribute.getNodeValue() + " ");
            }
            System.out.println();
        }
    }

    private void pointCircleCentre(Document document) {
        Element root = document.getDocumentElement();
        NodeList circles = root.getElementsByTagName("circle");

        for (int i = 0; i < circles.getLength(); i++) {
            Node circle = circles.item(i);
            Node parentNode = circle.getParentNode();
            Node center = circle.cloneNode(true);
            NamedNodeMap centerAttributes = center.getAttributes();
            centerAttributes.getNamedItem("r").setNodeValue("1");
            //Node next= center.getNextSibling();
            //if (next != null)
            parentNode.insertBefore(center, circle.getNextSibling());
            i++;

            System.out.print("Центр окружности: ");
            for (int j = 0; j < centerAttributes.getLength(); j++) {
                System.out.print(centerAttributes.item(j));
            }
            System.out.println();
        }
    }

    private void changeColor(Document document) {
        Element root = document.getDocumentElement();
        NodeList rectangles = root.getElementsByTagName("rect");
        NodeList paths = root.getElementsByTagName("path");

        for (int i = 0; i < rectangles.getLength(); i++) {
            Element rectangle = (Element) rectangles.item(i);
            rectangle.setAttribute("style", "fill:red");
        }

        for (int i = 0; i < paths.getLength(); i++) {
            Element path = (Element) paths.item(i);
            path.setAttribute("style", "fill:black");
        }

    }

    private void saveXML(Document document) throws IOException {
        Result result = new StreamResult(new FileWriter("result.xml"));
        DOMSource domSource = new DOMSource(document);

        Transformer transformer;
        try {
            transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(domSource, result);
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }
}
