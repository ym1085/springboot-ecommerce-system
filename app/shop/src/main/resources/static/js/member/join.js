let memberJoinInfo = {};

const URL_VERIFY_REQUEST = '/api/v1/email/verify-request'; // 인증 코드 전송
const URL_VERIFY_EMAIL = '/api/v1/email/verify'; // 인증
const URL_MEMBER_EXISTS_ACCOUNT = '/api/v1/member/exists'; // 아이디 중복
const URL_MEMBER_JOIN = '/api/v1/member/join'; // 회원 가입

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

/*
    공통 함수로 만들어서 유효성 검증을 할 수 도 있지만,
    이왕이면 SRP에 맞춰서 함수를 가장 작게 만들어서 사용 한다
*/

function validateUserName(userName) {
    if (isEmpty(userName.value)) {
        showMessage(messages.NOT_FOUND_MEMBER_NAME.message);
        userName.focus();
        return false;
    } else if (userName.length > 6) {
        showMessage(messages.OVER_LENGTH_MEMBER_NAME.message);
        userName.focus();
        return false;
    }
    return true;
}

function validateUserNameRegExp(userName) {
    // const regEx = /^[ㄱ-ㅎㅏ-ㅣ가-힣]*$/;
    const regEx = /^[가-힣]{2,}$/;
    if (!regEx.test(userName.value)) {
        showMessage(messages.INVALID_MEMBER_NAME.message);
        userName.focus();
        return false;
    }
    return true;
}

function validateAccount(account) {
    if (isEmpty(account.value)) {
        showMessage(messages.NOT_FOUND_MEMBER_ACCOUNT.message);
        account.focus();
        return false;
    } else if (account.length > 30) {
        showMessage(messages.OVER_LENGTH_ACCOUNT.message);
        account.focus();
        return false;
    }
    return true;
}

function validateAccountRegExp(account) {
    const regEx = /^(?=.*\d)[a-zA-Z\d]+$/; // 숫자가 한 개 이상 포함, 영문자 또는 숫자로 구성
    if (!regEx.test(account.value)) {
        showMessage(messages.INVALID_MEMBER_ACCOUNT.message);
        account.focus();
        return false;
    }
    return true;
}

