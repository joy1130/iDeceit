package gpstracedata;

import java.util.List;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: ns
 * Date: Sep 21, 2014
 * Time: 4:35:02 AM
 * To change this template use File | Settings | File Templates.
 */
public class SensitiveNodes {

    List<SensitiveNode> sensitiveNodes = new ArrayList<SensitiveNode>();

    boolean isSensitive(Place p, int t) {
        for(SensitiveNode sn : sensitiveNodes) {
            if(sn.place.id == p.id && sn.fromTime <= t && sn.toTime >=t)
                return true;
        }
        return false;
    }

    class SensitiveNode{
        Place place;
        int fromTime;  // minute of the day
        int toTime;    // minute of the day

        SensitiveNode(Place place, int fromTime, int toTime) {
            this.place = place;
            this.fromTime = fromTime;
            this.toTime = toTime;
        }
    }
}
