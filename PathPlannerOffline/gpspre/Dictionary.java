package gpstracedata;

import java.util.List;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: ns
 * Date: Sep 20, 2014
 * Time: 10:55:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class Dictionary {
    static List<Place> places = new ArrayList<Place>();

    Place getPlace(double lat, double lon) {
        Place retP=null;

        for(Place p : places) {
            if(dist(p.lat, p.lon, lat, lon) < Utils.CLUSTER_RADIUS) {
                return p;
            }
        }

        return null;
        
    }

        private int dist(double lat1, double lng1, double lat2, double lng2) {

        double earthRadius = 6371000; //kilometers
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double dist = (double) (earthRadius * c);

        return (int) dist;

    }
}
