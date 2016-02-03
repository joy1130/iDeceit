package msr_gps_privacy_dataset_2009.modeling;

import msr_gps_privacy_dataset_2009.util.MSRUtils;
import msr_gps_privacy_dataset_2009.util.MSRConfigResource;

import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.File;
import java.io.FileNotFoundException;

import msr_gps_privacy_dataset_2009.dictionary.Dictionary;
import msr_gps_privacy_dataset_2009.dictionary.Place;


/**
 * User: ns
 * Date: Sep 20, 2014
 * Time: 12:34:52 PM
 */
public class MarkovChain {

    int DT_MIN[][] = new int[MSRUtils.T][MSRUtils.N];
    int DT_MAX[][] = new int[MSRUtils.T][MSRUtils.N];

    int R[][][] = new int[MSRUtils.T][MSRUtils.N][MSRUtils.N];

    double M[][][][] = new double[MSRUtils.T][MSRUtils.N][MSRUtils.T][MSRUtils.N];

    //[userID][fromTime][fromIndex][toTime][toIndex]
    int totalUser=22;
    int DT_MIN_all[][][] = new int[totalUser][MSRUtils.T][MSRUtils.N];
    int DT_MAX_all[][][] = new int[totalUser][MSRUtils.T][MSRUtils.N];
    int R_all[][][][] = new int[totalUser][MSRUtils.T][MSRUtils.N][MSRUtils.N];
    double M_all[][][][][] = new double[totalUser][MSRUtils.T][MSRUtils.N][MSRUtils.T][MSRUtils.N];

    Dictionary d = new Dictionary();

    public MarkovChain() {
//        convertDaywiseDAta();

        createMC();
//        learningEffectMC();
    }

    void init() {
        for (int n_i = 0; n_i < MSRUtils.T; n_i++)
            for (int i = 0; i < MSRUtils.N; i++) {
                DT_MAX[n_i][i] = 0;
                DT_MIN[n_i][i] = 30;
                for (int n_j = 0; n_j < MSRUtils.T; n_j++)
                    for (int j = 0; j < MSRUtils.N; j++)
                        M[n_i][i][n_j][j] = 0;
            }
    }
      void learningEffectSUPERMC() {
        try {
            FileWriter f = new FileWriter(new File("D:\\Research\\data\\MSR GPS Privacy Dataset 2009\\learningEffectSUPER.csv"));
            String s = "ALL";
            List<Double> totalProbabilities = new ArrayList<Double>();

            for (int count = 1; count < 50; count++) {
                init();

                for (int uid = 1; uid < 22; uid++) {
                    String userId = uid + "";
                   // s = userId;

                    if (uid < 10) userId = "0" + userId;
                    String DIR = "D:\\Research\\data\\MSR GPS Privacy Dataset 2009\\daywiseDataMemphisLike\\" + userId + "\\";

                    double val = 0;//getProbabilityValue();

                    int tmpcount = 1;
                    File folder = new File(DIR);
                    for (File inputFile : folder.listFiles()) {
                        System.out.print("UID: " + uid + "; ");
                        List<LabelPlaceTraces> inputTraces = getPlaceTrace(inputFile);
                        List<Event> input = convertLabelPlaceToEventList(inputTraces);
                        if (input.size() % 2 != 0)
                            input.add(new Event(input.get(input.size() - 1).state, "exit", 24 * 60));
                        System.out.println(input.size());
                        doProcess(uid, input);

//                        val += getProbabilityValue();
                        //s = s+","+val;
                        if (tmpcount == count) break;
                        tmpcount++;
                    }

                }
                totalProbabilities.add(getProbabilityValue());
            }
            for (int i = 0; i < totalProbabilities.size(); i++)
                s = s + "," + (totalProbabilities.get(i) / totalProbabilities.get(totalProbabilities.size() - 1));
            f.write(s);
            f.write("\n");
            f.close();
        } catch (Exception e) {
        }
//        calculateMCProbability();
    }
    void learningEffectMC() {
        try {
            FileWriter f = new FileWriter(new File("D:\\Research\\data\\MSR GPS Privacy Dataset 2009\\learningEffectNormalize.csv"));
            String s="";
            List<Double> totalProbabilities = new ArrayList<Double>();
            

            for (int uid = 1; uid < 22; uid++) {
                String userId = uid + "";
                s=userId;
                totalProbabilities = new ArrayList<Double>();
                if (uid < 10) userId = "0" + userId;
                String DIR = "D:\\Research\\data\\MSR GPS Privacy Dataset 2009\\daywiseDataMemphisLike\\" + userId + "\\";
                init();

                int count = 1;
                File folder = new File(DIR);
                for (File inputFile : folder.listFiles()) {
                    System.out.print("UID: " + uid + "; ");
                    List<LabelPlaceTraces> inputTraces = getPlaceTrace(inputFile);
                    List<Event> input = convertLabelPlaceToEventList(inputTraces);
                    if (input.size() % 2 != 0) input.add(new Event(input.get(input.size() - 1).state, "exit", 24 * 60));
                    System.out.println(input.size());
                    doProcess(uid, input);

                    double val = getProbabilityValue();
                    totalProbabilities.add(val);
                    //s = s+","+val;
                }
                for(int i=1; i<totalProbabilities.size(); i++)
                    s = s +","+ ((totalProbabilities.get(i))/totalProbabilities.get(totalProbabilities.size()-1));
                f.write(s);
                f.write("\n");
            }
            f.close();
        } catch (Exception e) {
        }
//        calculateMCProbability();
    }

