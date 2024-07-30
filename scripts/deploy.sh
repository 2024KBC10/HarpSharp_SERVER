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


auth_ids=$(docker images --format '{{.Repository}}:{{.Tag}}' | grep '_auth$' | xargs -n1 docker images --format '{{.ID}}' --filter=reference)

if [ "$auth_ids" ]; then
    docker rmi -f $auth_ids
else
    echo "No images ending with '_auth' found."
fi


docker-compose up --build -d


cd /home/ubuntu/deploy/board/

board_ids=$(docker images --format '{{.Repository}}:{{.Tag}}' | grep '_board$' | xargs -n1 docker images --format '{{.ID}}' --filter=reference)

if [ "$board_ids" ]; then
    docker rmi -f $board_ids
else
    echo "No images ending with '_board$' found."
fi


docker-compose up --build -d

cd /home/ubuntu/deploy/nginx/
docker exec -it nginx /bin/bash << EOF
service nginx restart
exit
EOF

docker rmi $(docker images -f "dangling=true" -q)



