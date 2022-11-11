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

val minIOVersion = "8.4.5"
val minIO = "io.minio" % "minio" % minIOVersion

lazy val minIODependencies = Seq(
  minIO
    exclude ("com.fasterxml.jackson.core", "jackson-annotations")
    exclude ("com.fasterxml.jackson.core", "jackson-databind")
    exclude ("com.fasterxml.jackson.core", "jackson-core")
    exclude ("commons-configuration", "commons-configuration")
)

libraryDependencies ++= sparkDependencies ++ minIODependencies

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}

assembly / test := {}

assembly / mainClass := Some("com.spark.ml.spikes.MinIOConnection")