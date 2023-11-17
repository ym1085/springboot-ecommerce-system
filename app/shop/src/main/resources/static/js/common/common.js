/**
 * @since           :       2023-08-13
 * @author          :       youngmin
 * @version         :       1.0.0
 * @description     :       javascript 공통 함수
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-08-13       youngmin           최초 생성
 **/

/**
 * 문자열 공백 여부 체크
 * @param str
 */
function isEmpty(str) {
    return str === undefined || str === 'undefined' || str === null || str === 'null' || str.trim() === '';
}

/**
 * object 공백 체크
 * @param obj {key1: value1, key2: value2}
 * @returns {boolean}
 */
function isEmptyObject(obj) {
    return Object.keys(obj).length === 0 && obj.constructor === Object;
}

/**
 * object가 비어있지 않은지 체크
 * @param obj {key1: value1, key2: value2}
 * @returns {boolean}
 */
function isNotEmptyObject(obj) {
    // Object.keys(obj).length !== 0: 객체의 속성(key)들이 존재하는지 여부를 확인합니다.
    // obj.constructor === Object: 객체가 일반적인 객체(Object)인지 확인합니다.
    // 둘 중 하나라도 만족하면 객체가 비어있지 않다고 판단합니다.
    return Object.keys(obj).length !== 0 && obj.constructor === Object;
}

/**
 * 문자열 공백 여부 없는 경우 체크
 * @param str
 * @returns {boolean}
 */
function isNotEmpty(str) {
    return isEmpty(str) ? false : true;
}

/**
 * 입력 문자가 숫자인지 검증
 * @param value
 * @returns {boolean}
 */
function isNotNumericRegExp(value) {
    const regEx = /^\d+$/;
    return !regEx.test(value);
}
