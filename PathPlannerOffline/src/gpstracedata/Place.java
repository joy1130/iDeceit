package gpstracedata;

/**
 * User: ns
 * Date: Sep 20, 2014
 */
public class Place {

    int id;
    String label;
    double lat;
    double lon;

    int radius;

    public Place(int id, String label, double lat, double lon, int radius) {
        this.id = id;
        this.label = label;
        this.lat = lat;
        this.lon = lon;
        this.radius = radius;
    }
}
