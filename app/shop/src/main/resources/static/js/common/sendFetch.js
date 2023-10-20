/**
 * @since           :       2023-08-14
 * @author          :       youngmin
 * @version         :       1.0.0
 * @description     :       fetch 요청 공통 함수
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-08-14       youngmin           최초 생성
 **/
async function sendFetchRequest(dataObj) {
    try {
        let options = {
            method: dataObj.method,
            headers: {
                'Content-Type': 'application/json', // Todo: header opt도 받아서 처리
            },
        };

        let url = dataObj.url;
        if (dataObj.method === 'GET' || dataObj.method === 'DELETE') {
            if (dataObj.pathVariable) {
                for (let key in dataObj.pathVariable) {
                    url = url.replace(`{${key}}`, encodeURIComponent(dataObj.pathVariable[key]));
                }
            } else {
                let queryStr = Object.keys(dataObj.data)
                    .map(key => `${encodeURIComponent(key)}=${encodeURIComponent(dataObj.data[key])}`)
                    .join('&');
                url += `?${queryStr}`;
            }
        } else if (dataObj.method === 'POST' || dataObj.method === 'PUT') {
            options.body = JSON.stringify(dataObj.data);
        }

        return await fetch(url, options);
    } catch (error) {
        console.error(`error => ${error}, URL => ${dataObj.url}, METHOD => ${dataObj.method}`);
    }
}
