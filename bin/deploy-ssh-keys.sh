#!/usr/bin/env bash

sudo docker exec master01 -u 0 /usr/sbin/sshd-keygen -A && /usr/sbin/sshd && ssh-keygen -t rsa -P '' -f ~/.ssh/id_rsa

sudo docker exec master02 -u 0 /usr/sbin/sshd-keygen -A && /usr/sbin/sshd && ssh-keygen -t rsa -P '' -f ~/.ssh/id_rsa

sudo docker exec slave01 -u 0 /usr/sbin/sshd-keygen -A && /usr/sbin/sshd && ssh-keygen -t rsa -P '' -f ~/.ssh/id_rsa

sudo docker exec slave02 -u 0 /usr/sbin/sshd-keygen -A && /usr/sbin/sshd && ssh-keygen -t rsa -P '' -f ~/.ssh/id_rsa

sudo docker exec slave03 -u 0 /usr/sbin/sshd-keygen -A && /usr/sbin/sshd && ssh-keygen -t rsa -P '' -f ~/.ssh/id_rsa