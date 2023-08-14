/**
 * @since           :       2023-08-13
 * @author          :       youngmin
 * @version         :       1.0.0
 * @description     :       회원 가입 시 기본적인 Validation과 회원 가입 요청을 위해 사용
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-08-13       youngmin           최초 생성
 **/


/**
 * 유효한 성별인지 체크, 다른 성별일 경우 False 반환
 * @param memberJoinInfoObj.genie.value :: 'M:남자', 'F:여자'
 * @returns {boolean}
 */
function isNotValidGender(gender) {
    return gender !== 'M' || gender !== 'F';
}

/**
 * 유효한 생년월일인지 체크, 해당 포맷에 맞지 않으면 개발자 도구로 조작 한걸로 판단
 * @param memberJoinInfoObj
 * @returns {boolean}
 */
function isNotValidBirthDateFormat(birthDate) {
    const regex = /^\d{4}-\d{2}-\d{2}$/;
    return !regex.test(birthDate);
}

/**
 * 맴버의 회원 가입 정보를 저장하여 obj 형태로 반환
 */
function setMemberJoinInfoObj() {
    console.debug(`setMemberJoinInfoObj...`);
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

function validateUsername(username) {
    if (isEmpty(username)) {
        showMessage(messages.EMPTY_USER_NAME);
        return false;
    } else if (username.length > 6) {
        showMessage(messages.OVER_LENGTH_USER_NAME);
        return false;
    }
    return true;
}

function validateAccount(account) {
    if (isEmpty(account)) {
        showMessage(messages.EMPTY_ACCOUNT);
        return false;
    } else if (account.length > 30) {
        showMessage(messages.OVER_LENGTH_ACCOUNT);
        return false;
    }
    return true;
}

function validatePrefixPwd(password1) {
    if (isEmpty(password1)) {
        showMessage(messages.EMPTY_PASSWORD1);
        return false;
    } else if (password1.length < 8) {
        showMessage(messages.OVER_LENGTH_PWD);
        return false;
    }
    return true;
}

function validateLastPwd(password2) {
    if (isEmpty(password2)) {
        showMessage(messages.EMPTY_PASSWORD2)
        return false;
    } else if (password2.length < 8) {
        showMessage(messages.OVER_LENGTH_PWD);
        return false;
    }
    return true;
}

function validateEmail(email) {
    if (isEmpty(email)) {
        showMessage(messages.EMPTY_EMAIL);
        return false;
    } else if (email.length > 45) {
        showMessage(messages.OVER_LENGTH_EMAIL);
        return false;
    }
    return true;
}

function validatePhonePrefix(phonePrefix) {
    if (isEmpty(phonePrefix)) {
        showMessage(messages.EMPTY_PHONE_PREFIX);
        return false;
    } else if (phonePrefix.length > 3) {
        showMessage(messages.EMPTY_PHONE_PREFIX);
        return false;
    }
}

function validatePhoneMiddle(phoneMiddle) {
    if (isEmpty(phoneMiddle)) {
        showMessage(messages.EMPTY_PHONE_MIDDLE)
        return false;
    } else if (phoneMiddle.length > 4) {
        showMessage(messages.OVER_LENGTH_PHONE_MIDDLE);
        return false;
    }
    return true;
}

function validatePhoneLast(phoneLast) {
    if (isEmpty(phoneLast)) {
        showMessage(messages.EMPTY_PHONE_LAST);
        return false;
    } else if (phoneLast.length > 4) {
        showMessage(messages.OVER_LENGTH_PHONE_LAST);
        return false;
    }
    return true;
}

function validateGender(gender) {
    if (isEmpty(gender)) {
        showMessage(messages.EMPTY_GENDER);
        return false;
    } else if (isNotValidGender(gender)) {
        showMessage(messages.NOT_VALID_GENDER);
        return false;
    }
    return true;
}

function validateBirthDate(birthDate) {
    if (isEmpty(birthDate)) {
        showMessage(messages.EMPTY_BIRTH_DATE)
        return false;
    } else if (isNotValidBirthDateFormat(birthDate)) {
        showMessage(messages.NOT_VALID_DATE);
        return false;
    }
    return true;
}

/**
 * 맴버의 회원 가입 정보를 서버에 전달 하기전에 1차 검증 : 텍스트가 공백인지 확인
 */
function validateIsEmptyValue(memberJoinInfoObj) {
    console.debug(`validateMemberJoinInfoObj...`);

    // 각 요소의 텍스트가 공백인지 체크
    if (isEmpty(memberJoinInfoObj.username.value)) {
        alert(messages.EMPTY_USER_NAME);
        return;
    } else if (memberJoinInfoObj.username.value.length > 6) {
        alert(messages.OVER_LENGTH_USER_NAME);
        return;
    }

    else if (isEmpty(memberJoinInfoObj.account.value)) {
        alert(messages.EMPTY_ACCOUNT);
        return;
    } else if (memberJoinInfoObj.account.value.length > 30) {
        alert(messages.OVER_LENGTH_ACCOUNT);
        return;
    }

    else if (isEmpty(memberJoinInfoObj.password1.value)) {
        alert(messages.EMPTY_PASSWORD1);
        return;
    } else if (memberJoinInfoObj.password1.value.length < 8) {
        alert(messages.OVER_LENGTH_PWD);
        return;
    }

    else if (isEmpty(memberJoinInfoObj.password2.value)) {
        alert(messages.EMPTY_PASSWORD2)
        return;
    } else if (memberJoinInfoObj.password2.value.length < 8) {
        alert(messages.OVER_LENGTH_PWD);
        return;
    }

    else if (isEmpty(memberJoinInfoObj.email.value)) {
        alert(messages.EMPTY_EMAIL);
        return;
    } else if (memberJoinInfoObj.email.value.length > 45) {
        alert(messages.OVER_LENGTH_EMAIL);
        return;
    }

    else if (isEmpty(memberJoinInfoObj.phonePrefix.value)) {
        alert(messages.EMPTY_PHONE_PREFIX);
        return;
    } else if (memberJoinInfoObj.phonePrefix.value.length > 3) {
        alert(messages.EMPTY_PHONE_PREFIX);
        return;
    }

    else if (isEmpty(memberJoinInfoObj.phoneMiddle.value)) {
        alert(messages.EMPTY_PHONE_MIDDLE)
        return;
    } else if (memberJoinInfoObj.phoneMiddle.value.length > 4) {
        alert(messages.OVER_LENGTH_PHONE_MIDDLE);
        return;
    }

    else if (isEmpty(memberJoinInfoObj.phoneLast.value)) {
        alert(messages.EMPTY_PHONE_LAST);
        return;
    } else if (memberJoinInfoObj.phoneLast.value.length > 4) {
        alert(messages.OVER_LENGTH_PHONE_LAST);
        return;
    }

    else if (isEmpty(memberJoinInfoObj.gender.value)) {
        alert(messages.EMPTY_GENDER);
        return;
    } else if (isNotValidGender(memberJoinInfoObj)) {
        alert(messages.NOT_VALID_GENDER);
        return;
    }

    else if (isEmpty(memberJoinInfoObj.birthDate.value)) {
        alert(messages.EMPTY_BIRTH_DATE)
        return;
    } else if (isNotValidBirthDateFormat(memberJoinInfoObj)) {
        alert(messages.NOT_VALID_DATE);
        return;
    }
    return true;
}

const main = {
    init: function () {
        console.debug(`initializing join func... this => ${this}`);
        let _this = this;
        $('#btn-join').on('click', function () {
            alert('click btn-save button...');
            _this.validate();
            _this.join();
        });
        console.debug(`finished init func...`);
    },
    validate: function() {
        // validate join form
        console.debug(`starting validate func...`);

        // 값 세팅
        let memberJoinInfoObj = setMemberJoinInfoObj();
        console.debug(`memberJoinInfoObj => ${JSON.stringify(memberJoinInfoObj)}`);

        // 1차 유효성 검증
        const resultByValidateEmptyText  = validateIsEmptyValue(memberJoinInfoObj);
        console.debug(`resultByValidateEmptyText => ${resultByValidateEmptyText}`);

        // 2차 케이스별 유효성 검증

    },
    join: function () {
        // start join for basic member
        console.log(`starting join func...`);

    }
};

// initialized
main.init();