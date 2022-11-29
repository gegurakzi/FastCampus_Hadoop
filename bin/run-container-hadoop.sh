#!/usr/bin/env bash

sudo docker run --name hadoop-container -p 9870:9870 -d -it centos7/hadoop:ws
