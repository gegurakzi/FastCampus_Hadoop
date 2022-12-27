#!/usr/bin/env bash

get_authorized_keys() {
  sudo docker attach master01
  local m1=cat ~/.ssh/id_rsa.pub
  exit
  sudo docker start master01

  sudo docker attach master02
  local m2=cat ~/.ssh/id_rsa.pub
  exit
  sudo docker start master02

  sudo docker attach slave01
  local s1=cat ~/.ssh/id_rsa.pub
  exit
  sudo docker start slave01

  sudo docker attach slave02
  local s2=cat ~/.ssh/id_rsa.pub
  exit
  sudo docker start slave02

  sudo docker attach slave03
  local s3=cat ~/.ssh/id_rsa.pub
  exit
  sudo docker start slave03

  echo m1
  echo m2
  echo s1
  echo s2
  echo s3

}

get_authorized_keys