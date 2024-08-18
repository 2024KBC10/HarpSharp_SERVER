#!/bin/bash

runNetwork(){
  # 네트워크가 존재하는지 확인
  if [ -z "$(docker network ls | grep harpsharp)" ]
  then
      echo "harpsharp 네트워크를 생성합니다."
      docker network create harpsharp
  else
      echo "harpsharp 네트워크가 이미 존재합니다."
  fi

  if [ -z "$(docker network ls | grep datasource)" ]
    then
        echo "harpsharp 네트워크를 생성합니다."
        docker network create datasource
    else
        echo "harpsharp 네트워크가 이미 존재합니다."
    fi
}

serviceDown(){
  if [ $(docker ps -a -q -f name=$CONTAINER_NAME) ]
  then
      echo "컨테이너 $CONTAINER_NAME 종료 및 삭제 중..."

      IMAGE_ID=$(docker images -q $CONTAINER_NAME)

      if [ "$IMAGE_ID" ]; then
          echo "이미지 $CONTAINER_NAME 삭제 중..."

      docker rmi -f $IMAGE_ID
      fi
  fi
}

swaggerDown(){
  if [ $(docker ps -a -q -f name=$CONTAINER_NAME) ]
  then
      echo "컨테이너 $CONTAINER_NAME 종료 및 삭제 중..."
      docker stop $CONTAINER_NAME
      docker rm $CONTAINER_NAME
  fi
}
reloadNginx(){
  docker exec -it nginx nginx -s reload
}
cleanUpImages(){
  docker rmi $(docker images -f "dangling=true" -q)
  sudo docker system prune -af
}


runNetwork

cd /home/ubuntu/deploy/db/
docker-compose up -d

cd /home/ubuntu/deploy/auth
serviceDown auth
swaggerDown "swagger-auth"
docker-compose up --build -d

cd /home/ubuntu/deploy/board
serviceDown board
swaggerDown "swagger-board"
docker-compose up --build -d

cd /home/ubuntu/deploy/todo
serviceDown todo
swaggerDown "swagger-todo"
docker-compose up --build -d

cd /home/ubuntu/deploy/ai
mv env .env
serviceDown gpt4o
docker-compose up -d --build

cd /home/ubuntu/deploy/front
mv env.local .env.local
mv eslintrc.json .eslintrc.json
mv dockerignore .dockerignore
serviceDown "front-front-1"
docker-compose up -d --build

cd /home/ubuntu/deploy/swagger
serviceDown swagger
docker-compose up -d --build

reloadNginx
cleanUpImages


