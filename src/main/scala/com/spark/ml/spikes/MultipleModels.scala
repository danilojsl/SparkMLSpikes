package com.spark.ml.spikes

import com.spark.ml.spikes.LogisticRegression.spark
import org.apache.spark.ml.classification.LogisticRegression
import org.apache.spark.ml.linalg.Vectors
import org.apache.spark.sql.SparkSession

object MultipleModels {

  val spark = SparkSession
    .builder
    .master("local[*]")
    .getOrCreate()

  def main(args: Array[String]): Unit = {

    val logisticRegression = new LogisticRegression()

    val test = spark.createDataFrame(Seq(
      (1.0, Vectors.dense(-1.0, 1.5, 1.3)),
      (0.0, Vectors.dense(3.0, 2.0, -0.1)),
      (1.0, Vectors.dense(0.0, 2.2, -1.5))
    )).toDF("label", "features")

    val models = logisticRegression

  }

}
