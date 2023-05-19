# TODO

---

`참고 자료`

- [[SNS LOGIN] 구글, 네이버 카카오 로그인 구현](https://developerbee.tistory.com/245)
- [[SNS LOGIN] SNS 로그인시 받는 DATA분석 및 활용](https://skarlsla.github.io/2019/07/25/webserver-auth5/)
- [[대댓글] 댓글 및 대댓글 설계](https://velog.io/@alstn_dev/%EA%B2%8C%EC%8B%9C%ED%8C%90-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EB%8C%93%EA%B8%80-%EB%B0%8F-%EB%8C%80%EB%8C%93%EA%B8%80)

---

`2023-05-19`

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
    - movie:bulletin-board[게시판용]
    - movie:movie-search[ES 검색 API용]

---

# 구현 기능
- 영화 검색 기능
  - 일반 검색 [비회원, 회원]
  - 초성 검색 [비회원, 회원] -> x(보류)
- 회원 기능
  - 회원가입
  - 로그인/로그아웃
- 게시판 기능
  - 모든 게시글 및 특정 게시글 조회
  - 게시글 검색 (제목, 내용, 작성자)
  - 게시글 작성 [회원]
  - 게시글 수정 [회원, 게시글 작성자]
  - 게시글 삭제 [회원, 게시글 작성자]
  - 게시글 답글 작성 [회원]
- 댓글 기능
  - 댓글 조회
  - 댓글 작성 [회원]
  - 댓글 수정 [회원, 댓글 작성자]
  - 댓글 삭제 [회원, 댓글 작성자]
