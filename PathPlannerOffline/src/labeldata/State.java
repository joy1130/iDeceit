package labeldata;

/**
 * Created by IntelliJ IDEA.
 * User: ns
 * Date: Sep 20, 2014
 * Time: 10:53:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class State {
    Place place;
    int timeIndex;

    public State(Place place, int timeIndex) {
        this.place = place;
        this.timeIndex = timeIndex;
    }
}
