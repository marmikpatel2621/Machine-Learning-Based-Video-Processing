import java.io.{FileWriter, BufferedWriter, File}

import org.apache.log4j.{Level, Logger}
import org.apache.spark.mllib.evaluation.MulticlassMetrics
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.tree.DecisionTree
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by Naga on 19-09-2016.
  */
object ImageClassification_DT {
  def main(args: Array[String]) {
    val IMAGE_CATEGORIES = Array("Brain", "Breast", "Lung", "Prostate")
    System.setProperty("hadoop.home.dir", "E:\\WinUtilsForHadoop")
   Logger.getLogger("org").setLevel(Level.ERROR)
   Logger.getLogger("akka").setLevel(Level.ERROR)
    val sparkConf = new SparkConf().setAppName("ImageClassification").setMaster("local[*]")
    val sc = new SparkContext(sparkConf)
    val train = sc.textFile("data/train")

    val parsedData = train.map { line =>
      val parts = line.split(',')
      LabeledPoint(parts(0).toDouble, Vectors.dense(parts(1).split(' ').map(_.toDouble)))
    }

    val splits = parsedData.randomSplit(Array(0.8, 0.2))
    val (trainingData, testData) = (splits(0), splits(1))


    val numClasses = 5
    val categoricalFeaturesInfo = Map[Int, Int]()
    val impurity = "gini"
    val maxDepth = 10
    val maxBins = 32

    val model = DecisionTree.trainClassifier(trainingData, numClasses, categoricalFeaturesInfo,
      impurity, maxDepth, maxBins)

    val classify1 = testData.map { line =>
      val prediction = model.predict(line.features)
      (line.label, prediction)
    }

    val prediction1 = classify1.groupBy(_._1).map(f => {
      var fuzzy_Pred = Array(0, 0, 0, 0)
      f._2.foreach(ff => {
        fuzzy_Pred(ff._2.toInt) += 1
      })
      var count = 0.0
      fuzzy_Pred.foreach(f => {
        count += f
      })
      var i = -1
      var maxIndex = 4
      val max = fuzzy_Pred.max
      val pp = fuzzy_Pred.map(f => {
        val p = f * 100 / count
        i = i + 1
        if(f == max)
          maxIndex=i
        (i, p)
      })
      (f._1, pp, maxIndex)
    })
    prediction1.foreach(f => {
      println("\n\n\n" + f._1 + " : " + f._2.mkString(";\n"))
    })
    val y = prediction1.map(f => {
      (f._1, f._3.toDouble)
    })

    y.collect().foreach(println(_))
    MongoCall.insertIntoMongoDB(model.toDebugString)
    val metrics = new MulticlassMetrics(y)
    val file = new File("output/Output Decision Tree 80 20.txt")
    val bw = new BufferedWriter(new FileWriter(file))
    bw.write("Desicion Tree")
    bw.newLine()
    bw.write("Accuracy:" + metrics.accuracy)
    bw.newLine()
    bw.write("Precision:" + metrics.precision(1))
    bw.newLine()
    bw.write("FMeasure:" + metrics.fMeasure(1))
    bw.newLine()
    bw.write("Recall:" + metrics.recall(1))
    bw.newLine()
    bw.write("Confusion Matrix:")
    bw.newLine()
    bw.write(""+metrics.confusionMatrix)
    bw.close()


  }
}
