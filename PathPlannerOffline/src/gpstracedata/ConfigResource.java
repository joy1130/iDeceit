package gpstracedata;

/**
 * Created by IntelliJ IDEA.
 * User: ns
 * Date: Sep 21, 2014
 * Time: 5:57:08 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ConfigResource {
    static public String TYPE = "gps"; // "label"
    public static int T = 50;
    public static int N = 35;
    public static int allPlaceIndex = T-1;
    public static int allTimeIndex = N-1;

    public static int CLUSTER_RADIUS = 300;
}
