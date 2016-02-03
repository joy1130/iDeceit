package gpstracedata;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.Scanner;

/**
 * User: nazir
 * Date: Sep 20, 2014
 */
public class LoadMC {
    int DT_MIN[][];// = new int[processeddataset.util.labeldata.Utils.T][processeddataset.util.labeldata.Utils.N];
    int DT_MAX[][];// = new int[processeddataset.util.labeldata.Utils.T][labeldata.Utils.N];

    int DT_MIN_all[][][];// = new int[processeddataset.util.labeldata.Utils.T][processeddataset.util.labeldata.Utils.N];
    int DT_MAX_all[][][];// = new int[processeddataset.util.labeldata.Utils.T][labeldata.Utils.N];

    int R[][][];// = new int[labeldata.Utils.T][labeldata.Utils.N][labeldata.Utils.N];
    int R_all[][][][];// = new int[labeldata.Utils.T][labeldata.Utils.N][labeldata.Utils.N];
      int totalUser=22;
    double M[][][][];// = new double[labeldata.Utils.T][labeldata.Utils.N][labeldata.Utils.T][labeldata.Utils.N];
    double M_all[][][][][] = new double[totalUser][Utils.T][Utils.N][Utils.T][Utils.N];


    
    Dictionary d = new Dictionary();

    public LoadMC() {
        getMCFromFile("D:\\Research\\data\\MSR GPS Privacy Dataset 2009\\resourses_mobicom\\memphis_mc.txt");
        getDTFromFile("D:\\Research\\data\\MSR GPS Privacy Dataset 2009\\resourses_mobicom\\memphis_dt_min.txt", false);
        getDTFromFile("D:\\Research\\data\\MSR GPS Privacy Dataset 2009\\resourses_mobicom\\memphis_dt_max.txt", true);
        getRFromFile("D:\\Research\\data\\MSR GPS Privacy Dataset 2009\\resourses_mobicom\\memphis_tc.txt");

        for (int uid = 1; uid <= 20; uid++) {
            String userid = "" + uid;
            if (uid < 10) userid = "0" + userid;
            getMCFromFile(uid, "D:\\Research\\data\\MSR GPS Privacy Dataset 2009\\resourses_mobicom\\memphis_mc_" + userid + ".txt");
            getDTFromFile(uid, "D:\\Research\\data\\MSR GPS Privacy Dataset 2009\\resourses_mobicom\\memphis_dt_min_" + userid + ".txt", false);
            getDTFromFile(uid, "D:\\Research\\data\\MSR GPS Privacy Dataset 2009\\resourses_mobicom\\memphis_dt_max_" + userid + ".txt", true);
            getRFromFile(uid, "D:\\Research\\data\\MSR GPS Privacy Dataset 2009\\resourses_mobicom\\memphis_tc_" + userid + ".txt");

        }
    }

    public static void main(String[] args) {
/*        LoadMC S = new LoadMC();
        S.getMCFromFile("D:\\Research\\data\\MemphisDataSet\\resourses\\memphis_mc.txt");
        S.getDTFromFile("D:\\Research\\data\\MemphisDataSet\\resourses\\memphis_dt.txt", false);
        S.getRFromFile("D:\\Research\\data\\MemphisDataSet\\resourses\\memphis_tc.txt");

        S.writeDTOnFile("D:\\Research\\data\\MemphisDataSet\\resourses\\memphis_dt_s.txt");
        S.writeMCOnFile("D:\\Research\\data\\MemphisDataSet\\resourses\\memphis_mc_s.txt");
        S.writeROnFile("D:\\Research\\data\\MemphisDataSet\\resourses\\memphis_tc_s.txt");*/
    }


