# 🛒 스프링 부트로 만드는 쇼핑몰 프로젝트

## ⛏ 01. 사용 기술

### 📌 01-1. Backend
| 기술              | 버전    | 적용 여부 |
|-----------------|-------|------|
| Java            | 11    | o    |
| Spring Boot     | 2.7.1 | o    |
| Spring Security | 2.7.2 | o    |
| MyBatis         | 3.0.1 | o    |
| MySQL           | 8.0.28 | o    |
| Swagger         | 3.0.0 | o    |
| Docker          | 23.0.5 | o    |
| AWS EC2         |       | x    |

## ✅ 02. ERD

> ⚠️ DB 설계의 경우 지속적으로 수정을 하고 있습니다. 아래 링크 참고해주세요.

- [dbdiagram.io](https://dbdiagram.io/d/20231015_TOY_PROJECT_DB_DIAGRAM-652b82a9ffbf5169f0b329e7)

## 📜 03. API Docs

> ✏️ [swagger Document](http://localhost:8080/swagger-ui/index.html)

## 🚀 04. 프로젝트 실행 방법

### [classpath:/resources/application.yaml 파일 수정](./app/shop/src/main/resources/application-sample.yaml)

> 구글 이메일 전송 기능이 존재하기에 구글 앱 비밀번호 생성 후 application.yaml 파일에 입력

### [classpath:/resources/application-oauth.yaml](./app/shop/src/main/resources/application-oauth-sample.yaml)

> 소셜 로그인을 위해서 각 플랫폼별로 제공해주는 클라이언트 ID, Secret을 기재 필요  
> application-oauth-sample.yaml 설정 후 파일 이름 변경 -> application-oauth.yaml

### ⚙️ 파일 권한 변경

```shell
chmod +x run_docker.sh
chmod +x stop_docker.sh
```

- 파일 실행 권한 변경

### 🐳 docker-compose up

```shell 
./start_docker.sh
```

- run_docker.sh 실행시 로컬 Docker desktop의 모든 이미지를 지우고 이미지 생성
- 위 shell script는 유의하여 사용할 필요가 존재합니다
- gradle clean bootJar 진행 후 생성된 jar 파일을 가지고 docker 실행

### 🐳 docker-compose down

```shell
./stop_docker.sh
```

- docker 서버 중지

### 📂 참고

```shell
추가적으로 파일 업로드의 경우 본인 운영체제 맞춰서 업로드 하나  
디렉토리 이름은 본인 컴퓨터 환경에 맞춰서 작성해주시면 됩니다(application-dev.yaml)
```
