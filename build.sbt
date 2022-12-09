import sbtassembly.MergeStrategy

version := "0.1.0"

scalaVersion := "2.12.15"

lazy val root = (project in file("."))
  .settings(
    name := "SparkMLSpykes"
  )

val sparkVer = "3.2.0"

lazy val sparkDependencies = Seq(
  "org.apache.spark" %% "spark-core" % sparkVer,
  "org.apache.spark" %% "spark-mllib" % sparkVer,
)

val redisClientVersion = "4.3.1"
val redisClient = "redis.clients" % "jedis" % redisClientVersion

lazy val redisDependencies = Seq(
  redisClient
)

libraryDependencies ++= sparkDependencies ++ redisDependencies

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}

assembly / test := {}

assembly / mainClass := Some("com.spark.ml.spikes.ForestExample")