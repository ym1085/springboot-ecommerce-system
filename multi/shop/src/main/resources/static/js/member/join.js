/**
 * @since           :       2023-08-13
 * @author          :       youngmin
 * @version         :       1.0.0
 * @description     :       회원 가입 시 기본적인 Validation과 회원 가입 요청을 위해 사용
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-08-13       youngmin           최초 생성
 * 2023-08-14       youngmin           유효성 검증 시 하나의 함수가 너무 많은 역할을 가지고 있어 분리
 **/

/**
 * 회원 가입 버튼을 클릭 하는 순간에, 사용자의 모든 데이터를 객체에 저장 해둔다
 */
function setMemberJoinInfo() {
    return {
        username: document.getElementById("username"),
        account: document.getElementById("account"),
        password1: document.getElementById("password1"),
        password2: document.getElementById("password2"),
        email: document.getElementById("email"),
        phonePrefix: document.getElementById("phonePrefix"),
        phoneMiddle: document.getElementById("phoneMiddle"),
        phoneLast: document.getElementById("phoneLast"),
        gender: document.getElementById("gender"),
        birthDate: document.getElementById("birthDate"),
        certYn: document.getElementById("certYn")
    };
}

function showMessage(message) {
    alert(message);
}

/*
    공통 함수로 만들어서 유효성 검증을 할 수 도 있지만,
    이왕이면 SRP에 맞춰서 함수를 가장 작게 만들어서 사용 한다
*/

function validateUsername(memberJoinInfo) {
    if (isEmpty(memberJoinInfo.username.value)) {
        showMessage(messages.EMPTY_USER_NAME);
        memberJoinInfo.username.focus();
        return false;
    } else if (memberJoinInfo.username.length > 6) {
        showMessage(messages.OVER_LENGTH_USER_NAME);
        memberJoinInfo.username.focus();
        return false;
    }
    return true;
}

function validateAccount(memberJoinInfo) {
    if (isEmpty(memberJoinInfo.account.value)) {
        showMessage(messages.EMPTY_ACCOUNT);
        memberJoinInfo.account.focus();
        return false;
    } else if (memberJoinInfo.account.length > 30) {
        showMessage(messages.OVER_LENGTH_ACCOUNT);
        memberJoinInfo.account.focus();
        return false;
    }
    return true;
}

