#!/usr/bin/env bash

sudo docker exec --user root master01 /bin/bash && /usr/sbin/sshd-keygen -A && /usr/sbin/sshd && ssh-keygen -t rsa -P '' -f /root/.ssh/id_rsa

sudo docker exec --user root master02 /bin/bash && /usr/sbin/sshd-keygen -A && /usr/sbin/sshd && ssh-keygen -t rsa -P '' -f /root/.ssh/id_rsa

sudo docker exec --user root slave01 /bin/bash && /usr/sbin/sshd-keygen -A && /usr/sbin/sshd && ssh-keygen -t rsa -P '' -f /root/.ssh/id_rsa

sudo docker exec --user root slave02 /bin/bash && /usr/sbin/sshd-keygen -A && /usr/sbin/sshd && ssh-keygen -t rsa -P '' -f /root/.ssh/id_rsa

sudo docker exec --user root slave03 /bin/bash && /usr/sbin/sshd-keygen -A && /usr/sbin/sshd && ssh-keygen -t rsa -P '' -f /root/.ssh/id_rsa