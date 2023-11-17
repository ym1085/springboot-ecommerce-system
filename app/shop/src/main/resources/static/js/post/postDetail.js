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
function goListPage() {
    const queryString = new URLSearchParams(location.search);
    if (queryString.toString()) {
        location.href = '/post' + '?' + new URLSearchParams(location.search).toString();
    } else {
        location.href = '/post';
    }
}

function deletePosts(postId) {
    confirm('게시글을 삭제하시겠습니까?');
    if (isEmpty(postId.toString())) {
        //alert(messages.NOT_FOUND_POSTID);
        return;
    }

    let request = {
        url: '/api/v1/post/{id}',
        method: 'DELETE',
        contentType: 'application/json',
        pathVariable: {
            id: postId,
        },
    };

    commonFetchTemplate
        .sendFetchRequest(request)
        .then(response => {
            return response.json();
        })
        .then(data => {
            if (data.code === 1004) {
                alert(messages.SUCCESS_DELETE_POST);
                location.href = '/post';
            } else if (data.code === 2002) {
                alert(messages.FAIL_DELETE_POST);
            } else {
                alert(messages.COMMON_SERVER_ERROR_MSG);
            }
        });
}
