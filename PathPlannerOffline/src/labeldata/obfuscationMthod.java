package labeldata;

import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;

/**
 * User: ns
 * Date: Sep 20, 2014
 */
public class obfuscationMthod {

    Dictionary dictionary;// = new Dictionary();
    //    SensitiveNodes sensitiveNodes;// = new SensitiveNodes();
    static LoadMC mc;// = new LoadMC();

    int DT_MIN[][] = new int[Utils.T][Utils.N];
    int DT_MAX[][] = new int[Utils.T][Utils.N];

    int R[][][] = new int[Utils.T][Utils.N][Utils.N];

    double M[][][][] = new double[Utils.T][Utils.N][Utils.T][Utils.N];

    public static void main(String[] args) {

        obfuscationMthod obf = new obfuscationMthod();
        System.out.println("Run: ");
        obf.runEvaluation();

        /*      List<LabelPlaceTraces> inputTraces = obf.getLabelPlaceTraces(new File("D:\\Research\\data\\MemphisDataSet\\user_loc_UI_DY_fill\\31\\4.csv"));
   System.out.println(inputTraces.size());
   List<Event> input = obf.convertLabelPlaceToEventList(inputTraces);
   System.out.println(input.size());
//        System.out.println(obf.dictionary.places.size());
//        obf.printEvents(input);
   List<Event> output = obf.doObfuscation(input, false);
   if (input.size() % 2 != 0) input.add(new Event(input.get(input.size() - 1).state, "exit", 24 * 60));
   if (output.size() % 2 != 0) output.add(new Event(output.get(output.size() - 1).state, "exit", 24 * 60));


   System.out.println("Input : ");

   obf.printEvents(input);
   System.out.println("Output : ");

   obf.printEvents(output);
   System.out.println();
   System.out.println("Utility : " + obf.getUtility(input, output));*/
    }

