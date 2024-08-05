# 🛒 쇼핑몰 프로젝트

## ⛏ 01. 사용 기술

### 📌 Backend

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

## 📜 API Docs

> ✏️ [swagger Document](http://localhost:8080/swagger-ui/index.html)

## 🚀 프로젝트 추가 설정

### [application.yaml](./app/shop/src/main/resources/application-sample.yaml)

- 구글 이메일 전송 기능이 존재, 개인 구글 앱 비밀번호 생성 후 application.yaml 파일에 입력

### [application-oauth.yaml](./app/shop/src/main/resources/application-oauth-sample.yaml)

- 소셜 로그인을 위해서 각 플랫폼별로 제공해주는 클라이언트 ID, Secret을 기재 필요
- application-oauth-sample.yaml 설정 후 파일 이름 변경 -> application-oauth.yaml