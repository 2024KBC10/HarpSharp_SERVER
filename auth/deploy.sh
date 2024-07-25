#!/bin/bash

mkdir -p /home/ubuntu/deploy-auth/zip/
echo "> 현재 실행 중인 Docker 컨테이너 pid 확인" >> /home/ubuntu/deploy/deploy-auth.log
CURRENT_PID=$(docker container ls -q)

if [ -z $CURRENT_PID ]
then
  echo "> 현재 구동중인 Docker 컨테이너가 없으므로 종료하지 않습니다." >> /home/ec2-user/deploy/deploy-auth.log
else
  echo "> docker stop $CURRENT_PID" # 현재 구동중인 Docker 컨테이너가 있다면 모두 중지
  docker stop $CURRENT_PID
  sleep 5
fi

cd /home/ubuntu/deploy-auth/zip/

docker build -t auth ./          # Docker Image 생성
docker network create harpsharp
docker run --name db --network=harpsharp -e MYSQL_ROOT_PASSWORD=wlghks24461! -d -p 3306:3306 -v mysql-data:/var/lib/mysql mysql:latest
docker run --name redis --network=harpsharp -d -p 6379:6379 -v redis-data:/data redis
docker run --name swagger --network=harpsharp -d -p 80:8080 swaggerapi/swagger-ui
docker run --name auth -- network=harpsharp -d -p 4242:4242 auth  # Docker Container 생성