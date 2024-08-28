################################################################
#--------------------------------------------------------------#
# @author: youngminkim                                         #
#                                                              #
# 2023. 10. 20 : 쇼핑몰 서비스 운영을 위한 테이블 생성         #
# 2023. 10. 22 : 주문, 상품, 장바구니 테이블 추가 및 수정      #
#--------------------------------------------------------------#
USE SHOPPINGMALL;

-- Create the MEMBER table
CREATE TABLE MEMBER
(
    member_id    INT AUTO_INCREMENT PRIMARY KEY,
    user_name         VARCHAR(20) NOT NULL COMMENT '회원 이름',
    account      VARCHAR(300),
    password     VARCHAR(60),
    email        VARCHAR(50) NOT NULL COMMENT '회원 이메일',
    phone_number VARCHAR(15),
    picture      VARCHAR(100),
    birth_date   VARCHAR(12),
    use_yn       CHAR      DEFAULT 'N',
    cert_yn      CHAR      DEFAULT 'N',
    role         VARCHAR(20) NOT NULL,
    create_date  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_date  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    gender       VARCHAR(10) COMMENT '성별'
);

-- Create the SOCIAL_MEMBER table
CREATE TABLE SOCIAL_MEMBER
(
    social_id      INT AUTO_INCREMENT PRIMARY KEY,
    member_id      INT NOT NULL COMMENT '회원 ID',
    provider_type  VARCHAR(30) COMMENT '소셜 로그인 타입',
    provider_token VARCHAR(255) COMMENT '소셜 로그인 토큰',
    FOREIGN KEY (member_id) REFERENCES MEMBER (member_id)
);

-- Create the POST_CATEGORY table
CREATE TABLE POST_CATEGORY
(
    category_id   INT AUTO_INCREMENT PRIMARY KEY,
    category_name VARCHAR(100)       NOT NULL COMMENT '게시판 카테고리 이름'
);

-- Create the POST table
CREATE TABLE POST
(
    post_id     INT AUTO_INCREMENT PRIMARY KEY,
    title       VARCHAR(40)     NOT NULL COMMENT '게시글 제목',
    content     VARCHAR(1000)   NOT NULL COMMENT '게시글 내용',
    member_id   INT             NOT NULL COMMENT '회원 ID',
    category_id INT             NOT NULL COMMENT '카테고리 ID',
    read_cnt    INT             NOT NULL DEFAULT 0 COMMENT '조회수',
    fixed_yn    CHAR                  DEFAULT 'N' COMMENT '고정글 지정 여부',
    del_yn      CHAR                  DEFAULT 'N' COMMENT '삭제 여부',
    create_date TIMESTAMP             DEFAULT CURRENT_TIMESTAMP COMMENT '게시글 생성일',
    update_date TIMESTAMP             DEFAULT CURRENT_TIMESTAMP COMMENT '게시글 수정일',
    delete_date TIMESTAMP,
    FOREIGN KEY (member_id) REFERENCES MEMBER (member_id),
    FOREIGN KEY (category_id) REFERENCES POST_CATEGORY (category_id)
);

-- Create the COMMENT table
CREATE TABLE COMMENT
(
    comment_id  INT AUTO_INCREMENT PRIMARY KEY COMMENT '댓글 ID',
    parent_id   INT,
    post_id     INT  NOT NULL COMMENT '게시글 번호',
    content     TEXT NOT NULL COMMENT '댓글 내용',
    member_id   INT  NOT NULL COMMENT '회원 번호',
    del_yn      CHAR      DEFAULT 'N' COMMENT '댓글 삭제 여부',
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '생성일',
    update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '수정일',
    FOREIGN KEY (post_id) REFERENCES POST (post_id),
    FOREIGN KEY (member_id) REFERENCES MEMBER (member_id)
);

-- Create the POST_FILE table
CREATE TABLE POST_FILE
(
    post_file_id     INT AUTO_INCREMENT PRIMARY KEY,
    post_id          INT          NOT NULL COMMENT '게시글 ID',
    origin_file_name VARCHAR(300) NOT NULL COMMENT '원본 파일 이름',
    stored_file_name VARCHAR(300) NOT NULL COMMENT '서버에 저장될 파일 이름',
    file_path        VARCHAR(300) NOT NULL COMMENT '파일 경로',
    file_size        INT          NOT NULL COMMENT '파일 크기',
    file_exp        VARCHAR(50)  NOT NULL COMMENT '파일 타입',
    download_cnt     INT          NOT NULL DEFAULT 0 COMMENT '다운로드 횟수',
    del_Yn           CHAR         NOT NULL DEFAULT 'N' COMMENT '파일 삭제 여부',
    create_date      TIMESTAMP             DEFAULT CURRENT_TIMESTAMP COMMENT '생성일',
    delete_date      TIMESTAMP,
    file_attached    char      default 'N'               not null,
    FOREIGN KEY (post_id) REFERENCES POST (post_id)
);

-- Create the DELIVERY_ADDRESS table
CREATE TABLE DELIVERY_ADDRESS
(
    delivery_id    INT AUTO_INCREMENT PRIMARY KEY,
    member_id      INT     NOT NULL COMMENT '회원 기본 PK 번호',
    zipcode        VARCHAR(20) NOT NULL COMMENT '배송지 우편번호',
    address        VARCHAR(50) NOT NULL COMMENT '배송지 주소',
    address_detail VARCHAR(50) NOT NULL COMMENT '배송지 상세 주소',
    FOREIGN KEY (member_id) REFERENCES MEMBER (member_id)
);

