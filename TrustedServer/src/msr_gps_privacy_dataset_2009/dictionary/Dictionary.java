package msr_gps_privacy_dataset_2009.dictionary;

import msr_gps_privacy_dataset_2009.util.MSRUtils;
import msr_gps_privacy_dataset_2009.modeling.SensitivePlace;

import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;


/**
 * User: ns
 * Date: Sep 19, 2014
 */
public class Dictionary {
    String type; // "gps"/"label"
    public List<Place> places = new ArrayList<Place>();
//    public List<SensitivePlace> sensitivePlaces = new ArrayList<SensitivePlace>();

    public Dictionary() {
        getDictionaryFromFile("D:\\Research\\data\\MSR GPS Privacy Dataset 2009\\dictionary\\dictionary.csv");
//        getSensitivePlacesFromFile("D:\\Research\\data\\MemphisDataSet\\resourses\\memphis_sensitive_places.txt");
    }

    public Place getPlace(double lat, double lon) {
        Place retP = null;

        for (Place p : places) {
            if (dist(p.lat, p.lon, lat, lon) < MSRUtils.CLUSTER_RADIUS) {
                return p;
            }
        }

        return null;

    }

    private int dist(double lat1, double lng1, double lat2, double lng2) {

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


    public void getDictionaryFromFile(String fileName) {
        places = new ArrayList<Place>();
        try {
            File input = new File(fileName);
            Scanner sc = new Scanner(input);
            String tokens[];// = sc.nextLine().split(" ");
            //MSRUtils.TYPE = tokens[1];

            while (sc.hasNext()) {
                tokens = sc.nextLine().split(",");
                int id = Integer.parseInt(tokens[0]);
                String label = tokens[1];
                double lat = Double.parseDouble(tokens[2]);
                double lon = Double.parseDouble(tokens[3]);
//                int radius = Integer.parseInt(tokens[4]);
                places.add(new Place(id, label, lat, lon));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("Dictionary size : " + places.size());
    }

    public Place getPlace(int id) {
        for (Place p : places) {
            if (p.id == id) {
                return p;
            }
        }
        return null;

    }
/*

    public void getSensitivePlacesFromFile(String fileName) {
        sensitivePlaces = new ArrayList<SensitivePlace>();
        try {
            File input = new File(fileName);
            Scanner sc = new Scanner(input);

            while (sc.hasNext()) {
                String[] tokens = sc.nextLine().split(" ");
                int id = Integer.parseInt(tokens[0]);
                int sT = Integer.parseInt(tokens[1]);
                int eT = Integer.parseInt(tokens[2]);

                sensitivePlaces.add(new SensitivePlace(getPlace(id), sT, eT));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("Sensitive Node size : " + sensitivePlaces.size());

    }
*/

      public void writeROnFile(String fileName) {
        try {
            FileWriter f = new FileWriter(new File(fileName));
            f.write(places.size() + " label");
            f.write("\n");
            for (int i= 0; i< places.size(); i++) {
                Place p = places.get(i);
                f.write(p.id + " " + p.label+" "+p.lat + " "+p.lon + " "+100+"\n");
            }
            f.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