    public double[][][][] getMCFromFile(String fileName) {
        double[][][][] M = null;
        try {
            File input = new File(fileName);
            Scanner sc = new Scanner(input);
//        String s = sc.nextLine();
            String tokens[] = sc.nextLine().split(" ");
            int T = Integer.parseInt(tokens[0]);
            int N = Integer.parseInt(tokens[1]);
            M = new double[T][N][T][N];
            for (int t = 0; t < T; t++)
                for (int n = 0; n < N; n++) {
                    //  String s = sc.nextLine();
                    tokens = sc.nextLine().split(" ");

                    for (String tok : tokens) {
                        if (tok.length() == 0) continue;
                        String[] parseValue = tok.split(":");
                        int t2 = Integer.parseInt(parseValue[0]);
                        int n2 = Integer.parseInt(parseValue[1]);
                        double val = Double.parseDouble(parseValue[2]);
                        M[t][n][t2][n2] = val;
                    }

                }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        this.M = M;
        return M;
    }

    public void getMCFromFile(int uid, String fileName) {
        //double[][][][] M = null;
        try {
            File input = new File(fileName);
            Scanner sc = new Scanner(input);
//        String s = sc.nextLine();
            String tokens[] = sc.nextLine().split(" ");
            int T = Integer.parseInt(tokens[0]);
            int N = Integer.parseInt(tokens[1]);
         //   M_all = new double[totalUser][T][N][T][N];     d
            for (int t = 0; t < T; t++)
                for (int n = 0; n < N; n++) {
                    //  String s = sc.nextLine();
                    tokens = sc.nextLine().split(" ");

                    for (String tok : tokens) {
                        if (tok.length() == 0) continue;
                        String[] parseValue = tok.split(":");
                        int t2 = Integer.parseInt(parseValue[0]);
                        int n2 = Integer.parseInt(parseValue[1]);
                        double val = Double.parseDouble(parseValue[2]);
                        M_all[uid][t][n][t2][n2] = val;
                    }

                }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public int[][] getDTFromFile(String fileName, boolean isMax) {
        int[][] D = null;
        try {
            File input = new File(fileName);
            Scanner sc = new Scanner(input);
            String tokens[] = sc.nextLine().split(" ");
            int T = Integer.parseInt(tokens[0]);
            int N = Integer.parseInt(tokens[1]);
            D = new int[T][N];
            for (int t = 0; t < T; t++) {
//                String s = sc.nextLine();
                tokens = sc.nextLine().split(" ");

                int cnt = 0;
                for (String tok : tokens) {
                    int val = Integer.parseInt(tok);
                    D[t][cnt++] = val;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (isMax)
            this.DT_MAX = D;
        else
            this.DT_MIN = D;

        return D;
    }
    public void getDTFromFile(int uid, String fileName, boolean isMax) {
        int[][][] D = null;
        try {
            File input = new File(fileName);
            Scanner sc = new Scanner(input);
            String tokens[] = sc.nextLine().split(" ");
            int T = Integer.parseInt(tokens[0]);
            int N = Integer.parseInt(tokens[1]);
            D = new int[totalUser][T][N];
            for (int t = 0; t < T; t++) {
//                String s = sc.nextLine();
                tokens = sc.nextLine().split(" ");

                int cnt = 0;
                for (String tok : tokens) {
                    int val = Integer.parseInt(tok);
                    D[uid][t][cnt++] = val;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (isMax)
            this.DT_MAX_all = D;
        else
            this.DT_MIN_all = D;


    }

    public int[][][] getRFromFile(String fileName) {
        int[][][] R = null;
        try {
            File input = new File(fileName);
            Scanner sc = new Scanner(input);
            String tokens[] = sc.nextLine().split(" ");
            int T = Integer.parseInt(tokens[0]);
            int N = Integer.parseInt(tokens[1]);
            R = new int[T][N][N];
            for (int t = 0; t < T; t++)
                for (int n = 0; n < N; n++) {
                    //  String s = sc.nextLine();
                    tokens = sc.nextLine().split(" ");

                    for (String tok : tokens) {
                        if (tok.length() == 0) continue;
                        String[] parseValue = tok.split(":");
                        int n2 = Integer.parseInt(parseValue[0]);
                        int val = Integer.parseInt(parseValue[1]);
                        R[t][n][n2] = val;
                    }

                }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        this.R = R;
        return R;
    }

   public void getRFromFile(int uid, String fileName) {

        try {
            File input = new File(fileName);
            Scanner sc = new Scanner(input);
            String tokens[] = sc.nextLine().split(" ");
            int T = Integer.parseInt(tokens[0]);
            int N = Integer.parseInt(tokens[1]);
            R_all = new int[totalUser][T][N][N];
            for (int t = 0; t < T; t++)
                for (int n = 0; n < N; n++) {
                    //  String s = sc.nextLine();
                    tokens = sc.nextLine().split(" ");

                    for (String tok : tokens) {
                        if (tok.length() == 0) continue;
                        String[] parseValue = tok.split(":");
                        int n2 = Integer.parseInt(parseValue[0]);
                        int val = Integer.parseInt(parseValue[1]);
                        R_all[uid][t][n][n2] = val;
                    }

                }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
       
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
                    f.write((DT_MAX[t1][n1] + DT_MAX[t1][n1]) / 2 + " ");
                }
                f.write("\n");
            }
            f.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
