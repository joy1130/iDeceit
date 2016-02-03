package gpstracedata;

/**
 * Created by IntelliJ IDEA.
 * User: ns
 * Date: Sep 20, 2014
 * Time: 10:53:33 PM
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
