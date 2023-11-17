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

$(function () {});

let memberJoinInfo = {};

/**
 * 회원 가입 버튼을 클릭 하는 순간에, 사용자의 모든 데이터를 객체에 저장 해둔다
 */
function initializedMemberInfo() {
    return {
        username: document.getElementById('username'),
        account: document.getElementById('account'),
        password1: document.getElementById('password1'),
        password2: document.getElementById('password2'),
        email: document.getElementById('email'),
        phonePrefix: document.getElementById('phonePrefix'),
        phoneMiddle: document.getElementById('phoneMiddle'),
        phoneLast: document.getElementById('phoneLast'),
        gender: document.getElementById('gender'),
        birthDate: document.getElementById('birthDate'),
        certYn: document.getElementById('certYn'),
        accountCertYn: document.getElementById('accountCertYn'),
    };
}

function showMessage(message) {
    alert(message);
}

/*
    공통 함수로 만들어서 유효성 검증을 할 수 도 있지만,
    이왕이면 SRP에 맞춰서 함수를 가장 작게 만들어서 사용 한다
*/

function validateUserName(userName) {
    if (isEmpty(userName.value)) {
        showMessage(messages.EMPTY_USER_NAME);
        userName.focus();
        return false;
    } else if (userName.length > 6) {
        showMessage(messages.OVER_LENGTH_USER_NAME);
        userName.focus();
        return false;
    }
    return true;
}

function validateUserNameRegExp(userName) {
    // const regEx = /^[ㄱ-ㅎㅏ-ㅣ가-힣]*$/;
    const regEx = /^[가-힣]{2,}$/;
    if (!regEx.test(userName.value)) {
        showMessage(messages.NOT_VALID_USER_NAME);
        userName.focus();
        return false;
    }
    return true;
}

function validateAccount(account) {
    if (isEmpty(account.value)) {
        showMessage(messages.EMPTY_ACCOUNT);
        account.focus();
        return false;
    } else if (account.length > 30) {
        showMessage(messages.OVER_LENGTH_ACCOUNT);
        account.focus();
        return false;
    }
    return true;
}

function validateAccountRegExp(account) {
    const regEx = /^(?=.*\d)[a-zA-Z\d]+$/; // 숫자가 한 개 이상 포함, 영문자 또는 숫자로 구성
    if (!regEx.test(account.value)) {
        showMessage(messages.NOT_VALID_ACCOUNT);
        account.focus();
        return false;
    }
    return true;
}

