
# using base image centos
FROM centos:centos7
# Dockerfile Author
MAINTAINER Malachai Lee <prussian1933@naver.com>

# ===========================
# initial environment
# java and utils installation
RUN \
  yum install which -y	&& \
  yum install openssh-server openssh-clients openssh-askpass -y && \
  yum install nano -y	&& \
  yum install java-1.8.0-openjdk-devel.x86_64 -y
# java path
ENV JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk-1.8.0.352.b08-2.el7_9.x86_64
ENV PATH=$PATH:$JAVA_HOME/bin

# hadoop installation
RUN \
  cd /home		&& \
  curl -L -O https://dlcdn.apache.org/hadoop/common/hadoop-3.3.4/hadoop-3.3.4.tar.gz	&& \
  tar -xzvf hadoop-3.3.4.tar.gz	&& \
  mv hadoop-3.3.4 /usr/lib/hadoop-3.3.4
# hadoop path
ENV HADOOP_HOME=/usr/lib/hadoop-3.3.4
ENV PATH=$PATH:$HADOOP_HOME/bin:$HADOOP_HOME/sbin

# maven installation for hadoop java api
RUN \
  cd /home		&& \
  curl -L -O https://dlcdn.apache.org/maven/maven-3/3.8.6/binaries/apache-maven-3.8.6-bin.tar.gz  && \
  tar -xzvf apache-maven-3.8.6-bin.tar.gz	&& \
  mv apache-maven-3.8.6 /usr/lib/apache-maven-3.8.6
# maven path
ENV MAVEN_HOME=/usr/lib/apache-maven-3.8.6
ENV PATH=$PATH:$MAVEN_HOME/bin

# python3 and spark installation
RUN \
  cd /home		&& \
  yum install python3 -y		&& \
  curl https://bootstrap.pypa.io/pip/3.6/get-pip.py | python3
RUN \
  cd /home		&& \
  curl -L -O https://dlcdn.apache.org/spark/spark-3.3.1/spark-3.3.1-bin-hadoop3.tgz		&& \
  tar -xvzf spark-3.3.1-bin-hadoop3.tgz		&& \
  mv spark-3.3.1-bin-hadoop3 /usr/lib
# spark path
ENV SPARK_HOME=/usr/lib/spark-3.3.1-bin-hadoop3
ENV PATH=$PATH:$SPARK_HOME/bin
# ===========================

# ===========================
# hadoop configuration
# copy hadoop config files from local
COPY hadoop-3.3.4/etc/hadoop/core-site.xml $HADOOP_HOME/etc/hadoop/core-site.xml
COPY hadoop-3.3.4/etc/hadoop/hdfs-site.xml $HADOOP_HOME/etc/hadoop/hdfs-site.xml
COPY hadoop-3.3.4/etc/hadoop/mapred-site.xml $HADOOP_HOME/etc/hadoop/mapred-site.xml
COPY hadoop-3.3.4/etc/hadoop/yarn-site.xml $HADOOP_HOME/etc/hadoop/yarn-site.xml
# copy hadoop environment script
COPY hadoop-3.3.4/etc/hadoop/hadoop-env.sh $HADOOP_HOME/etc/hadoop/hadoop-env.sh

# make namenode, datanode config directory
RUN \
  mkdir $HADOOP_HOME/etc/dfs  && \
  mkdir $HADOOP_HOME/etc/dfs/namenode  && \
  mkdir $HADOOP_HOME/etc/dfs/datanode
# ===========================


# port numbers to export
# default file system
EXPOSE 9000
# hdfs web ui
EXPOSE 9870
# spark web ui
#EXPOSE

# change working directory
#WORKDIR ~


