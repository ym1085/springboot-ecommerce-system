# Search System with Bulletin Board

---

간단한 쇼핑몰 제작을 위한 웹 애플리케이션 프로젝트 입니다.

`Spring Boot`를 활용하여 시스템을 구축할 예정입니다.

해당 프로젝트를 통해 이루고자 하는 목표는 회원 시스템을 구축하면서 인증, 인가에 대한 부분을  
자세히 알아보고 간단한 쇼핑몰 시스템을 구축하기 위함입니다.

# 📁 목차

- 사용 기술
- 구현 기능
- API 명세서
- ERD 설계

# ⛏ 사용 기술

## 📌 Backend
| 기술               | 버전     |
|------------------|--------|
| Java             | 11     |
| Spring Boot      | 2.7.1  |
| Spring Security  | 2.7.2  |
| MyBatis          | 3.0.1  |
| MySQL Connector J | 8.0.28 |
| Swagger          | 3.0.0  |
| Elasticsearch    | 7.10.0 |
| Docker           | 23.0.5 |

# 🎢 구현 기능
- 검색 기능
  - 일반 검색 [비회원, 회원]
  - 초성 검색 [비회원, 회원] -> x(보류)
- 회원 기능
  - 회원가입
    - 소셜 로그인(구글, 네이버, 카카오 - OAuth 2.0)
  - 로그인/로그아웃
- 게시판 기능
  - 모든 게시글 및 특정 게시글 조회
  - 게시글 검색 (제목, 내용, 작성자)
  - 게시글 작성 [회원]
  - 게시글 수정 [회원, 게시글 작성자]
  - 게시글 삭제 [회원, 게시글 작성자]
  - 게시글 답글 작성 [회원]
  - 다중 파일 업로드 [회원, 게시글 작성자]
  - 다중 파일 다운로드 [회원, 게시글 작성자]
  - 다중 파일 압축 다운로드 [회원, 게시글 작성자]
  - 대댓글 조회 [회원, 게시글 작성자] - 대댓글 depth는 1단계만 가능하도록 구현 
  - 댓글 작성 [회원]
  - 댓글 수정 [회원, 댓글 작성자]
  - 댓글 삭제 [회원, 댓글 작성자]

# 소셜 로그인

```yaml
# application-oauth-sample.yaml
spring:
  security:
    oauth2:
      client:
        registration:
          google:
            client-id: 클라이언트 ID
            client-secret: 클라이언트 Secret
            scope:
              - profile
              - email
          naver:
            client-id: 클라이언트 ID
            client-secret: 클라이언트 Secret
            redirect-uri: "{baseUrl}/{action}/oauth2/code/{registrationId}"
            authorization-grant-type: authorization_code
            scope:
              - name
              - email
              - profile_image
            client-name: Naver
          kakao:
            client-id: 클라이언트 ID
            client-secret: 클라이언트 Secret
            client-name: Kakao
            scope:
              - account_email
              - profile_nickname
              - profile_image
            authorization-grant-type: authorization_code
            redirect-uri: http://localhost:8080/login/oauth2/code/kakao
            client-authentication-method: POST
        provider:
          naver:
            authorization-uri: https://nid.naver.com/oauth2.0/authorize
            token-uri: https://nid.naver.com/oauth2.0/token
            user-info-uri: https://openapi.naver.com/v1/nid/me
            user-name-attribute: response
          kakao:
            authorization-uri: https://kauth.kakao.com/oauth/authorize
            token-uri: https://kauth.kakao.com/oauth/token
            user-info-uri: https://kapi.kakao.com/v2/user/me
            user-name-attribute: id

```

- 로그인의 경우 각 업체에서 제공해주는 ClientId, ClientSecret을 기재해야 합니다.

# API 명세서

# ERD 설계
