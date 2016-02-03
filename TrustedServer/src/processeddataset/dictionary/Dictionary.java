package processeddataset.dictionary;

import processeddataset.util.Utils;

import java.util.List;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.File;

/**
 * User: ns
 * Date: Sep 19, 2014
 */
public class Dictionary {
    public List<Place> places=new ArrayList<Place>();
    public int uniquePlace =0 ;

        public void writeROnFile(String fileName) {
        try {
            FileWriter f = new FileWriter(new File(fileName));
            f.write(places.size() + " " + Utils.TYPE);
            f.write("\n");
            for (int i= 0; i< places.size(); i++) {
                Place p = places.get(i);
                f.write(p.id + " " + p.label+" "+p.lat + " "+p.lon + " "+p.radius+"\n");
            }
            f.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
