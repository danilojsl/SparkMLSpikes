package com.spark.ml.spikes

import io.minio.{MinioClient, PutObjectArgs}
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.spark.sql.SparkSession

class MinIOClient(sparkSession: SparkSession) {

  private lazy val client: MinioClient = {
    MinioClient.builder
      .endpoint("https://play.min.io")
      .credentials("Q3AM3UQ867SPQQA43P2F", "zuf+tfteSlswRu7BJ86wekitnifILbZam1KYY3TG")
      .build
  }

  def copyFileToMinIO(sourceFilePath: String): Unit = {

//    val fileName = sourceFilePath.split("/").last
//    val minIOBucket = bucket.replace("s3://", "").split("/").head
//    val bucketPath = bucket.substring(s"s3://$minIOBucket".length) + "/"
//    val bucketFilePath = s"$bucketPath$fileName"

    val fileSystem = FileSystem.get(sparkSession.sparkContext.hadoopConfiguration)
    val inputStream = fileSystem.open(new Path(sourceFilePath))
    val fileName = sourceFilePath.split("/").last

    val objectStream = PutObjectArgs
      .builder()
      .bucket("test.danilo")
      .`object`(fileName)
      .stream(inputStream, inputStream.available(), -1)
      .build()

    client.putObject(objectStream)
  }


}
