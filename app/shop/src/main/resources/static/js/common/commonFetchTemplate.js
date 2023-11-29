const requestParamMethod = ['GET', 'DELETE', 'POST', 'PUT', 'PATCH'];
const requestHttpFieldType = ['pathVariable', 'queryString', 'requestBody']; // fetch 요청 시 { 키워드 : 데이터 }

function handleResponseError(error, request) {
    console.error(`[fetch Request] error => ${error}, URL => ${request.url}, METHOD => ${request.method} `);
    showMessage(messages.COMMON_SERVER_ERROR_MSG);
}

const queryBuilder = {
    createQueryBuilder: function () {
        const request = {
            url: '',
            method: '',
            contentType: '',
            pathVariable: {},
            requestBody: {},
            queryString: {},
        };

        const url = function (url) {
            request.url = url;
            return this;
        };

        const method = function (method) {
            request.method = method;
            return this;
        };

        const contentType = function (contentType) {
            request.contentType = contentType;
            return this;
        };

        const pathVariable = function (pathVariable) {
            request.pathVariable = pathVariable;
            return this;
        };

        const requestBody = function (requestBody) {
            request.requestBody = requestBody;
            return this;
        };

        const queryString = function (queryString) {
            request.queryString = queryString;
            return this;
        };

        const build = function () {
            return request;
        };

        return {
            url,
            method,
            contentType,
            pathVariable,
            requestBody,
            build,
            queryString,
        };
    },
};

let httpReqOptions = {};
let URL = '';
const commonFetchTemplate = {
    createHttpRequestHeader: function (request) {
        return {
            'Content-Type': request.contentType,
        };
    },
    createHttpRequestOptions: function (request) {
        return {
            method: request.method,
            headers: this.createHttpRequestHeader(request),
        };
    },
    setRequestURLByExtractedPathVariables: function (request) {
        for (let key in request.pathVariable) {
            // pathVariable 로 들어온 경우 /api/v1/{postId} -> /api/v1/54 형태로 변경해서 서버에 요청
            // Todo: 주의! pathVariable에 넣은 key값이 URL로 표현되기에 서버의 key 값이랑 맞춰줘야 한다
            URL = URL.replace(`{${key}}`, encodeURIComponent(request.pathVariable[key]));
        }
    },
    setRequestURLByExtractQueryString: function (request) {
        URL += `?${Object.keys(request.queryString)
            .map(key => `${encodeURIComponent(key)}=${encodeURIComponent(request.queryString[key])}`)
            .join('&')}`;
    },
    setRequestBody: function (request) {
        httpReqOptions.body =
            isNotEmptyObject(request.requestBody) && isNotEmpty(request.requestBody) && request.requestBody ? JSON.stringify(request.requestBody) : '';
    },
    setRequestData: function (request) {
        console.info(`request => ${JSON.stringify(request)}`);
        try {
            // https://negabaro.github.io/archive/js-exists_keys_in_object
            if (isNotEmptyObject(request.pathVariable) && request.pathVariable) {
                this.setRequestURLByExtractedPathVariables(request);
            }
            if (isNotEmptyObject(request.queryString) && request.queryString) {
                this.setRequestURLByExtractQueryString(request);
            }
            if (isNotEmptyObject(request.requestBody) && request.requestBody) {
                this.setRequestBody(request);
            }
        } catch (error) {
            console.error(`setRequestParam => error => ${error}, URL => ${request.url}, METHOD => ${request.method} `);
        }
    },
    isValidRequestData: function (request) {
        if (isEmptyObject(request.url) || isEmpty(request.url)) {
            return false;
        }
        if (isEmptyObject(request.method) || isEmpty(request.method)) {
            return false;
        }
        //https://developer.mozilla.org/ko/docs/Web/JavaScript/Reference/Global_Objects/Array/every
        if (!Object.keys(request).some(key => requestHttpFieldType.includes(key))) {
            return false;
        }
        return true;
    },
    setRequestURLByHttpMethod: function (request) {
        if (requestParamMethod.includes(request.method)) {
            this.setRequestData(request);
        } else {
            throw new Error('Not supported Method type');
        }
    },
    sendFetchRequest: async function (request) {
        if (!this.isValidRequestData(request)) {
            alert(`서버 요청 전 오류가 발생 하였습니다.`);
            return;
        }

        URL = request.url;
        httpReqOptions = this.createHttpRequestOptions(request);
        try {
            this.setRequestURLByHttpMethod(request);
            return await fetch(URL, httpReqOptions);
        } catch (error) {
            console.error(`sendFetchRequest => error => ${error}, URL => ${request.url}, METHOD => ${request.method}`);
        }
    },
};
