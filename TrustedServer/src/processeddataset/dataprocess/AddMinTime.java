package processeddataset.dataprocess;

import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

/**
 * Created by IntelliJ IDEA.
 * User: ns
 * Date: Sep 21, 2014
 * Time: 12:22:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class AddMinTime {
    public static void main(String[] args) {
        convertCSVToTxt();
        System.out.print("f1");
    }
    private static void convertCSVToTxt() {
          String DIR = "D:\\Research\\data\\for suprio\\";
          try {
              File output = new File("D:\\Research\\data\\MemphisDataSet\\allDataFileWithMinTimeInterval.txt");
              FileWriter f = new FileWriter(output);
              File folder = new File(DIR + "h");

              for (File input : folder.listFiles()) {
                  System.out.println(input);
                  Scanner sc = new Scanner(input);
                  String ss = sc.nextLine();
                  f.write(ss+ "\n");

                  while (sc.hasNextLine()) {
                      String s = sc.nextLine();
                      f.write(s+ "\n");

//                  f.write(uId + ","+ dayId+ "," + sT + "," + eT + "," +locId+ "\n");
//                  System.out.println(uId + ","+ dayId+ "," + sT + "," + eT + "," +locId+ "\n");

                  }
              }
              f.close();

          } catch (Exception e) {
              System.err.println(e.toString());
          }
      }

    private static void doProcess() {
        String DIR = "D:\\Research\\data\\for suprio\\";
        try {
            File output = new File("D:\\Research\\data\\for suprio\\allDataFileWithMinTimeInterval.txt");
            FileWriter f = new FileWriter(output);
            File folder = new File(DIR + "h");

            for (File input : folder.listFiles()) {
                System.out.println(input);
                Scanner sc = new Scanner(input);
                String ss = sc.nextLine();
                f.write(ss+"Start Time In Minute,End Time In Minute" + "\n");

                while (sc.hasNextLine()) {
                    String s = sc.nextLine();
                    String tokens[] = s.split(",");
//                    System.out.println(s);
//                    if (tokens[3].charAt(0) == 'n') continue;
                    if (tokens.length < 8) continue;
                    int uId = Integer.parseInt(tokens[0]);
                    int dayId = Integer.parseInt(tokens[1]);
                    int locId = Integer.parseInt(tokens[5]);

                    int sT = (int) (Double.parseDouble(tokens[2]) / (60000))-6*60;
                    sT %= (24 * 60);
                    int eT = (int) (Double.parseDouble(tokens[3]) / 60000)-6*60;
                    eT %= (24 * 60);
/*
                    for (int t = sT; t <= eT; t += 4)
                        f.write(uId + "," + dayId + "," + t + "," + locId + "\n");
*/
                    f.write(uId + "," + dayId + "," + eT + "," + locId + "\n");

//                  f.write(uId + ","+ dayId+ "," + sT + "," + eT + "," +locId+ "\n");
//                  System.out.println(uId + ","+ dayId+ "," + sT + "," + eT + "," +locId+ "\n");

                }
            }
            f.close();

        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

}
