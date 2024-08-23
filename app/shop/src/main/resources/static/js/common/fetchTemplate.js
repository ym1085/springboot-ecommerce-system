const commonFetchTemplate = {
	createHttpRequestOptions(method, contentType, requestBody) {
		if (!method || typeof method !== 'string' || method.trim() === '') {
			throw new Error('Error Not allowed HTTP Method Type');
		}

		// Setting headers and method opt
		const headers = contentType ? { 'Content-Type': contentType } : {};
		const options = {
			method,
			headers,
		};

		// Setting requestBody when requestBody is not null
		if (requestBody && Object.keys(requestBody).length > 0) {
			options.body =
				requestBody instanceof FormData ? requestBody : JSON.stringify(requestBody);
		}
		return options;
	},

	createRequestURL(baseUrl = '', pathVariables = {}, queryParams = {}) {
		let url = baseUrl;
		// Replace pathVariable URL -> /api/v1/{id} -> /api/v1/123
		if (pathVariables && typeof pathVariables === 'object') {
			for (let key in pathVariables) {
				if (pathVariables.hasOwnProperty(key)) {
					url = url.replace(`{${key}}`, encodeURIComponent(pathVariables[key]));
				}
			}
		}

		// Add queryString parameter
		if (queryParams && Object.keys(queryParams).length > 0) {
			// { search: 'test', page: 2 } --> search=test&page=2
			const queryString = new URLSearchParams(queryParams).toString();
			url += `?${queryString}`;
		}

		return url;
	},

	// Handling error when communicating with the API server
	handleResponseError(error, request) {
		console.error(
			`Error Fetch request, error => ${error}, URL => ${request.baseUrl}, method => ${request.method} `,
		);
		showMessage(messages.COMMON_SERVER_ERROR_MSG.msg);
	},

	async sendFetchRequest({
		baseUrl,
		method,
		contentType,
		pathVariables = {},
		requestBody = {},
		queryString = {},
	}) {
		let requestURL = this.createRequestURL(baseUrl, pathVariables, queryString);
		let requestOptions = this.createHttpRequestOptions(
			method,
			contentType,
			requestBody,
		);

		try {
			return await fetch(requestURL, requestOptions);
		} catch (error) {
			this.handleResponseError(error, { baseUrl, method });
			throw error;
		}
	},
};
