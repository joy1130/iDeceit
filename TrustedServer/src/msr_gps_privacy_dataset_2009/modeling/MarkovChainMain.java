package msr_gps_privacy_dataset_2009.modeling;


/**
 * Created by IntelliJ IDEA.
 * User: ns
 * Date: Sep 20, 2014
 * Time: 12:30:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class MarkovChainMain {
    public static void main(String[] args) {
        MarkovChain S = new MarkovChain();
        S.d.writeROnFile("D:\\Research\\data\\MSR GPS Privacy Dataset 2009\\resourses_mobicom\\memphis_dictionary.txt");
        S.writeDTOnFile("D:\\Research\\data\\MSR GPS Privacy Dataset 2009\\resourses_mobicom\\memphis_dt_max.txt", true);
        S.writeDTOnFile("D:\\Research\\data\\MSR GPS Privacy Dataset 2009\\resourses_mobicom\\memphis_dt_min.txt", false);
        S.writeMCOnFile("D:\\Research\\data\\MSR GPS Privacy Dataset 2009\\resourses_mobicom\\memphis_mc.txt");
        S.writeROnFile("D:\\Research\\data\\MSR GPS Privacy Dataset 2009\\resourses_mobicom\\memphis_tc.txt");

        for(int uid=1; uid<=20; uid++) {
            String userid=""+uid;
            if(uid<10) userid="0"+userid;

            S.writeDTOnFile(uid, "D:\\Research\\data\\MSR GPS Privacy Dataset 2009\\resourses_mobicom\\memphis_dt_max_"+userid+".txt", true);
            S.writeDTOnFile(uid, "D:\\Research\\data\\MSR GPS Privacy Dataset 2009\\resourses_mobicom\\memphis_dt_min_"+userid+".txt", false);
            S.writeMCOnFile(uid, "D:\\Research\\data\\MSR GPS Privacy Dataset 2009\\resourses_mobicom\\memphis_mc_"+userid+".txt");
            S.writeROnFile(uid, "D:\\Research\\data\\MSR GPS Privacy Dataset 2009\\resourses_mobicom\\memphis_tc_"+userid+".txt");
        }
    }
}
