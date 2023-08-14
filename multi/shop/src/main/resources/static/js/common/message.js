/**
 * @since           :       2023-08-14
 * @author          :       youngmin
 * @version         :       1.0.0
 * @description     :       javascript에서 사용되는 alert 문구를 공통처리 하기 위해 생성, 나중에 팝업으로 변경?
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-08-14       youngmin           최초 생성
 **/
const messages = {
    // 공백 체크용
    EMPTY_USER_NAME: '회원명이 존재하지 않네요, 다시 시도해주세요.',
    EMPTY_ACCOUNT: '계정이 존재하지 않네요, 다시 시도해주세요.',
    EMPTY_PASSWORD1: '1차 비밀번호를 입력해주세요.',
    EMPTY_PASSWORD2: '2차 비밀번호를 입력해주세요.',
    EMPTY_EMAIL: '이메일이 존재하지 않네요, 다시 시도해주세요.',
    EMPTY_PHONE_PREFIX: '전화번호 앞자리를 입력해주세요.',
    EMPTY_PHONE_MIDDLE: '전화번호 중간자리를 입력해주세요.',
    EMPTY_PHONE_LAST: '전화번호 끝자리를 입력해주세요.',
    EMPTY_GENDER: '성별을 선택해주세요.',
    EMPTY_BIRTH_DATE: '생년월일을 입력해주세요.',
    // 필드 길이 체크용
    OVER_LENGTH_USER_NAME: '회원명은 6자리를 초과할 수 없습니다. 다시 시도해주세요.',
    OVER_LENGTH_ACCOUNT: '계정명은 30자리를 초고활 수 없습니다. 다시 시도해주세요.',
    OVER_LENGTH_PWD: '영어와 특수문자를 포함한 최소 8자 이상의 비밀번호를 입력해주세요.',
    OVER_LENGTH_EMAIL: '이메일은 45자리를 초고할 수 없습니다. 다시 시도해주세요.',
    OVER_LENGTH_PHONE_PREFIX: '휴대폰 맨 앞자리는 3자리를 초과할 수 없습니다. 다시 시도해주세요.',
    OVER_LENGTH_PHONE_MIDDLE: '휴대폰 중간 자리는 4자리를 초과할 수 없습니다. 다시 시도해주세요.',
    OVER_LENGTH_PHONE_LAST: '휴대폰 마지막 자리는 4자리를 초과할 수 없습니다. 다시 시도해주세요.',
    // 필드 패턴 체크용
    NOT_VALID_EMAIL: '올바른 메일 형식이 아닙니다. 다시 시도해주세요.',
    NOT_VALID_PWD: '영어와 특수문자를 포함한 최소 8자 이상의 비밀번호를 입력해주세요.',
    NOT_VALID_PHONE: '휴대폰 번호에 문자를 입력할 수 없습니다. 다시 시도해주세요.',
    NOT_VALID_GENDER: '유효하지 않은 성별 입니다. 다시 시도해주세요.',
    NOT_VALID_DATE: '유효하지 않은 생년월일 입니다. 다시 시도해주세요.',
    NOT_MATCH_PWD: '1차 비밀번호와 2차 비밀번호가 동일하지 않습니다. 다시 시도해주세요.',
};