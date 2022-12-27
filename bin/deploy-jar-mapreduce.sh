#!/usr/bin/env bash

git pull origin develop-malachai
cd /home/malachai/fastcampus-lecture/hadoop_ws/job/mapreduce
mvn package
cd /home/malachai/fastcampus-lecture/hadoop_ws
sudo sudo docker cp job/mapreduce/target/mapreduce-job-1.0-SNAPSHOT.jar hadoop-container:/usr/lib/hadoop-3.3.4/share/hadoop/mapreduce/mapreduce-job-1.0-SNAPSHOT.jar
