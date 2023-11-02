FROM mysql:latest

WORKDIR /

# 파일을 그대로 복사, 복사만 하고 다른 행동 안함
# 로컬 파일만 복사 -> 이미지 추가 가능
#COPY ./initdb.d /docker-entrypoint-initdb.d

# 압축 파일이면 압축을 해제한 상태로 파일을 복사
# 외부 URL, tar 파일에서도 파일 추가 가능
ADD init /docker-entrypoint-initdb.d