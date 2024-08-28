let memberJoinInfoObj = {};

$(function () {
    /* document is already on loaded */
    main.init();
});

/**
 * Set the member information global variable memberJoinInfoObj,
 * when the Join button is clicked
 */
function initializedMemberJoinInfo() {
    memberJoinInfoObj = {
        userName: document.getElementById('userName'),
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

function validateMemberJoinInfo() {
    const validations = [
        () => memberJoinValidator.validateUserName(memberJoinInfoObj.userName),
        () => memberJoinValidator.validateUserNameRegExp(memberJoinInfoObj.userName),
        () => memberJoinValidator.validateAccount(memberJoinInfoObj.account),
        () => memberJoinValidator.validateAccountRegExp(memberJoinInfoObj.account),
        () => memberJoinValidator.validatePrefixPwd(memberJoinInfoObj.password1),
        () => memberJoinValidator.validateLastPwd(memberJoinInfoObj.password2),
        () => memberJoinValidator.validateEqualsPasswords(memberJoinInfoObj.password1, memberJoinInfoObj.password2),
        () => memberJoinValidator.validatePasswordRegExp(memberJoinInfoObj.password1, memberJoinInfoObj.password2),
        () => memberJoinValidator.validateEmail(memberJoinInfoObj.email),
        () => memberJoinValidator.validateEmailRegExp(memberJoinInfoObj.email),
        () => memberJoinValidator.validatePhonePrefix(memberJoinInfoObj.phonePrefix),
        () => memberJoinValidator.validatePhoneMiddle(memberJoinInfoObj.phoneMiddle),
        () => memberJoinValidator.validatePhoneLast(memberJoinInfoObj.phoneLast),
        () => memberJoinValidator.validateGender(memberJoinInfoObj.gender),
        () => memberJoinValidator.validateBirthDate(memberJoinInfoObj.birthDate),
        () =>
            memberJoinValidator.validateNumericPhoneNumber(
                memberJoinInfoObj.phonePrefix,
                memberJoinInfoObj.phoneMiddle,
                memberJoinInfoObj.phoneLast,
            ),
        () => memberJoinValidator.validateCertYn(memberJoinInfoObj.certYn),
        () => memberJoinValidator.validateDuplicateAccount(memberJoinInfoObj.accountCertYn),
    ];

    for (const validation of validations) {
        if (!validation()) {
            return false;
        }
    }
    return true;
}

const main = {
    /**
     * Initializing member objects
     */
    init() {
        const _this = this;
        $('#mainModalContainer').on('click', '#btn-join', function () {
            if (!confirm(messages.CONFIRM_MEMBER_JOIN.msg)) {
                return;
            }

            if (_this.validate()) {
                showMessage(messages.PROCEED_MEMBER_JOIN.msg);
                _this.join();
            }
        });
    },

    /**
     * Validate member data compatibility before signing up
     * @returns {boolean}
     */
    validate() {
        initializedMemberJoinInfo();
        return validateMemberJoinInfo();
    },

    /**
     * Proceed with signup
     * @returns {Promise<void>}
     */
    async join() {
        const requestDataObj = {
            baseUrl: URL_MEMBER_JOIN,
            method: HTTP_METHODS.POST,
            headers: {
                [HEADER_KEY.CONTENT_TYPE]: CONTENT_TYPE.JSON,
            },
            requestBody: {
                userName: memberJoinInfoObj.userName.value,
                account: memberJoinInfoObj.account.value,
                password: memberJoinInfoObj.password1.value,
                email: memberJoinInfoObj.email.value,
                phonePrefix: memberJoinInfoObj.phonePrefix.value,
                phoneMiddle: memberJoinInfoObj.phoneMiddle.value,
                phoneLast: memberJoinInfoObj.phoneLast.value,
                certYn: memberJoinInfoObj.certYn.value,
                accountCertYn: memberJoinInfoObj.accountCertYn.value,
                birthDate: memberJoinInfoObj.birthDate.value,
                gender: memberJoinInfoObj.gender.value,
                //role: memberJoinInfoObj.role.value, TODO: 추가 필요
            },
        };

        let requestFetchObj = memberRequestDataBuilder.createMemberRequestBodyInfoBuilder(requestDataObj);

        try {
            const response = await commonFetchTemplate.sendFetchRequest(requestFetchObj);
            const result = await response.json();
            if (result.statusCode === messages.STATUS.OK && result.success) {
                showMessage(result.message);
                redirectURL('/'); // move to main page
            } else {
                showMessage(result.message);
            }
        } catch (error) {
            console.error(`requestURL => ${requestFetchObj.baseUrl} error => ${error}`);
            commonFetchTemplate.handleResponseError(error, requestFetchObj);
        }
    },
};
