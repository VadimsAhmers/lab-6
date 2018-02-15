import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StreetData that = (StreetData) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(highways, that.highways);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, highways);
    }
}
