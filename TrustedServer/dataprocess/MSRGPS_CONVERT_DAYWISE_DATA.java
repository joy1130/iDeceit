package dataprocess;

import msr_gps_privacy_dataset_2009.util.ConfigResource;

import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

/**
 * Created by IntelliJ IDEA.
 * User: ns
 * Date: Sep 20, 2014
 * Time: 12:36:31 AM
 * To change this template use File | Settings | File Templates.
 */
public class MSRGPS_CONVERT_DAYWISE_DATA {
    public static void main(String[] args) {
        convertDaywiseDAta();
    }

    private static void convertDaywiseDAta() {
        try {
            File folder = new File(ConfigResource.MSR_GPS_PRIVACY_DATA_2009_CONVERTED_CSVALL_DATA_DIR);
            for (File input : folder.listFiles()) {
                System.out.println(input);
                processFile(input);
            }
        } catch (Exception e) {
            System.err.println("File not found. Please scan in new file.");
        }
    }

    private static void processFile(File input) {
      try {
            String userId = input.getName().substring(13, input.getName().length() - 4);
            new File(ConfigResource.MSR_GPS_PRIVACY_DATA_2009_DAYWISE_DATA_DIR + userId).mkdir();
            Scanner sc = new Scanner(input);

            String s = sc.nextLine();
            FileWriter f = new FileWriter("D:\\Research\\Reality Mining dataset\\test.txt");
            boolean firtFile=true;
            String curDay = "";

            int numberOfDays = 0;

            while (sc.hasNextLine()) {
                s = sc.nextLine();
                String tokens[] = s.split(",");
//                  System.out.println(tokens[1] + "," +curDay);
                if (!tokens[4].equals(curDay)) {
                    if(!firtFile)
                        f.close();
                    String dy = tokens[4];
                    dy = dy.replaceAll("/", "_");
                    f = new FileWriter(ConfigResource.MSR_GPS_PRIVACY_DATA_2009_DAYWISE_DATA_DIR + userId + "\\" + dy + ".csv");
                    firtFile=false;
                    curDay = tokens[4];
                    f.write(s + "\n");
                    numberOfDays++;

                } else if (tokens[4].equals(curDay)) {
                    f.write(s + "\n");
                }
            }
             f = new FileWriter(ConfigResource.MSR_GPS_PRIVACY_DATA_2009_DAYWISE_DATA_DIR+"statstics_participant_days.txt");
            f.write(userId+ ","+numberOfDays);
            f.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
