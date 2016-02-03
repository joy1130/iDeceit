package processeddataset.dataprocess;

import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: ns
 * Date: Sep 21, 2014
 * Time: 12:25:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class DaywiseDataGEn {
    public static void main(String[] args) {
//        doProcess();
        System.out.print("f1");
        doProcessFillUp("D:\\Research\\data\\for suprio\\code\\matlabCode\\user_loc_UI_DY\\");
        System.out.print("finished");

    }

    private static void doProcess() {
        String DIR = "D:\\Research\\data\\for suprio\\";
        try {
            File output = new File("D:\\Research\\data\\for suprio\\allDataFilenew.csv");
            FileWriter f = new FileWriter(output);
            File folder = new File(DIR + "h");
            f.write("UserId,Day,TimeInMinute,LocationId" + "\n");

            for (File input : folder.listFiles()) {
                System.out.println(input);
                Scanner sc = new Scanner(input);
                sc.nextLine();
                while (sc.hasNextLine()) {
                    String s = sc.nextLine();
                    String tokens[] = s.split(",");
//                    System.out.println(s);
//                    if (tokens[3].charAt(0) == 'n') continue;
                    if (tokens.length < 8) continue;
                    int uId = Integer.parseInt(tokens[0]);
                    int dayId = Integer.parseInt(tokens[1]);
                    int locId = Integer.parseInt(tokens[8]);
                    Date date = new Date((int) Double.parseDouble(tokens[2]));

                    int sT = (int) (Double.parseDouble(tokens[2]) / (60000));
//                    sT %= (24 * 60);

                    int eT = (int) (Double.parseDouble(tokens[3]) / 60000);


                    date = new Date((int) Double.parseDouble(tokens[3]));

                    for (int t = sT; t < eT; t += 4) {
                        int tt = (t - 6 * 60) % (24 * 60);
                        f.write(uId + "," + dayId + "," + tt + "," + locId + "\n");
                    }
                    eT %= (24 * 60);
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

    static void fillBlankGaps(String dir, String userId) {
        String outStr = "D:\\Research\\data\\for suprio\\code\\matlabCode\\user_loc_UI_DY_fill\\";
        try {
            File folder = new File(dir);
            System.out.println("total files->" + folder.listFiles().length);

            int[] nodes = new int[24 * 60 / 5];
            for (int i = 0; i < 24 * 60 / 5; i++) nodes[i] = 8;
            File directory = new File(outStr + userId);
            directory.mkdir();

            for (File input : folder.listFiles()) {
                Scanner sc = new Scanner(input);

                File output = new File(outStr + userId + "\\" + input.getName());
                FileWriter f = new FileWriter(output);

                System.out.println(input);
                int pre = 0;
                int cnt = 0;
                String s = sc.nextLine();
                while (sc.hasNextLine()) {
                    s = sc.nextLine();
                    String tokens[] = s.split(",");
                    int t = Integer.parseInt(tokens[2]);
                    int v = Integer.parseInt(tokens[3]);
                    nodes[t / 5] = v;
                }
                for (int i = 0; i < 24 * 60 / 5; i++) {
                    f.write(i * 5 + "," + nodes[i] + "\n");
                }
                f.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static public List<String> getUserIds(String dirPath) {
        File userIdFile = new File(dirPath + "UserId.txt");

        List<String> userIds = new ArrayList<String>();
        try {
            Scanner sc = new Scanner(userIdFile);
            while (sc.hasNextLine()) {

                String s = sc.nextLine();
                userIds.add(s);
            }
        } catch (Exception e) {

        }
        return userIds;

    }

    static public void doProcessFillUp(String dirPath) {
        List<String> userIds = getUserIds(dirPath);

        for (int i = 0; i < userIds.size(); i++) {

            fillBlankGaps(dirPath + userIds.get(i), userIds.get(i));

        }
    }

}
