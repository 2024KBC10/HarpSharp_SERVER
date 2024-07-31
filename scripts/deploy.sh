#!/bin/bash

runDB(){
  MYSQL_CONTAINER="mysql"
  REDIS_CONTAINER="redis"

  # MySQL 컨테이너가 실행 중인지 확인
  MYSQL_RUNNING=$(docker ps | grep -q $MYSQL_CONTAINER; echo $?)

  # Redis 컨테이너가 실행 중인지 확인
  REDIS_RUNNING=$(docker ps | grep -q $REDIS_CONTAINER; echo $?)

  # MySQL 또는 Redis 컨테이너가 실행 중이지 않으면 docker-compose up 실행
  if [ $MYSQL_RUNNING -ne 0 ] || [ $REDIS_RUNNING -ne 0 ]; then
    echo "MySQL 또는 Redis 컨테이너가 실행 중이지 않습니다. docker-compose up을 실행합니다."
    cd /home/ubuntu/deploy/db/
    docker-compose up -d
  else
    echo "MySQL 및 Redis 컨테이너가 이미 실행 중입니다."
  fi
}

runNetwork(){
  # 네트워크가 존재하는지 확인
  if [ -z "$(docker network ls | grep harpsharp)" ]
  then
      echo "harpsharp 네트워크를 생성합니다."
      docker network create harpsharp
  else
      echo "harpsharp 네트워크가 이미 존재합니다."
  fi
}

serviceDown(){
  if [ $(docker ps -a -q -f name=$CONTAINER_NAME) ]; then
      echo "컨테이너 $CONTAINER_NAME 종료 및 삭제 중..."

      IMAGE_ID=$(docker images -q $CONTAINER_NAME)

      if [ "$IMAGE_ID" ]; then
          echo "이미지 $CONTAINER_NAME 삭제 중..."

      docker rmi -f $IMAGE_ID
      fi
  fi
}

serviceUp(){
  cd /home/ubuntu/deploy/$SERVICE_NAME
  serviceDown $SERVICE_NAME
  docker-compose up --build -d
}

restartNginx(){
  docker exec nginx nginx -s reload
}

cleanUpImages(){
  docker rmi $(docker images -f "dangling=true" -q)
}


runNetwork
runDB

serviceUp "auth"
serviceUp "board"
serviceUp "todo"

restartNginx
cleanUpImages



