import Jama.Matrix;
import org.apache.poi.util.SystemOutLogger;
import org.openimaj.feature.local.list.LocalFeatureList;
import org.openimaj.feature.local.matcher.FastBasicKeypointMatcher;
import org.openimaj.feature.local.matcher.consistent.ConsistentLocalFeatureMatcher2d;
import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.FImage;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.colour.RGBColour;
import org.openimaj.image.colour.Transforms;
import org.openimaj.image.feature.local.engine.DoGSIFTEngine;
import org.openimaj.image.feature.local.keypoints.Keypoint;
import org.openimaj.image.renderer.MBFImageRenderer;
import org.openimaj.image.typography.hershey.HersheyFont;
import org.openimaj.math.geometry.point.Point2d;
import org.openimaj.math.geometry.transforms.HomographyModel;
import org.openimaj.math.geometry.transforms.HomographyRefinement;
import org.openimaj.math.geometry.transforms.MatrixTransformProvider;
import org.openimaj.math.geometry.transforms.check.TransformMatrixConditionCheck;
import org.openimaj.math.geometry.transforms.estimation.RobustHomographyEstimator;
import org.openimaj.math.model.fit.RANSAC;
import org.openimaj.video.Video;
import org.openimaj.video.xuggle.XuggleVideo;
import org.openimaj.video.xuggle.XuggleVideoWriter;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.AttributedString;
import java.util.List;
//import org.openimaj.math.geometry.shape.Polygon;

/**
 * Created by Naga on 16-09-2016.
 */
public class ObjectDetection {

    public static void main(String args[]) throws IOException {
        ObjectMainDetection objectMainDetection = new ObjectMainDetection();
    }
}

class ObjectMainDetection {
    private ConsistentLocalFeatureMatcher2d<Keypoint> matcher1;
    private ConsistentLocalFeatureMatcher2d<Keypoint> matcher2;
    private ConsistentLocalFeatureMatcher2d<Keypoint> matcher3;
    private ConsistentLocalFeatureMatcher2d<Keypoint> matcher4;
    final DoGSIFTEngine engine;
//    private RenderMode renderMode = RenderMode.SQUARE;

    private MBFImage modelImage1, modelImage2, modelImage3, modelImage4, modelImage5;


    public ObjectMainDetection() throws IOException {
        this.engine = new DoGSIFTEngine();
      //  this.x = new XuggleVideoWriter("abc.mpg",300,300,20);
        this.engine.getOptions().setDoubleInitialImage(true);
        this.matcher1 = new ConsistentLocalFeatureMatcher2d<Keypoint>(
                new FastBasicKeypointMatcher<Keypoint>(8));
        this.matcher2 = new ConsistentLocalFeatureMatcher2d<Keypoint>(
                new FastBasicKeypointMatcher<Keypoint>(8));
        this.matcher3 = new ConsistentLocalFeatureMatcher2d<Keypoint>(
                new FastBasicKeypointMatcher<Keypoint>(8));
        this.matcher4 = new ConsistentLocalFeatureMatcher2d<Keypoint>(
                new FastBasicKeypointMatcher<Keypoint>(8));
        final RobustHomographyEstimator ransac = new RobustHomographyEstimator(0.5, 1500,
                new RANSAC.PercentageInliersStoppingCondition(0.6), HomographyRefinement.NONE,
                new TransformMatrixConditionCheck<HomographyModel>(10000));
        this.matcher1.setFittingModel(ransac);
        this.matcher2.setFittingModel(ransac);
        this.matcher3.setFittingModel(ransac);
        this.matcher4.setFittingModel(ransac);
        LoadReferenceObject();
        StartVideo();
    }

