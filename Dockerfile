
# using base image centos
FROM centos:centos7
# Dockerfile Author
MAINTAINER Malachai Lee <prussian1933@naver.com>

# commands to set initial environment
RUN \
yum install which -y	&& \
yum install nano -y	&& \
yum install java-1.8.0-openjdk-devel.x86_64 -y	&& \
export JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk-1.8.0.352.b08-2.el7_9.x86_64	&& \
export PATH=$PATH:$JAVA_HOME/bin	&& \
cd /home		&& \
curl -L -O https://dlcdn.apache.org/hadoop/common/hadoop-3.3.4/hadoop-3.3.4.tar.gz	&& \
tar -xzvf hadoop-3.3.4.tar.gz	&& \
mv hadoop-3.3.4 /usr/lib/hadoop-3.3.4	&& \
export HADOOP_HOME=/usr/lib/hadoop-3.3.4	&& \
export PATH=$PATH:$HADOOP_HOME/bin	&& \
cd /home

# port numbers to export
#EXPOSE 8080

# change working directory
#WORKDIR ~


