import org.openimaj.feature.local.list.LocalFeatureList;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.feature.local.engine.DoGSIFTEngine;
import org.openimaj.image.feature.local.keypoints.Keypoint;

import java.io.*;

/**
 * Created by Naga on 20-09-2016.
 */
public class ObjectFeatureExtraction {
    public static void main(String args[]) throws IOException {
        String inputFolder = "data/test/Brain";
        String outputFolder = "output/";
        String[] IMAGE_CATEGORIES = {"Brain", "Breast", "Lung"};
        Extract(inputFolder,0);
    }

    public static void Extract(String folder, int category)
    {
        File f = new File(folder);
        File[] listFile = f.listFiles();
        for(File file: listFile)
        {
            try {
                System.out.println(file.getPath());
            MBFImage mbfImage = ImageUtilities.readMBF(file);
            DoGSIFTEngine doGSIFTEngine = new DoGSIFTEngine();
            LocalFeatureList<Keypoint> features = doGSIFTEngine.findFeatures(mbfImage.flatten());
            FileWriter fw = new FileWriter("output/testing.txt",true);
            BufferedWriter bw = new BufferedWriter(fw);
            for (int i = 0; i < features.size(); i++) {
                double c[] = features.get(i).getFeatureVector().asDoubleVector();
                bw.write(category + ",");
                for (int j = 0; j < c.length; j++) {
                    bw.write(c[j] + " ");
                }
                bw.newLine();
            }
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
