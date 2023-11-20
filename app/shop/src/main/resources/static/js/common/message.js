/**
 * Todo:
 *  - message는 API에서 주는거랑 front에서 사용하는거랑 구분해야 해서
 *  - 나중에 분리 시켜야함, 일단 전반적으로 다 구성되면 변경하는걸로
 */
const messages = {
    EMPTY_USER_NAME: {
        message: '회원명이 존재하지 않네요, 다시 시도해주세요.',
        code: 3100,
    },
    EMPTY_ACCOUNT: {
        message: '계정이 존재하지 않네요, 다시 시도해주세요.',
        code: 3101,
    },
    EMPTY_PASSWORD1: {
        message: '1차 비밀번호를 입력해주세요.',
        code: 9999,
    },
    EMPTY_PASSWORD2: {
        message: '2차 비밀번호를 입력해주세요.',
        code: 9999,
    },
    EMPTY_EMAIL: {
        message: '이메일이 존재하지 않네요, 다시 시도해주세요.',
        code: 3103,
    },
    EMPTY_EMAIL_AUTH_CODE: {
        message: '이메일 인증 번호는 반드시 입력 되어야 합니다.',
        code: 3105,
    },
    EMPTY_PHONE_PREFIX: {
        message: '전화번호 앞자리를 입력해주세요.',
        code: 9999,
    },
    EMPTY_PHONE_MIDDLE: {
        message: '전화번호 중간자리를 입력해주세요.',
        code: 9999,
    },
    EMPTY_PHONE_LAST: {
        message: '전화번호 끝자리를 입력해주세요.',
        code: 9999,
    },
    EMPTY_GENDER: {
        message: '성별을 선택해주세요.',
        code: 3107,
    },
    EMPTY_BIRTH_DATE: {
        message: '생년월일을 입력해주세요.',
        code: 3108,
    },
    EMPTY_POST_ID: {
        message: '게시글 번호가 존재하지 않습니다. 다시 시도해주세요',
        code: 3000,
    },
    EMPTY_POST_TITLE: {
        message: '게시글 제목이 존재하지 않습니다. 다시 시도해주세요',
        code: 3001,
    },
    EMPTY_POST_CONTENT: {
        message: '게시글 내용이 존재하지 않습니다. 다시 시도해주세요',
        code: 3002,
    },
    EMPTY_POST_FIXED_YN: {
        message: '고정글 여부(Y/N)가 선택되지 않았습니다. 다시 시도해주세요',
        code: 3003,
    },
    EMPTY_POST_FILES: {
        message: '첨부 파일이 존재하지 않습니다. 다시 시도해주세요.',
        code: 3004,
    },
    EMPTY_URL: {
        message: '요청 URL이 존재하지 않습니다. 다시 시도해주세요',
        code: 9999,
    },
    EMPTY_REQUEST_RESULT_DATA: {
        message: 'API 요청 결과값이 존재하지 않습니다. 다시 시도해주세요',
        code: 9999,
    },
    OVER_LENGTH_MEMBER_NAME: {
        message: '회원명은 6자리를 초과할 수 없습니다. 다시 시도해주세요.',
        code: 3109,
    },
    OVER_LENGTH_ACCOUNT: {
        message: '계정명은 30자리를 초고활 수 없습니다. 다시 시도해주세요.',
        code: 3110,
    },
    OVER_LENGTH_PWD: {
        message: '영어, 특수문자 포함 최소 8자 이상의 비밀번호를 입력해주세요.',
        code: 3111,
    },
    OVER_LENGTH_EMAIL: {
        message: '이메일은 45자리를 초과할 수 없습니다. 다시 시도해주세요.',
        code: 3112,
    },
    OVER_LENGTH_PHONE_PREFIX: {
        message: '휴대폰 맨 앞자리는 3자리를 초과할 수 없습니다.',
        code: 9999,
    },
    OVER_LENGTH_PHONE_MIDDLE: {
        message: '휴대폰 중간 자리는 4자리를 초과할 수 없습니다.',
        code: 9999,
    },
    OVER_LENGTH_PHONE_LAST: {
        message: '휴대폰 마지막 자리는 4자리를 초과할 수 없습니다.',
        code: 9999,
    },
    INVALID_MEMBER_NAME: {
        message: '유효하지않은 이름입니다. 다시 시도해주세요.',
        code: 3109,
    },
    INVALID_MEMBER_ACCOUNT: {
        message: '계정은 숫자 한 개 이상 포함, 영문자 또는 숫자로 구성 되어야 합니다.',
        code: 3110,
    },
    INVALID_MEMBER_PWD: {
        message: '영어, 특수문자 포함 최소 8자 이상 비밀번호를 입력해주세요.',
        code: 3111,
    },
    INVALID_MEMBER_EMAIL: {
        message: '올바른 메일 형식이 아닙니다. 다시 시도해주세요.',
        code: 3112,
    },
    INVALID_MEMBER_PHONE: {
        message: '휴대폰 번호에 문자를 입력할 수 없습니다. 다시 시도해주세요.',
        code: 3113,
    },
    INVALID_MEMBER_GENDER: {
        message: '유효하지 않은 성별 입니다. 다시 시도해주세요.',
        code: 9999,
    },
    INVALID_MEMBER_BIRTHDATE: {
        message: '유효하지 않은 생년월일 입니다. 다시 시도해주세요.',
        code: 9999,
    },
    NOT_MATCH_PWD: {
        message: '1차, 2차 비밀번호가 동일하지 않습니다. 다시 시도해주세요.',
        code: 9999,
    },
    NOT_CERT_EMAIL: {
        message: '이메일 인증이 진행되지 않았습니다. 다시 시도해주세요.',
        code: 3105,
    },
    NOT_CERT_ACCOUNT: {
        message: '아이디 중복 인증이 진행되지 않았습니다. 다시 시도해주세요.',
        code: 3106,
    },
    SUCCESS_GET_POSTS: {
        message: '전체 게시글 조회에 성공 하였습니다.',
        code: 1000,
    },
    SUCCESS_GET_POST: {
        message: '단일 게시글 조회에 성공 하였습니다.',
        code: 1001,
    },
    SUCCESS_SAVE_POST: {
        message: '게시글 등록에 성공 하였습니다.',
        code: 1002,
    },
    SUCCESS_UPDATE_POST: {
        message: '게시글 수정에 성공 하였습니다.',
        code: 1003,
    },
    SUCCESS_DELETE_POST: {
        message: '게시글 삭제에 성공 하였습니다.',
        code: 1004,
    },
    SUCCESS_SAVE_MEMBER: {
        message: '회원 가입에 성공 하였습니다.',
        code: 1100,
    },
    SUCCESS_SEND_EMAIL: {
        message: '메일 인증 번호가 발송되었습니다. 해당 이메일을 확인해주세요.',
        code: 1101,
    },
    SUCCESS_CERT_EMAIL: {
        message: '메일 인증에 성공 하였습니다.',
        code: 1102,
    },
    SUCCESS_DUPL_ACCOUNT: {
        message: '사용 가능한 계정입니다.',
        code: 1103,
    },
    FAIL_SAVE_MEMBER: {
        message: '회원 가입 진행 중 오류가 발생하였습니다.',
        code: 2100,
    },
    FAIL_CERT_EMAIL: {
        message: '메일 인증에 실패 하였습니다.',
        code: 2101,
    },
    FAIL_DUPL_MEMBER: {
        message: '중복된 아이디 입니다. 다시 시도해주세요',
        code: 2102,
    },
    FAIL_VALIDATE_DUPL_MEMBER: {
        message: '아이디 중복 체크에 실패하였습니다. 다시 시도해주세요',
        code: 2103,
    },
    FAIL_UPDATE_POST: {
        message: '게시글 수정에 실패하였습니다. 다시 시도해주세요.',
        code: 2001,
    },
    FAIL_DELETE_POST: {
        message: '게시글 삭제에 실패하였습니다. 다시 시도해주세요.',
        code: 2002,
    },
    END_EMAIL_AUTH_TIME: {
        message: '이메일 인증 시간이 모두 경과하였습니다. 다시 시도해주세요.',
        code: 9999,
    },
    PROCEED_MEMBER_JOIN: {
        message: '회원 가입을 진행 하겠습니다.',
        code: 9999,
    },
    CANNOT_SEND_EMAIL: {
        message: '이메일 인증이 이미 진행중입니다. 다시 시도해주세요.',
        code: 9999,
    },
    COMMON_SERVER_ERROR_MSG: {
        message: '알 수 없는 서버 오류 입니다. 다시 시도해주세요.',
        code: -9999,
    },
    COMMON_FRONT_ERROR_MSG: {
        message: '알 수 없는 오류 입니다. 다시 시도해주세요.',
        code: -9999,
    },
};
