# Spring Security

- [Spring Security](https://its-ward.tistory.com/entry/Spring-Security%EC%99%80Oauth-20-%EA%B0%9C%EB%85%90-%EC%95%8C%EC%95%84%EB%B3%B4%EA%B8%B0)

- 막강한 **인증**(**Authentication**)과 **인가**(**Authorization**) 기능을 가진 **프레임워크**
- 스프링 애플리케이션의 **보안 표준**이라 봐도 무방
- **인터셉터, 필터 기반**의 보안 기능 구현보다는 `시큐리티 사용을 적극 권장`

# @EnableWebSecurity

> Spring Version 업그레이드에 따라 WebSecurityConfigurerAdapter 상속이 deprecated 되었다.  
> https://ksyy.tistory.com/307

- **WebSecurityConfigurerAdapter**를 상속받는 클래스(SecurityConfig)에 @EnableWebSecurity 선언 시 `SpringSecurityFilterChain`이 자동 포함됨
- **WebSecurityConfigurerAdapter** 상속 후 메서드 오버라이팅을 통해 보안 설정 커스텀 가능

# OAuth 2.0(Open Authorization 2.0, OAuth 2.0)

- 인증을 위한 개방형 표준 프로토콜(`스프링만의 것이 아닌 표준 프로토콜`)
- 클라이언트가 비밀번호를 제공하지 않고 다른 웹 상의 자신들의 정보에 대해 웹 사이트나 애플리케이션의  
접근 권한을 부여할 수 있는 공통적인 수단으로서 사용되는 접근 위임을 위한 개방형 표준 프로토콜.

> OAuth 2.0 사용 하는 이유는?

- 최근 들어서는 ID, Password 기반의 로그인 보다는 구글, 페이스북, 네이버와 같은 소셜 로그인 기능 사용
- 이러한 소셜 로그인 기능을 사용하는 이유는?
  - 로그인 기능을 구현하다 보면 배보다 배꼽이 더 커지는 경우가 발생
  - 공수가 많이 들어간다는 의미로 다음과 같은 기능 구현 필요
    - 로그인 시 보안
    - 회원가입 시 이메일 또는 전화번호 인증
    - 비밀번호 찾기
    - 비밀번호 변경
    - 회원정보 변경
  - OAuth 2.0 로그인 구현 시 위 목록 대체 가능
  - 개발자는 비즈니스 로직 작성에만 집중 가능

> 결론은 로그인 관련을 프레임워크를 통해 제공 받아서 사용하여 효율적인 개발을 하기 위함.

# 스프링 부트 1.5 vs 스프링 부트 2.0

- spring boot 1.5 -> 2.0으로 넘어오면서 OAuth2 연동 방법이 크게 변경됨
- 설정 방법에는 크게 차이가 없음 -> 이는 아래 라이브러리 덕분이다

```groovy
// build.gradle 파일에 spring-security-oauth2-autoconfigure lib를 추가해준다
implementation 'org.springframework.security.oauth.boot:spring-security-oauth2-autoconfigure' // oauth 2.0
```

- build.gradle에 oauth2 security 관련 설정 추가

```yaml
# spring boot 1.5 시절
google:
  client:
    clientId: 인증정보
    clientSecret: 인증정보
    accessTokenUri: https://acccounts.google.com/o/oauth2/token
    userAuthorizationUri: https://accounts.google.com/o/oauth2/auth
    clientAuthenticationScema: form
    scope: email, profile
  resource:
    userInfoUri: https://www.googleapis.com/oauth2/v2/userinfo
```

- spring boot 1.5 시절의 oauth2 설정

```yaml
# spring boot 2.x
spring:
  security:
    oauth2:
      client:
        clientId: 인증 정보
        clientSecret: 인증 정보
```

- spring boot 2.x 이후의 설정
- 1.5 버전보다 훨씬 간결하게 작성이 가능
