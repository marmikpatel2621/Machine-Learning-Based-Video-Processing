import dtParsing.ParsingNew;
import org.json.JSONObject;
import topology.CreateBolt;
import topology.CreateStromMainClass;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

/**
 * Created by Naga on 27-10-2016.
 */
public class MainClass {



    public static void main(String args[]) throws IOException {

        String Path = "/media/marmik2621/Extra/StudyMaterial/Real Time Big Data Analytics/Project/TopologyGeneration/src/main/java/";


        /*
        Getting model from mongo DB
         */
        String urlString = "https://api.mlab.com/api/1/databases/videoprocessing/collections/model/58461c4dc2ef165eccfa3d4a?apiKey=7IjZ5q5Y8cBtuOadP_iRf5b0__Qz13Vu";

        StringBuilder sb1 = new StringBuilder();
        InputStreamReader  in = null;
        URL url = new URL(urlString);
        URLConnection conn = url.openConnection();
        if (conn != null && conn.getInputStream() != null) {
            in = new InputStreamReader(conn.getInputStream(),
                    Charset.defaultCharset());
            InputStream is = conn.getInputStream();


            BufferedReader bufferedReader = new BufferedReader(in);
            if (bufferedReader != null) {
                int cp;
                while ((cp = bufferedReader.read()) != -1) {
                    sb1.append((char) cp);
                }
                bufferedReader.close();
            }
        }

        JSONObject jsonObject = new JSONObject(sb1.toString());
        String model = jsonObject.get("Model").toString();

        System.out.println("Got Model from Mongo");
        
         /*
        From the input model, derive paths for each class
         */

        ParsingNew parsing1 = new ParsingNew(model, "data/Class1.txt", "1.0");
        ParsingNew parsing2 = new ParsingNew(model, "data/Class2.txt", "2.0");
        ParsingNew parsing3 = new ParsingNew(model, "data/Class3.txt", "3.0");
        ParsingNew parsing4 = new ParsingNew(model, "data/Class4.txt", "4.0");

        System.out.println("Derived Class Paths");
        /*
        Create Storm Topology
        Bolt for Each Path
         */

        String[] bolts = {"Brain", "Breast", "Lung", "Prostate"};
        String[] classDTpaths = {"data/Class1.txt", "data/Class2.txt", "data/Class3.txt", "data/Class4.txt"};

        /*
        Create Bolt Class files
         */
        for(int i=0; i<bolts.length; i++){
            CreateBolt createBolt =  new CreateBolt(Path, bolts[i], classDTpaths[i]);
        }

        /*
        Creating Storm Main Class
         */

        String spoutName = "kafka_spout_audioFeatures";
        StringBuilder sb = new StringBuilder();
        String spout = "        topology.setSpout(\"" + spoutName+ "\", new KafkaSpout(kafkaConf), 4);";
        sb.append(spout).append("\n");
        for(int i=0; i<bolts.length; i++){
            String boltName = bolts[i] + "Bolt";
            String s2 = "        topology.setBolt(\"" + bolts[i] + "\", new " +boltName +"(), 4).shuffleGrouping(\"" + spoutName+ "\");";
            sb.append(s2);
            sb.append("\n");
        }


        CreateStromMainClass createStromMainClass = new CreateStromMainClass(sb.toString(), Path);

        System.out.println("Created Storm Topology");
    }
}
