#!/bin/bash

mkdir -p /home/ubuntu/deploy/nginx-zip/

cd /home/ubuntu/deploy/nginx-zip/

# 네트워크가 존재하는지 확인
if [ -z "$(docker network ls | grep harpsharp)" ]
then
    echo "harpsharp 네트워크를 생성합니다."
    docker network create harpsharp
else
    echo "harpsharp 네트워크가 이미 존재합니다."
fi

docker-compose up --build -d --no-recreate