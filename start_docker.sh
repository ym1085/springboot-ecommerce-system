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
read -p "PROFILE 옵션을 숫자로 선택 해주세요 (1:dev or 2:prod): " OPTION
echo

case $OPTION in
  1)
    PROFILE="dev"
    ;;
  2)
    PROFILE="prod"
    ;;
  3)
    echo "Invalid option. Exiting..."
    exit 1
    ;;
esac
echo "👌 선택된 배포 PROFILE 옵션 => $PROFILE"
echo

if [ -z "$PROFILE" ]; then
  echo "please enter profile..."
  exit 1
fi

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

APP_PATH=$(pwd)
echo 'spring boot jar -> bootJar를 위해 -> APP 폴더 경로로 이동 전 ==> ' $APP_PATH
APP_PATH=./app/shop/
cd $APP_PATH
echo 'spring boot jar -> bootJar를 위해 -> APP 폴더 경로로 이동 후 ==> '$APP_PATH

echo 'APP 빌드 시작..'
echo

echo 'gradle clean..'
sh ./gradlew clean

for i in {5..1}
do
  echo "gradle clean 후 5초 대기... $i 초 전.."
  sleep 1
done
echo

echo
echo 'gradle bootJar..'
sh ./gradlew bootJar
echo

echo 'APP 빌드 완료..'
echo '현재 경로 => ' $APP_PATH
echo

# 빌드 완료 후 루트 경로로 이동하여 docker-compose-{profile}.yaml 파일 실행
cd ../../
APP_PATH=$(pwd)
echo '빌드 완료 후 폴더 변경, 현재 경로 => ' $APP_PATH

sleep 3

# docker build 및 구동
docker-compose -f docker-compose-${PROFILE}.yaml up -d