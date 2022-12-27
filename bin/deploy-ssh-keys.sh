#!/usr/bin/env bash

sudo docker attach master01
/usr/sbin/sshd-keygen -A
/usr/sbin/sshd
ssh-keygen -t rsa -P '' -f ~/.ssh/id_rsa

sudo docker attach master02
/usr/sbin/sshd-keygen -A
/usr/sbin/sshd
ssh-keygen -t rsa -P '' -f ~/.ssh/id_rsa

sudo docker attach slave01
/usr/sbin/sshd-keygen -A
/usr/sbin/sshd
ssh-keygen -t rsa -P '' -f ~/.ssh/id_rsa

sudo docker attach slave02
/usr/sbin/sshd-keygen -A
/usr/sbin/sshd
ssh-keygen -t rsa -P '' -f ~/.ssh/id_rsa

sudo docker attach slave03
/usr/sbin/sshd-keygen -A
/usr/sbin/sshd
ssh-keygen -t rsa -P '' -f ~/.ssh/id_rsa


cat ~/.ssh/id_dsa.pub >> ~/.ssh/authorized_keys