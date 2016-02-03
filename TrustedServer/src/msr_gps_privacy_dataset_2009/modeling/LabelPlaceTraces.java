package msr_gps_privacy_dataset_2009.modeling;

/**
 * Created by IntelliJ IDEA.
 * User: ns
 * Date: Sep 24, 2014
 * Time: 1:28:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class LabelPlaceTraces {

    int id;
    String label;
    int timeInMin;

    public LabelPlaceTraces(int id, String label, int timeInMin) {
        this.id = id;
        this.label = label;
        this.timeInMin = timeInMin;
    }
}