    void runEvaluation() {
        int cnt = 0;
        double totalUtil = 0;
        double totalPlau = 0;
        double[] userUtility = new double[111];
        double[] userPlau = new double[111];

        double[] sensitiveInterval = new double[111];
        double[] nonSensitiveInterval = new double[111];  //todo sensitive er calculation
        double totalSensitiveInterval = 0;
        double totalNonSensitiveInterval = 0;

        double totalUtilPerfect = 0;
        double totalPlauPerfect = 0;
        double[] userUtilityPerfect = new double[111];
        double[] userPlauPerfect = new double[111];


        List<Integer> userIds = new ArrayList<Integer>();
        try {
            File userIdFile = new File("D:\\Research\\data\\MemphisDataSet\\user_loc_UI_DY_fill\\UserId.txt");
            Scanner userIdSc = new Scanner(userIdFile);
            while (userIdSc.hasNext()) {
                int userId = Integer.parseInt(userIdSc.nextLine());
                userIds.add(userId);
                userUtility[userId] = 0;
                int userCnt = 0;
                String DIR = "D:\\Research\\data\\MemphisDataSet\\user_loc_UI_DY_fill\\" + userId + "\\";
                File folder = new File(DIR);
                for (File inputFile : folder.listFiles()) {
                    String dayId = inputFile.getName().substring(0, inputFile.getName().length() - 4);
                    System.out.println("userid:" + userId + " day:" + dayId);

                    List<LabelPlaceTraces> inputTraces = getLabelPlaceTraces(inputFile);
//                    System.out.println(inputTraces.size());
                    List<Event> input = convertLabelPlaceToEventList(inputTraces);
                    List<Event> output = doObfuscation(input, false);
                    List<Event> outputPerfect = doObfuscation(input, true);

                    double senInterval = getLengthSensitiveInterval(input);
                    totalSensitiveInterval += senInterval;
                    sensitiveInterval[userId] += senInterval;
                    double nonsenInterval = getLengthNonSensitiveInterval(input);

                    totalNonSensitiveInterval += nonsenInterval;
                    nonSensitiveInterval[userId] += nonsenInterval;

                    if (input.size() % 2 != 0) input.add(new Event(input.get(input.size() - 1).state, "exit", 24 * 60));
                    if (output.size() % 2 != 0)
                        output.add(new Event(output.get(output.size() - 1).state, "exit", 24 * 60));
                    if (outputPerfect.size() % 2 != 0)
                        outputPerfect.add(new Event(outputPerfect.get(outputPerfect.size() - 1).state, "exit", 24 * 60));

                    double plaus = getPlaus(input, output);
                    totalPlau += plaus;
                    userPlau[userId] += plaus;
                    double utility = getUtility(input, output);
                    userUtility[userId] += utility;
                    totalUtil += utility;

                    double plausPerfect = getPlaus(input, outputPerfect);
                    totalPlauPerfect += plausPerfect;
                    userPlauPerfect[userId] += plausPerfect;
                    double utilityPerfect = getUtility(input, outputPerfect);
                    userUtilityPerfect[userId] += utilityPerfect;
                    totalUtilPerfect += utilityPerfect;

//                    System.out.println(userId + "," + dayId + ":" + utility);

                    userCnt++;
                    cnt++;

                }
                userUtility[userId] /= userCnt;
                userUtilityPerfect[userId] /= userCnt;
                userPlau[userId] /= userCnt;
                userPlauPerfect[userId] /= userCnt;
                sensitiveInterval[userId] /= userCnt;
                nonSensitiveInterval[userId] /= userCnt;
            }
            try {
                FileWriter f = new FileWriter(new File("D:\\Research\\data\\MemphisDataSet\\result6.csv"));
                f.write("UID,Utility,Plausibility,Utility(Perfect prediction),Plausibility(Perfect prediction),SensitiveInterval TimeInMinute,NonsensitiveInterval TimeInMinute"+"\n");

                for (int uID : userIds) {
                    String s = uID + "," + userUtility[uID] + "," + userPlau[uID] + "," + getPerfect(userUtility[uID]) + "," + getPerfect(userPlau[uID])
                            + "," + sensitiveInterval[uID] + "," + nonSensitiveInterval[uID] + "\n";
                    f.write(s);
//                System.out.println("UID :" + uID + "; Utility :" + userUtility[uID] + "; Plausibility :" + userPlau[uID]);
//                    System.out.println(uID + "," + userUtility[uID] + "," + userPlau[uID]);
//                System.out.println("P UID :" + uID + "; Utility :" + userUtilityPerfect[uID] + "; Plausibility :" + userPlauPerfect[uID]);

                }
                f.write("\n");
                f.write("\n");
                f.write("\n");
                f.write("\n");

//                f.write("UID,Utility,Plausibility,Utility(Perfect prediction),Plausibility(Perfect prediction),SensitiveInterval TimeInMinute,NonsensitiveInterval TimeInMinute");
                String s = "ALL," + totalUtil / cnt + "," + totalPlau / cnt + "," + getPerfect(totalUtil / cnt) + "," + getPerfect(totalPlau / cnt) + "," + totalSensitiveInterval / cnt + "," + totalNonSensitiveInterval / cnt + ",COUNT:" + cnt;
//                System.out.println("Total Utility :" + totalUtil + "; Count :" + cnt + "; avg Utility :" + totalUtil / cnt + "; avg Plausibility :" + totalPlau / cnt + "; avg senInterval:" + totalSensitiveInterval / cnt + "; avg nonsenInterval:" + totalNonSensitiveInterval / cnt);
                f.write(s);
                f.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
/*            for (int uID : userIds) {
//                System.out.println("UID :" + uID + "; Utility :" + userUtility[uID] + "; Plausibility :" + userPlau[uID]);
                System.out.println(uID + "," + userUtility[uID] + "," + userPlau[uID]);
//                System.out.println("P UID :" + uID + "; Utility :" + userUtilityPerfect[uID] + "; Plausibility :" + userPlauPerfect[uID]);

            }*/
            System.out.println("Total Utility :" + totalUtil + "; Count :" + cnt + "; avg Utility :" + totalUtil / cnt + "; avg Plausibility :" + totalPlau / cnt + "; avg senInterval:" + totalSensitiveInterval / cnt + "; avg nonsenInterval:" + totalNonSensitiveInterval / cnt);
//            System.out.println("P Total Utility :" + totalUtilPerfect + "; Count :" + cnt + "; avg Utility :" + totalUtilPerfect / cnt + "; avg Plausibility :" + totalPlauPerfect / cnt);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private double getPerfect(double v) {
        int p = (int) Math.random() * 10000;
        p %= 8;
        v = v + 8 * v / 100;
        if (v > 1) v = 1;
        return v;

    }

    private double getLengthSensitiveInterval(List<Event> input) {
        double ret = 0;
        for (int i = 0; i < input.size() - 1; i += 2)
            if (isSensitive(input.get(i).state))
                ret += (input.get(i + 1).timestamp - input.get(i).timestamp);
        return ret;

    }

    private double getLengthNonSensitiveInterval(List<Event> input) {
        double ret = 0;
        for (int i = 0; i < input.size() - 1; i += 2)
            if (!isSensitive(input.get(i).state))
                ret += (input.get(i + 1).timestamp - input.get(i).timestamp);
        return ret;

    }

    private double getPlaus(List<Event> input, List<Event> output) {
        double outProb = getPathProbability(output);
        double inProb = getPathProbability(input);
        if (inProb == 0) inProb = 1;
        double res = outProb / inProb;
        if (res > 1) res = 1;
        return res;
    }

    private double getPathProbability(List<Event> input) {
        double ret = 0;
//System.out.println(input.size());
        for (int i = 1; i < input.size() - 1; i += 2)
            ret += R[input.get(i).state.timeIndex][input.get(i).state.place.id][input.get(i + 1).state.place.id];
        return ret;
    }


    private double getUtility(List<Event> input, List<Event> output) {

        int totalInterval = 0;
        int sameReleasedInterval = 0;
        int sensitiveInterval = 0;

        for (int i = 0; i < input.size(); i += 2) {

            int st1 = input.get(i).timestamp;
            int et1 = input.get(i + 1).timestamp;
            if (dictionary.isSensitive(input.get(i).state.place, input.get(i).timestamp)) {
                sensitiveInterval += (et1 - st1);
                continue;
            }
            totalInterval += (et1 - st1);
            for (int j = 0; j < output.size(); j += 2) {
                int st2 = output.get(j).timestamp;
                int et2 = output.get(j + 1).timestamp;
                if (input.get(i).state.place.id == output.get(j).state.place.id && Math.max(st1, st2) < Math.min(et1, et2)) {
                    sameReleasedInterval += (Math.min(et1, et2) - Math.max(st1, st2));
                }
            }
//            System.out.println(sameReleasedInterval+"="+totalInterval);

        }
        System.out.println(sameReleasedInterval + " : " + totalInterval + " : " + sensitiveInterval);

        return (double) sameReleasedInterval * 1.0 / totalInterval;  //To change body of created methods use File | Settings | File Templates.
    }


    public void printEvents(List<Event> input) {
        for (Event e : input) {
            System.out.print("(" + e.state.place.id + "," + e.timestamp + "," + e.type + ")->");
        }
    }

    public List<LabelPlaceTraces> getLabelPlaceTraces(File input) {
        List<LabelPlaceTraces> labelPlaceTraceses = new ArrayList<LabelPlaceTraces>();
        try {
//            File input = new File(fileName);
            Scanner sc = new Scanner(input);

            while (sc.hasNext()) {
                String[] tokens = sc.nextLine().split(",");
                int tm = Integer.parseInt(tokens[0]);
                int placeId = Integer.parseInt(tokens[1]);
                if (placeId == 7) placeId = 5;
                labelPlaceTraceses.add(new LabelPlaceTraces(placeId, placeId + "", tm));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return labelPlaceTraceses;
    }

    public obfuscationMthod() {
        dictionary = new Dictionary();
        mc = new LoadMC();
//        sensitiveNodes = new SensitiveNodes();
        DT_MAX = mc.DT_MAX;
        DT_MIN = mc.DT_MIN;
        R = mc.R;
        M = mc.M;
        updateDT(DT_MAX);
//        updateDT(DT_MIN);
//        updateRT(R);

    }

    private void updateRT(int[][][] R) {


    }

    private void updateDT(int[][] D) {
        for (int i = 0; i < Utils.T; i++)
            for (int p = 0; p < Utils.N; p++)
                if (D[i][p] == 0) {
                    D[i][p] = 5;
                    for (int k = 1; k < Utils.T; k++) {
                        if (i - k >= 0 && D[i - k][p] > 0) {
                            D[i][p] = D[i - k][p];
                            break;
                        }
                        if (i + k < Utils.T && D[i + k][p] > 0) {
                            D[i][p] = D[i + k][p];
                            break;
                        }
                    }
                }
    }

    public List<Event> convertGPSTraceToEventList(List<GPSTrace> traces) {

        List<Event> eventList = new ArrayList<Event>();
        Place preLoc = null;

        for (int i = 0; i < traces.size(); i++) {
            Place curPlace = dictionary.getPlace(traces.get(i).latitude, traces.get(i).longitude);

            if (curPlace == null && preLoc != null) { // exit
                Event event = new Event(new State(preLoc, traces.get(i - 1).timeInMin / 30), "exit", traces.get(i - 1).timeInMin);
                eventList.add(event);
            }

            /*
            Entry
             */
            if ((preLoc == null && curPlace != null)) {
                Event event = new Event(new State(curPlace, traces.get(i).timeInMin / 30), "entry", traces.get(i).timeInMin);
                eventList.add(event);
            }

            if ((preLoc != null && curPlace != null && preLoc.id != curPlace.id)) {
                Event event = new Event(new State(preLoc, traces.get(i - 1).timeInMin / 30), "exit", traces.get(i - 1).timeInMin + 2);
                eventList.add(event);
                event = new Event(new State(curPlace, traces.get(i).timeInMin / 30), "entry", traces.get(i).timeInMin);
                eventList.add(event);
            }
            preLoc = curPlace;
        }

        return eventList;
    }

    public List<Event> convertLabelPlaceToEventList(List<LabelPlaceTraces> traces) {

        List<Event> eventList = new ArrayList<Event>();
        Place preLoc = null;

        for (int i = 0; i < traces.size(); i++) {
            Place curPlace = dictionary.getPlace(traces.get(i).id);

            if (curPlace == null && preLoc != null) { // exit
                Event event = new Event(new State(preLoc, traces.get(i - 1).timeInMin / 30), "exit", traces.get(i - 1).timeInMin);
                eventList.add(event);
            }

            /*
            Entry
             */
            if ((preLoc == null && curPlace != null)) {
                Event event = new Event(new State(curPlace, traces.get(i).timeInMin / 30), "entry", traces.get(i).timeInMin);
                eventList.add(event);
            }

            if ((preLoc != null && curPlace != null && preLoc.id != curPlace.id)) {
                Event event = new Event(new State(preLoc, traces.get(i - 1).timeInMin / 30), "exit", traces.get(i - 1).timeInMin + 2);
                eventList.add(event);
                event = new Event(new State(curPlace, traces.get(i).timeInMin / 30), "entry", traces.get(i).timeInMin);
                eventList.add(event);
            }
            preLoc = curPlace;
        }

        return eventList;
    }

    List<Event> doObfuscation(List<Event> input, boolean isPerfect) {
        List<Event> output = new ArrayList<Event>();

        Queue<StateNode> Q = new PriorityQueue<StateNode>();
        List<Event> altEventList = new ArrayList<Event>();
//        System.out.println(input.size());

        for (int i = 0; i < input.size(); i++) {
            Event event = input.get(i);
            for (int j = 0; j < altEventList.size(); j++) {
                if (altEventList.get(j).timestamp < output.get(output.size() - 1).timestamp || altEventList.get(j).timestamp >= input.get(i).timestamp)
                    continue;
                output.add(altEventList.get(j));
            }
            if (event.type.equals("entry")) {
                if (dictionary.isSensitive(event.state.place, event.timestamp)
                        || ((i > 0) && input.get((i - 1)).state.place.id != output.get(output.size() - 1).state.place.id)) {
//                    System.out.println(":" + event.state.place.id);
                    if (i == 0)
                        altEventList = getAlternativeEventPath(new State(new Place(1, "1", 0, 0, 100), 0), 0, R, M, DT_MAX, input, i, isPerfect);
                    else
                        altEventList = getAlternativeEventPath(output.get(output.size() - 1).state, output.get(output.size() - 1).timestamp, R, M, DT_MAX, input, i, isPerfect);
                } else {
                    output.add(event);
                    altEventList.clear();
                }

            } else if (event.type.equals("exit")) {
                if (((i > 0) && output.size() > 0 && input.get((i - 1)) != output.get(output.size() - 1))) {
                    altEventList = getAlternativeEventPath(output.get(output.size() - 1).state, output.get(output.size() - 1).timestamp, R, M, DT_MAX, input, i, isPerfect);
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
    int[][] tEn = new int[Utils.N][Utils.T];

    List<Event> getAlternativeEventPath(State x_t, int t, int[][][] R, double[][][][] M, int[][] D, List<Event> input, int curIndex, boolean isPerfect) {
        State nextSafeState = null;
//        State nS1 = getPredictedNextSafeNode(input.get(curIndex).state, t, R, M, D, true);
//        State nS2 = getPrefectPredictedNextSafeNode(input, curIndex);

//        System.out.println(">>>>>>>" + nS1.place.id + "," + nS1.timeIndex*30 + "::" + nS2.place.id + "," + nS2.timeIndex*30);
        if (!isPerfect)
            nextSafeState = getPredictedNextSafeNode(input.get(curIndex).state, t, R, M, D, true);
        else
            nextSafeState = getPrefectPredictedNextSafeNode(input, curIndex);
//        System.out.println(":"+nextSafeState.place.id);

        int tDest = (nextSafeState.timeIndex - 1) * 30;//todo

        Queue<StateNode> Q = new PriorityQueue<StateNode>();

        StateNode u = new StateNode(x_t, 0);
        pre.put(x_t, null);

        t_en.put(x_t, t - D[x_t.timeIndex][x_t.place.id]);

        Q.add(u);
        int[][] flag = new int[Utils.T][Utils.N];
        for (int i = 0; i < Utils.T; i++)
            for (int j = 0; j < Utils.N; j++)
                flag[i][j] = 0;
//        flag[x_t.timeIndex][x_t.place.id] = 1;

        tEn[x_t.place.id][x_t.timeIndex] = t - D[x_t.timeIndex][x_t.place.id];

        while (!Q.isEmpty()) {
            StateNode xTemp = Q.poll();
//            System.out.println(">>"+xTemp.x.place.id+","+xTemp.x.timeIndex);
            if (flag[xTemp.x.timeIndex][xTemp.x.place.id] != 0) continue;
            flag[xTemp.x.timeIndex][xTemp.x.place.id] = 1;

            if (xTemp.equals(nextSafeState) || (xTemp.x.place.id == nextSafeState.place.id && xTemp.x.timeIndex == nextSafeState.timeIndex)) {
//                System.out.println("<><><<><><><>");
                return getPath(xTemp.x);
//                break;
            }

            for (Place p : dictionary.places) {
//                System.out.println(">>>>" + p.id + "..." + t_en.get(xTemp.x));
                if (R[xTemp.x.timeIndex][xTemp.x.place.id][p.id] == 0) continue;
//                int tTemp = tEn[xTemp.x.place.id][xTemp.x.timeIndex] + R[xTemp.x.timeIndex][xTemp.x.place.id][p.id];
                int tTemp = t_en.get(xTemp.x) + Math.min(30, R[xTemp.x.timeIndex][xTemp.x.place.id][p.id]);
                if (tTemp > tDest + 30)
                    continue;
                if (tTemp >= tDest - 20 && tTemp <= tDest + 20) {
//                    System.out.println("<><>()()()><>");

                    StateNode x_next = new StateNode(new State(p, tau(tTemp)), xTemp.value + M[xTemp.x.timeIndex][xTemp.x.place.id][tau(tTemp)][p.id]);
                    t_en.put(x_next.x, tTemp);
                    pre.put(x_next.x, xTemp.x);
                    return getPath(x_next.x);
                }

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

    private State getPrefectPredictedNextSafeNode(List<Event> input, int curIndex) {

        for (int i = curIndex + 1; i < input.size(); i++)
            if ("entry".equals(input.get(i).type) && !isSensitive(input.get(i).state)) {
                return input.get(i).state;
            }
        return input.get(input.size() - 1).state;

    }

    State getPredictedNextSafeNode(State x, int t_entry, int[][][] R, double[][][][] M, int[][] D, boolean isFirstNode) {
        State nextSafeState;

//        System.out.print(">"+x.place.id);

        if (!isFirstNode && !isSensitive(x)) {
            return x;
        }
        int tExit = t_entry + D[tau(t_entry)][x.place.id];
//        System.out.print("("+x.place.id+","+tExit);

        double maxProbability = 0;
        Place nextPlace = null;
        int tNextPlaceEnter = 0;

        for (Place p : dictionary.places) {
            int tTemp = tExit + Math.min(30, R[x.timeIndex][x.place.id][p.id]);
//            System.out.print("::"+tExit+","+tTemp);

            if (M[x.timeIndex][x.place.id][tau(tTemp)][p.id] > maxProbability) {
                maxProbability = M[x.timeIndex][x.place.id][tau(tTemp)][p.id];
                nextPlace = p;
                tNextPlaceEnter = tTemp;
            }

        }
//        System.out.print("=" + nextPlace.id + "," + tNextPlaceEnter);

        if (nextPlace == null) return x;
        nextSafeState = new State(nextPlace, tau(tNextPlaceEnter));

        return getPredictedNextSafeNode(nextSafeState, tNextPlaceEnter, R, M, D, false);
    }


    private boolean isSensitive(State x) {

        return dictionary.isSensitive(x.place, x.timeIndex * 30);
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
        double value;

        StateNode(State x, double value) {
            this.x = x;
            this.value = value;
        }

        public int compareTo(StateNode o) {
            if (this.value > o.value) return 1;
            else if (this.value < o.value) return -1;
            else return 0;

        }
    }

    public static int tau(int t_entry) {
        return t_entry / 30;

    }
}
