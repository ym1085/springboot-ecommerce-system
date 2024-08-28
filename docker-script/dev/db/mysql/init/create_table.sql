################################################################
#--------------------------------------------------------------#
# @author: youngminkim                                         #
#                                                              #
# 2023. 10. 20 : 쇼핑몰 서비스 운영을 위한 테이블 생성         #
# 2023. 10. 22 : 주문, 상품, 장바구니 테이블 추가 및 수정      #
#--------------------------------------------------------------#
#USE shoppingmall;
SET NAMES utf8;
SET character_set_client = utf8mb4;

-- 테이블이 존재한다면, 삭제 후 다시 생성
DROP TABLE IF EXISTS MEMBER;
DROP TABLE IF EXISTS SOCIAL_MEMBER;
DROP TABLE IF EXISTS POST_CATEGORY;
DROP TABLE IF EXISTS POST;
DROP TABLE IF EXISTS COMMENT;
DROP TABLE IF EXISTS POST_FILE;
DROP TABLE IF EXISTS DELIVERY_ADDRESS;
DROP TABLE IF EXISTS PRODUCT_CATEGORY;
DROP TABLE IF EXISTS PRODUCT_FILE;
DROP TABLE IF EXISTS PRODUCT;
DROP TABLE IF EXISTS `ORDER`;
DROP TABLE IF EXISTS ORDER_DETAIL;
DROP TABLE IF EXISTS CART;

-- Create the MEMBER table
CREATE TABLE MEMBER
(
    member_id    INT AUTO_INCREMENT PRIMARY KEY,
    user_name         VARCHAR(20) NOT NULL COMMENT '회원 이름',
    account      VARCHAR(500),
    password     VARCHAR(60),
    email        VARCHAR(50) NOT NULL COMMENT '회원 이메일',
    phone_number VARCHAR(15),
    picture      VARCHAR(100),
    birth_date   VARCHAR(12),
    use_yn       CHAR      DEFAULT 'N',
    cert_yn      CHAR      DEFAULT 'N',
    role         VARCHAR(10) NOT NULL,
    create_date  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_date  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    gender       VARCHAR(10) COMMENT '성별'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Create the SOCIAL_MEMBER table
CREATE TABLE SOCIAL_MEMBER
(
    social_id      INT AUTO_INCREMENT PRIMARY KEY,
    member_id      INT NOT NULL COMMENT '회원 ID',
    provider_type  VARCHAR(30) COMMENT '소셜 로그인 타입',
    provider_token VARCHAR(255) COMMENT '소셜 로그인 토큰',
    FOREIGN KEY (member_id) REFERENCES MEMBER (member_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Create the POST_CATEGORY table
CREATE TABLE POST_CATEGORY
(
    category_id   INT AUTO_INCREMENT PRIMARY KEY,
    category_name VARCHAR(100)       NOT NULL COMMENT '게시판 카테고리 이름'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Create the POST table
CREATE TABLE POST
(
    post_id     INT AUTO_INCREMENT PRIMARY KEY,
    title       VARCHAR(40)  NOT NULL COMMENT '게시글 제목',
    content     VARCHAR(500) NOT NULL COMMENT '게시글 내용',
    member_id   INT          NOT NULL COMMENT '회원 ID',
    category_id INT          NOT NULL COMMENT '카테고리 ID',
    read_cnt    INT          NOT NULL DEFAULT 0 COMMENT '조회수',
    fixed_yn    CHAR                  DEFAULT 'N' COMMENT '고정글 지정 여부',
    del_yn      CHAR                  DEFAULT 'N' COMMENT '삭제 여부',
    create_date TIMESTAMP             DEFAULT CURRENT_TIMESTAMP COMMENT '게시글 생성일',
    update_date TIMESTAMP             DEFAULT CURRENT_TIMESTAMP COMMENT '게시글 수정일',
    delete_date TIMESTAMP,
    FOREIGN KEY (member_id) REFERENCES MEMBER (member_id),
    FOREIGN KEY (category_id) REFERENCES POST_CATEGORY (category_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Create the DELIVERY_ADDRESS table
CREATE TABLE DELIVERY_ADDRESS
(
    delivery_id    INT AUTO_INCREMENT PRIMARY KEY,
    member_id      INT     NOT NULL COMMENT '회원 기본 PK 번호',
    zipcode        VARCHAR(20) NOT NULL COMMENT '배송지 우편번호',
    address        VARCHAR(50) NOT NULL COMMENT '배송지 주소',
    address_detail VARCHAR(50) NOT NULL COMMENT '배송지 상세 주소',
    FOREIGN KEY (member_id) REFERENCES MEMBER (member_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Create the PRODUCT_CATEGORY table
CREATE TABLE PRODUCT_CATEGORY
(
    category_id   INT AUTO_INCREMENT PRIMARY KEY,
    category_name VARCHAR(50) NOT NULL COMMENT '상품 카테고리명'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Create the PRODUCT_FILE table
CREATE TABLE PRODUCT_FILE
(
    product_file_id     INT AUTO_INCREMENT PRIMARY KEY,
    product_number      INT          NOT NULL COMMENT '상품 ID',
    origin_file_name    VARCHAR(300) NOT NULL COMMENT '원본 파일 이름',
    stored_file_name    VARCHAR(300) NOT NULL COMMENT '서버에 저장될 파일 이름',
    stored_thumb_nail   VARCHAR(300) NOT NULL COMMENT '썸네일 이미지',
    delegate_thumb_nail VARCHAR(1)   NOT NULL COMMENT '대표썸네일 여부',
    file_size           INT          NOT NULL COMMENT '파일 크기',
    create_date         TIMESTAMP             DEFAULT CURRENT_TIMESTAMP COMMENT '생성일',
    del_yn              CHAR         NOT NULL DEFAULT 'N' COMMENT '파일삭제여부'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Create the PRODUCT table
CREATE TABLE PRODUCT
(
    product_id          INT AUTO_INCREMENT PRIMARY KEY,
    category_id         INT       NOT NULL COMMENT '카테고리 코드',
    product_name        VARCHAR(50)   NOT NULL COMMENT '상품명',
    product_price       INT       NOT NULL DEFAULT 0 COMMENT '상품 가격',
    product_stock       INT       NOT NULL DEFAULT 0 COMMENT '상품 가격',
    product_desc        TEXT   NOT NULL COMMENT '상품 설명',
    product_create_date TIMESTAMP NOT NULL COMMENT '상품 등록일',
    product_hits        INT       NOT NULL DEFAULT 0 COMMENT '상품 조회수',
    FOREIGN KEY (category_id) REFERENCES PRODUCT_CATEGORY (category_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;