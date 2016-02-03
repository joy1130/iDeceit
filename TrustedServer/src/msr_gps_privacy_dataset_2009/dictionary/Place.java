package msr_gps_privacy_dataset_2009.dictionary;

/**
 * User: ns
 * Date: Sep 19, 2014
 */
public class Place {
     public int id;
    public String label;
    public double lat;
    public double lon;


    public Place(int id, String label, double lat, double lon) {
        this.id = id;
        this.label = label;
        this.lat = lat;
        this.lon = lon;
    }
}
