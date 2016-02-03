package msr_gps_privacy_dataset_2009.modeling;

import msr_gps_privacy_dataset_2009.dictionary.Place;

/**
 * Created by IntelliJ IDEA.
 * User: ns
 * Date: Sep 24, 2014
 * Time: 12:55:47 PM
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
