package msr_gps_privacy_dataset_2009.modeling;

/**
 * Created by IntelliJ IDEA.
 * User: ns
 * Date: Sep 24, 2014
 * Time: 12:49:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class Event {
       State state;
    String type;
    int timestamp;

    public Event(State state, String type, int timestamp) {
        this.state = state;
        this.type = type;
        this.timestamp = timestamp;
    }
}
