package msr_gps_privacy_dataset_2009.modeling;

import msr_gps_privacy_dataset_2009.dictionary.Place;

/**
 * Created by IntelliJ IDEA.
 * User: ns
 * Date: Sep 24, 2014
 * Time: 12:50:21 PM
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
