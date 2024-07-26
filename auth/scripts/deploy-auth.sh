#!/bin/bash

mkdir -p /home/ubuntu/deploy-auth/zip/

docker rm  -f $(docker ps -a -q)
docker rmi -f $(docker images -q)

sudo service docker restart

cd /home/ubuntu/deploy-auth/zip/

docker build -t auth ./          # Docker Image 생성
aws s3 cp s3://haaaarp/HarpSharp_API_Auth.json ./

docker run --name db --network=harpsharp -e MYSQL_ROOT_PASSWORD=wlghks24461! -d -p 3306:3306 -v mysql-data:/var/lib/mysql mysql:latest
docker run --name redis --network=harpsharp -d -p 6379:6379 -v redis-data:/data redis
docker run -d -p 80:8080 --name swagger -e SWAGGER_JSON=/tmp/HarpSharp_API_Auth.json -v ./:/tmp swaggerapi/swagger-ui
docker run --name auth --network=harpsharp -d -p 4242:4242 auth  # Docker Container 생성
