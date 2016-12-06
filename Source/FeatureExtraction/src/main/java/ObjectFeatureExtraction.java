import org.apache.poi.util.SystemOutLogger;
import org.openimaj.feature.local.list.LocalFeatureList;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.feature.local.engine.DoGSIFTEngine;
import org.openimaj.image.feature.local.keypoints.Keypoint;

import java.io.*;

/**
 * Created by Marmik on 20-09-2016.
 */
public class ObjectFeatureExtraction {
    public static void main(String args[]) throws IOException {
        String inputFolder = "data/Testing/Brain";
        String outputFolder = "output/";
        String[] IMAGE_CATEGORIES = {"Brain", "Breast", "Lung", "Throat"};
        int input_class = 0;
        File folder = new File(inputFolder);
        File[] listOfFiles = folder.listFiles();
        for (File file : listOfFiles) {
            if (file.isFile()) {
                System.out.println(file.getName());
                MBFImage mbfImage = ImageUtilities.readMBF(file);
                DoGSIFTEngine doGSIFTEngine = new DoGSIFTEngine();
                LocalFeatureList<Keypoint> features = doGSIFTEngine.findFeatures(mbfImage.flatten());
                FileWriter fw = new FileWriter(outputFolder + "training.txt", true);
                BufferedWriter bw = new BufferedWriter(fw);
                for (int i = 0; i < features.size(); i++) {
                    double c[] = features.get(i).getFeatureVector().asDoubleVector();
                    bw.write(input_class + ",");
                    for (int j = 0; j < c.length; j++) {
                        bw.write(c[j] + " ");
                    }
                    bw.newLine();
                }
                bw.close();
            }
        }
    }
}
