const commonFetchTemplate = {
    validHttpMethod(method) {
        if (!method || typeof method !== 'string' || method.trim() === '') {
            console.error(`Invalid HTTP Method Type`);
            throw new Error('Error Not allowed HTTP Method Type');
        }
    },

    validRequestBody(requestBody) {
        return requestBody && Object.keys(requestBody).length > 0;
    },

    convertToFormData(requestBody) {
        const formData = new FormData();
        for (const key in requestBody) {
            if (Object.prototype.hasOwnProperty.call(requestBody, key)) {
                formData.append(key, requestBody[key]);
            }
        }
        return formData;
    },

    addRequestBodyFromContentType(headers, requestBody) {
        switch (headers[HEADER_KEY.CONTENT_TYPE]) {
            case CONTENT_TYPE.JSON:
                return JSON.stringify(requestBody);
            case CONTENT_TYPE.FORM:
                return new URLSearchParams(requestBody).toString();
            case CONTENT_TYPE.MULTIPART:
                return requestBody instanceof FormData ? requestBody : this.convertToFormData(requestBody);
            case CONTENT_TYPE.TEXT:
                return requestBody.toString();
            default:
                return requestBody;
        }
    },

    createHttpRequestOptions(method, headers, requestBody) {
        this.validHttpMethod(method); // validate HTTP method
        const options = {
            method,
            headers,
        };

        // If request body is not null or empty, create request body options
        if (this.validRequestBody(requestBody)) {
            options.body = this.addRequestBodyFromContentType(headers, requestBody);
        }
        return options;
    },

    replacePathVariables(url, pathVariables) {
        if (pathVariables && typeof pathVariables === 'object') {
            for (let key in pathVariables) {
                if (pathVariables.hasOwnProperty(key)) {
                    url = url.replace(`{${key}`, encodeURIComponent(pathVariables[key]));
                }
            }
        }
        return url;
    },

    validQueryParams(queryParams) {
        return queryParams && Object.keys(queryParams).length > 0;
    },

    addQueryParams(url, queryParams) {
        const queryString = new URLSearchParams(queryParams).toString();
        return `${url}?${queryString}`;
    },

    createHttpRequestURL(baseUrl = '', pathVariable = {}, queryParams = {}) {
        let url = this.replacePathVariables(baseUrl, pathVariable);

        // validate query params is not null and setting query string
        if (this.validQueryParams(queryParams)) {
            url = this.addQueryParams(url, queryParams);
        }
        return url;
    },

    handleResponseError(error) {
        console.error(`Error Fetch request, error => ${error}`);
        showMessage(messages.COMMON_SERVER_ERROR_MSG.msg);
    },

    // Handling error when communicating with the API server
    async sendFetchRequest({
        baseUrl = '',
        method = '',
        headers = {},
        pathVariables = {},
        requestBody = {},
        queryString = {},
    }) {
        if (isEmpty(baseUrl) || isEmpty(method)) {
            console.error(`Invalid baseUrl and method`);
            return;
        }

        let requestURL = this.createHttpRequestURL(baseUrl, pathVariables, queryString);
        let options = this.createHttpRequestOptions(method, headers, requestBody);
        console.log(
            `[sendFetchRequest.requestURL] => ${JSON.stringify(requestURL)}, options => ${JSON.stringify(options)}`,
        );

        try {
            const response = await fetch(requestURL, options);
            console.log(`response => ${JSON.stringify(response)}`);
            return response;
        } catch (error) {
            this.handleResponseError(error, { baseUrl, method });
            throw error;
        }
    },
};
