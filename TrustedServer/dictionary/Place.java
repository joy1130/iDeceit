package dictionary;

/**
 * User: ns
 * Date: Sep 19, 2014
 */
public class Place {

    double lat;
    double lon;
    int id;
    String label;

    public Place(String label, double lat, double lon, int id) {
        this.label = label;
        this.lat = lat;
        this.lon = lon;
        this.id = id;
    }
}
