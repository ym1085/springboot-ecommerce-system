#!/bin/sh
# gradle clean and build
# TODO: Gradle build 자동화 수정
#cd .
#./gradlew clean
#./gradlew bootJar

echo '=============================================================================================='
echo '| ⚠️ [주의] run_docker.sh 실행 시 로컬 Docker desktop의 모든 이미지를 삭제한 후 진행 됩니다.|'
echo '| ✏️ [주의] 빌드를 멈추시려면 ctrl + d || N을 입력해주세요.                                 |'
echo '=============================================================================================='

for i in {3..1}
do
  echo "Docker build 시작... $i 초 전.."
  sleep 1
done
echo

read -p "정말로 docker-compose build를 시작하시겠습니까? => (Y/N): " PROCEED_OPTION
echo

if [[ $PROCEED_OPTION == "N" || $PROCEED_OPTION == "n" || $PROCEED_OPTION == "" ]]; then
  echo "docker-compose build가 중단되었습니다...."
  exit 0
fi

for i in {5..1}
do
  echo "Docker build를 진행 합니다... $i 초 전.."
  sleep 1
done
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

# docker build 및 구동
docker-compose -f docker-compose.yaml up -d