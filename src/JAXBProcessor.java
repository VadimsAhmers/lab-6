import generated.Node;
import generated.Osm;
import generated.Tag;
import generated.Way;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.*;

public class JAXBProcessor {
    Osm osm;
    List<String> busStopList = new ArrayList<>();
    Map<String, StreetData> streetMap = new TreeMap<String, StreetData>();

    void unmarshal(String fileName) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance("generated");
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        osm = (Osm) unmarshaller.unmarshal(new File(fileName));

        for (Object o: osm.getBoundOrUserOrPreferences()){
            boolean isBusStop = false;
            String proposalBusStopName = null;
            String highway = null;
            String streetName = null;

            if (o.getClass()== Node.class){
                Node node = (Node) o;
                for (Tag tag : node.getTag()){
                    if (tag.getK().equals("highway")&&(tag.getV().equals("bus_stop"))) isBusStop = true;
                    if (tag.getK().equals("name")) proposalBusStopName = tag.getV();
                    if ((isBusStop)&&(proposalBusStopName!=null)) {
                        busStopList.add(proposalBusStopName);
                        break;
                    }
                }
            }else
                if (o.getClass()== Way.class){
                Way way = (Way) o;
                for (Object o1 : way.getRest()){
                    if (o1.getClass().equals(Tag.class)){
                        Tag tag = (Tag) o1;
                        if (tag.getK().equals("highway")) highway = tag.getV();
                        if (tag.getK().equals("name")) streetName = tag.getV();
                    }

                }
                    if ((highway!=null)&&(streetName!=null)) {
                        if (streetMap.containsKey(streetName)){
                            streetMap.get(streetName).highways.add(highway);
                        }else {
                            StreetData streetData = new StreetData(streetName, highway);
                            streetMap.put(streetName, streetData);
                        }
                    }

                }
        }
        //System.out.println(busStopList);
        //System.out.println(streetMap);


    }
}
