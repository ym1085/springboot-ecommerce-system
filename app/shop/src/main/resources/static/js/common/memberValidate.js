/**
 * Create member request object data
 */
const memberRequestDataBuilder = {
    createMemberRequestBodyInfoBuilder(request) {
        const requestBody = {};
        if (request.requestBody.userName) {
            requestBody.userName = request.requestBody.userName;
        }
        if (request.requestBody.account) {
            requestBody.account = request.requestBody.account;
        }
        if (request.requestBody.password) {
            requestBody.password = request.requestBody.password;
        }
        if (request.requestBody.email) {
            requestBody.email = request.requestBody.email;
        }
        if (request.requestBody.phonePrefix && request.requestBody.phoneMiddle && request.requestBody.phoneLast) {
            requestBody.phoneNumber = `${request.requestBody.phonePrefix}-${request.requestBody.phoneMiddle}-${request.requestBody.phoneLast}`;
        }
        if (request.requestBody.certYn) {
            requestBody.certYn = request.requestBody.certYn;
        }
        if (request.requestBody.accountCertYn) {
            requestBody.accountCertYn = request.requestBody.accountCertYn;
        }
        if (request.requestBody.gender) {
            requestBody.gender = request.requestBody.gender;
        }
        if (request.requestBody.birthDate) {
            requestBody.birthDate = request.requestBody.birthDate;
        }
        if (request.requestBody.role) {
            requestBody.role = request.requestBody.role;
        }

        return {
            baseUrl: request.baseUrl,
            method: request.method,
            headers: request.headers,
            requestBody: requestBody,
        };
    },

    createMemberRequestQueryStringInfoBuilder(request) {
        const queryParams = [];
        if (request.queryString.userName) {
            queryParams.push(`userName=${encodeURIComponent(request.queryString.userName)}`);
        }
        if (request.queryString.account) {
            queryParams.push(`account=${encodeURIComponent(request.queryString.account)}`);
        }
        if (request.queryString.password) {
            queryParams.push(`password=${encodeURIComponent(request.queryString.password)}`);
        }
        if (request.queryString.email) {
            queryParams.push(`email=${encodeURIComponent(request.queryString.email)}`);
        }
        if (request.queryString.phonePrefix && request.queryString.phoneMiddle && request.queryString.phoneLast) {
            const phoneNumber = `${request.queryString.phonePrefix}-${request.queryString.phoneMiddle}-${request.queryString.phoneLast}`;
            queryParams.push(`phoneNumber=${encodeURIComponent(phoneNumber)}`);
        }
        if (request.queryString.certYn) {
            queryParams.push(`certYn=${encodeURIComponent(request.queryString.certYn)}`);
        }
        if (request.queryString.accountCertYn) {
            queryParams.push(`accountCertYn=${encodeURIComponent(request.queryString.accountCertYn)}`);
        }
        if (request.queryString.birthDate) {
            queryParams.push(`birthDate=${encodeURIComponent(request.queryString.birthDate)}`);
        }
        if (request.queryString.gender) {
            queryParams.push(`gender=${encodeURIComponent(request.queryString.gender)}`);
        }
        if (request.queryString.role) {
            queryParams.push(`role=${encodeURIComponent(request.queryString.role)}`);
        }
        if (request.queryString.verifyCode) {
            queryParams.push(`verifyCode=${encodeURIComponent(request.queryString.verifyCode)}`);
        }

        // Combine elements from each array with &
        const queryString = queryParams.join('&');

        return {
            baseUrl: request.baseUrl,
            method: request.method,
            headers: request.headers,
            queryString: queryString,
        };
    },
};