    private double getProbabilityValue() {
        double ret = 0;
        int cnt = 0;
        for (int n_i = 0; n_i < MSRUtils.T; n_i++)
            for (int i = 0; i < MSRUtils.N; i++)
                for (int n_j = 0; n_j < MSRUtils.T; n_j++)
                    for (int j = 0; j < MSRUtils.N; j++)
                        if (M[n_i][i][MSRUtils.allTimeIndex][MSRUtils.allPlaceIndex] > 0){
                            ret += (M[n_i][i][n_j][j] / M[n_i][i][MSRUtils.allTimeIndex][MSRUtils.allPlaceIndex]);
                            cnt++;
                        }

        return ret;
    }


    void createMC() {
        for(int u_id=0; u_id<totalUser; u_id++)
            for (int n_i = 0; n_i < MSRUtils.T; n_i++)
                for (int i = 0; i < MSRUtils.N; i++) {
                    DT_MAX[n_i][i] = 0;
                    DT_MIN[n_i][i] = 30;

                    DT_MAX_all[u_id][n_i][i] = 0;
                    DT_MIN_all[u_id][n_i][i] = 30;
                    for (int n_j = 0; n_j < MSRUtils.T; n_j++)
                        for (int j = 0; j < MSRUtils.N; j++){
                            M[n_i][i][n_j][j] = 0;
                            M_all[u_id][n_i][i][n_j][j] = 0;

                        }
                }

        for (int uid = 1; uid < 22; uid++) {
            String userId = uid + "";
            if (uid < 10) userId = "0" + userId;
            String DIR = "D:\\Research\\data\\MSR GPS Privacy Dataset 2009\\daywiseDataMemphisLike\\" + userId + "\\";
            File folder = new File(DIR);
            for (File inputFile : folder.listFiles()) {
                System.out.print("UID: " + uid + "; ");
                List<LabelPlaceTraces> inputTraces = getPlaceTrace(inputFile);
                List<Event> input = convertLabelPlaceToEventList(inputTraces);
                if (input.size() % 2 != 0) input.add(new Event(input.get(input.size() - 1).state, "exit", 24 * 60));
                System.out.println(input.size());
                doProcess(uid, input);
            }
        }
        calculateMCProbability();
    }

    public static void main(String[] args) {
        MarkovChain mc = new MarkovChain();
        /*      LoadMC S = new LoadMC();
        S.getMCFromFile("D:\\Research\\data\\MemphisDataSet\\resourses\\memphis_mc.txt");
        S.getDTFromFile("D:\\Research\\data\\MemphisDataSet\\resourses\\memphis_dt.txt", false);
        S.getRFromFile("D:\\Research\\data\\MemphisDataSet\\resourses\\memphis_tc.txt");

        S.writeDTOnFile("D:\\Research\\data\\MemphisDataSet\\resourses\\memphis_dt_s.txt");
        S.writeMCOnFile("D:\\Research\\data\\MemphisDataSet\\resourses\\memphis_mc_s.txt");
        S.writeROnFile("D:\\Research\\data\\MemphisDataSet\\resourses\\memphis_tc_s.txt");*/
    }

