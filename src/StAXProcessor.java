import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class StAXProcessor {

    void process() {
        String xsdFileName = "/Users/macbook/Downloads/__FILES_FOR_LABS/osm.xsd";
        String checkingFileName = "/Users/macbook/Downloads/__FILES_FOR_LABS/UfaCenter.xml";

        checkSchema(xsdFileName, checkingFileName);
        readUsingStream(checkingFileName);

    }

    private void readUsingStream(String fileName) {
        XMLInputFactory xmlInputFactory = XMLInputFactory.newFactory().newInstance();
        try {
            XMLStreamReader xmlStreamReader = xmlInputFactory.createXMLStreamReader(new FileReader(fileName));

            List<String> busStopList = new ArrayList<>();
            Map<String, StreetData> streetMap = new TreeMap<String, StreetData>();
            int count = 0;

            while (xmlStreamReader.hasNext()) {
                boolean inNode = false;

                int code = xmlStreamReader.next();

                //ищем автобусные остановки
                if ((code == XMLEvent.START_ELEMENT) && (xmlStreamReader.getLocalName().equals("node"))) {
                    inNode = true;
                    boolean isBusStop = false;
                    String proposalBusStopName = null;
                    while (inNode) {
                        int code2 = xmlStreamReader.next();
                        if (code2 == XMLEvent.END_ELEMENT && xmlStreamReader.getLocalName().equals("node")) {
                            inNode = false;
                        }
                        if ((code2 == XMLEvent.START_ELEMENT) && (xmlStreamReader.getLocalName().equals("tag"))) {
                            String k = xmlStreamReader.getAttributeValue("", "k");
                            String v = xmlStreamReader.getAttributeValue("", "v");
                            if (v.equals("bus_stop")) isBusStop = true;
                            if (k.equals("name")) proposalBusStopName = xmlStreamReader.getAttributeValue(1);
                        }
                    }
                    if (isBusStop && (proposalBusStopName != null)) busStopList.add(proposalBusStopName);
                }
                //ищем улицы
                boolean inWay = false;
                String highway = null;
                String streetName = null;
                if ((code == XMLEvent.START_ELEMENT) && (xmlStreamReader.getLocalName().equals("way"))) inWay = true;
                while (inWay) {
                    int code2 = xmlStreamReader.next();
                    if ((code2 == XMLEvent.START_ELEMENT) && (xmlStreamReader.getLocalName().equals("tag"))) {
                        String s = xmlStreamReader.getAttributeValue(0);
                        if (s.equals("highway")) {
                            highway = xmlStreamReader.getAttributeValue(1);
                        } else if (s.equals("name")) {
                            streetName = xmlStreamReader.getAttributeValue(1);
                        }
                    }
                    if ((code2 == XMLEvent.END_ELEMENT) && (xmlStreamReader.getLocalName().equals("way")))
                        inWay = false;
                }
                if ((highway != null) && (streetName != null)) {
                    if (streetMap.containsKey(streetName)) {
                        streetMap.get(streetName).highways.add(highway);
                    } else streetMap.put(streetName, new StreetData(streetName, highway));
                }
            }
            System.out.println(busStopList);
            System.out.println("Список улиц");
            for (Map.Entry<String, StreetData> entry : streetMap.entrySet()) {
                System.out.println(entry.getValue().name + " " + entry.getValue().highways.size() + entry.getValue().highways);
            }
        } catch (XMLStreamException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void checkSchema(String xsdFileName, String checkingFileName) {
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        try {
            Schema schema = schemaFactory.newSchema(new File(xsdFileName));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new File(checkingFileName)));
            System.out.printf("Файл %s соответствует схеме %s\n", checkingFileName, xsdFileName);
        } catch (SAXException | IOException e) {
            e.printStackTrace();
        }
    }
}
