package msr_gps_privacy_dataset_2009.dataprocess;

import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;
import java.util.TimeZone;
import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * User: ns
 * Date: Sep 18, 2014
 */
public class MSRGPSPrivacyDataset2009Converter {
    static String DIR = "D:\\Research\\data\\MSR GPS Privacy Dataset 2009\\";

    public static void main(String[] args) {
        convertCSV();
    }

    private static void convertCSV() {
        try {
            File folder = new File(DIR + "UserData\\");
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
            String fileName = input.getName().substring(0, input.getName().length() - 4) + ".csv";
            File file = new File(DIR + "userDataCSVAllDATA\\" + fileName);
            Scanner sc = new Scanner(input);

            String s = sc.nextLine();

            FileWriter f = new FileWriter(file);
//            f.write("Date,Time (UTC),Time Stamp,dayTimeInSecLatitude (degrees), Longitude (degrees)"+"\n");

            while (sc.hasNextLine()) {
                s = sc.nextLine();
                String tokens[] = s.split("\t");
                String date = tokens[0];
                String time = tokens[1];
                long timeStamp = getTimeMin(date, time);
                String lat = tokens[2];
                String log = tokens[3];
//                System.out.println("Date:"+date+"; time:"+time+"; lat:"+lat+"; lon:"+log+"; timeMin:"+timeStamp);
//                String timeStampStr = timeStamp+"";
//                f.write(timeStamp+","+(timeStamp/100)%(24*60*60)+","+lat+","+log + "\n");
                f.write(timeStamp+","+(timeStamp/1000)%(24*60*60)+","+lat+","+log + ","+date+","+time+ "\n");
//                f.write(date+","+time+","+timeStamp+","+(timeStamp/100)%(24*60*60)+","+lat+","+log + "\n");
//                break;

            }
            // f = new FileWriter("D:\\Research\\Reality Mining dataset\\statstics_participant_days.txt");
            //  f.write(userId+ ","+numberOfDays);
            f.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static long getTimeMin(String date, String time) {
        String originalString = date + " " + time;
        Date dt=new Date();
        //	Date dt = new SimpleDateFormat("MM/dd/yyyy hh:mm a").parse(originalString);
        try {
            SimpleDateFormat isoFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
            isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            dt = isoFormat.parse(originalString);
        } catch (Exception e) {

        }
        long tm=dt.getTime()/1000;
//        System.out.println("h:"+((tm/3600)%24+1)+"; m:"+(tm/60)%60+"; s:"+tm%60);
        return dt.getTime();

    }

}
