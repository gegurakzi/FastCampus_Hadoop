# FastCampus Hadoop Lecture Workshop

this project builds docker container with hadoop application using dockerfile.

as the root directory of this project is /usr/lib directory in docker container, it is recommended to put some files in the right directory.


## 1. Hadoop Pseudo-distributed mode

```
# ssh key generation for localhost ssh connections
> /usr/sbin/sshd-keygen -A
> /usr/sbin/sshd
> ssh-keygen -t rsa -P '' -f ~/.ssh/id_dsa
> cat ~/.ssh/id_dsa.pub >> ~/.ssh/authorized_keys

# starting hadoop
> hdfs namenode -format
> start-all.sh

# simple example for map-reduce test
> hadoop jar $HADOOP_HOME/share/hadoop/mapreduce/hadoop-mapreduce-examples-3.3.4.jar pi 16 10000
```

## 2. Simple Java APIs for Hadoop
project root : /api directory

`com.fastcampus.hadoop.FileSystemPrint` Simple std print for a file in HDFS\
`com.fastcampus.hadoop.FileSystemListStatus` Simple file list for a directory in HDFS\
`com.fastcampus.hadoop.FileSystemCopyFromLocal` Simple file copy for a file in local to HDFS\
`com.fastcampus.hadoop.FileSystemDeleteFile` Simple file deletion for a file in HDFS\

## 3. Simple Hadoop MapReduce Jobs
project root : /job/mapreduce directory

`com.fastcampus.hadoop.WordCount` Simple word count job\
`com.fastcampus.hadoop.GenericOptionsParserApp` Simple GenericOptionsParser example application\
`com.fastcampus.hadoop.ToolRunnerApp` Simple ToolRunner example application\
`com.fastcampus.hadoop.WordCountWithCounter` Simple Counter example application\
`com.fastcampus.hadoop.SortWordCount` Simple sorting job\
`com.fastcampus.hadoop.DistCacheMapSideJoin` Simple Map-Side Join job\
`com.fastcampus.hadoop.DistCacheReduceSideJoin` Simple Reduce-Side Join job\
`com.fastcampus.hadoop.DistCacheReduceSideJoinCustomKey` Reduce-Side Join job with secondary sort

## 4. Simple Cluster Setup with docker-compose
total 5 containers deployed by docker-compose.yml
including scripts that help ssh keygen and sharing public keys by authorized_keys file

- exposed ports
  - 9870: HDFS Web UI\
  - 8088: YARN Web UI\
  - 4040: Driver(Application Master) Web UI\
  - 18080: Spark History Server UI
```
# cluster deployment with docker-compose(with superuser)
> docker-compose up -d

# ssh keygen
> sudo bin/deploy-ssh-keys.sh

# share ssh public keys
> sudo bin/deploy-ssh-authorized-keys.sh

# ...you should configure *-site.xml hostnames to master01
```