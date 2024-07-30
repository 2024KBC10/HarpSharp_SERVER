#!/bin/bash

# 네트워크가 존재하는지 확인
if [ -z "$(docker network ls | grep harpsharp)" ]
then
    echo "harpsharp 네트워크를 생성합니다."
    docker network create harpsharp
else
    echo "harpsharp 네트워크가 이미 존재합니다."
fi

cd /home/ubuntu/deploy/db/

docker-compose up -d


cd /home/ubuntu/deploy/auth/

if [ $(docker ps -a -q -f name=auth) ]; then
    docker stop auth && docker rm auth
fi
if [ $(docker images -q auth) ]; then
    docker rmi auth
fi

# 'swagger-board' 컨테이너와 이미지 삭제
if [ $(docker ps -a -q -f name=swagger-auth) ]; then
    docker stop swagger-auth && docker rm swagger-auth
fi
if [ $(docker images -q swagger-auth) ]; then
    docker rmi swagger-auth
fi

docker-compose up --build -d


cd /home/ubuntu/deploy/board/

# 'board' 컨테이너와 이미지 삭제
if [ $(docker ps -a -q -f name=board) ]; then
    docker stop board && docker rm board
fi
if [ $(docker images -q board) ]; then
    docker rmi board
fi

# 'swagger-board' 컨테이너와 이미지 삭제
if [ $(docker ps -a -q -f name=swagger-board) ]; then
    docker stop swagger-board && docker rm swagger-board
fi
if [ $(docker images -q swagger-board) ]; then
    docker rmi swagger-board
fi

docker-compose up --build -d





