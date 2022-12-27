#!/usr/bin/env bash

get_authorized_keys() {
  local m1=$(sudo docker exec master01 cat /root/.ssh/id_rsa.pub)
  local m2=$(sudo docker exec master02 cat /root/.ssh/id_rsa.pub)
  local s1=$(sudo docker exec slave01 cat /root/.ssh/id_rsa.pub)
  local s2=$(sudo docker exec slave02 cat /root/.ssh/id_rsa.pub)
  local s3=$(sudo docker exec slave03 cat /root/.ssh/id_rsa.pub)

  echo $m1
  echo $m2
  echo $s1
  echo $s2
  echo $s3

  local auth_keys=${m1}$'\n'${m2}$'\n'${s1}$'\n'${s2}$'\n'${s3}$'\n'

  echo ${auth_keys}

  sudo docker exec -u 0 master01 sh -c 'echo ${auth_keys} > /root/.ssh/authorized_keys'
  sudo docker exec -u 0 master02 sh -c 'echo ${auth_keys} > /root/.ssh/authorized_keys'
  sudo docker exec -u 0 slave01 sh -c 'echo ${auth_keys} > /root/.ssh/authorized_keys'
  sudo docker exec -u 0 slave02 sh -c 'echo ${auth_keys} > /root/.ssh/authorized_keys'
  sudo docker exec -u 0 slave03 sh -c 'echo ${auth_keys} > /root/.ssh/authorized_keys'

}

get_authorized_keys