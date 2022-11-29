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

`com.fastcampus.hadoop.FileSystemPrint` Simple std print for a file in HDFS
`com.fastcampus.hadoop.FileSystemListStatus` Simple file list for a directory in HDFS
`com.fastcampus.hadoop.FileSystemCopyFromLocal` Simple file copy for a file in local to HDFS
`com.fastcampus.hadoop.FileSystemDeleteFile` Simple file deletion for a file in HDFS

