const requestParamMethod = ['GET', 'DELETE'];
const requestBodyMethod = ['POST', 'PUT', 'PATCH'];
const requestHttpFieldType = ['pathVariable', 'queryString', 'requestBody']; // fetch 요청 시 { 키워드 : 데이터 }

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
            // pathVariable 로 들어온 경우 /api/v1/{id} -> /api/v1/54 형태로 변경해서 서버에 요청
            URL = URL.replace(`{${key}}`, encodeURIComponent(request.pathVariable[key]));
        }
    },
    setRequestURLByExtractQueryString: function (request) {
        URL += `?${Object.keys(request.data)
            .map(key => `${encodeURIComponent(key)}=${encodeURIComponent(request.data[key])}`)
            .join('&')}`;
    },
    setRequestParam: function (request) {
        console.info(`request => ${JSON.stringify(request)}`);
        try {
            // https://negabaro.github.io/archive/js-exists_keys_in_object
            if (isNotEmptyObject(request.pathVariable) && request.pathVariable) {
                this.setRequestURLByExtractedPathVariables(request);
            } else if (isNotEmptyObject(request.queryString) && request.queryString) {
                this.setRequestURLByExtractQueryString(request);
            }
        } catch (error) {
            console.error(`setRequestParam => error => ${error}, URL => ${request.url}, METHOD => ${request.method} `);
        }
    },
    setRequestBody: function (request) {
        httpReqOptions.body =
            isNotEmptyObject(request.requestBody) && isNotEmpty(request.requestBody) && request.requestBody ? JSON.stringify(request.requestBody) : '';
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
        requestParamMethod.includes(request.method) ? this.setRequestParam(request) : this.setRequestBody(request);
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