function validatePasswordRegExp(password1, password2) {
    const regEx = /^(?=.*[a-zA-Z])(?=.*[!@#$%^&*]).{8,}$/;
    if (!regEx.test(password1.value)) {
        showMessage(messages.INVALID_MEMBER_PWD.message);
        password1.focus();
        return false;
    }
    if (!regEx.test(password2.value)) {
        showMessage(messages.INVALID_MEMBER_PWD.message);
        password2.focus();
        return false;
    }
    return true;
}

function validateEqualsPasswords(password1, password2) {
    if (password1.value !== password2.value) {
        showMessage(messages.NOT_MATCH_PWD.message);
        password1.focus();
        return false;
    }
    return true;
}

function validatePrefixPwd(password1) {
    if (isEmpty(password1.value)) {
        showMessage(messages.NOT_FOUND_MEMBER_PASSWORD1.message);
        password1.focus();
        return false;
    } else if (password1.length < 8) {
        showMessage(messages.OVER_LENGTH_PWD.message);
        password1.focus();
        return false;
    }
    return true;
}

function validateLastPwd(password2) {
    if (isEmpty(password2.value)) {
        showMessage(messages.NOT_FOUND_MEMBER_PASSWORD2.message);
        password2.focus();
        return false;
    } else if (password2.length < 8) {
        showMessage(messages.OVER_LENGTH_PWD.message);
        password2.focus();
        return false;
    }
    return true;
}

function validateEmailRegExp(email) {
    const regExp = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;
    if (!regExp.test(email.value)) {
        showMessage(messages.INVALID_MEMBER_EMAIL.message);
        email.focus();
        return false;
    }
    return true;
}

function validateEmail(email) {
    if (isEmpty(email.value)) {
        showMessage(messages.NOT_FOUND_MEMBER_EMAIL.message);
        email.focus();
        return false;
    } else if (email.length > 45) {
        showMessage(messages.OVER_LENGTH_EMAIL.message);
        email.focus();
        return false;
    }
    return true;
}

function validateNumericPhoneNumber(phonePrefix, phoneMiddle, phoneLast) {
    if (isNotNumericRegExp(phonePrefix.value) && isNotNumericRegExp(phoneMiddle.value) && isNotNumericRegExp(phoneLast.value)) {
        showMessage(messages.INVALID_MEMBER_PHONE.message);
        phoneMiddle.focus();
        return false;
    }
    return true;
}

function validatePhonePrefix(phonePrefix) {
    if (isEmpty(phonePrefix.value)) {
        showMessage(messages.NOT_FOUND_MEMBER_PHONE_PREFIX.message);
        phonePrefix.focus();
        return false;
    } else if (phonePrefix.value.trim().length > 3) {
        showMessage(messages.OVER_LENGTH_PHONE_PREFIX.message);
        phonePrefix.focus();
        return false;
    }
    return true;
}

function validatePhoneMiddle(phoneMiddle) {
    if (isEmpty(phoneMiddle.value)) {
        showMessage(messages.NOT_FOUND_MEMBER_PHONE_MIDDLE.message);
        phoneMiddle.focus();
        return false;
    } else if (phoneMiddle.value.trim().length > 4) {
        showMessage(messages.OVER_LENGTH_PHONE_MIDDLE.message);
        phoneMiddle.focus();
        return false;
    }
    return true;
}

function validatePhoneLast(phoneLast) {
    if (isEmpty(phoneLast.value)) {
        showMessage(messages.NOT_FOUND_MEMBER_PHONE_LAST.message);
        phoneLast.focus();
        return false;
    } else if (phoneLast.value.trim().length > 4) {
        showMessage(messages.OVER_LENGTH_PHONE_LAST.message);
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
        showMessage(messages.NOT_FOUND_MEMBER_GENDER.message);
        gender.focus();
        return false;
    } else if (isNotValidGender(gender.value)) {
        showMessage(messages.INVALID_MEMBER_GENDER.message);
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
        showMessage(messages.NOT_FOUND_MEMBER_BIRTHDATE.message);
        birthDate.focus();
        return false;
    } else if (isNotValidBirthDateFormat(birthDate.value)) {
        showMessage(messages.INVALID_MEMBER_BIRTHDATE.message);
        birthDate.focus();
        return false;
    }
    return true;
}

function validateCertYn(certYn) {
    if (certYn.value === 'Y') {
        return true;
    } else if (certYn.value === 'N') {
        showMessage(messages.NOT_CERT_EMAIL.message);
        return false;
    } else {
        showMessage(messages.NOT_CERT_EMAIL.message);
        return false;
    }
}

function validateAccountDupl(accountCertYn) {
    if (accountCertYn.value === 'Y') {
        return true;
    } else if (accountCertYn.value === 'N') {
        showMessage(messages.NOT_CERT_ACCOUNT.message);
        return false;
    } else {
        showMessage(messages.NOT_CERT_ACCOUNT.message);
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
            showMessage(messages.END_EMAIL_AUTH_TIME.message);
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

function hideVerifySectionLayer() {
    let verifySection = document.getElementById('verifySection');
    if (verifySection.style.display === 'none') {
        verifySection.style.display = 'block';
    } else {
        verifySection.style.display = 'none';
    }
}

function hideTimerSectionLayer() {
    let timeLimitArea = document.getElementById('timeLimitArea');
    if (timeLimitArea.style.display === 'none') {
        timeLimitArea.style.display = 'block';
    } else {
        timeLimitArea.style.display = 'none';
    }
}

// 인증 이메일 전송
function sendAuthEmail(event) {
    let certEmail = document.getElementById('email');

    if (!validateEmail(certEmail)) {
        return;
    }

    if (checkDoubleClick()) {
        showMessage(messages.CANNOT_SEND_EMAIL.message);
        return;
    }

    const request = queryBuilder
        .createQueryBuilder()
        .baseUrl(URL_VERIFY_REQUEST)
        .method('POST')
        .contentType('application/json')
        .requestBody({ email: certEmail.value })
        .build();

    printLoadingWithMask();

    const response = fetchTemplate
        .sendFetchRequest(request)
        .then(response => response.json())
        .then(result => {
            if (result.code === messages.STATUS.OK) {
                showMessage(result.message);
                closeLoadingWithMask();
                hideVerifySectionLayer();
                hideTimerSectionLayer();
                startTimer();
            } else {
                showMessage(result.message);
                certEmail.focus();
            }
        });
}

// 이메일 인증 후 버튼 비활성화
function changeCertYnStatusLayer() {
    document.getElementById('certYn').value = 'Y';
    document.getElementById('verificationCode').disabled = true;
    document.getElementById('sendVerification').disabled = true;
    document.getElementById('verifyCode').disabled = true;
    document.getElementById('sendVerification').removeEventListener('click', sendAuthEmail);
}

function verifyEmailAuthCode() {
    const certEmail = document.getElementById('email');
    const verificationCode = document.getElementById('verificationCode');
    if (isEmpty(verificationCode.value) || isNotNumericRegExp(verificationCode.value)) {
        showMessage(messages.NOT_FOUND_MEMBER_EMAIL_CERT_YN.message);
        verificationCode.focus();
        return;
    }

    if (!validateEmail(certEmail) || certEmail.length === 0) {
        return;
    }

    const request = queryBuilder
        .createQueryBuilder()
        .baseUrl(URL_VERIFY_EMAIL)
        .method('GET')
        .contentType('application/json')
        .queryString({
            email: certEmail.value,
            code: verificationCode.value,
        })
        .build();

    const response = fetchTemplate
        .sendFetchRequest(request)
        .then(response => response.json())
        .then(result => {
            if (result.code === messages.STATUS.OK) {
                showMessage(result.message);
                changeCertYnStatusLayer();
            } else {
                showMessage(result.message);
                certEmail.focus();
            }
        })
        .catch(error => handleResponseError(error, request))
        .finally(() => {
            stopInterval();
            resetInterval();
        });
}

function checkDuplAccount() {
    alert(`1`);
    const account = document.getElementById('account');
    const accountCertYn = document.getElementById('accountCertYn');
    const password1 = document.getElementById('password1');
    if (!validateAccount(account)) {
        return;
    } else if (!validateAccountRegExp(account)) {
        return;
    }
    alert(`2`);

    const request = queryBuilder
        .createQueryBuilder()
        .baseUrl(URL_MEMBER_EXISTS_ACCOUNT)
        .method('POST')
        .contentType('application/json')
        .requestBody({ account: account.value })
        .build();

    alert(`3`);

    const response = fetchTemplate
        .sendFetchRequest(request)
        .then(response => response.json())
        .then(result => {
            if (result.code === messages.STATUS.OK) {
                showMessage(result.message);
                accountCertYn.value = 'Y';
                password1.focus();
            } else {
                showMessage(result.message);
                accountCertYn.value = 'N';
                account.focus();
            }
        }).catch(error => {
            console.error(`아이디 중복 검사에 실패하였습니다. error => ${error}`);
        });
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
                showMessage(messages.PROCEED_MEMBER_JOIN.message);
                _this.join(); // 회원 가입 진행
            }
        });
    },
    validate: function () {
        memberJoinInfo = initializedMemberInfo();
        return validateMemberJoinInfo();
    },
    createMemberJoinBuilder: function () {
        return queryBuilder
            .createQueryBuilder()
            .baseUrl(URL_MEMBER_JOIN)
            .method('POST')
            .contentType('application/json')
            .requestBody({
                name: memberJoinInfo.username.value,
                account: memberJoinInfo.account.value,
                password: memberJoinInfo.password1.value,
                email: memberJoinInfo.email.value,
                phoneNumber: [memberJoinInfo.phonePrefix.value, memberJoinInfo.phoneMiddle.value, memberJoinInfo.phoneLast.value].join('-'),
                certYn: memberJoinInfo.certYn.value,
                accountCertYn: memberJoinInfo.accountCertYn.value,
                gender: memberJoinInfo.gender.value,
                birthDate: memberJoinInfo.birthDate.value,
                // picture: memberJoinInfo.picture.value
            })
            .build();
    },
    join: function () {
        console.log(`start join for new member`);
        const request = this.createMemberJoinBuilder();

        const response = fetchTemplate
            .sendFetchRequest(request)
            .then(response => response.json())
            .then(result => {
                if (result.code === messages.STATUS.OK) {
                    showMessage(result.message);
                    redirectURL('/');
                } else {
                    showMessage(result.message);
                }
            });
    },
};

main.init();
