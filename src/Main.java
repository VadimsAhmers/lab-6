public class Main {
    public static void main(String[] args){
        String macSVGPath = "/Users/macbook/Downloads/__FILES_FOR_LABS/clouds.svg";
        String schemaForSVG = "";

        XMLProcessor XMLProcessor = new XMLProcessor(macSVGPath,schemaForSVG);

            XMLProcessor.process();
    }
}
