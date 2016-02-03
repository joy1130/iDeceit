package labeldata;

/**
 * Created by IntelliJ IDEA.
 * User: ns
 * Date: Sep 21, 2014
 * Time: 11:22:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class SensitivePlace {
    public Place place;
    public int startTimeMin;
    public int endTimeMin;

    public SensitivePlace(Place place, int startTimeMin, int endTimeMin) {
        this.place = place;
        this.startTimeMin = startTimeMin;
        this.endTimeMin = endTimeMin;
    }
}
