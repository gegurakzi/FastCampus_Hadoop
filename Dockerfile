
# using base image centos
FROM centos:centos7
# Dockerfile Author
MAINTAINER Malachai Lee <prussian1933@naver.com>

# ===========================
# initial environment
# java and utils installation
RUN \
  yum install which -y	&& \
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
ENV PATH=$PATH:$HADOOP_HOME/bin
# ===========================

# ===========================
# hadoop configuration
# copy hadoop config files from local
COPY hadoop-3.3.4/etc/hadoop/core-site.xml $HADOOP_HOME/etc/hadoop/core-site.xml
COPY hadoop-3.3.4/etc/hadoop/hdfs-site.xml $HADOOP_HOME/etc/hadoop/hdfs-site.xml
COPY hadoop-3.3.4/etc/hadoop/mapred-site.xml $HADOOP_HOME/etc/hadoop/mapred-site.xml
COPY hadoop-3.3.4/etc/hadoop/yarn-site.xml $HADOOP_HOME/etc/hadoop/yarn-site.xml
# copy hadoop environment script
COPY hadoop-3.3.4/sbin/hadoop-env.sh $HADOOP_HOME/sbin/hadoop-env.sh

# make namenode, datanode config directory
RUN \
  mkdir $HADOOP_HOME/etc/dfs  && \
  mkdir $HADOOP_HOME/etc/dfs/namenode  && \
  mkdir $HADOOP_HOME/etc/dfs/datanode
# ===========================

# port numbers to export
EXPOSE 9870

# change working directory
#WORKDIR ~


