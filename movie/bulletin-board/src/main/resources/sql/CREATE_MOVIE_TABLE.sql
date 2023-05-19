USE movie;
-- 1정규화 : 하나의 행에 하나의 데이터만 삽입
-- 2정규화 : 현재 테이블의 주제와 관련없는 컬럼을 다른 테이블로 뺴는 작업

-- ROLE
CREATE TABLE ROLE (
    role_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    role_name VARCHAR(5) NOT NULL DEFAULT 'ROLE_USER' COMMENT '역할 이름' -- 역할 이름에 대한 주석
);

-- SOCIAL_MEMBER
CREATE TABLE SOCIAL_MEMBER (
    social_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    member_id BIGINT COMMENT '회원 ID',
    provider_type VARCHAR(30) COMMENT '소셜 로그인 타입',
    provider_token VARCHAR(255) COMMENT '소셜 로그인 토큰',
    FOREIGN KEY (member_id) REFERENCES MEMBER(member_id)
);

-- MEMBER
CREATE TABLE MEMBER (
    member_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(20) NOT NULL COMMENT '회원명',
    account VARCHAR(30) NOT NULL COMMENT '회원 아이디',
    password VARCHAR(50) NOT NULL COMMENT '회원 비밀번호',
    email VARCHAR(50) NOT NULL COMMENT '회원 이메일',
    phone_number VARCHAR(15) NOT NULL COMMENT '회원 휴대폰번호',
    image_url VARCHAR(200) COMMENT '회원 이미지 URL',
    birth_date VARCHAR(12) COMMENT '생년월일 (예: 1993-09-23)',
    use_yn CHAR(1) DEFAULT 'N' COMMENT '회원 계정 사용 여부',
    cert_yn CHAR(1) DEFAULT 'N' COMMENT '회원 이메일 인증 여부',
    role_id BIGINT COMMENT '회원 권한',
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '가입일',
    update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일',
    FOREIGN KEY (role_id) REFERENCES ROLE(role_id)
);

-- POST
CREATE TABLE POST (
    post_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(40) NOT NULL COMMENT '제목',
    content NVARCHAR(500) NOT NULL COMMENT '내용',
    member_id BIGINT(1) NOT NULL COMMENT '작성자 번호',
    read_cnt INT NOT NULL DEFAULT 0 COMMENT '조회수',
    fixed_yn CHAR(1) DEFAULT 'N' COMMENT '고정글 여부',
    del_yn CHAR(1) DEFAULT 'N' COMMENT '게시글 삭제 여부',
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '생성일',
    update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일',
    delete_date TIMESTAMP COMMENT '삭제일',
    FOREIGN KEY (member_id) REFERENCES MEMBER(member_id)
);

-- COMMENT
CREATE TABLE COMMENT (
    comment_id INT PRIMARY KEY AUTO_INCREMENT,
    post_id BIGINT COMMENT '게시글 번호',
    parent_id INT COMMENT '댓글 부모 번호',
    content TEXT COMMENT '댓글 내용',
    member_id BIGINT NOT NULL COMMENT '회원 번호',
    del_yn CHAR(1) DEFAULT 'N' COMMENT '댓글 삭제 여부',
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '생성일',
    update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일',
    comment_depth INT DEFAULT 0 COMMENT '댓글의 깊이 (일반 댓글: 0, 대댓글: 1)',
    comment_group INT COMMENT '대댓글인 경우 comment_id값을 저장하여 누구의 대댓글인지 확인',
    FOREIGN KEY (post_id) REFERENCES POST(post_id),
    FOREIGN KEY (member_id) REFERENCES MEMBER(member_id)
);

-- FILE
CREATE TABLE FILE (
    file_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    post_id BIGINT NOT NULL COMMENT '게시글 ID',
    file_name VARCHAR(255) NOT NULL COMMENT '파일 이름',
    file_path VARCHAR(255) NOT NULL COMMENT '파일 경로',
    ori_file_name VARCHAR(255) NOT NULL COMMENT '원본 파일 이름',
    file_size INT NOT NULL COMMENT '파일 크기',
    file_type VARCHAR(50) NOT NULL COMMENT '파일 타입',
    download_cnt INT DEFAULT 0 COMMENT '다운로드 횟수',
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '생성일',
    update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일',
    FOREIGN KEY (post_id) REFERENCES POST(post_id)
);
