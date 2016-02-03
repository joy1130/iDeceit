package processeddataset.modeling;

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
        S.d.writeROnFile("D:\\Research\\data\\MemphisDataSet\\resourses\\memphis_dictionary.txt");
        S.writeDTOnFile("D:\\Research\\data\\MemphisDataSet\\resourses\\memphis_dt_max.txt", true);
        S.writeDTOnFile("D:\\Research\\data\\MemphisDataSet\\resourses\\memphis_dt_min.txt", false);
        S.writeMCOnFile("D:\\Research\\data\\MemphisDataSet\\resourses\\memphis_mc.txt");
        S.writeROnFile("D:\\Research\\data\\MemphisDataSet\\resourses\\memphis_tc.txt");
    }
}
