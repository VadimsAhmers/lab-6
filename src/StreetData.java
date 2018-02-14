import java.util.ArrayList;
import java.util.List;

public class StreetData {
    String name;
    List<String> highways;

    public StreetData(String name, String highway) {
        this.name = name;
        this.highways =new ArrayList<>();
        highways.add(highway);
    }
    @Override
    public String toString(){
        return name + "(" + highways + ")";
    }
}
