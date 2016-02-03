package gpstracedata;

/**
 * Created by IntelliJ IDEA.
 * User: ns
 * Date: Sep 20, 2014
 * Time: 10:53:21 PM
 * To change this template use File | Settings | File Templates.
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