function validatePasswordRegExp(password1, password2) {
    const regEx = /^(?=.*[a-zA-Z])(?=.*[!@#$%^&*]).{8,}$/;
    if (!regEx.test(password1.value)) {
        showMessage(messages.NOT_VALID_PWD);
        password1.focus();
        return false;
    }
    if (!regEx.test(password2.value)) {
        showMessage(messages.NOT_VALID_PWD);
        password2.focus();
        return false;
    }
    return true;
}

function validateEqualsPasswords(password1, password2) {
    if (password1.value !== password2.value) {
        showMessage(messages.NOT_MATCH_PWD);
        password1.focus();
        return false;
    }
    return true;
}

function validatePrefixPwd(password1) {
    if (isEmpty(password1.value)) {
        showMessage(messages.EMPTY_PASSWORD1);
        password1.focus();
        return false;
    } else if (password1.length < 8) {
        showMessage(messages.OVER_LENGTH_PWD);
        password1.focus();
        return false;
    }
    return true;
}

function validateLastPwd(password2) {
    if (isEmpty(password2.value)) {
        showMessage(messages.EMPTY_PASSWORD2);
        password2.focus();
        return false;
    } else if (password2.length < 8) {
        showMessage(messages.OVER_LENGTH_PWD);
        password2.focus();
        return false;
    }
    return true;
}

function validateEmailRegExp(email) {
    const regExp = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;
    if (!regExp.test(email.value)) {
        showMessage(messages.NOT_VALID_EMAIL);
        email.focus();
        return false;
    }
    return true;
}

function validateEmail(email) {
    if (isEmpty(email.value)) {
        showMessage(messages.EMPTY_EMAIL);
        email.focus();
        return false;
    } else if (email.length > 45) {
        showMessage(messages.OVER_LENGTH_EMAIL);
        email.focus();
        return false;
    }
    return true;
}

function validateNumericPhoneNumber(phonePrefix, phoneMiddle, phoneLast) {
    if (isNotNumericRegExp(phonePrefix.value) && isNotNumericRegExp(phoneMiddle.value) && isNotNumericRegExp(phoneLast.value)) {
        showMessage(messages.NOT_VALID_PHONE);
        phoneMiddle.focus();
        return false;
    }
    return true;
}

function validatePhonePrefix(phonePrefix) {
    if (isEmpty(phonePrefix.value)) {
        showMessage(messages.EMPTY_PHONE_PREFIX);
        phonePrefix.focus();
        return false;
    } else if (phonePrefix.value.trim().length > 3) {
        showMessage(messages.EMPTY_PHONE_PREFIX);
        phonePrefix.focus();
        return false;
    }
    return true;
}

function validatePhoneMiddle(phoneMiddle) {
    if (isEmpty(phoneMiddle.value)) {
        showMessage(messages.EMPTY_PHONE_MIDDLE);
        phoneMiddle.focus();
        return false;
    } else if (phoneMiddle.value.trim().length > 4) {
        showMessage(messages.OVER_LENGTH_PHONE_MIDDLE);
        phoneMiddle.focus();
        return false;
    }
    return true;
}

function validatePhoneLast(phoneLast) {
    if (isEmpty(phoneLast.value)) {
        showMessage(messages.EMPTY_PHONE_LAST);
        phoneLast.focus();
        return false;
    } else if (phoneLast.value.trim().length > 4) {
        showMessage(messages.OVER_LENGTH_PHONE_LAST);
        phoneLast.focus();
        return false;
    }
    return true;
}

function isNotValidGender(gender) {
    return gender !== 'M' && gender !== 'F';
}

function validateGender(gender) {
    if (isEmpty(gender.value)) {
        showMessage(messages.EMPTY_GENDER);
        gender.focus();
        return false;
    } else if (isNotValidGender(gender.value)) {
        showMessage(messages.NOT_VALID_GENDER);
        gender.focus();
        return false;
    }
    return true;
}

function isNotValidBirthDateFormat(birthDate) {
    const regex = /^\d{4}-\d{2}-\d{2}$/;
    return !regex.test(birthDate);
}

function validateBirthDate(birthDate) {
    if (isEmpty(birthDate.value)) {
        showMessage(messages.EMPTY_BIRTH_DATE);
        birthDate.focus();
        return false;
    } else if (isNotValidBirthDateFormat(birthDate.value)) {
        showMessage(messages.NOT_VALID_DATE);
        birthDate.focus();
        return false;
    }
    return true;
}

function validateCertYn(certYn) {
    if (certYn.value === 'Y') {
        return true;
    } else if (certYn.value === 'N') {
        showMessage(messages.NOT_CERT_EMAIL);
        return false;
    } else {
        showMessage(messages.NOT_CERT_EMAIL);
        return false;
    }
}

function validateAccountDupl(accountCertYn) {
    if (accountCertYn.value === 'Y') {
        return true;
    } else if (accountCertYn.value === 'N') {
        showMessage(messages.NOT_CERT_ACCOUNT);
        return false;
    } else {
        showMessage(messages.NOT_CERT_ACCOUNT);
        return false;
    }
}

let timeLeft = 180;
let timer;
function startTimer() {
    timer = setInterval(() => {
        const min = Math.floor(timeLeft / 60);
        const sec = timeLeft % 60;

        document.getElementById('timeLeft').innerText = `${min.toString().padStart(2, '0')}:${sec.toString().padStart(2, '0')}`;

        timeLeft--;

        if (timeLeft < 0) {
            // 제한 시간 모두 경과
            showMessage(messages.END_EMAIL_AUTH_TIME);
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
    document.getElementById('timeLeft').innerText = '03:00';
}

// let isClickEnabled = false; // Todo: 전역 변수 문제 될수도 있을듯... 확인 필요
function checkDoubleClick() {
    let isClickEnabled = localStorage.getItem('isClickEnabled');
    if (isClickEnabled) {
        return true;
    } else {
        localStorage.setItem('isClickEnabled', 'true');
        return false;
    }
}

// window.onload = function () {
//     localStorage.removeItem('isClickEnabled');
// };

// 페이지를 새로 고침하거나 닫을 때 실행되는 코드
window.addEventListener('beforeunload', function () {
    localStorage.removeItem('isClickEnabled');
});

// 인증 이메일 전송
function sendAuthEmail(event) {
    let certEmail = document.getElementById('email');
    if (!validateEmail(certEmail)) {
        return;
    }

    if (checkDoubleClick()) {
        showMessage(messages.CANNOT_SEND_EMAIL);
        return;
    }

    let request = {
        url: '/api/v1/email/verify-request',
        method: 'POST',
        contentType: 'application/json',
        requestBody: {
            email: certEmail.value,
        },
    };

    printLoadingWithMask();

    commonFetchTemplate
        .sendFetchRequest(request)
        .then(response => {
            return response.json();
        })
        .then(data => {
            if (data.code === 1009) {
                closeLoadingWithMask();
                showMessage(messages.SUCCESS_SEND_EMAIL);

                // 인증번호 입력 영역
                let verifySection = document.getElementById('verifySection');
                if (verifySection.style.display === 'none') {
                    verifySection.style.display = 'block';
                } else {
                    verifySection.style.display = 'none';
                }

                // 타이머 영역
                let timeLimitArea = document.getElementById('timeLimitArea');
                if (timeLimitArea.style.display === 'none') {
                    timeLimitArea.style.display = 'block';
                } else {
                    timeLimitArea.style.display = 'none';
                }
                startTimer(); // 타이머 시작
            } else {
                showMessage(messages.FAIL_SEND_EMAIL);
                document.getElementById('email').focus();
            }
        });
}

function verifyEmailAuthCode() {
    let certEmail = document.getElementById('email');
    let verificationCode = document.getElementById('verificationCode');
    if (isEmpty(verificationCode.value) || isNotNumericRegExp(verificationCode.value)) {
        showMessage(messages.EMPTY_EMAIL_AUTH_CODE);
        verificationCode.focus();
        return;
    }

    if (!validateEmail(certEmail) || certEmail.length === 0) {
        return;
    }

    let request = {
        url: '/api/v1/email/verify',
        method: 'GET',
        contentType: 'application/json',
        queryString: {
            email: certEmail.value,
            code: verificationCode.value,
        },
    };

    commonFetchTemplate
        .sendFetchRequest(request)
        .then(response => {
            return response.json();
        })
        .then(data => {
            if (data.code === 1010) {
                showMessage(messages.SUCCESS_CERT_EMAIL);

                document.getElementById('certYn').value = 'Y';
                document.getElementById('verificationCode').disabled = true;
                document.getElementById('sendVerification').disabled = true;
                document.getElementById('verifyCode').disabled = true;
                document.getElementById('sendVerification').removeEventListener('click', sendAuthEmail);

                stopInterval();
                resetInterval();
            } else {
                showMessage(messages.FAIL_CERT_EMAIL);
                document.getElementById('email').focus();
                stopInterval();
                resetInterval();
            }
        })
        .catch(error => {
            ``;
            console.error(`URL => ${request.url}, 이메일 인증코드 인증 확인 시 오류 발생`);
        });
}

function checkDuplAccount() {
    let account = document.getElementById('account');
    if (!validateAccount(account)) {
        return;
    } else if (!validateAccountRegExp(account)) {
        return;
    }

    let request = {
        url: '/api/v1/member/exists/{account}',
        method: 'GET',
        contentType: 'application/json',
        pathVariable: {
            account: account.value,
        },
    };

    commonFetchTemplate
        .sendFetchRequest(request)
        .then(response => {
            return response.json();
        })
        .then(data => {
            if (data.code === 1010) {
                showMessage(messages.SUCCESS_DUPL_ACCOUNT);
                document.getElementById('accountCertYn').value = 'Y';
                document.getElementById('password1').focus();
            } else if (data.code === 3001) {
                showMessage(messages.EMPTY_ACCOUNT);
                account.focus();
            } else if (data.code === 2008) {
                showMessage(messages.FAIL_DUPL_MEMBER);
                document.getElementById('accountCertYn').value = 'N';
                account.focus();
            } else if (data.code === 2009) {
                showMessage(messages.FAIL_VALIDATE_DUPL_MEMBER);
                document.getElementById('accountCertYn').value = 'N';
                account.focus();
            } else {
                showMessage(messages.FAIL_VALIDATE_DUPL_MEMBER);
                document.getElementById('accountCertYn').value = 'N';
                account.focus();
            }
        });
}

function onSignUpSuccess() {
    location.href = '/';
}

/**
 * 회원 가입 시 서버 전송 전에 앞에서 1차적으로 검증하는 로직
 * 서버 DTO 에서 어차피 검증이 되지만, 미리 검증
 * @returns {boolean} true: 검증 성공, false: 검증 실패
 */
function validateMemberJoinInfo() {
    let isValidation = false;
    if (!validateUserName(memberJoinInfo.username)) {
    } else if (!validateUserNameRegExp(memberJoinInfo.username)) {
    } else if (!validateAccount(memberJoinInfo.account)) {
    } else if (!validateAccountRegExp(memberJoinInfo.account)) {
    } else if (!validatePrefixPwd(memberJoinInfo.password1)) {
    } else if (!validateLastPwd(memberJoinInfo.password2)) {
    } else if (!validateEqualsPasswords(memberJoinInfo.password1, memberJoinInfo.password2)) {
    } else if (!validatePasswordRegExp(memberJoinInfo.password1, memberJoinInfo.password2)) {
    } else if (!validateEmail(memberJoinInfo.email)) {
    } else if (!validateEmailRegExp(memberJoinInfo.email)) {
    } else if (!validatePhonePrefix(memberJoinInfo.phonePrefix)) {
    } else if (!validatePhoneMiddle(memberJoinInfo.phoneMiddle)) {
    } else if (!validatePhoneLast(memberJoinInfo.phoneLast)) {
    } else if (!validateGender(memberJoinInfo.gender)) {
    } else if (!validateBirthDate(memberJoinInfo.birthDate)) {
    } else if (!validateNumericPhoneNumber(memberJoinInfo.phonePrefix, memberJoinInfo.phoneMiddle, memberJoinInfo.phoneLast)) {
    } else if (!validateCertYn(memberJoinInfo.certYn)) {
    } else if (!validateAccountDupl(memberJoinInfo.accountCertYn)) {
    } else {
        isValidation = true;
    }
    return isValidation;
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
    validate: function () {
        memberJoinInfo = initializedMemberInfo();
        return validateMemberJoinInfo();
    },
    join: function () {
        console.log(`start join for new member`);
        let request = {
            url: '/api/v1/member/join',
            method: 'POST',
            contentType: 'application/json',
            requestBody: {
                name: memberJoinInfo.username.value, // Todo: username -> userName camel case
                account: memberJoinInfo.account.value,
                password: memberJoinInfo.password1.value,
                email: memberJoinInfo.email.value,
                phoneNumber: [memberJoinInfo.phonePrefix.value, memberJoinInfo.phoneMiddle.value, memberJoinInfo.phoneLast.value].join('-'),
                certYn: memberJoinInfo.certYn.value,
                accountCertYn: memberJoinInfo.accountCertYn.value,
                gender: memberJoinInfo.gender.value,
                birthDate: memberJoinInfo.birthDate.value,
                // picture: memberJoinInfo.picture.value
            },
        };

        commonFetchTemplate
            .sendFetchRequest(request)
            .then(response => {
                return response.json();
            })
            .then(data => {
                if (data.code === 1008) {
                    showMessage(messages.SUCCESS_SAVE_MEMBER);
                    onSignUpSuccess();
                } else if (data.code === 2008) {
                    showMessage(messages.FAIL_DUPL_MEMBER);
                    memberJoinInfo.account.focus();
                } else if (data.code === 2006) {
                    showMessage(messages.FAIL_SAVE_MEMBER);
                } else {
                    showMessage(messages.FAIL_SAVE_MEMBER);
                }
            });
    },
};

main.init();
