# Create Table on shoppingmall database

################################################################
#                    CREATE TABLE                              #
################################################################
#--------------------------------------------------------------#
# @author: youngminkim                                         #
#                                                              #
# 2023. 10. 20 : 쇼핑몰 서비스 운영을 위한 테이블 생성         #
#--------------------------------------------------------------#

USE SHOPPINGMALL;

create table if not exists CATEGORY
(
    category_id   bigint auto_increment primary key,
    category_name varchar(100) not null
);

create table if not exists MEMBER
(
    member_id    bigint auto_increment primary key,
    name         varchar(20)                         not null comment '회원명',
    account      varchar(300)                        null,
    password     varchar(60)                         null,
    email        varchar(50)                         not null comment '회원 이메일',
    phone_number varchar(15)                         null comment '회원 휴대폰번호',
    picture      varchar(200)                        null comment '회원 이미지 URL',
    birth_date   varchar(12)                         null comment '생년월일 (예: 1993-09-23)',
    use_yn       char      default 'N'               null comment '회원 계정 사용 여부',
    cert_yn      char      default 'N'               null comment '회원 이메일 인증 여부',
    role         varchar(10)                         null comment '회원 권한',
    create_date  timestamp default CURRENT_TIMESTAMP null comment '가입일',
    update_date  timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '수정일',
    gender       varchar(10)                         null comment '성별'
);

create table if not exists POST
(
    post_id     bigint auto_increment primary key,
    title       varchar(40)                         not null comment '제목',
    content     varchar(500) charset utf8mb3        not null comment '내용',
    member_id   bigint                              not null comment '작성자 번호',
    category_id bigint                              not null comment '서브 카테고리 번호',
    read_cnt    int       default 0                 not null comment '조회수',
    fixed_yn    char      default 'N'               null comment '고정글 여부',
    del_yn      char      default 'N'               null comment '게시글 삭제 여부',
    create_date timestamp default CURRENT_TIMESTAMP null comment '생성일',
    update_date timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '수정일',
    delete_date timestamp                           null comment '삭제일',
    constraint post_ibfk_1 foreign key (member_id) references MEMBER (member_id),
    constraint post_ibfk_2 foreign key (category_id) references CATEGORY (category_id)
);

create table if not exists COMMENT
(
    comment_id  int auto_increment comment '댓글 ID' primary key,
    parent_id   int unsigned                        null comment '부모 댓글 번호',
    post_id     bigint                              null comment '게시글 번호',
    content     text                                null comment '댓글 내용',
    member_id   bigint                              not null comment '회원 번호',
    del_yn      char      default 'N'               null comment '댓글 삭제 여부',
    create_date timestamp default CURRENT_TIMESTAMP null comment '생성일',
    update_date timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '수정일',
    constraint comment_ibfk_1 foreign key (post_id) references post (post_id),
    constraint comment_ibfk_2 foreign key (member_id) references member (member_id)
);

create index member_id on COMMENT (member_id);
create index post_id on COMMENT (post_id);

create table if not exists FILE
(
    file_id       bigint auto_increment primary key,
    post_id       bigint                              not null comment '게시글 ID',
    original_name varchar(255)                        not null comment '업로드 하는 원본 파일 이름',
    save_name     varchar(255)                        not null comment '파일 이름',
    file_path     varchar(255)                        not null comment '파일 경로',
    file_size     int                                 not null comment '파일 크기',
    file_type     varchar(50)                         not null comment '파일 타입',
    download_cnt  int       default 0                 null comment '다운로드 횟수',
    del_Yn        char      default 'N'               null comment '파일 삭제 여부',
    create_date   timestamp default CURRENT_TIMESTAMP null comment '생성일',
    delete_date   timestamp                           null,
    constraint file_ibfk_1 foreign key (post_id) references post (post_id)
);

create index post_id on FILE (post_id);
create index category_id on POST (category_id);
create index member_id on POST (member_id);

create table if not exists SOCIALMEMBER
(
    social_id      bigint auto_increment primary key,
    member_id      bigint       null comment '회원 ID',
    provider_type  varchar(30)  null comment '소셜 로그인 타입',
    provider_token varchar(255) null comment '소셜 로그인 토큰',
    constraint social_member_ibfk_1 foreign key (member_id) references member (member_id)
);

create index member_id on SOCIALMEMBER (member_id);

################################################################
#                    CREATE STORED PROCEDURE                   #
################################################################
#--------------------------------------------------------------#
# @since: 2023. 10. 20                                         #
# @author: ymkim                                               #
# @desc                                                        #
# > 소셜 로그인 시에 기존 MEMBER 테이블에 데이터를 INSERT하고  #
# > SOCIALMEMBER 테이블에도 데이터를 INSERT 하도록 SP를 사용   #
#--------------------------------------------------------------#
#--------------------------------------------------------------#
# @author: youngminkim                                         #
#                                                              #
# 2023. 10. 20 : 소셜 로그인에서 2개의 TABLE에 INSERT SP 생성  #
#--------------------------------------------------------------#
CREATE PROCEDURE sp_insert_member(
    IN p_name VARCHAR(20),
    IN p_account VARCHAR(300),
    IN p_email VARCHAR(50),
    IN p_picture VARCHAR(200),
    IN p_role VARCHAR(20),
    IN p_registration_id VARCHAR(30),
    IN p_provider_token VARCHAR(255)
)
BEGIN
    START TRANSACTION;
    INSERT INTO MEMBER
        (
            name,
            account,
            password,
            email,
            phone_number,
            picture,
            birth_date,
            use_yn,
            cert_yn,
            role,
            create_date,
            update_date,
            gender
        )
    VALUES
        (
            p_name,
            p_account,
            NULL,
            p_email,
            NULL,
            IFNULL(p_picture, NULL),
            NULL,
            'Y',
            'Y',
            p_role,
            NOW(),
            NOW(),
            NULL
        );

--     SET p_member_id = LAST_INSERT_ID();

    INSERT INTO SOCIALMEMBER
        (
            member_id,
            provider_type,
            provider_token
        )
    VALUES
        (
            LAST_INSERT_ID(),
            p_registration_id,
            p_provider_token
        );
    COMMIT;
END;
