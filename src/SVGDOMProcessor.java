import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.IOException;

public class SVGDOMProcessor {
    private String fileName;
    private String schemaName;

    SVGDOMProcessor(String fileName, String schemaName){
        this.fileName = fileName;
        this.schemaName = schemaName;
    }

    private Document readSVGtoDOM() throws SAXException{
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        System.out.println("dbf = " + dbf);

        dbf.setNamespaceAware(false);
        dbf.setIgnoringElementContentWhitespace(true);

        /*Source schemaSource = new StreamSource(schemaName);
        SchemaFactory schemaFactory = SchemaFactory
                .newInstance(XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI);
        Schema schema = schemaFactory.newSchema(schemaSource);
        dbf.setSchema(schema);*/

        DocumentBuilder documentBuilder = null;
        try {
            documentBuilder = dbf.newDocumentBuilder();
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

    void process() throws SAXException{
        Document document = readSVGtoDOM();
    }
}
