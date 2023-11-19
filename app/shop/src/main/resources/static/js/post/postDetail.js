const URL_DELETE_BY_POST_ID = '/api/v1/post/{id}';

function goListPage() {
    const queryString = new URLSearchParams(location.search);
    if (queryString.toString()) {
        location.href =
            '/post' + '?' + new URLSearchParams(location.search).toString();
    } else {
        location.href = '/post';
    }
}

function deletePosts(postId) {
    const confirmFlag = confirm('게시글을 삭제하시겠습니까?');
    if (!confirmFlag) {
        alert('게시글 삭제가 중지 되었습니다.');
        return;
    }

    if (isEmpty(postId.toString())) {
        //alert(messages.NOT_FOUND_POSTID);
        return;
    }

    const request = queryBuilder
        .createQueryBuilder()
        .url(URL_DELETE_BY_POST_ID)
        .method('DELETE')
        .contentType('application/json')
        .pathVariable({
            id: postId,
        })
        .build();
    console.log(`request => ${request}`);

    const response = commonFetchTemplate
        .sendFetchRequest(request)
        .then(response => response.json())
        .then(result => {
            if (result.code === messages.SUCCESS_DELETE_POST.code) {
                showMessage(result.message);
                redirectURL('/post');
            } else if (result.code === messages.FAIL_DELETE_POST.code) {
                showMessage(result.message);
            } else {
                showMessage(result.message);
            }
        })
        .catch(error => handleResponseError(error, request));
}

function updatePosts() {
    const confirmFlag = confirm(`게시글을 수정 하시겠습니까?`);
    if (!confirmFlag) {
        alert('게시글 수정이 중지 되었습니다.');
        return;
    }

    let postId = document.getElementById('postId');
    let title = document.getElementById('title');
    let content = document.getElementById('content');
    let fixedYn = document.getElementById('fixedYn');

    if (
        isEmpty(postId.value) ||
        isNaN(postId.value) ||
        isNotNumericRegExp(postId.value)
    ) {
        showMessage(messages.EMPTY_POST_ID.message);
        return;
    } else if (isEmpty(title.value) || title.value.length > 20) {
        showMessage(messages.EMPTY_POST_TITLE.message);
        return;
    } else if (isEmpty(content.value) || content.value.length > 1000) {
        showMessage(messages.EMPTY_POST_CONTENT.message);
        return;
    } else if (isEmpty(fixedYn.value)) {
        showMessage(messages.EMPTY_POST_FIXED_YN.message);
        return;
    } else {
        console.log(`ok`);
        // Todo: 유효성 검증 후 서버 요청
    }
}
