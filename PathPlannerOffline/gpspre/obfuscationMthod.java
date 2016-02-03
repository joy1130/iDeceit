package gpstracedata;

import java.util.*;

/**
 * User: ns
 * Date: Sep 20, 2014
 */
public class obfuscationMthod {

    Dictionary dictionary = new Dictionary();
    SensitiveNodes sensitiveNodes = new SensitiveNodes();

    int DT_MIN[][] = new int[Utils.T][Utils.N];
    int DT_MAX[][] = new int[Utils.T][Utils.N];

    int R[][][] = new int[Utils.T][Utils.N][Utils.N];

    int M[][][][] = new int[Utils.T][Utils.N][Utils.T][Utils.N];

    List<Event> convertGPSTraceToEventList(List<GPSTrace> traces) {

        List<Event> eventList = new ArrayList<Event>();
        Place preLoc = null;

        for (int i = 0; i < traces.size(); i++) {
            Place curPlace = dictionary.getPlace(traces.get(i).latitude, traces.get(i).longitude);

            if (curPlace == null && preLoc != null) { // exit
                Event event = new Event(new State(curPlace, traces.get(i).timeInMin / 30), "exit", traces.get(i).timeInMin);
                eventList.add(event);
            }

            /*
            Entry
             */
            if ((preLoc == null && curPlace != null) || (preLoc != null && curPlace != null && preLoc.id != curPlace.id)) {
                Event event = new Event(new State(curPlace, traces.get(i).timeInMin / 30), "entry", traces.get(i).timeInMin);
                eventList.add(event);
            }
        }

        return eventList;
    }

    List<Event> doObfuscation(List<Event> input) {
        List<Event> output = new ArrayList<Event>();

        Queue<StateNode> Q = new PriorityQueue<StateNode>();

        for (int i = 0; i < input.size(); i++) {
            Event event = input.get(i);
            if (event.type.equals("entry")) {
                if (sensitiveNodes.isSensitive(event.state.place, event.timestamp)
                        || ((i > 0) && input.get((i - 1)) != output.get(output.size() - 1))) {

                    List<Event> altEventList = getAlternativeEventPath(output.get(output.size() - 1).state, output.get(output.size() - 1).timestamp, R, M, DT_MAX);


                } else {
                    output.add(event);
                }

            } else if (event.type.equals("exit")) {
                if (((i > 0) && input.get((i - 1)) != output.get(output.size() - 1))) {

                    List<Event> altEventList = getAlternativeEventPath(output.get(output.size() - 1).state, output.get(output.size() - 1).timestamp, R, M, DT_MAX);


                } else {
                    output.add(event);
                }
            }
        }

        return output;

    }


    ////// PATH PLANNER /////////////////
    Map<State, State> pre = new HashMap<State, State>();
    Map<State, Integer> t_en = new HashMap<State, Integer>();

    List<Event> getAlternativeEventPath(State x_t, int t, int[][][] R, int[][][][] M, int[][] D) {
        State nextSafeState = getPredictedNextSafeNode(x_t, t, R, M, D);
        int tDest = 0;//todo
        Queue<StateNode> Q = new PriorityQueue<StateNode>();
        StateNode u = new StateNode(x_t, 0);
        pre.put(x_t, null);
        t_en.put(x_t, t - D[x_t.timeIndex][x_t.place.id]);
        Q.add(u);

        while (!Q.isEmpty()) {
            StateNode xTemp = Q.poll();
            if (xTemp.equals(nextSafeState))
                break;

            for (Place p : Dictionary.places) {
                int tTemp = t_en.get(xTemp) + R[xTemp.x.timeIndex][xTemp.x.place.id][p.id];
                if (tTemp > tDest + 30)
                    continue;
                if (M[xTemp.x.timeIndex][xTemp.x.place.id][tau(tTemp)][p.id] > 0) {
                    StateNode x_next = new StateNode(new State(p, tau(tTemp)), xTemp.value + M[xTemp.x.timeIndex][xTemp.x.place.id][tau(tTemp)][p.id]);
                    t_en.put(x_next.x, tTemp);
                    pre.put(x_next.x, xTemp.x);
                    Q.add(x_next);
                }

            }
        }
        return getPath(x_t);
    }

    State getPredictedNextSafeNode(State x, int t_entry, int[][][] R, int[][][][] M, int[][] D) {
        State nextSafeState;

        if (!isSensitive(x)) {
            return x;
        }
        int tExit = t_entry + D[tau(t_entry)][x.place.id];
        double maxProbability = 0;
        Place nextPlace = null;
        int tNextPlaceEnter = 0;

        for (Place p : Dictionary.places) {
            int tTemp = tExit + R[x.timeIndex][x.place.id][p.id];
            if (M[x.timeIndex][x.place.id][tau(tTemp)][p.id] > maxProbability) {
                maxProbability = M[x.timeIndex][x.place.id][tau(tTemp)][p.id];
                nextPlace = p;
                tNextPlaceEnter = tTemp;
            }

        }
        nextSafeState = new State(nextPlace, tau(tNextPlaceEnter));

        return getPredictedNextSafeNode(nextSafeState, tNextPlaceEnter, R, M, D);
    }


    private boolean isSensitive(State x) {

        return sensitiveNodes.isSensitive(x.place, x.timeIndex * 30);
    }

    List<Event> getPath(State x) {
        if (x == null) return null;
        List<Event> eventList = new ArrayList<Event>();// getpath(pre[x]);
/*        eventList.add(makeEvent(x.id, x.place, t_en[x], ‘Enter’));
        eventList.add(makeEvent(x.id, x.place, t_ex[x], ‘Exit’));*/
        return eventList;
    }


    class StateNode implements Comparable<StateNode> {
        State x;
        int value;

        StateNode(State x, int value) {
            this.x = x;
            this.value = value;
        }

        public int compareTo(StateNode o) {
            return this.value - o.value;
        }
    }

    public static int tau(int t_entry) {
        return t_entry / 30;

    }
}
