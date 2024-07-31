#!/bin/bash

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


serviceDown "auth"
docker-compose up --build -d


cd /home/ubuntu/deploy/board/

serviceDown "board"
docker-compose up --build -d

cd /home/ubuntu/deploy/nginx/

serviceDown "nginx"
docker-compose up --build -d

docker rmi $(docker images -f "dangling=true" -q)



