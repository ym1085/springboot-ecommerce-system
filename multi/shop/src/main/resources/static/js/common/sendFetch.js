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
        // common으로 만드려면 손 봐야할 것 같음
        let options = {
            method: dataObj.method,
            headers: {
                "Content-Type": "application/json",
            },
        };

        let url = dataObj.url;
        if (dataObj.method === "GET" || dataObj.method === "DELETE") {
            const queryStr = Object.keys(dataObj.data)
                .map(key => `${encodeURIComponent(key)}=${encodeURIComponent(dataObj.data[key])}`)
                .join('&');
            url += `?${queryStr}`;
        } else if (dataObj.method === "POST" || dataObj.method === "PUT") {
            options.body = JSON.stringify(dataObj.data);
        }
        console.log(`before request fetch, url => ${url}, options => ${JSON.stringify(options)}`);

        return await fetch(url, options);
    } catch (error) {
        console.error(`Error => ${error}, URL => ${dataObj.url}, METHOD => ${dataObj.method}`);
    }
}