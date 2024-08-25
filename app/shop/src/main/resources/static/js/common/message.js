const messages = {
    STATUS: {
        OK: 200,
        INVALID_PARAMETER: 400,
        RESOURCE_NOT_FOUND: 404,
        INTERNAL_SERVER_ERROR: 500,
        FORBIDDEN: 403,
        METHOD_NOT_ALLOWED: 405,
    },
    NOT_FOUND_MEMBER_NAME: { msg: '회원명이 존재하지 않네요, 다시 시도해주세요.' },
    NOT_FOUND_MEMBER_ACCOUNT: {
        msg: '계정이 존재하지 않네요, 다시 시도해주세요.',
    },
    NOT_FOUND_MEMBER_PASSWORD1: { msg: '1차 비밀번호를 입력해주세요.' },
    NOT_FOUND_MEMBER_PASSWORD2: { msg: '2차 비밀번호를 입력해주세요.' },
    NOT_FOUND_MEMBER_EMAIL: {
        msg: '이메일이 존재하지 않네요, 다시 시도해주세요.',
    },
    NOT_FOUND_MEMBER_EMAIL_CERT_YN: {
        msg: '이메일 인증 번호는 반드시 입력 되어야 합니다.',
    },
    NOT_FOUND_MEMBER_PHONE_PREFIX: { msg: '전화번호 앞자리를 입력해주세요.' },
    NOT_FOUND_MEMBER_PHONE_MIDDLE: {
        msg: '전화번호 중간자리를 입력해주세요.',
    },
    NOT_FOUND_MEMBER_PHONE_LAST: { msg: '전화번호 끝자리를 입력해주세요.' },
    NOT_FOUND_MEMBER_GENDER: { msg: '성별을 선택해주세요.' },
    NOT_FOUND_MEMBER_BIRTHDATE: { msg: '생년월일을 입력해주세요.' },
    NOT_FOUND_POST_ID: {
        msg: '게시글 번호가 존재하지 않습니다. 다시 시도해주세요.',
    },
    NOT_FOUND_POST_TITLE: {
        msg: '게시글 제목이 존재하지 않습니다. 다시 시도해주세요.',
    },
    NOT_FOUND_POST_CONTENT: {
        msg: '게시글 내용이 존재하지 않습니다. 다시 시도해주세요.',
    },
    NOT_FOUND_POST_FIXED_YN: {
        msg: '고정글 여부(Y/N)가 선택되지 않았습니다. 다시 시도해주세요.',
    },
    NOT_FOUND_COMMENT_CONTENT: {
        msg: '댓글이 존재하지 않습니다. 다시 시도해주세요.',
    },
    NOT_FOUND_POST_FILES_ID: {
        msg: '첨부 파일 번호가 존재하지 않습니다. 다시 시도해주세요.',
    },
    OVER_LENGTH_MEMBER_NAME: {
        msg: '회원명은 6자리를 초과할 수 없습니다. 다시 시도해주세요.',
    },
    OVER_LENGTH_ACCOUNT: {
        msg: '계정명은 30자리를 초고활 수 없습니다. 다시 시도해주세요.',
    },
    OVER_LENGTH_PWD: {
        msg: '영어, 특수문자 포함 최소 8자 이상의 비밀번호를 입력해주세요.',
    },
    OVER_LENGTH_EMAIL: {
        msg: '이메일은 45자리를 초과할 수 없습니다. 다시 시도해주세요.',
    },
    OVER_LENGTH_PHONE_PREFIX: {
        msg: '휴대폰 맨 앞자리는 3자리를 초과할 수 없습니다.',
    },
    OVER_LENGTH_PHONE_MIDDLE: {
        msg: '휴대폰 중간 자리는 4자리를 초과할 수 없습니다.',
    },
    OVER_LENGTH_PHONE_LAST: {
        msg: '휴대폰 마지막 자리는 4자리를 초과할 수 없습니다.',
    },
    OVER_LENGTH_COMMENT: { msg: '댓글을 300자를 초과할 수 없습니다.' },
    INVALID_MEMBER_NAME: {
        msg: '유효하지않은 이름입니다. 다시 시도해주세요.',
    },
    INVALID_MEMBER_ACCOUNT: {
        msg: '계정은 숫자 한 개 이상 포함, 영문자 또는 숫자로 구성 되어야 합니다.',
    },
    INVALID_MEMBER_PWD: {
        msg: '영어, 특수문자 포함 최소 8자 이상 비밀번호를 입력해주세요.',
    },
    INVALID_MEMBER_EMAIL: {
        msg: '올바른 메일 형식이 아닙니다. 다시 시도해주세요.',
    },
    INVALID_MEMBER_PHONE: {
        msg: '휴대폰 번호에 문자를 입력할 수 없습니다. 다시 시도해주세요.',
    },
    INVALID_MEMBER_GENDER: {
        msg: '유효하지 않은 성별 입니다. 다시 시도해주세요.',
    },
    INVALID_MEMBER_BIRTHDATE: {
        msg: '유효하지 않은 생년월일 입니다. 다시 시도해주세요.',
    },
    NOT_MATCH_PWD: {
        msg: '1차, 2차 비밀번호가 동일하지 않습니다. 다시 시도해주세요.',
    },
    NOT_CERT_EMAIL: {
        msg: '이메일 인증이 진행되지 않았습니다. 다시 시도해주세요.',
    },
    NOT_CERT_ACCOUNT: {
        msg: '아이디 중복 인증이 진행되지 않았습니다. 다시 시도해주세요.',
    },
    FAIL_SAVE_MEMBER: { msg: '회원 가입 진행 중 오류가 발생하였습니다.' },
    FAIL_CERT_EMAIL: { msg: '메일 인증에 실패 하였습니다.' },
    FAIL_DUPLICATE_MEMBER: { msg: '중복된 아이디 입니다. 다시 시도해주세요' },
    FAIL_UPDATE_POST: {
        msg: '게시글 수정에 실패하였습니다. 다시 시도해주세요.',
    },
    FAIL_DELETE_POST: {
        msg: '게시글 삭제에 실패하였습니다. 다시 시도해주세요.',
    },
    FAIL_SAVE_COMMENT: {
        msg: '댓글 등록에 실패하였습니다. 다시 시도해주세요.',
    },
    FAIL_DELETE_COMMENT: {
        msg: '댓글 삭제에 실패하였습니다. 다시 시도해주세요.',
    },
    FAIL_UPDATE_COMMENT: {
        msg: '댓글 수정에 실패하였습니다. 다시 시도해주세요.',
    },
    FAIL_DOWNLOAD_FILES: { msg: '파일 다운로드에 실패 하였습니다.' },
    END_EMAIL_AUTH_TIME: {
        msg: '이메일 인증 시간이 모두 경과하였습니다. 다시 시도해주세요.',
    },
    PROCEED_MEMBER_JOIN: { msg: '회원 가입을 진행 하겠습니다.' },
    CONFIRM_MEMBER_JOIN: { msg: '회원가입을 진행하시겠습니까?' },
    CANNOT_SEND_EMAIL: {
        msg: '이메일 인증이 이미 진행중입니다. 다시 시도해주세요.',
    },
    COMMON_SERVER_ERROR_MSG: {
        msg: '알 수 없는 서버 오류 입니다. 다시 시도해주세요.',
    },
};
