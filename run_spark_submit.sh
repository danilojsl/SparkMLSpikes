#!/bin/bash

echo "Running spark-submit..."

JAR_PATH=$1

sh "$SPARK_HOME"/bin/spark-submit \
      --master local[*] \
      --class com.spark.ml.spikes.MinIOConnection \
      "$JAR_PATH"