# 외래키 검사 활성화 or 비활성화
# 0: 비활성화, 1: 활성화 -> 해당 옵션은 웬만하면 건들지 말자
#SET foreign_key_checks = 0;
#DROP TABLE product_category;
#SET foreign_key_checks = 1;
-- PRODUCT_CATEGORY 테이블에 데이터 삽입
INSERT INTO product_category (category_name)
VALUES
    ('휴대폰'),
    ('스마트 시계'),
    ('태블릿'),
    ('노트북'),
    ('액세서리');

-- Create the PRODUCT_CATEGORY table
CREATE TABLE product_category
(
    category_id   INT AUTO_INCREMENT PRIMARY KEY,
    category_name VARCHAR(50) NOT NULL COMMENT '상품 카테고리명'
);

-- Create the PRODUCT table
create table PRODUCT
(
    product_id       int auto_increment
        primary key,
    category_id      int                         not null comment '카테고리 코드',
    product_name     varchar(50)                 not null comment '상품명',
    product_price    int default 0               not null comment '상품 가격',
    product_stock    int default 0               not null comment '상품 가격',
    product_desc     text                        not null comment '상품 설명',
    product_hits     int default 0               not null comment '상품 조회수',
    create_date      timestamp                   not null comment '상품 등록일',
    item_sell_status varchar(10) charset utf8mb3 null,
    update_date      timestamp                   not null comment '상',
    constraint product_ibfk_1
        foreign key (category_id) references shoppingmall.PRODUCT_CATEGORY (category_id)
);

create index category_id
    on shoppingmall.PRODUCT (category_id);

-- Create ths PRODUCT_FILE table
create table PRODUCT_FILE
(
    product_file_id     int auto_increment
        primary key,
    product_id          int                                 null comment '상품 ID',
    origin_file_name    varchar(300)                        not null comment '원본 파일 이름',
    stored_file_name    varchar(300)                        not null comment '서버에 저장될 파일 이름',
    stored_thumb_nail   varchar(300)                        not null comment '썸네일 이미지',
    delegate_thumb_nail varchar(1)                          not null comment '대표썸네일 여부',
    file_size           int                                 not null comment '파일 크기',
    create_date         timestamp default CURRENT_TIMESTAMP null comment '생성일',
    del_yn              char      default 'N'               not null comment '파일삭제여부',
    constraint PRODUCT_FILE_PRODUCT_product_id_fk
        foreign key (product_id) references shoppingmall.PRODUCT (product_id)
);

-- Create the ORDER table
CREATE TABLE `ORDER`
(
    order_id          INT AUTO_INCREMENT PRIMARY KEY,
    member_id         INT     NOT NULL COMMENT '회원 기본 PK 번호',
    zipcode           VARCHAR(20) NOT NULL COMMENT '주문 우편번호',
    address           VARCHAR(50) NOT NULL COMMENT '주문 주소',
    address_detail    VARCHAR(50) NOT NULL COMMENT '주문 상세 주소',
    req_upon_delivery VARCHAR(100) NOT NULL COMMENT '배송 시 요청사항',
    receiver_name     VARCHAR(20) NOT NULL COMMENT '수령자 이름',
    receiver_phone    VARCHAR(20) NOT NULL COMMENT '수령자 전화번호',
    FOREIGN KEY (member_id) REFERENCES MEMBER (member_id)
);

-- Create the ORDER_DETAIL table
CREATE TABLE ORDER_DETAIL
(
    order_detail_id     INT AUTO_INCREMENT PRIMARY KEY,
    order_id            INT     NOT NULL COMMENT '주문 ID',
    product_id          INT     NOT NULL COMMENT '상품 ID',
    product_cnt         INT     NOT NULL DEFAULT 0 COMMENT '주문 상품 수량(개수)',
    product_price       INT     NOT NULL DEFAULT 0 COMMENT '주문 상품 가격',
    order_detail_status VARCHAR(10) NOT NULL COMMENT '주문 처리 상태',
    FOREIGN KEY (order_id) REFERENCES `ORDER` (order_id),
    FOREIGN KEY (product_id) REFERENCES PRODUCT (product_id)
);

-- Create the CART table
CREATE TABLE CART
(
    cart_id     INT AUTO_INCREMENT PRIMARY KEY COMMENT '장바구니 ID',
    member_id   INT NOT NULL COMMENT '회원 ID',
    product_id  INT NOT NULL COMMENT '상품 ID',
    product_cnt INT NOT NULL DEFAULT 0 COMMENT '상품 수량(개수)',
    cart_val    VARCHAR(10) NOT NULL DEFAULT 'Guest' COMMENT '아이디 또는 비회원 식별값',
    FOREIGN KEY (member_id) REFERENCES MEMBER (member_id),
    FOREIGN KEY (product_id) REFERENCES PRODUCT (product_id)
);

-- Create then refresh token table
create table REFRESH_TOKEN
(
    refresh_token_id INT AUTO_INCREMENT PRIMARY KEY,
    memberId INT NOT NULL,
    refresh_token VARCHAR(300) NOT NULL
)
    comment 'JWT REFRESH_TOKEN 관련 테이블';