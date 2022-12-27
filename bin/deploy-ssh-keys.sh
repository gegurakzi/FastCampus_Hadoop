#!/usr/bin/env bash

sudo docker exec master01 -itu 0 //bin/bash && /usr/sbin/sshd-keygen -A && /usr/sbin/sshd && ssh-keygen -t rsa -P '' -f ~/.ssh/id_rsa

sudo docker exec master02 -itu 0 //bin/bash && /usr/sbin/sshd-keygen -A && /usr/sbin/sshd && ssh-keygen -t rsa -P '' -f ~/.ssh/id_rsa

sudo docker exec slave01 -itu 0 //bin/bash && /usr/sbin/sshd-keygen -A && /usr/sbin/sshd && ssh-keygen -t rsa -P '' -f ~/.ssh/id_rsa

sudo docker exec slave02 -itu 0 //bin/bash && /usr/sbin/sshd-keygen -A && /usr/sbin/sshd && ssh-keygen -t rsa -P '' -f ~/.ssh/id_rsa

sudo docker exec slave03 -itu 0 //bin/bash && /usr/sbin/sshd-keygen -A && /usr/sbin/sshd && ssh-keygen -t rsa -P '' -f ~/.ssh/id_rsa