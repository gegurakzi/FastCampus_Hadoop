#!/usr/bin/env bash

sudo docker exec master01 /usr/sbin/sshd-keygen -A && /usr/sbin/sshd && ssh-keygen -t rsa -P '' -f ~/.ssh/id_rsa

sudo docker exec master02 /usr/sbin/sshd-keygen -A && /usr/sbin/sshd && ssh-keygen -t rsa -P '' -f ~/.ssh/id_rsa

sudo docker exec slave01 /usr/sbin/sshd-keygen -A && /usr/sbin/sshd && ssh-keygen -t rsa -P '' -f ~/.ssh/id_rsa

sudo docker exec slave02 /usr/sbin/sshd-keygen -A && /usr/sbin/sshd && ssh-keygen -t rsa -P '' -f ~/.ssh/id_rsa

sudo docker exec slave03 /usr/sbin/sshd-keygen -A && /usr/sbin/sshd && ssh-keygen -t rsa -P '' -f ~/.ssh/id_rsa