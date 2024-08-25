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

function validateMemberJoinInfo() {
    const validations = [
        () => memberJoinValidator.validateUserName(memberJoinInfoObj.username),
        () => memberJoinValidator.validateUserNameRegExp(memberJoinInfoObj.username),
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
                console.log(`success validate`);
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
        const requestMemberJoinObj = {
            baseUrl: URL_MEMBER_JOIN,
            method: HTTP_METHODS.POST,
            contentType: CONTENT_TYPE.JSON,
            requestBody: {
                username: memberJoinInfoObj.username.value,
                account: memberJoinInfoObj.account.value,
                password1: memberJoinInfoObj.password1.value,
                email: memberJoinInfoObj.email.value,
                phonePrefix: memberJoinInfoObj.phonePrefix.value,
                phoneMiddle: memberJoinInfoObj.phoneMiddle.value,
                phoneLast: memberJoinInfoObj.phoneLast.value,
                certYn: memberJoinInfoObj.certYn.value,
                accountCertYn: memberJoinInfoObj.accountCertYn.value,
                gender: memberJoinInfoObj.gender.value,
                birthDate: memberJoinInfoObj.birthDate.value,
                // picture: memberJoinInfo.picture.value
            },
        };

        // create request data
        let request = memberRequestDataBuilder.createMemberRequestBodyInfoBuilder(requestMemberJoinObj);

        try {
            const response = await commonFetchTemplate.sendFetchRequest(request);
            const result = await response.json();
            if (result.code === messages.STATUS.OK) {
                showMessage(result.message);
                redirectURL('/'); // move to main page
            }
        } catch (error) {
            console.error(`requestURL => ${request.baseUrl} error => ${error}`);
            commonFetchTemplate.handleResponseError(error, request);
        }
    },
};
