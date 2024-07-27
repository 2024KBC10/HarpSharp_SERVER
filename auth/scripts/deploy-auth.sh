#!/bin/bash

mkdir -p /home/ubuntu/deploy-auth/zip/

docker rm  -f $(docker ps -a -q)

cd /home/ubuntu/deploy-auth/zip/

# 네트워크가 존재하는지 확인
if [ -z "$(docker network ls | grep harpsharp)" ]
then
    echo "harpsharp 네트워크를 생성합니다."
    docker network create harpsharp
else
    echo "harpsharp 네트워크가 이미 존재합니다."
fi

# 실행 중인 컨테이너의 포트를 kill
echo "실행 중인 컨테이너의 포트를 kill합니다."
docker ps -q | xargs -r docker kill

docker-compose up --build -d