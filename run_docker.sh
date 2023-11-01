#!/bin/sh
echo '=================================================='
echo '|         Docker build 시작...                   |'
echo '=================================================='
echo

# docker 기존 image 삭제
DOCKER_IMAGES=$(docker images -q)
if [[ -n "$DOCKER_IMAGES" ]]; then
  echo "docker image 삭제 => $DOCKER_IMAGES"
  docker rmi $(docker images -q) -f
else
  echo "삭제할 docker image가 존재하지 않습니다."
fi
echo

for i in {3..1}
do
  echo "$i 초 대기 중..."
  sleep 1
done
echo

# docker build 및 구동
docker-compose -f docker-compose.yaml up -d