    public void doProcess(int uid, List<Event> input) {

        int count = 0;
        int pre_n_j = -1;
        int pre_eT_mempnis_min = -1;
        int pre_locId = -1;

        for (int i = 0; i < input.size(); i += 2) {

            int locId = input.get(i).state.place.id;

            int sT_mempnis_min = input.get(i).timestamp;
            int eT_mempnis_min = input.get(i + 1).timestamp;//  (sT_min - 6 * 60) % (24 * 60);
            
            if (sT_mempnis_min > eT_mempnis_min)
                eT_mempnis_min = 24 * 60 - 1;
            int n_i = getInterval(sT_mempnis_min) + 1;
            int n_j = getInterval(eT_mempnis_min) + 1;
//            System.out.println(locId + ":" + n_i + "," + n_j);


//                System.out.print(":" +count++);
//                System.out.println(":" + sT_mempnis_min+","+eT_mempnis_min+"<"+tokens[2]+","+tokens[3]+">"+uId+","+dayId);

            if (sT_mempnis_min > eT_mempnis_min)
                System.out.print(":" + count++);


            for (int n = n_i; n <= n_j; n++) {
                if (n <= n_j) {

                    M[n][locId][n + 1][locId]++;
                    M[n][locId][MSRUtils.allTimeIndex][MSRUtils.allPlaceIndex]++;

                    M_all[uid][n][locId][n + 1][locId]++;
                    M_all[uid][n][locId][MSRUtils.allTimeIndex][MSRUtils.allPlaceIndex]++;

                    int d = Math.min(eT_mempnis_min, n * 30) - Math.max(sT_mempnis_min, (n - 1) * 30);
                    DT_MIN[n][locId] = Math.min(DT_MIN[n][locId], d);
                    DT_MAX[n][locId] = Math.max(DT_MAX[n][locId], d);

                    DT_MIN_all[uid][n][locId] = Math.min(DT_MIN[n][locId], d);
                    DT_MAX_all[uid][n][locId] = Math.max(DT_MAX[n][locId], d);

                }
/*
                int d = Math.min(eT_mempnis_min, n * 30) - Math.max(sT_mempnis_min, (n - 1) * 30);
                DT_MIN[n][locId] = Math.min(DT_MIN[n][locId], d);
                DT_MAX[n][locId] = Math.min(DT_MAX[n][locId], d);
*/
            }
            if (pre_locId != -1) { // we have previous value
                R[pre_n_j][pre_locId][locId] = sT_mempnis_min - pre_eT_mempnis_min;
                R_all[uid][pre_n_j][pre_locId][locId] = sT_mempnis_min - pre_eT_mempnis_min;

                M[pre_n_j][pre_locId][n_i][locId]++;
                M[pre_n_j][pre_locId][MSRUtils.allTimeIndex][MSRUtils.allPlaceIndex]++;

                M_all[uid][pre_n_j][pre_locId][n_i][locId]++;
                M_all[uid][pre_n_j][pre_locId][MSRUtils.allTimeIndex][MSRUtils.allPlaceIndex]++;

            }

            pre_n_j = n_j;
            pre_locId = locId;
            pre_eT_mempnis_min = eT_mempnis_min;

//            count++;
        }

    }

    void calculateMCProbability() {
        // calculate probability
        for (int n_i = 0; n_i < MSRUtils.T; n_i++)
            for (int i = 0; i < MSRUtils.N; i++)
                for (int n_j = 0; n_j < MSRUtils.T; n_j++)
                    for (int j = 0; j < MSRUtils.N; j++)
                        if (M[n_i][i][MSRUtils.allTimeIndex][MSRUtils.allPlaceIndex] > 0)
                            M[n_i][i][n_j][j] = M[n_i][i][n_j][j] / M[n_i][i][MSRUtils.allTimeIndex][MSRUtils.allPlaceIndex];

        for(int uid=0; uid<totalUser; uid++)
            for (int n_i = 0; n_i < MSRUtils.T; n_i++)
                for (int i = 0; i < MSRUtils.N; i++)
                    for (int n_j = 0; n_j < MSRUtils.T; n_j++)
                        for (int j = 0; j < MSRUtils.N; j++)
                            if (M_all[uid][n_i][i][MSRUtils.allTimeIndex][MSRUtils.allPlaceIndex] > 0)
                                M_all[uid][n_i][i][n_j][j] = M_all[uid][n_i][i][n_j][j] / M_all[uid][n_i][i][MSRUtils.allTimeIndex][MSRUtils.allPlaceIndex];

    }