const memberJoinValidator = {
    validateUserName(userName) {
        if (isEmpty(userName.value)) {
            showMessage(messages.NOT_FOUND_MEMBER_NAME.msg);
            userName.focus();
            return false;
        } else if (userName.length > 6) {
            showMessage(messages.OVER_LENGTH_MEMBER_NAME.msg);
            userName.focus();
            return false;
        }
        return true;
    },

    validateUserNameRegExp(userName) {
        // const regEx = /^[ㄱ-ㅎㅏ-ㅣ가-힣]*$/;
        const regEx = /^[가-힣]{2,}$/;
        if (!regEx.test(userName.value)) {
            showMessage(messages.INVALID_MEMBER_NAME.msg);
            userName.focus();
            return false;
        }
        return true;
    },

    validateAccount(account) {
        if (isEmpty(account.value)) {
            showMessage(messages.NOT_FOUND_MEMBER_ACCOUNT.msg);
            account.focus();
            return false;
        } else if (account.length > 30) {
            showMessage(messages.OVER_LENGTH_ACCOUNT.msg);
            account.focus();
            return false;
        }
        return true;
    },

    validateAccountRegExp(account) {
        const regEx = /^(?=.*\d)[a-zA-Z\d]+$/; // 숫자가 한 개 이상 포함, 영문자 또는 숫자로 구성
        if (!regEx.test(account.value)) {
            showMessage(messages.INVALID_MEMBER_ACCOUNT.msg);
            account.focus();
            return false;
        }
        return true;
    },

    validatePasswordRegExp(password1, password2) {
        const regEx = /^(?=.*[a-zA-Z])(?=.*[!@#$%^&*]).{8,}$/;
        if (!regEx.test(password1.value)) {
            showMessage(messages.INVALID_MEMBER_PWD.msg);
            password1.focus();
            return false;
        }
        if (!regEx.test(password2.value)) {
            showMessage(messages.INVALID_MEMBER_PWD.msg);
            password2.focus();
            return false;
        }
        return true;
    },

    validateEqualsPasswords(password1, password2) {
        if (password1.value !== password2.value) {
            showMessage(messages.NOT_MATCH_PWD.msg);
            password1.focus();
            return false;
        }
        return true;
    },

    validatePrefixPwd(password1) {
        if (isEmpty(password1.value)) {
            showMessage(messages.NOT_FOUND_MEMBER_PASSWORD1.msg);
            password1.focus();
            return false;
        } else if (password1.length < 8) {
            showMessage(messages.OVER_LENGTH_PWD.msg);
            password1.focus();
            return false;
        }
        return true;
    },

    validateLastPwd(password2) {
        if (isEmpty(password2.value)) {
            showMessage(messages.NOT_FOUND_MEMBER_PASSWORD2.msg);
            password2.focus();
            return false;
        } else if (password2.length < 8) {
            showMessage(messages.OVER_LENGTH_PWD.msg);
            password2.focus();
            return false;
        }
        return true;
    },

    validateEmailRegExp(email) {
        const regExp = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;
        if (!regExp.test(email.value)) {
            showMessage(messages.INVALID_MEMBER_EMAIL.msg);
            email.focus();
            return false;
        }
        return true;
    },

    validateEmail(email) {
        if (isEmpty(email.value)) {
            showMessage(messages.NOT_FOUND_MEMBER_EMAIL.msg);
            email.focus();
            return false;
        } else if (email.length > 45) {
            showMessage(messages.OVER_LENGTH_EMAIL.msg);
            email.focus();
            return false;
        }
        return true;
    },

    validateNumericPhoneNumber(phonePrefix, phoneMiddle, phoneLast) {
        if (
            isNotNumericRegExp(phonePrefix.value) &&
            isNotNumericRegExp(phoneMiddle.value) &&
            isNotNumericRegExp(phoneLast.value)
        ) {
            showMessage(messages.INVALID_MEMBER_PHONE.msg);
            phoneMiddle.focus();
            return false;
        }
        return true;
    },

    validatePhonePrefix(phonePrefix) {
        if (isEmpty(phonePrefix.value)) {
            showMessage(messages.NOT_FOUND_MEMBER_PHONE_PREFIX.msg);
            phonePrefix.focus();
            return false;
        } else if (phonePrefix.value.trim().length > 3) {
            showMessage(messages.OVER_LENGTH_PHONE_PREFIX.msg);
            phonePrefix.focus();
            return false;
        }
        return true;
    },

    validatePhoneMiddle(phoneMiddle) {
        if (isEmpty(phoneMiddle.value)) {
            showMessage(messages.NOT_FOUND_MEMBER_PHONE_MIDDLE.msg);
            phoneMiddle.focus();
            return false;
        } else if (phoneMiddle.value.trim().length > 4) {
            showMessage(messages.OVER_LENGTH_PHONE_MIDDLE.msg);
            phoneMiddle.focus();
            return false;
        }
        return true;
    },

    validatePhoneLast(phoneLast) {
        if (isEmpty(phoneLast.value)) {
            showMessage(messages.NOT_FOUND_MEMBER_PHONE_LAST.msg);
            phoneLast.focus();
            return false;
        } else if (phoneLast.value.trim().length > 4) {
            showMessage(messages.OVER_LENGTH_PHONE_LAST.msg);
            phoneLast.focus();
            return false;
        }
        return true;
    },

    isNotValidGender(gender) {
        return gender !== 'M' && gender !== 'F';
    },

    validateGender(gender) {
        if (isEmpty(gender.value)) {
            showMessage(messages.NOT_FOUND_MEMBER_GENDER.msg);
            gender.focus();
            return false;
        } else if (this.isNotValidGender(gender.value)) {
            showMessage(messages.INVALID_MEMBER_GENDER.msg);
            gender.focus();
            return false;
        }
        return true;
    },

    isNotValidBirthDateFormat(birthDate) {
        const regex = /^\d{4}-\d{2}-\d{2}$/;
        return !regex.test(birthDate);
    },

    validateBirthDate(birthDate) {
        if (isEmpty(birthDate.value)) {
            showMessage(messages.NOT_FOUND_MEMBER_BIRTHDATE.msg);
            birthDate.focus();
            return false;
        } else if (this.isNotValidBirthDateFormat(birthDate.value)) {
            showMessage(messages.INVALID_MEMBER_BIRTHDATE.msg);
            birthDate.focus();
            return false;
        }
        return true;
    },

    validateCertYn(certYn) {
        if (certYn.value === 'Y') {
            return true;
        } else if (certYn.value === 'N') {
            showMessage(messages.NOT_CERT_EMAIL.msg);
            return false;
        } else {
            showMessage(messages.NOT_CERT_EMAIL.msg);
            return false;
        }
    },

    validateDuplicateAccount(accountCertYn) {
        if (accountCertYn.value === 'Y') {
            return true;
        } else if (accountCertYn.value === 'N') {
            showMessage(messages.NOT_CERT_ACCOUNT.msg);
            return false;
        } else {
            showMessage(messages.NOT_CERT_ACCOUNT.msg);
            return false;
        }
    },

    hideVerifySectionLayer() {
        let verifySection = document.getElementById('verifySection');
        if (verifySection.style.display === 'none') {
            verifySection.style.display = 'block';
        } else {
            verifySection.style.display = 'none';
        }
    },

    hideTimerSectionLayer() {
        let timeLimitArea = document.getElementById('timeLimitArea');
        if (timeLimitArea.style.display === 'none') {
            timeLimitArea.style.display = 'block';
        } else {
            timeLimitArea.style.display = 'none';
        }
    },

    async sendAuthEmail(event) {
        let certEmail = document.getElementById('email');
        if (!this.validateEmail(certEmail)) {
            return;
        }

        if (checkDoubleClick()) {
            showMessage(messages.CANNOT_SEND_EMAIL.msg);
            return;
        }

        // FIXME: 추후 수정 해야함, 로딩 나오도록
        // showLoadingMask();

        const requestDataObj = {
            baseUrl: URL_VERIFY_REQUEST,
            method: HTTP_METHODS.POST,
            headers: {
                [HEADER_KEY.CONTENT_TYPE]: CONTENT_TYPE.JSON,
            },
            requestBody: {
                email: certEmail.value,
            },
        };

        let requestFetchObj = memberRequestDataBuilder.createMemberRequestBodyInfoBuilder(requestDataObj);

        try {
            const response = await commonFetchTemplate.sendFetchRequest(requestFetchObj);
            const result = await response.json();
            if (result.statusCode === messages.STATUS.OK && result.success) {
                showMessage(result.message);
                closeLoadingWithMask();
                this.hideVerifySectionLayer();
                this.hideTimerSectionLayer();
                startTimer();
            } else {
                showMessage(result.message);
                certEmail.focus();
            }
        } catch (error) {
            console.error(`requestURL => ${requestFetchObj.baseUrl} error => ${error}`);
            commonFetchTemplate.handleResponseError(error, requestFetchObj);
        }
    },

    /**
     * 이메일 인증 완료 후, 이메일 인증 버튼 비활성화 수행
     */
    changeCertYnStatusLayer() {
        document.getElementById('certYn').value = 'Y';
        document.getElementById('verificationCode').disabled = true;
        document.getElementById('sendVerification').disabled = true;
        document.getElementById('verifyCode').disabled = true;
        document.getElementById('sendVerification').removeEventListener('click', this.sendAuthEmail);
    },

    /**
     * 이메일 인증 수행
     */
    async verifyEmailAuthCode() {
        const certEmail = document.getElementById('email');
        const verificationCode = document.getElementById('verificationCode');
        if (isEmpty(verificationCode.value) || isEmpty(verificationCode.value)) {
            showMessage(messages.NOT_FOUND_MEMBER_EMAIL_CERT_YN.msg);
            verificationCode.focus();
            return;
        }

        if (!this.validateEmail(certEmail) || certEmail.length === 0) {
            return;
        }

        const requestDataObj = {
            baseUrl: URL_VERIFY_EMAIL,
            method: HTTP_METHODS.GET,
            queryString: {
                email: certEmail.value,
                verifyCode: verificationCode.value,
            },
        };

        let requestFetchObj = memberRequestDataBuilder.createMemberRequestQueryStringInfoBuilder(requestDataObj);

        try {
            const response = await commonFetchTemplate.sendFetchRequest(requestFetchObj);
            const result = await response.json();
            if (result.statusCode === messages.STATUS.OK && result.success) {
                showMessage(result.message);
                this.changeCertYnStatusLayer();
            } else {
                showMessage(result.message);
                certEmail.focus();
            }
        } catch (error) {
            commonFetchTemplate.handleResponseError(error);
        } finally {
            stopTimer();
            resetTimer();
        }
    },

    async checkDuplicateAccount() {
        const account = document.getElementById('account');
        const accountCertYn = document.getElementById('accountCertYn');
        const password1 = document.getElementById('password1');
        if (!this.validateAccount(account)) {
            return;
        } else if (!this.validateAccountRegExp(account)) {
            return;
        }

        const requestDataObj = {
            baseUrl: URL_MEMBER_EXISTS_ACCOUNT,
            method: HTTP_METHODS.POST,
            headers: {
                [HEADER_KEY.CONTENT_TYPE]: CONTENT_TYPE.JSON,
            },
            requestBody: {
                account: account.value,
            },
        };

        let requestFetchObj = memberRequestDataBuilder.createMemberRequestBodyInfoBuilder(requestDataObj);

        try {
            showLoadingMask();
            const response = await commonFetchTemplate.sendFetchRequest(requestFetchObj);
            const result = await response.json();
            if (result.statusCode === messages.STATUS.OK && result.success) {
                showMessage(result.message);
                accountCertYn.value = 'Y';
                password1.focus();
            } else {
                showMessage(result.message);
                accountCertYn.value = 'N';
                account.focus();
            }
        } catch (error) {
            commonFetchTemplate.handleResponseError(error, requestFetchObj);
        }
    },
};
