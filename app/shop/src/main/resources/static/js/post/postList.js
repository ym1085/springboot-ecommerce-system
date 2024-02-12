/**
 * @since           :       2023-11-18
 * @author          :       youngmin
 * @version         :       1.0.0
 * @description     :       내용
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-11-18       youngmin           최초 생성
 **/

/* page onload */
$(function () {
    setURLQueryString();
});

function setURLQueryString() {
    if (!location.search) {
        return false;
    }

    const searchForm = document.getElementById('searchForm');
    new URLSearchParams(location.search).forEach((value, key) => {
        console.debug(`key => ${key}`);
        if (searchForm[key]) {
            searchForm[key].value = value;
            console.debug(`searchForm[key].value => ${searchForm[key].value}`);
        }
    });
}

/*function movePageWithPageNo(pageNo) {
    const searchForm = document.getElementById('searchForm');
    const queryString = {
        pageNo: pageNo ? pageNo : 1,
        recordSizePerPage: 10,
        pageSize: 10,
        searchType: searchForm.searchType.value,
        searchKeyword: searchForm.searchKeyword.value,
    };
    location.href = location.pathname + '?' + new URLSearchParams(queryString).toString();
}*/

function showDetailPost(postId) {
    const queryString = new URLSearchParams(location.search);
    if (queryString.toString()) {
        location.href = '/post/' + postId + '?' + queryString.toString(); // 검색 > 상세 게시글 클릭 > 서버 전송
    } else {
        location.href = '/post/' + postId;
    }
}
