################################################################
#                    STORED PROCEDURE                          #
################################################################
#--------------------------------------------------------------#
# @since: 2023. 10. 20                                         #
# @author: ymkim                                               #
# @desc                                                        #
# - 소셜 로그인 시에 기존 MEMBER 테이블에 데이터를 INSERT하고  #
# - SOCIAL_MEMBER 테이블에도 데이터를 INSERT 하도록 SP를 사용   #
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

    INSERT INTO SOCIAL_MEMBER
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
