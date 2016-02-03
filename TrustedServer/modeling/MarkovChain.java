package modeling;

import msr_gps_privacy_dataset_2009.util.Utils;

import java.util.Scanner;
import java.io.FileWriter;
import java.io.File;

import dictionary.Dictionary;

/**
 * Created by IntelliJ IDEA.
 * User: ns
 * Date: Sep 20, 2014
 * Time: 12:34:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class MarkovChain {

    int DT_MIN[][] = new int[Utils.T][Utils.N];
    int DT_MAX[][] = new int[Utils.T][Utils.N];

    int R[][][] = new int[Utils.T][Utils.N][Utils.N];

    int M[][][][] = new int[Utils.T][Utils.N][Utils.T][Utils.N];

    Dictionary d = new Dictionary();

    public MarkovChain() {
    }

    public MarkovChain(String fileName, boolean isMemphis) {
        super();
        if (isMemphis) {
            doProcessMemphis(fileName);
        }
    }

    public void doProcessMemphis(String fileName) {
        System.out.println(fileName);
        Scanner sc = new Scanner(fileName);
        sc.nextLine();
        int count = 0;
        int pre_n_j = -1;
        int pre_eT_mempnis_min = -1;
        int pre_locId = -1;

        while (sc.hasNextLine()) {

            String tokens[] = sc.nextLine().split(",");
            if (tokens.length < 8) continue;
            int uId = Integer.parseInt(tokens[0]);
            int dayId = Integer.parseInt(tokens[1]);
            int locId = Integer.parseInt(tokens[8]);

            int sT_mempnis_min = memphisMinTime(tokens[2]);//  (sT_min - 6 * 60) % (24 * 60);
            int eT_mempnis_min = memphisMinTime(tokens[3]);//  (sT_min - 6 * 60) % (24 * 60);

            int n_i = getInterval(sT_mempnis_min);
            int n_j = getInterval(eT_mempnis_min);

            for (int n = n_i; n <= n_j; n++) {
                if (n < n_j) {
                    M[n][locId][n + 1][locId]++;
                    M[n][locId][Utils.allTimeIndex][Utils.allPlaceIndex]++;
                }
                int d = Math.min(eT_mempnis_min, n * 30) - Math.max(sT_mempnis_min, (n - 1) * 30);
                DT_MIN[n][locId] = Math.min(DT_MIN[n][locId], d);
                DT_MAX[n][locId] = Math.min(DT_MAX[n][locId], d);
            }
            if (count > 0) {
                R[pre_n_j][pre_locId][locId] = sT_mempnis_min - pre_eT_mempnis_min;
                M[pre_n_j][pre_locId][n_i][locId]++;
                M[pre_n_j][pre_locId][Utils.allTimeIndex][Utils.allPlaceIndex]++;
            }

            pre_n_j = n_j;
            pre_locId = locId;
            pre_eT_mempnis_min = eT_mempnis_min;

            count++;
        }

        // calculate probability
        for (int n_i = 0; n_i < Utils.T; n_i++)
            for (int i = 0; i < Utils.N; i++)
                for (int n_j = 0; n_j < Utils.T; n_j++)
                    for (int j = 0; j < Utils.N; j++)
                        if (M[n_i][i][Utils.allTimeIndex][Utils.allPlaceIndex] > 0)
                            M[n_i][i][n_j][j] = M[n_i][i][n_j][j] / M[n_i][i][Utils.allTimeIndex][Utils.allPlaceIndex];
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
            f.write(Utils.T + " " + Utils.N);
            f.write("\n");
            for (int t1 = 0; t1 < Utils.T; t1++) {
                for (int n1 = 0; n1 < Utils.N; n1++) {
                    for (int t2 = 0; t2 < Utils.T; t2++)
                        for (int n2 = 0; n2 < Utils.N; n2++)
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

    public void writeROnFile(String fileName) {
        try {
            FileWriter f = new FileWriter(new File(fileName));
            f.write(Utils.T + " " + Utils.N);
            f.write("\n");
            for (int t1 = 0; t1 < Utils.T; t1++) {
                for (int n1 = 0; n1 < Utils.N; n1++) {
                    for (int n2 = 0; n2 < Utils.N; n2++)
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

    public void writeDTOnFile(String fileName) {
        try {
            FileWriter f = new FileWriter(new File(fileName));
            f.write(Utils.T + " " + Utils.N);
            f.write("\n");
            for (int t1 = 0; t1 < Utils.T; t1++) {
                for (int n1 = 0; n1 < Utils.N; n1++) {
                    f.write((DT_MAX[t1][n1] + DT_MIN[t1][n1]) / 2 + " ");
                }
                f.write("\n");
            }
            f.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
