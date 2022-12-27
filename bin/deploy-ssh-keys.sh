#!/usr/bin/env bash

sudo docker exec -itu 0 master01 /bin/bash && /usr/sbin/sshd-keygen -A && /usr/sbin/sshd && ssh-keygen -t rsa -P '' -f ~/.ssh/id_rsa

sudo docker exec -itu 0 master02 /bin/bash && /usr/sbin/sshd-keygen -A && /usr/sbin/sshd && ssh-keygen -t rsa -P '' -f ~/.ssh/id_rsa

sudo docker exec -itu 0 slave01 /bin/bash && /usr/sbin/sshd-keygen -A && /usr/sbin/sshd && ssh-keygen -t rsa -P '' -f ~/.ssh/id_rsa

sudo docker exec -itu 0 slave02 /bin/bash && /usr/sbin/sshd-keygen -A && /usr/sbin/sshd && ssh-keygen -t rsa -P '' -f ~/.ssh/id_rsa

sudo docker exec -itu 0 slave03 /bin/bash && /usr/sbin/sshd-keygen -A && /usr/sbin/sshd && ssh-keygen -t rsa -P '' -f ~/.ssh/id_rsa