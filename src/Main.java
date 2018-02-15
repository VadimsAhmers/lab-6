import javax.xml.bind.JAXBException;

public class Main {
    public static void main(String[] args){
        String macSVGPath = "/Users/macbook/Downloads/__FILES_FOR_LABS/clouds.svg";
        String schemaForSVG = "";
        String xmlFileName = "/Users/macbook/Downloads/__FILES_FOR_LABS/UfaCenter.xml";

        // XMLProcessor XMLProcessor = new XMLProcessor(macSVGPath,schemaForSVG);
        // XMLProcessor.process();

        StAXProcessor stAXProcessor = new StAXProcessor();
        stAXProcessor.process();

        JAXBProcessor jaxbProcessor = new JAXBProcessor();
        try {
            jaxbProcessor.unmarshal(xmlFileName);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        System.out.println(
                (stAXProcessor.busStopList.equals(jaxbProcessor.busStopList)?
                        "Списки остановок совпадают" :
                        "Списки остановок НЕ совпадают" ));

        System.out.println(
                (stAXProcessor.streetMap.equals(jaxbProcessor.streetMap)?
                        "Списки улиц совпадают" :
                        "Списки улиц НЕ совпадают" ));
    }
}
