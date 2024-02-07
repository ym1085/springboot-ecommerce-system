function handleResponseError(error, request) {
    console.error(`[Fetch] API 통신 과정 중 에러 발생, [ERROR INFO] => ${error}, URL => ${request.baseUrl}, METHOD => ${request.method} `);
    showMessage(messages.COMMON_SERVER_ERROR_MSG.message);
}

const queryBuilder = {
    createQueryBuilder() {
        const request = {
            baseUrl: '',
            method: '',
            contentType: '',
            pathVariable: {},
            requestBody: {},
            queryString: {},
        };

        return {
            baseUrl(baseUrl) {
                request.baseUrl = baseUrl;
                return this;
            },
            method(method) {
                request.method = method;
                return this;
            },
            contentType(contentType) {
                request.contentType = contentType;
                return this;
            },
            pathVariable(pathVariable) {
                request.pathVariable = pathVariable;
                return this;
            },
            requestBody(requestBody) {
                request.requestBody = requestBody;
                return this;
            },
            queryString(queryString) {
                request.queryString = queryString;
                return this;
            },
            build() {
                return request;
            },
        };
    },
};

// 공통 API Fetch 함수
const fetchTemplate = {
    createHttpRequestOptions(request) {
        // method => required : true
        if (typeof request.method !== 'string' || request.method.trim() === '') {
            throw new Error('올바른 요청 HTTP Method가 아닙니다.');
        }

        // headers => required : false
        // headers의 경우 추후 추가 될 수 있음, 그 때 아래 로직 변경
        const headers = request.contentType ? { 'Content-Type': request.contentType } : {};
        const options = {
            method: request.method,
            headers: headers,
        };

        // Check if requestBody is not null/undefined and not an empty object
        if (request.requestBody && !(Object.keys(request.requestBody).length === 0 && request.requestBody.constructor === Object)) {
            if (request.requestBody instanceof FormData) {
                options.body = request.requestBody;
            } else if (request.requestBody instanceof Object) {
                options.body = JSON.stringify(request.requestBody);
            } else {
                throw new Error('올바른 요청 Body가 아닙니다.');
            }
        }
        return options;
    },

    constructURL(baseUrl = '', pathVariables = {}, queryParams = {}) {
        let url = baseUrl;

        // Ensure pathVariables is an object and not null
        if (pathVariables && typeof pathVariables === 'object' && Object.keys(pathVariables).length > 0) {
            for (let key in pathVariables) {
                if (pathVariables.hasOwnProperty(key)) {
                    url = url.replace(`{${key}}`, encodeURIComponent(pathVariables[key]));
                }
            }
        }

        // Ensure queryParams is an object and not null
        if (queryParams && typeof queryParams === 'object' && Object.keys(queryParams).length > 0) {
            const queryString = Object.entries(queryParams)
                .map(([key, value]) => `${encodeURIComponent(key)}=${encodeURIComponent(value)}`)
                .join('&');
            url += `?${queryString}`;
        }

        return url;
    },

    async sendFetchRequest(request) {
        const url = this.constructURL(request.baseUrl, request.pathVariable, request.queryString);
        const options = this.createHttpRequestOptions(request);
        try {
            return await fetch(url, options);
        } catch (error) {
            console.error(`${error.message}`);
        }
    },
};
