const memberRequestDataBuilder = {
	createMemberRequestInfoBuilder(data) {
		const requestBody = {};

		if (data.requestBody.username) requestBody.username = data.username;
		if (data.requestBody.account) requestBody.account = data.account;
		if (data.requestBody.password1) requestBody.password = data.password1;
		if (data.requestBody.email) requestBody.email = data.email;
		if (
			data.requestBody.phonePrefix &&
			data.requestBody.phoneMiddle &&
			data.requestBody.phoneLast
		) {
			requestBody.phoneNumber = `${data.phonePrefix}-${data.phoneMiddle}-${data.phoneLast}`;
		}
		if (data.requestBody.certYn) requestBody.certYn = data.certYn;
		if (data.requestBody.accountCertYn) {
			requestBody.accountCertYn = data.accountCertYn;
		}
		if (data.requestBody.gender) requestBody.gender = data.gender;
		if (data.requestBody.birthDate) requestBody.birthDate = data.birthDate;

		return {
			baseUrl: data.baseUrl,
			method: data.method,
			contentType: data.contentType,
			requestBody: requestBody,
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

		const requestMemberSendAuthEmailObj = {
			baseUrl: URL_VERIFY_REQUEST,
			method: HTTP_METHODS.POST,
			contentType: CONTENT_TYPE.JSON,
			requestBody: {
				email: certEmail.value,
			},
		};

		// create request data
		const request = memberRequestDataBuilder.createMemberRequestInfoBuilder(
			requestMemberSendAuthEmailObj,
		);

		try {
			const response = await commonFetchTemplate.sendFetchRequest(request);
			const result = await response.json();
			if (result.code === messages.STATUS.OK) {
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
			console.error(`requestURL => ${request.baseUrl} error => ${error}`);
			commonFetchTemplate.handleResponseError(error, request);
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
		document
			.getElementById('sendVerification')
			.removeEventListener('click', this.sendAuthEmail);
	},

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

		const requestMemberVerifyEmailAuthCodeObj = {
			baseUrl: URL_VERIFY_EMAIL,
			method: HTTP_METHODS.GET,
			queryString: {
				email: certEmail.value,
				code: verificationCode.value,
			},
		};

		// create request data
		const request = memberRequestDataBuilder.createMemberRequestInfoBuilder(
			requestMemberVerifyEmailAuthCodeObj,
		);

		try {
			const response = await commonFetchTemplate.sendFetchRequest(request);
			const result = await response.json();
			if (result.code === messages.STATUS.OK) {
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

		const requestMemberDuplicateAccountObj = {
			baseUrl: URL_MEMBER_EXISTS_ACCOUNT,
			method: HTTP_METHODS.POST,
			contentType: CONTENT_TYPE.JSON,
			requestBody: {
				account: account.value,
			},
		};

		// create request data
		const request = memberRequestDataBuilder.createMemberRequestInfoBuilder(
			requestMemberDuplicateAccountObj,
		);

		try {
			const response = await commonFetchTemplate.sendFetchRequest(request);
			const result = await response.json();
			if (result.code === messages.STATUS.OK) {
				showMessage(result.message);
				accountCertYn.value = 'Y';
				password1.focus();
			} else {
				showMessage(result.message);
				accountCertYn.value = 'N';
				account.focus();
			}
		} catch (error) {
			commonFetchTemplate.handleResponseError(error, request);
		}
	},
};