    public void StartVideo() throws IOException {
        Video<MBFImage> video = new XuggleVideo(new File("data/Mix_MKV.mkv"));
        int count1 = 0, count2 = 0, count3 = 0, count4 = 0, count5 = 0;
        String o1 = "output/features.txt";
        FileWriter fw = new FileWriter(o1);
        BufferedWriter bw = new BufferedWriter(fw);
        System.out.println(video.countFrames());
        int i1=0;
        for (MBFImage mbfImage : video) {
            System.out.println(video.getCurrentFrameIndex());
            final LocalFeatureList<Keypoint> kpl = this.engine.findFeatures(Transforms.calculateIntensity(mbfImage));
            final MBFImageRenderer renderer = mbfImage.createRenderer();
            renderer.drawPoints(kpl, RGBColour.MAGENTA, 3);
            System.out.println(i1++);
            if (this.matcher1.findMatches(kpl)
                    && ((MatrixTransformProvider) this.matcher1.getModel()).getTransform().cond() < 1e6 ) {
                try {
                    final Matrix boundsToPoly = ((MatrixTransformProvider) this.matcher1.getModel()).getTransform()
                            .inverse();

                    if (modelImage1.getBounds().transform(boundsToPoly).isConvex()) {

                        renderer.drawShape(this.modelImage1.getBounds().transform(boundsToPoly), 3, RGBColour.RED);
                        renderer.drawText("Brain Cancer",(int)this.modelImage2.getBounds().getTopLeft().getX(),60, HersheyFont.CURSIVE,20,RGBColour.GREEN);
                        if(count1 <= 10){
                            List<Point2d> vertices = this.modelImage1.getBounds().transform(boundsToPoly).asPolygon().getVertices();
                            int x[] = new int[4], y[] = new int[4];
                            for (int i = 0; i < vertices.size(); i++) {
                                x[i] = (int) vertices.get(i).getX();
                                y[i] = (int) vertices.get(i).getY();
                            }
                            Polygon polygon = new Polygon(x, y, 4);
                            for (int i = 0; i < kpl.size(); i++) {
                                if (polygon.contains(kpl.get(i).getX(), kpl.get(i).getY())) {
                                    double c[] = kpl.get(i).getFeatureVector().asDoubleVector();
                                    bw.write("0,");
                                    String str="";
                                    for (int j = 0; j < c.length; j++) {
                                        bw.write(c[j] + " ");
                                        str = str+c[j]+" ";
                                    }
                             //       System.out.println(str);
                                    KafkaRestCall.restCall(str);
                                    bw.newLine();
                                }
                            }

                            count1++;
                        }

                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

            }
            if (this.matcher2.findMatches(kpl)
                    && ((MatrixTransformProvider) this.matcher2.getModel()).getTransform().cond() < 1e6 ) {
                try {
                    final Matrix boundsToPoly = ((MatrixTransformProvider) this.matcher2.getModel()).getTransform()
                            .inverse();

                    renderer.drawShape(this.modelImage2.getBounds().transform(boundsToPoly), 3, RGBColour.RED);
                    renderer.drawText("Lung Cancer",(int)this.modelImage2.getBounds().getTopLeft().getX(),50, HersheyFont.CURSIVE,20,RGBColour.RED);
                    if(count2 <= 10){
                        if (modelImage2.getBounds().transform(boundsToPoly).isConvex()) {
                            List<Point2d> vertices = this.modelImage2.getBounds().transform(boundsToPoly).asPolygon().getVertices();
                            int x[] = new int[4], y[] = new int[4];
                            for (int i = 0; i < vertices.size(); i++) {
                                x[i] = (int) vertices.get(i).getX();
                                y[i] = (int) vertices.get(i).getY();
                            }
                            Polygon polygon = new Polygon(x, y, 4);
                            for (int i = 0; i < kpl.size(); i++) {
                                if (polygon.contains(kpl.get(i).getX(), kpl.get(i).getY())) {
                                    double c[] = kpl.get(i).getFeatureVector().asDoubleVector();
                                    bw.write("1,");
                                    String str1 = "";
                                    for (int j = 0; j < c.length; j++) {
                                        bw.write(c[j] + " ");
                                        str1 = str1+c[j]+" ";
                                    }
                                   // System.out.println(str1);
                                    KafkaRestCall.restCall(str1);
                                    bw.newLine();
                                }
                            }

                            count2++;
                    }

                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

            }
            if (this.matcher3.findMatches(kpl)
                    && ((MatrixTransformProvider) this.matcher3.getModel()).getTransform().cond() < 1e6) {
                try {
                    final Matrix boundsToPoly = ((MatrixTransformProvider) this.matcher3.getModel()).getTransform()
                            .inverse();

                    if (modelImage3.getBounds().transform(boundsToPoly).isConvex()) {

                        renderer.drawShape(this.modelImage3.getBounds().transform(boundsToPoly), 3, RGBColour.RED);
                        renderer.drawText(new AttributedString("breast cancer cells"),(int)this.modelImage3.getBounds().getTopLeft().getX()+25,10);
                        if (count3 <= 10){
                            List<Point2d> vertices = this.modelImage3.getBounds().transform(boundsToPoly).asPolygon().getVertices();
                            int x[] = new int[4], y[] = new int[4];
                            for (int i = 0; i < vertices.size(); i++) {
                                x[i] = (int) vertices.get(i).getX();
                                y[i] = (int) vertices.get(i).getY();
                            }
                            Polygon polygon = new Polygon(x, y, 4);
                            for (int i = 0; i < kpl.size(); i++) {
                                if (polygon.contains(kpl.get(i).getX(), kpl.get(i).getY())) {
                                    double c[] = kpl.get(i).getFeatureVector().asDoubleVector();
                                    bw.write("2,");
                                    String str2 = "";
                                    for (int j = 0; j < c.length; j++) {
                                        bw.write(c[j] + " ");
                                        str2 = str2+c[j]+" ";
                                    }
                                   // System.out.println(str2);
                                    KafkaRestCall.restCall(str2);
                                    bw.newLine();
                                }
                            }

                            count3++;
                        }

                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

            }
            if (this.matcher4.findMatches(kpl)
                    && ((MatrixTransformProvider) this.matcher4.getModel()).getTransform().cond() < 1e6) {
                try {
                    final Matrix boundsToPoly = ((MatrixTransformProvider) this.matcher4.getModel()).getTransform()
                            .inverse();

                    if (modelImage4.getBounds().transform(boundsToPoly).isConvex()) {

                        renderer.drawShape(this.modelImage4.getBounds().transform(boundsToPoly), 3, RGBColour.RED);
                        renderer.drawText(new AttributedString("Prostate cancer cells"),(int)this.modelImage4.getBounds().getTopLeft().getX()+25,10);
                        if (count4 <= 10) {
                            List<Point2d> vertices = this.modelImage4.getBounds().transform(boundsToPoly).asPolygon().getVertices();
                            int x[] = new int[4], y[] = new int[4];
                            for (int i = 0; i < vertices.size(); i++) {
                                x[i] = (int) vertices.get(i).getX();
                                y[i] = (int) vertices.get(i).getY();
                            }
                            Polygon polygon = new Polygon(x, y, 4);
                            for (int i = 0; i < kpl.size(); i++) {
                                if (polygon.contains(kpl.get(i).getX(), kpl.get(i).getY())) {
                                    double c[] = kpl.get(i).getFeatureVector().asDoubleVector();
                                    bw.write("3,");
                                    String str3 ="";
                                    for (int j = 0; j < c.length; j++) {
                                        bw.write(c[j] + " ");
                                        str3 = str3+c[j]+" ";
                                    }
                                   // System.out.println(str3);
                                    KafkaRestCall.restCall(str3);
                                    bw.newLine();
                                }
                            }

                            count4++;
                        }

                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

            }
            DisplayUtilities.displayName(mbfImage, "Image");
        }
        bw.close();
    }

    public void LoadReferenceObject() {

        final DoGSIFTEngine engine = new DoGSIFTEngine();
        engine.getOptions().setDoubleInitialImage(true);

        try {
            modelImage1 = ImageUtilities.readMBF(new File("data/brain1.png"));
            modelImage2 = ImageUtilities.readMBF(new File("data/Lung1.png"));
            modelImage3 = ImageUtilities.readMBF(new File("data/breast1.png"));
            modelImage4 = ImageUtilities.readMBF(new File("data/Prostate1.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        FImage modelF1 = Transforms.calculateIntensityNTSC(modelImage1);
        this.matcher1.setModelFeatures(engine.findFeatures(modelF1));

        FImage modelF2 = Transforms.calculateIntensityNTSC(modelImage2);
        this.matcher2.setModelFeatures(engine.findFeatures(modelF2));

        FImage modelF3 = Transforms.calculateIntensityNTSC(modelImage3);
        this.matcher3.setModelFeatures(engine.findFeatures(modelF3));

        FImage modelF4 = Transforms.calculateIntensityNTSC(modelImage4);
        this.matcher4.setModelFeatures(engine.findFeatures(modelF4));

    }
}
