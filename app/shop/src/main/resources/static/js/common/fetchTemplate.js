/**
 * @desc HTTP API 요청 시 사용되는 fetch 템플릿
 * @author ymkim
 */
const commonFetchTemplate = {

    createHttpRequestOptions(method, contentType, requestBody) {
        // Throw Error
        if (!method || typeof method !== 'string' || method.trim() === '') {
            throw new Error('Error Not allowed HTTP Method Type');
        }

        // Header, Method type 셋팅
        const headers = contentType ? {'Content-Type': contentType} : {};
        const options = {
            method,
            headers
        }

        // Request Body 타입이 FormData | JSON 여부에 따라 셋팅
        if (requestBody && Object.keys(requestBody).length > 0) {
            options.body = requestBody instanceof FormData ? requestBody : JSON.stringify(requestBody);
        }

        return options;
    },

    constructURL(baseUrl = '', pathVariables = {}, queryParams = {}) {
        let url = baseUrl;
        // -> /api/v1/{id} -> /api/v1/123 변경
        if (pathVariables && typeof pathVariables === 'object') {
            for (let key in pathVariables) {
                if (pathVariables.hasOwnProperty(key)) {
                    url = url.replace(`{${key}}`, encodeURIComponent(pathVariables[key]));
                }
            }
        }

        // -> queryString parameter 추가
        if (queryParams && Object.keys(queryParams).length > 0) {
            // { search: 'test', page: 2 } --> search=test&page=2
            const queryString = new URLSearchParams(queryParams).toString();
            url += `?${queryString}`;
        }

        return url;
    },

    // error handling
    handleResponseError(error, request) {
        console.error(`Error Fetch request, error => ${error}, URL => ${request.baseUrl}, method => ${request.method} `);
        showMessage(messages.COMMON_SERVER_ERROR_MSG.message);
    },

    async sendFetchRequest({ baseUrl, method, contentType, pathVariables = {}, requestBody = {}, queryString = {} }) {
        // 요청 URL 생성
        let requestURL = this.constructURL(baseUrl, pathVariables, queryString);

        // HTTP Option 설정
        let options = this.createHttpRequestOptions(method, contentType, requestBody);

        try {
            return await fetch(requestURL, options); // res -> return promise
        } catch (error) {
            this.handleResponseError(error, { baseUrl, method })
            throw error; // 오류 발생 시 다시 throw하여 호출 측에서 처리 가능하도록 함
        }
    }
}