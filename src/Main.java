import org.xml.sax.SAXException;

public class Main {
    public static void main(String[] args) {
        String macSVGPath = "/Users/macbook/Downloads/__FILES_FOR_LABS/clouds.svg";

        SVGDOMProcessor svgdomProcessor = new SVGDOMProcessor(macSVGPath,"");
        try {
            svgdomProcessor.process();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }
}
