package com.spark.ml.spikes

import org.apache.spark.ml.classification.{RandomForestClassificationModel, RandomForestClassifier}
import org.apache.spark.ml.feature.{IndexToString, StringIndexer, VectorIndexer}
import org.apache.spark.sql.SparkSession
import org.apache.spark.{SparkConf, SparkContext}

/**
* Example from
 * https://github.com/RedisLabs/spark-redis-ml
 * http://bit.ly/sparkredisml
* */

object ForestExample {

  def main(args: Array[String]): Unit = {
//    val conf = new SparkConf().setAppName("Forest Example")
//    val sc = new SparkContext(conf)
//    sc.setLogLevel("WARN")

//    val movieId = args(0).split("/").last
//    val nTrees = args(1).toInt
    val spark = SparkSession
      .builder
      .master("local[*]")
      .getOrCreate()
    // Load and parse the data file, converting it to a DataFrame.
    val data = spark.read.format("libsvm").load("/home/danilo/JSL/datasets/spikes/ml-100k/ua.base")
      data.show()
//    val data = spark.read.format("libsvm").load(args(0))

    // Index labels, adding metadata to the label column.
    // Fit on whole dataset to include all labels in index.
    val labelIndexer = new StringIndexer().setInputCol("label").setOutputCol("indexedLabel")
      .fit(data)
    // Automatically identify categorical features, and index them.
    // Set maxCategories so features with > 4 distinct values are treated as continuous.
    val featureIndexer = new VectorIndexer().setInputCol("features").setOutputCol("indexedFeatures").setMaxCategories(20)
      .fit(data)

    // Split the data into training and test sets (30% held out for testing).
    val Array(trainingData, test) = data.randomSplit(Array(0.8, 0.2))

    // Train a RandomForest model.
    val rf = new RandomForestClassifier()
      .setFeatureSubsetStrategy("all").setLabelCol("indexedLabel").setFeaturesCol("indexedFeatures").setNumTrees(10)

    // Convert indexed labels back to original labels.
    val labelConverter = new IndexToString().setInputCol("prediction").setOutputCol("predictedLabel").setLabels(labelIndexer.labels)

    // Chain indexers and forest in a Pipeline.
    val pipeline = new org.apache.spark.ml.Pipeline().setStages(Array(labelIndexer, featureIndexer, rf, labelConverter))

    // Train model. This also runs the indexers.
    val model = pipeline.fit(trainingData)

    // Make predictions.
    val predictions = model.transform(test)

    // Select example rows to display.
    predictions.select("predictedLabel", "label", "features").show(5)

//    val rfModel = model.stages(2).asInstanceOf[RandomForestClassificationModel]
//    println("Learned classification forest model:\n" + rfModel.toDebugString)
//
//    val f = new Forest(rfModel.trees)
//    f.loadToRedis(s"movie-${movieId}", "localhost")
  }


}
