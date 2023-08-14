# TODO

---

`참고 자료`

- [[SNS LOGIN] 구글, 네이버 카카오 로그인 구현](https://developerbee.tistory.com/245)
- [[SNS LOGIN] SNS 로그인시 받는 DATA분석 및 활용](https://skarlsla.github.io/2019/07/25/webserver-auth5/)
- [[대댓글] 댓글 및 대댓글 설계](https://velog.io/@alstn_dev/%EA%B2%8C%EC%8B%9C%ED%8C%90-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EB%8C%93%EA%B8%80-%EB%B0%8F-%EB%8C%80%EB%8C%93%EA%B8%80)
- [[파일 업로드] 게시판 - 다중 파일 업로드 & 다운로드 구현](https://congsong.tistory.com/39)
- [[Builder Pattern]빌더 패턴(Builder pattern)을 써야하는 이유, @Builder](https://pamyferret.tistory.com/67)
- [[Clean Code] 주석](https://o-o-wl.tistory.com/34)
- [[여러 파일을 .zip 파일로 압축해서 다운로드]](https://jaimemin.tistory.com/2092)
- [[SpringBoot - zip 압축 및 다운로드 구현하기!]](https://jaehoney.tistory.com/325)
- [[Diff] Diff Checker](https://www.diffchecker.com/text-compare/)
- [[대댓글] MySQL 재귀쿼리](https://dalili.tistory.com/228)
- [[회원가입] 회원가입](https://congsong.tistory.com/37)
- [[OAuth 2 Login Flow] 쟈미의 devlog](https://jyami.tistory.com/121)

---

`2023. 05. 19`

- [x] 기본 테이블 설계
    - 권한 테이블[ROLE]
    - 소셜 로그인 회원 테이블[SOCIAL_MEMBER]
    - 회원 테이블[MEMBER]
    - 게시판 테이블[POST]
    - 댓글 테이블[COMMENT]
    - 파일 테이블[FILE]
- [x] 기본적인 프로젝트 환경 셋팅
    - [x] 멀티 모듈 프로젝트로 전환 시킴[멀티 모듈해야 나중에 MSA 처럼 분리 가능]
        - core:elastic-client
        - multi:bulletin-board[게시판용]
        - multi:movie-search[ES 검색 API용]

`2023. 05. 20`

- [x] 게시판 제작
    - [x] 전체 게시글 조회
    - [x] 단일 게시글 조회
    - [x] 게시글 저장
        - [x] 파일 업로드 처리

`2023. 05. 23`

- [x] 게시글 등록 테스트 코드 작성
- [x] 파일 업로드 테스트 코드 작성
- [x] 게시글 및 파일 수정 기능 구현

`2023. 05. 24`

- [x] 게시글 및 파일 수정 테스트 코드 작성
- [x] 게시글 및 파일 삭제 기능 구현
- [x] 게시글 삭제 테스트 로직 구현

`2023. 05. 25(목)`

- [x] 파일 다운로드 기능 구현

`2023. 05. 27(토)`

- [x] 게시판 페이징 처리 기능 구현
    - [x] 게시판 페이징 처리 테스트 코드 작성
    - [x] 게시판 검색 기능 추가
        - [x] 제목 기반 검색
        - [x] 내용 기반 검색
        - [x] 작성자 기반 검색
- [x] 게시글 조회 시 카테고리는 다른 테이블에 뺴서 조회 하도록 수정
- [x] 기존 Response 데이터 구조를 새로운 Dto를 생성(Message)하여 값이 나가도록 수정

`2023. 05. 28(일)`

- [ ] Spring Rest Docs 틀만 잡아둠 -> 일단 보류
- [x] 댓글 및 대댓글 기능 구현
- [x] 대댓글 저장 기능 구현
    - [x] 일반 댓글, 대댓글 구분해서 등록 가능하도록 수정...

`2023. 06. 10(토)`

- [x] MySQL 비밀번호 변경
- [x] Naver 로그인 오류 나는 부분 수정
- [x] 카카오 로그인 기능 추가

`2023. 07. xx(토)`

- [x] OAuth 2.0 로그인 시에 Spring Security 전용 핸들러 생성(Success, Fail) 하여 로그인 창으로 반환
- [x] 회원 가입 유효성 검증 로직 javascript 구현
- [x] 이메일 인증 발송 기능 구현
- [x] 이메일 인증 검증 기능 구현
- [ ] 회원 가입 시 Cookie, session 등록 기능 구현
- [ ] 회원 가입 후 해당 id, pwd 기반으로 로그인 되는지 확인 진행
    - [ ] SecurityConfig 파일에 passwordEncode 관련 설정 추가해야하는지 확인 필요