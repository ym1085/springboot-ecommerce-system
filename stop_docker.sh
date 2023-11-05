#!/bin/sh
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

# docker 중지
docker-compose -f docker-compose-${PROFILE}.yaml down -v