USE movie;
-- 1정규화 : 하나의 행에 하나의 데이터만 삽입
-- 2정규화 : 현재 테이블의 주제와 관련없는 컬럼을 다른 테이블로 뺴는 작업

-- TABLE : ROLE
CREATE TABLE ROLE (
    role_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    role_name VARCHAR(5) NOT NULL DEFAULT 'ROLE_USER' COMMENT '역할 이름' -- 역할 이름에 대한 주석
);

-- INSERT INTO ROLE
select * from role;
INSERT INTO ROLE (role_name)
VALUES
    ('ROLE_USER'),
    ('ROLE_GUEST'),
    ('ROLE_ADMIN');

-- TABLE : SOCIAL_MEMBER
CREATE TABLE SOCIAL_MEMBER (
    social_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    member_id BIGINT COMMENT '회원 ID',
    provider_type VARCHAR(30) COMMENT '소셜 로그인 타입',
    provider_token VARCHAR(255) COMMENT '소셜 로그인 토큰',
    FOREIGN KEY (member_id) REFERENCES MEMBER(member_id)
);

-- TABLE : MEMBER
select * from member;

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

-- INSERT INTO MEMBER
INSERT INTO MEMBER (name, account, password, email, phone_number, image_url, birth_date, use_yn, cert_yn, role_id)
VALUES
    ('회원1', 'user1', 'password1', 'user1@example.com', '010-1111-1111', NULL, '1993-09-23', 'Y', 'Y', 1),
    ('회원2', 'user2', 'password2', 'user2@example.com', '010-2222-2222', NULL, '1994-05-15', 'Y', 'N', 2),
    ('회원3', 'user3', 'password3', 'user3@example.com', '010-3333-3333', NULL, '1995-12-07', 'Y', 'Y', 1),
    ('회원4', 'user4', 'password4', 'user4@example.com', '010-4444-4444', NULL, '1996-03-29', 'Y', 'N', 3),
    ('회원5', 'user5', 'password5', 'user5@example.com', '010-5555-5555', NULL, '1997-08-11', 'Y', 'Y', 1),
    ('회원6', 'user6', 'password6', 'user6@example.com', '010-6666-6666', NULL, '1998-01-03', 'Y', 'N', 2),
    ('회원7', 'user7', 'password7', 'user7@example.com', '010-7777-7777', NULL, '1999-11-21', 'Y', 'Y', 3),
    ('회원8', 'user8', 'password8', 'user8@example.com', '010-8888-8888', NULL, '2000-06-14', 'Y', 'N', 1),
    ('회원9', 'user9', 'password9', 'user9@example.com', '010-9999-9999', NULL, '2001-09-05', 'Y', 'Y', 2),
    ('회원10', 'user10', 'password10', 'user10@example.com', '010-1010-1010', NULL, '2002-02-27', 'Y', 'N', 3);

-- TABLE : POST
-- AUTO_INCREMENT Start with 1
-- DELETE FROM post where create_date = '2023-05-20 17:11:53';
ALTER TABLE POST AUTO_INCREMENT = 1;
SELECT * FROM post;

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

