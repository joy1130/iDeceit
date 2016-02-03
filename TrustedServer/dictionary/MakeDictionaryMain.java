package dictionary;

import msr_gps_privacy_dataset_2009.util.Utils;

import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.File;

/**
 * User: nazir
 * Date: Sep 19, 2014
 */
public class MakeDictionaryMain {
    static Dictionary dAll= new Dictionary();;

    public static void main(String[] args) {
        dAll = makeDictionary("D:\\Research\\data\\MSR GPS Privacy Dataset 2009\\userDataCluster\\gps_volunteer_all.csv", 15);
        writeDictionaryInFile(dAll, "D:\\Research\\data\\MSR GPS Privacy Dataset 2009\\dictionary\\dictionary.csv");

        for (int i = 1; i < 22; i++) {
            String uid = i + "";
            if (i < 10) uid = "0" + i;
            System.out.print("User:" + i + " ::: ");
            Dictionary d = makeDictionary("D:\\Research\\data\\MSR GPS Privacy Dataset 2009\\userDataCluster\\gps_volunteer_" + uid + ".csv", 3);
            writeDictionaryInFile(d, "D:\\Research\\data\\MSR GPS Privacy Dataset 2009\\dictionary\\dictionary" + uid + ".csv");
        }
        System.out.print("For all user >>");

    }

    /*
    dictionary.csv will be like <id, label, lat, long> in each row
     */
    private static void writeDictionaryInFile(Dictionary d, String fileName) {

        try {
            FileWriter f = new FileWriter(new File(fileName));

            for (Place p : d.places) {
                f.write(p.id + "," + p.label + "," + p.lat + "," + p.lon + "\n");
            }
            f.close();

        } catch (Exception e) {

        }
    }

    /*
    File contains all the cluster points. each row contains <lat, lon, start time, end time>
     */
    private static Dictionary makeDictionary(String fileName, int maxCount) {
//        System.out.println(fileName);
        int[] cnt = new int[555];
        Dictionary d = new Dictionary();
        int currentLocationIndex = 1;
        try {
            Scanner sc = new Scanner(new File(fileName));

            while (sc.hasNextLine()) {
                String str = sc.nextLine();
                String tokens[] = str.split(",");

                double lat = Double.parseDouble(tokens[0]);
                double lon = Double.parseDouble(tokens[1]);

                boolean isNew = true;
                for (int i = 0; i < d.places.size(); i++)
                    if (dist(d.places.get(i).lat, d.places.get(i).lon, lat, lon) < Utils.CLUSTER_RADIUS) {
//                        System.out.println();
                        Place p = new Place(d.places.get(i).label, lat, lon, d.places.get(i).id);
                        cnt[d.places.get(i).id]++;
                        d.places.add(p);
                        isNew = false;
                        break;
                    }
                if (isNew) {
                    d.places.add(new Place(currentLocationIndex + "", lat, lon, currentLocationIndex));
                    cnt[currentLocationIndex] = 1;
                    currentLocationIndex++;
                }
            }
        } catch (Exception e) {

        }
        int c = 0;
        List<Integer> indexList = new ArrayList<Integer>();
        List<Integer> cntList = new ArrayList<Integer>();

        for (int i = 0; i < currentLocationIndex; i++)
            if (cnt[i] >  maxCount) {
//            if (cnt[i] >= 10 || inAllPlace(d.places.get(i))) {
                c++;
                indexList.add(i);
//                System.out.println(i + ":: " + cnt[i]);
            }

        for (int i = 0; i < d.places.size(); i++)
            if (!indexList.contains(d.places.get(i).id)) {
                d.places.get(i).id = 0;
                d.places.get(i).label = 0 + "";
            }

        System.out.println("Total clusters : " + d.places.size() + "; Total unique places : " + currentLocationIndex + "; More than "+maxCount + " : " + c);
        return d;

    }

    private static boolean inAllPlace(Place place) {
        for(Place p : dAll.places)
            if(p.id!=0 && dist(p.lat, p.lon, place.lat, place.lon) < Utils.CLUSTER_RADIUS)
                return true;
         return false;

    }

    private static int dist(double lat1, double lng1, double lat2, double lng2) {

        double earthRadius = 6371000; //kilometers
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double dist = (double) (earthRadius * c);

        return (int) dist;

    }
}