function validatePasswordRegExp(memberJoinInfo) {
    const regEx = /^(?=.*[a-zA-Z])(?=.*[!@#$%^&*]).{8,}$/;
    if (!regEx.test(memberJoinInfo.password1.value)) {
        showMessage(messages.NOT_VALID_PWD);
        memberJoinInfo.password1.focus();
        return false;
    }
    if (!regEx.test(memberJoinInfo.password2.value)) {
        showMessage(messages.NOT_VALID_PWD);
        memberJoinInfo.password2.focus();
        return false;
    }
    return true;
}

function validateEqualsPasswords(memberJoinInfo) {
    if (memberJoinInfo.password1.value !== memberJoinInfo.password2.value) {
        showMessage(messages.NOT_MATCH_PWD);
        memberJoinInfo.password1.focus();
        return false;
    }
    return true;
}

function validatePrefixPwd(memberJoinInfo) {
    if (isEmpty(memberJoinInfo.password1.value)) {
        showMessage(messages.EMPTY_PASSWORD1);
        memberJoinInfo.password1.focus();
        return false;
    } else if (memberJoinInfo.password1.length < 8) {
        showMessage(messages.OVER_LENGTH_PWD);
        memberJoinInfo.password1.focus();
        return false;
    }
    return true;
}

function validateLastPwd(memberJoinInfo) {
    if (isEmpty(memberJoinInfo.password2.value)) {
        showMessage(messages.EMPTY_PASSWORD2)
        memberJoinInfo.password2.focus();
        return false;
    } else if (memberJoinInfo.password2.length < 8) {
        showMessage(messages.OVER_LENGTH_PWD);
        memberJoinInfo.password2.focus();
        return false;
    }
    return true;
}

function validateEmailRegExp(memberJoinInfo) {
    const regExp = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;
    if (!regExp.test(memberJoinInfo.email.value)) {
        showMessage(messages.NOT_VALID_EMAIL);
        memberJoinInfo.email.focus();
        return false;
    }
    return true;
}

function validateEmail(memberJoinInfo) {
    if (isEmpty(memberJoinInfo.email.value)) {
        showMessage(messages.EMPTY_EMAIL);
        memberJoinInfo.email.focus();
        return false;
    } else if (memberJoinInfo.email.length > 45) {
        showMessage(messages.OVER_LENGTH_EMAIL);
        memberJoinInfo.email.focus();
        return false;
    }
    return true;
}

function validateNumericPhoneNumber(memberJoinInfo) {
    if (isNotNumericRegExp(memberJoinInfo.phonePrefix.value)
        && isNotNumericRegExp(memberJoinInfo.phoneMiddle.value)
        && isNotNumericRegExp(memberJoinInfo.phoneLast.value)) {

        showMessage(messages.NOT_VALID_PHONE);
        memberJoinInfo.phoneMiddle.focus();
        return false;
    }
    return true;
}

function validatePhonePrefix(memberJoinInfo) {
    // console.log(`memberJoinInfo.phonePrefix.value = > ${memberJoinInfo.phonePrefix.value}`);
    // console.log(`isEMpty => ${isEmpty(memberJoinInfo.phonePrefix.value)}`);
    if (isEmpty(memberJoinInfo.phonePrefix.value)) {
        showMessage(messages.EMPTY_PHONE_PREFIX);
        memberJoinInfo.phonePrefix.focus();
        return false;
    } else if (memberJoinInfo.phonePrefix.value.trim().length > 3) {
        showMessage(messages.EMPTY_PHONE_PREFIX);
        memberJoinInfo.phonePrefix.focus();
        return false;
    }
    return true;
}

function validatePhoneMiddle(memberJoinInfo) {
    if (isEmpty(memberJoinInfo.phoneMiddle.value)) {
        showMessage(messages.EMPTY_PHONE_MIDDLE)
        memberJoinInfo.phoneMiddle.focus();
        return false;
    } else if (memberJoinInfo.phoneMiddle.value.trim().length > 4) {
        showMessage(messages.OVER_LENGTH_PHONE_MIDDLE);
        memberJoinInfo.phoneMiddle.focus();
        return false;
    }
    return true;
}

function validatePhoneLast(memberJoinInfo) {
    if (isEmpty(memberJoinInfo.phoneLast.value)) {
        showMessage(messages.EMPTY_PHONE_LAST);
        memberJoinInfo.phoneLast.focus();
        return false;
    } else if (memberJoinInfo.phoneLast.value.trim().length > 4) {
        showMessage(messages.OVER_LENGTH_PHONE_LAST);
        memberJoinInfo.phoneLast.focus();
        return false;
    }
    return true;
}

function isNotValidGender(gender) {
    return gender !== 'M' && gender !== 'F';
}

function validateGender(memberJoinInfo) {
    if (isEmpty(memberJoinInfo.gender.value)) {
        showMessage(messages.EMPTY_GENDER);
        memberJoinInfo.gender.focus();
        return false;
    } else if (isNotValidGender(memberJoinInfo.gender.value)) {
        showMessage(messages.NOT_VALID_GENDER);
        memberJoinInfo.gender.focus();
        return false;
    }
    return true;
}

function isNotValidBirthDateFormat(birthDate) {
    const regex = /^\d{4}-\d{2}-\d{2}$/;
    return !regex.test(birthDate);
}

function validateBirthDate(memberJoinInfo) {
    if (isEmpty(memberJoinInfo.birthDate.value)) {
        showMessage(messages.EMPTY_BIRTH_DATE);
        memberJoinInfo.birthDate.focus();
        return false;
    } else if (isNotValidBirthDateFormat(memberJoinInfo.birthDate.value)) {
        showMessage(messages.NOT_VALID_DATE);
        memberJoinInfo.birthDate.focus();
        return false;
    }
    return true;
}

function validateCertYn(memberJoinInfo) {
    if (memberJoinInfo.certYn.value === 'Y') {
        return true;
    } else if (memberJoinInfo.certYn.value === 'N') {
        return false;
    } else {
        return false;
    }
}

/**
 * 회원 가입 시 서버 전송 전에 앞에서 1차적으로 검증하는 로직
 * 서버 DTO 에서 어차피 검증이 되지만, 미리 검증
 * @param memberJoinInfo
 * @returns {boolean} true: 검증 성공, false: 검증 실패
 */
function validateMemberJoinInfo(memberJoinInfo) {
    return validateUsername(memberJoinInfo)
        && validateAccount(memberJoinInfo)
        && validatePrefixPwd(memberJoinInfo)
        && validateLastPwd(memberJoinInfo)
        && validateEqualsPasswords(memberJoinInfo)
        && validatePasswordRegExp(memberJoinInfo)
        && validateEmail(memberJoinInfo)
        && validateEmailRegExp(memberJoinInfo)
        && validatePhonePrefix(memberJoinInfo)
        && validatePhoneMiddle(memberJoinInfo)
        && validatePhoneLast(memberJoinInfo)
        && validateGender(memberJoinInfo)
        && validateBirthDate(memberJoinInfo)
        && validateNumericPhoneNumber(memberJoinInfo)
        && validateCertYn(memberJoinInfo);
}

let timeLeft = 180;
let timer;
function startTimer() {
    timer = setInterval(() => {
        const min = Math.floor(timeLeft / 60);
        const sec = timeLeft % 60;

        document.getElementById("timeLeft").innerText = `${min.toString().padStart(2, '0')}:${sec.toString().padStart(2, '0')}`;

        timeLeft--;

        if (timeLeft < 0) { // 제한 시간 모두 경과
            showMessage(messages.END_EMAIL_AUTH_TIME);
            doubleClickFlag = false;
            stopInterval();
            resetInterval();
        }
    }, 1000); // 1초마다 실행
}

function stopInterval() {
    clearInterval(timer);
}

function resetInterval() {
    timeLeft = 180;
    document.getElementById("timeLeft").innerText = "03:00";
}

let doubleClickFlag = false; // Todo: 전역 변수 문제 될수도 있을듯... 확인 필요
function checkDoubleClick() {
    if (doubleClickFlag) {
        return false;
    } else {
        doubleClickFlag = true;
        return true;
    }
}

// 인증 이메일 전송
function sendAuthEmail(event) {
    if (!checkDoubleClick()) {
        alert('이미 작업이 진행 되었습니다. 새로고침 후 다시 시도해주세요.');
        return;
    }

    let certEmail = document.getElementById("email");
    if (isEmpty(certEmail.value)) {
        showMessage(messages.EMPTY_EMAIL);
        certEmail.focus();
        return
    }

    let dataObj = {
        url : "/api/v1/email/verify-request",
        method : "POST",
        data : { email : certEmail.value }
    };
    // console.log(`before send server, dataObj => ${dataObj}`)

    // url, method, data
    let responsePromiseByJson = sendFetchRequest(dataObj);
    responsePromiseByJson.then((response) => {
        if (response.ok) {
            return response.json();
        } else {
            throw new Error("Request failed, url => " + dataObj.url);
        }
    }).then((data) => {
        if (data === null) {
            throw new Error("Data is empty, url => " + dataObj.url);
        }

        // {'statusCode' : '200', 'message': '이메일 xxxx'}
        if (data.statusCode === 200) {
            showMessage(messages.SUCCESS_SEND_EMAIL);
            // 인증번호 입력 영역
            let verifySection = document.getElementById("verifySection");
            if(verifySection.style.display === 'none') {
                verifySection.style.display = 'block';
            } else {
                verifySection.style.display = 'none';
            }

            // 타이머 영역
            let timeLimitArea = document.getElementById("timeLimitArea");
            if (timeLimitArea.style.display === 'none') {
                timeLimitArea.style.display = 'block'
            } else {
                timeLimitArea.style.display = 'none'
            }
            startTimer(); // 타이머 시작
        } else {
            showMessage(messages.FAIL_SEND_EMAIL);
            document.getElementById("email").focus();
            doubleClickFlag = false;
            return;
        }
    })
}

function verifyEmailAuthCode() {
    let certEmail = document.getElementById("email");
    let verificationCode = document.getElementById("verificationCode");
    if (isEmpty(verificationCode.value) || isNotNumericRegExp(verificationCode.value)) {
        showMessage(messages.EMPTY_EMAIL_AUTH_CODE);
        return;
    }

    if (isEmpty(certEmail.value) || certEmail.length === 0) {
        showMessage(messages.EMPTY_EMAIL);
        return;
    }

    let dataObj = {
        url : "/api/v1/email/verify",
        method : "GET",
        data : {
            email : certEmail.value,
            code : verificationCode.value
        }
    };

    let responsePromiseByJson = sendFetchRequest(dataObj);
    responsePromiseByJson.then((response) => {
        if (response.ok) {
            return response.json();
        } else {
            throw new Error("Request failed, url => " + dataObj.url);
        }
    }).then((data) => {
        if (data === null) {
            throw new Error("Data is empty, url => " + dataObj.url);
        }

        if (data === 1) { // 1: 성공
            showMessage(messages.SUCCESS_CERT_EMAIL);
            document.getElementById("certYn").value = 'Y';
            document.getElementById("verificationCode").disabled = true;
            stopInterval();
            resetInterval();
        } else {
            showMessage(messages.FAIL_CERT_EMAIL);
            document.getElementById("email").focus();
            doubleClickFlag = false;
            stopInterval();
            resetInterval();
            return;
        }
    }).catch((error) => {
        console.error(`URL => ${dataObj.url}, 이메일 인증코드 인증 확인 시 오류 발생`);
    }).finally(() => {
        // ...
    })
}

const main = {
    init: function () {
        let _this = this;
        $('#btn-join').on('click', function () {
            if (_this.validate()) {
                showMessage(messages.PROCEED_MEMBER_JOIN);
                _this.join(); // 회원 가입 진행
            }
        });
    },
    validate: function() {
        let memberJoinInfo = setMemberJoinInfo();
        return validateMemberJoinInfo(memberJoinInfo);
    },
    join: function () {
        // Todo : 20230814 ~ 서버에 회원 가입 양식 내용 전송
    }
};

// initialized
main.init();