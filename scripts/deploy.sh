#!/bin/bash

# 네트워크가 존재하는지 확인
if [ -z "$(docker network ls | grep harpsharp)" ]
then
    echo "harpsharp 네트워크를 생성합니다."
    docker network create harpsharp
else
    echo "harpsharp 네트워크가 이미 존재합니다."
fi

cd /home/ubuntu/deploy/db

docker-compose up -d

docker stop auth | rm auth | rmi auth
docker stop swagger-auth | rm swagger-auth | rmi swagger-auth

cd /home/ubuntu/deploy-auth/

docker-compose up --build -d

docker stop board | rm board | rmi board
docker stop swagger-board | rm swagger-board | rmi swagger-board

cd /home/ubuntu/deploy/board/

docker-compose up --build -d





