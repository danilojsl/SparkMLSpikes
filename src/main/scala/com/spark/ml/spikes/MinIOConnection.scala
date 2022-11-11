package com.spark.ml.spikes

import org.apache.spark.sql.SparkSession

object MinIOConnection {

  def main(args: Array[String]): Unit = {

    val sourceFilePath = "/home/danilo/Downloads/NerDL_c11506ac1bd5.log"

    val sparkSession = SparkSession.getActiveSession.getOrElse(
        SparkSession
          .builder()
          .appName("SparkNLP Default Session")
          .master("local[*]")
          .config("spark.driver.memory", "22G")
          .config("spark.driver.maxResultSize", "0")
          .config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
          .config("spark.kryoserializer.buffer.max", "1000m")
          .getOrCreate())

    val minIOClient = new MinIOClient(sparkSession)
    minIOClient.copyFileToMinIO(sourceFilePath)

  }

}
