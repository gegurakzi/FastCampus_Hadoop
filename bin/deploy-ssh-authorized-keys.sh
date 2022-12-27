#!/usr/bin/env bash

get_authorized_keys() {
  local m1=sudo docker exec master01 cat ~/.ssh/id_rsa.pub

  local m2=sudo docker exec master02 cat ~/.ssh/id_rsa.pub

  local s1=sudo docker exec slave01 cat ~/.ssh/id_rsa.pub

  local s2=sudo docker exec slave02 cat ~/.ssh/id_rsa.pub

  local s3=sudo docker exec slave03 cat ~/.ssh/id_rsa.pub

  echo $m1
  echo $m2
  echo $s1
  echo $s2
  echo $s3

}

get_authorized_keys