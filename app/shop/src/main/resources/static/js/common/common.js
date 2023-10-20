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
    return (str === undefined || str === "undefined" || str === null || str === "null" || str.trim() === '');
}

/**
 * 문자열 공백 여부 없는 경우 체크
 * @param str
 * @returns {boolean}
 */
function isNotEmpty(str) {
    return (isEmpty(str)) ? false : true;
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