    List<LabelPlaceTraces> getPlaceTrace(File fileName) {
        System.out.println("FILE: "+fileName.getName());
        int[] placeID = new int[24 * 60];
        List<LabelPlaceTraces> placeTraces = new ArrayList<LabelPlaceTraces>();
        try {
            for (int i = 0; i < placeID.length; i++) placeID[i] = -1;
            Scanner sc = new Scanner(fileName);
            while (sc.hasNextLine()) {
                String tokens[] = sc.nextLine().split(",");
                if (tokens.length < 2) continue;
                int timeMin = Integer.parseInt(tokens[0]);
                int pid = Integer.parseInt(tokens[1]);
                if (pid != -1 && timeMin <24 * 60) placeID[timeMin] = pid;
            }
        } catch (FileNotFoundException e) {

        }
        for (int i = 0; i < placeID.length; i++)
            placeTraces.add(new LabelPlaceTraces(placeID[i], "" + placeID[i], i));
        return placeTraces;
    }

    public List<Event> convertLabelPlaceToEventList(List<LabelPlaceTraces> traces) {

        List<Event> eventList = new ArrayList<Event>();
        Place preLoc = null;

        for (int i = 0; i < traces.size(); i++) {
            Place curPlace = d.getPlace(traces.get(i).id);

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

    private static int getInterval(int day_min) {
        return day_min / 30;
    }

    private static int memphisMinTime(String time) {
        int T_min = (int) (Double.parseDouble(time) / 60000);
        int T_mempnis_min = (T_min - 6 * 60) % (24 * 60);

        return T_mempnis_min;

    }

    public void writeMCOnFile(String fileName) {
        try {
            FileWriter f = new FileWriter(new File(fileName));
            f.write(MSRUtils.T + " " + MSRUtils.N);
            f.write("\n");
            for (int t1 = 0; t1 < MSRUtils.T; t1++) {
                for (int n1 = 0; n1 < MSRUtils.N; n1++) {
                    for (int t2 = 0; t2 < MSRUtils.T; t2++)
                        for (int n2 = 0; n2 < MSRUtils.N; n2++)
                            if (M[t1][n1][t2][n2] > 0)
                                f.write(t2 + ":" + n2 + ":" + M[t1][n1][t2][n2] + " ");
                    f.write("\n");
                }
            }
            f.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeMCOnFile(int uid, String fileName) {
        try {
            FileWriter f = new FileWriter(new File(fileName));
            f.write(MSRUtils.T + " " + MSRUtils.N);
            f.write("\n");
            for (int t1 = 0; t1 < MSRUtils.T; t1++) {
                for (int n1 = 0; n1 < MSRUtils.N; n1++) {
                    for (int t2 = 0; t2 < MSRUtils.T; t2++)
                        for (int n2 = 0; n2 < MSRUtils.N; n2++)
                            if (M_all[uid][t1][n1][t2][n2] > 0)
                                f.write(t2 + ":" + n2 + ":" + M_all[uid][t1][n1][t2][n2] + " ");
                    f.write("\n");
                }
            }
            f.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeROnFile(String fileName) {
        try {
            FileWriter f = new FileWriter(new File(fileName));
            f.write(MSRUtils.T + " " + MSRUtils.N);
            f.write("\n");
            for (int t1 = 0; t1 < MSRUtils.T; t1++) {
                for (int n1 = 0; n1 < MSRUtils.N; n1++) {
                    for (int n2 = 0; n2 < MSRUtils.N; n2++)
                        if (R[t1][n1][n2] > 0)
                            f.write(n2 + ":" + R[t1][n1][n2] + " ");
                    f.write("\n");
                }
            }
            f.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeROnFile(int uid, String fileName) {
        try {
            FileWriter f = new FileWriter(new File(fileName));
            f.write(MSRUtils.T + " " + MSRUtils.N);
            f.write("\n");
            for (int t1 = 0; t1 < MSRUtils.T; t1++) {
                for (int n1 = 0; n1 < MSRUtils.N; n1++) {
                    for (int n2 = 0; n2 < MSRUtils.N; n2++)
                        if (R_all[uid][t1][n1][n2] > 0)
                            f.write(n2 + ":" + R_all[uid][t1][n1][n2] + " ");
                    f.write("\n");
                }
            }
            f.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeDTOnFile(String fileName, boolean isMax) {
        try {
            FileWriter f = new FileWriter(new File(fileName));
            f.write(MSRUtils.T + " " + MSRUtils.N);
            f.write("\n");
            for (int t1 = 0; t1 < MSRUtils.T; t1++) {
                for (int n1 = 0; n1 < MSRUtils.N; n1++) {
                    if (isMax)
                        f.write((DT_MAX[t1][n1] + DT_MAX[t1][n1]) / 2 + " ");
                    else
                        f.write((DT_MIN[t1][n1] + DT_MIN[t1][n1]) / 2 + " ");
                }
                f.write("\n");
            }
            f.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeDTOnFile(int uid, String fileName, boolean isMax) {
        try {
            FileWriter f = new FileWriter(new File(fileName));
            f.write(MSRUtils.T + " " + MSRUtils.N);
            f.write("\n");
            for (int t1 = 0; t1 < MSRUtils.T; t1++) {
                for (int n1 = 0; n1 < MSRUtils.N; n1++) {
                    if (isMax)
                        f.write((DT_MAX_all[uid][t1][n1] + DT_MAX_all[uid][t1][n1]) / 2 + " ");
                    else
                        f.write((DT_MIN_all[uid][t1][n1] + DT_MIN_all[uid][t1][n1]) / 2 + " ");
                }
                f.write("\n");
            }
            f.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void convertDaywiseDAta() {
        try {
            File folder = new File(MSRConfigResource.MSR_GPS_PRIVACY_DATA_2009_CONVERTED_CSVALL_DATA_DIR);
            for (File input : folder.listFiles()) {
                System.out.println(input);
                processFile(input);
            }
        } catch (Exception e) {
            System.err.println("File not found. Please scan in new file.");
        }
    }

    private void processFile(File input) {
        try {
            String userId = input.getName().substring(14, input.getName().length() - 4);
            new File(MSRConfigResource.MSR_GPS_PRIVACY_DATA_2009_DAYWISE_DATA_DIR_LIKE_MEMPHISDATA + userId).mkdir();
            Scanner sc = new Scanner(input);

            String s = sc.nextLine();
            FileWriter f = new FileWriter("D:\\Research\\Reality Mining dataset\\test.txt");
            boolean firtFile = true;
            String curDay = "";

            int numberOfDays = 0;

            while (sc.hasNextLine()) {
                s = sc.nextLine();
                String tokens[] = s.split(",");
//                  System.out.println(tokens[1] + "," +curDay);
                if (!tokens[4].equals(curDay)) {
                    if (!firtFile)
                        f.close();
                    String dy = tokens[4];
                    dy = dy.replaceAll("/", "_");
                    f = new FileWriter(MSRConfigResource.MSR_GPS_PRIVACY_DATA_2009_DAYWISE_DATA_DIR_LIKE_MEMPHISDATA + userId + "\\" + dy + ".csv");
                    firtFile = false;
                    curDay = tokens[4];
                    int minTime = Integer.parseInt(tokens[1]) / 60;
                    double lat = Double.parseDouble(tokens[2]);
                    double lon = Double.parseDouble(tokens[3]);
                    Place p = d.getPlace(lat, lon);
                    int pid = -1;
                    if (p != null) pid = p.id;
                    f.write(minTime + "," + pid + "\n");
                    numberOfDays++;

                } else if (tokens[4].equals(curDay)) {
                    int minTime = Integer.parseInt(tokens[1]) / 60;
                    double lat = Double.parseDouble(tokens[2]);
                    double lon = Double.parseDouble(tokens[3]);
                    Place p = d.getPlace(lat, lon);
                    int pid = -1;
                    if (p != null) pid = p.id;
                    f.write(minTime + "," + pid + "\n");
                }
            }
            f = new FileWriter(MSRConfigResource.MSR_GPS_PRIVACY_DATA_2009_DAYWISE_DATA_DIR_LIKE_MEMPHISDATA + "statstics_participant_days.txt");
            f.write(userId + "," + numberOfDays);
            f.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