-- INSERT INTO POST
-- PostServiceImplTest.java 파일에 데이터 밀어넣는 코드 있습니다
# INSERT INTO POST (title, content, member_id, read_cnt, fixed_yn, del_yn)
# VALUES
#     ('제목1', '내용1', 1, 0, 'N', 'N'),
#     ('제목2', '내용2', 2, 0, 'N', 'N'),
#     ('제목3', '내용3', 3, 0, 'N', 'N'),
#     ('제목4', '내용4', 4, 0, 'N', 'N'),
#     ('제목5', '내용5', 5, 0, 'N', 'N'),
#     ('제목6', '내용6', 6, 0, 'N', 'N'),
#     ('제목7', '내용7', 7, 0, 'N', 'N'),
#     ('제목8', '내용8', 8, 0, 'N', 'N'),
#     ('제목9', '내용9', 9, 0, 'N', 'N'),
#     ('제목10', '내용10', 10, 0, 'N', 'N'),
#     ('제목11', '내용11', 1, 0, 'N', 'N'),
#     ('제목12', '내용12', 2, 0, 'N', 'N'),
#     ('제목13', '내용13', 3, 0, 'N', 'N'),
#     ('제목14', '내용14', 4, 0, 'N', 'N'),
#     ('제목15', '내용15', 5, 0, 'N', 'N'),
#     ('제목16', '내용16', 6, 0, 'N', 'N'),
#     ('제목17', '내용17', 7, 0, 'N', 'N'),
#     ('제목18', '내용18', 8, 0, 'N', 'N'),
#     ('제목19', '내용19', 9, 0, 'N', 'N'),
#     ('제목20', '내용20', 10, 0, 'N', 'N'),
#     ('제목21', '내용21', 1, 0, 'N', 'N'),
#     ('제목22', '내용22', 1, 0, 'N', 'N'),
#     ('제목23', '내용23', 2, 0, 'N', 'N'),
#     ('제목24', '내용24', 2, 0, 'N', 'N'),
#     ('제목25', '내용25', 4, 0, 'N', 'N'),
#     ('제목26', '내용26', 4, 0, 'N', 'N'),
#     ('제목27', '내용27', 4, 0, 'N', 'N'),
#     ('제목28', '내용28', 4, 0, 'N', 'N'),
#     ('제목29', '내용29', 8, 0, 'N', 'N'),
#     ('제목30', '내용30', 9, 0, 'N', 'N'),
#     ('제목31', '내용31', 10, 0, 'N', 'N'),
#     ('제목32', '내용32', 1, 0, 'N', 'N'),
#     ('제목33', '내용33', 1, 0, 'N', 'N'),
#     ('제목34', '내용34', 2, 0, 'N', 'N'),
#     ('제목35', '내용35', 2, 0, 'N', 'N'),
#     ('제목36', '내용36', 4, 0, 'N', 'N'),
#     ('제목37', '내용37', 4, 0, 'N', 'N'),
#     ('제목38', '내용38', 4, 0, 'N', 'N'),
#     ('제목39', '내용39', 4, 0, 'N', 'N'),
#     ('제목40', '내용40', 8, 0, 'N', 'N'),
#     ('제목41', '내용41', 1, 0, 'N', 'N'),
#     ('제목42', '내용42', 2, 0, 'N', 'N'),
#     ('제목43', '내용43', 3, 0, 'N', 'N'),
#     ('제목44', '내용44', 4, 0, 'N', 'N'),
#     ('제목45', '내용45', 5, 0, 'N', 'N'),
#     ('제목46', '내용46', 6, 0, 'N', 'N'),
#     ('제목47', '내용47', 7, 0, 'N', 'N'),
#     ('제목48', '내용48', 8, 0, 'N', 'N'),
#     ('제목49', '내용49', 9, 0, 'N', 'N'),
#     ('제목50', '내용50', 10, 0, 'N', 'N'),
#     ('제목51', '내용51', 1, 0, 'N', 'N'),
#     ('제목52', '내용52', 2, 0, 'N', 'N'),
#     ('제목53', '내용53', 3, 0, 'N', 'N'),
#     ('제목54', '내용54', 4, 0, 'N', 'N'),
#     ('제목55', '내용55', 5, 0, 'N', 'N'),
#     ('제목56', '내용56', 6, 0, 'N', 'N'),
#     ('제목57', '내용57', 7, 0, 'N', 'N'),
#     ('제목58', '내용58', 8, 0, 'N', 'N'),
#     ('제목59', '내용59', 9, 0, 'N', 'N'),
#     ('제목60', '내용60', 10, 0, 'N', 'N'),
#     ('제목61', '내용61', 1, 0, 'N', 'N'),
#     ('제목62', '내용62', 2, 0, 'N', 'N'),
#     ('제목63', '내용63', 3, 0, 'N', 'N'),
#     ('제목64', '내용64', 4, 0, 'N', 'N'),
#     ('제목65', '내용65', 5, 0, 'N', 'N'),
#     ('제목66', '내용66', 6, 0, 'N', 'N'),
#     ('제목67', '내용67', 7, 0, 'N', 'N'),
#     ('제목68', '내용68', 8, 0, 'N', 'N'),
#     ('제목69', '내용69', 9, 0, 'N', 'N'),
#     ('제목70', '내용70', 10, 0, 'N', 'N'),
#     ('제목71', '내용71', 1, 0, 'N', 'N'),
#     ('제목72', '내용72', 2, 0, 'N', 'N'),
#     ('제목73', '내용73', 3, 0, 'N', 'N'),
#     ('제목74', '내용74', 4, 0, 'N', 'N'),
#     ('제목75', '내용75', 5, 0, 'N', 'N'),
#     ('제목76', '내용76', 6, 0, 'N', 'N'),
#     ('제목77', '내용77', 7, 0, 'N', 'N'),
#     ('제목78', '내용78', 8, 0, 'N', 'N'),
#     ('제목79', '내용79', 9, 0, 'N', 'N'),
#     ('제목80', '내용80', 10, 0, 'N', 'N'),
#     ('제목81', '내용81', 1, 0, 'N', 'N'),
#     ('제목82', '내용82', 2, 0, 'N', 'N'),
#     ('제목83', '내용83', 3, 0, 'N', 'N'),
#     ('제목84', '내용84', 4, 0, 'N', 'N'),
#     ('제목85', '내용85', 5, 0, 'N', 'N'),
#     ('제목86', '내용86', 6, 0, 'N', 'N'),
#     ('제목87', '내용87', 7, 0, 'N', 'N'),
#     ('제목88', '내용88', 8, 0, 'N', 'N'),
#     ('제목89', '내용89', 9, 0, 'N', 'N'),
#     ('제목90', '내용90', 10, 0, 'N', 'N'),
#     ('제목91', '내용91', 1, 0, 'N', 'N'),
#     ('제목92', '내용92', 2, 0, 'N', 'N'),
#     ('제목93', '내용93', 3, 0, 'N', 'N'),
#     ('제목94', '내용94', 4, 0, 'N', 'N'),
#     ('제목95', '내용95', 5, 0, 'N', 'N'),
#     ('제목96', '내용96', 6, 0, 'N', 'N'),
#     ('제목97', '내용97', 7, 0, 'N', 'N'),
#     ('제목98', '내용98', 8, 0, 'N', 'N'),
#     ('제목99', '내용99', 9, 0, 'N', 'N'),
#     ('제목100', '내용100', 10, 0, 'N', 'N');

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
    original_name VARCHAR(255) NOT NULL COMMENT '업로드 하는 원본 파일 이름',
    save_name VARCHAR(255) NOT NULL COMMENT '파일 이름',
    file_path VARCHAR(255) NOT NULL COMMENT '파일 경로',
    file_size INT NOT NULL COMMENT '파일 크기',
    file_type VARCHAR(50) NOT NULL COMMENT '파일 타입',
    download_cnt INT DEFAULT 0 COMMENT '다운로드 횟수',
    del_Yn char(1) default 'N' COMMENT '파일 삭제 여부',
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '생성일',
    -- update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일',
    delete_date TIMESTAMP,
    FOREIGN KEY (post_id) REFERENCES POST(post_id)
);
