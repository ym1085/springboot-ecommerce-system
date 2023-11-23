const URL_BY_POST_ID = '/api/v1/post/{id}';
const URL_SAVE_COMMENT = '/api/v1/post/{postId}/comments';

let postInfo = {
    postId: document.getElementById('postId'),
    title: document.getElementById('title'),
    content: document.getElementById('content'),
    fixedYn: document.getElementById('fixedYn'),
};

function goListPage() {
    const queryString = new URLSearchParams(location.search);
    if (queryString.toString()) {
        location.href = '/post' + '?' + new URLSearchParams(location.search).toString();
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
        .url(URL_BY_POST_ID)
        .method('DELETE')
        .contentType('application/json')
        .pathVariable({
            id: postId,
        })
        .build();

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
                showMessage(messages.COMMON_SERVER_ERROR_MSG.message);
            }
        })
        .catch(error => handleResponseError(error, request));
}

const isValidPostId = function (postId) {
    return !isEmpty(postId) && !isNaN(postId) && !isNotNumericRegExp(postId);
};

const isValidTitle = function (title) {
    return !isEmpty(title) && title.length <= 20;
};

const isValidContent = function (content) {
    return !isEmpty(content) && content.length <= 1000;
};

const validatePostInfo = function () {
    if (!isValidPostId(postInfo.postId.value)) {
        showMessage(messages.EMPTY_POST_ID.message);
        return false;
    }

    if (!isValidTitle(postInfo.title.value)) {
        showMessage(messages.EMPTY_POST_TITLE.message);
        return false;
    }

    if (!isValidContent(postInfo.content.value)) {
        showMessage(messages.EMPTY_POST_CONTENT.message);
        return false;
    }

    if (isEmpty(postInfo.fixedYn.value)) {
        showMessage(messages.EMPTY_POST_FIXED_YN.message);
        return false;
    }

    return true;
};

function updatePosts(postId) {
    const confirmFlag = confirm(`게시글을 수정 하시겠습니까?`);
    if (!confirmFlag) {
        alert('게시글 수정이 중지 되었습니다.');
        return;
    }
    alert(`postId => ${postId}`);

    if (validatePostInfo()) {
        const request = queryBuilder
            .createQueryBuilder()
            .url(URL_BY_POST_ID)
            .method('PUT')
            .contentType('application/json')
            .pathVariable({
                id: postId,
            })
            .requestBody({
                title: postInfo.title.value,
                content: postInfo.content.value,
                fixedYn: postInfo.fixedYn.value === 'on' ? 'Y' : 'N',
            })
            .build();
        alert(`postId => ${postId}`);

        const response = commonFetchTemplate
            .sendFetchRequest(request)
            .then(response => response.json())
            .then(result => {
                if (result.code === messages.SUCCESS_UPDATE_POST.code) {
                    showMessage(result.message);
                    redirectURL('/post');
                } else if (result.code === messages.FAIL_UPDATE_POST.code) {
                    showMessage(result.message);
                } else {
                    showMessage(messages.COMMON_SERVER_ERROR_MSG.message);
                }
            })
            .catch(error => handleResponseError(error, request));
    } else {
        showMessage(messages.COMMON_FRONT_ERROR_MSG.message);
    }
}

// 굳이 재사용 안할 것 같기는 하지만...
const renderParentComment = (comment, parentElement) => {
    const parent = document.createElement('p');
    parent.textContent = comment.content;
    parent.setAttribute('id', 'parent');
    parent.setAttribute('data-comment-id', comment.commentId);
    parentElement.appendChild(parent);
};

const renderChildComment = (comment, parentElement) => {
    const child = document.createElement('div');
    child.setAttribute('id', 'child');
    child.setAttribute('data-parent-id', comment.parentId);

    const commentListElement = document.createElement('ul');
    const commentListItem = document.createElement('li');
    commentListItem.textContent = comment.content;
    commentListElement.appendChild(commentListItem);

    child.appendChild(commentListElement);
    parentElement.appendChild(child);
};

const renderCommentFirstChild = comments => {
    const commentContent = document.getElementById('commentContent');
    const commentParent = document.getElementById('comment_parent');
    commentParent.innerHTML = '';

    comments.forEach(comment => {
        const commentSecondChild = document.createElement('div');
        commentSecondChild.setAttribute('id', 'comment_second_child');

        if (!comment.path || !comment.path.includes('-')) {
            renderParentComment(comment, commentSecondChild); // 댓글
        } else {
            renderChildComment(comment, commentSecondChild); // 대댓글
        }
        commentParent.appendChild(commentSecondChild); // 댓글 랜더링
        commentContent.value = '';
    });
};

function saveComment(postId) {
    const commentContent = document.getElementById('commentContent');
    if (isEmpty(commentContent.value)) {
        showMessage(messages.EMPTY_COMMENT_CONTENT.message);
        focus(commentContent);
        return;
    } else if (commentContent.value.length > 300) {
        showMessage(messages.OVER_LENGTH_COMMENT.message);
        focus(commentContent);
        return;
    }

    if (isEmpty(postId)) {
        showMessage(messages.EMPTY_POST_ID.message);
        return;
    }

    const request = queryBuilder
        .createQueryBuilder()
        .url(URL_SAVE_COMMENT)
        .method('POST')
        .contentType('application/json')
        .pathVariable({
            postId: postId,
        })
        .requestBody({
            content: commentContent.value,
        })
        .build();

    const response = commonFetchTemplate
        .sendFetchRequest(request)
        .then(response => response.json())
        .then(result => {
            if (result.code === messages.SUCCESS_SAVE_COMMENT.code) {
                showMessage(messages.SUCCESS_SAVE_COMMENT.message);
                renderCommentFirstChild(result.result);
            } else if (result.code === messages.FAIL_SAVE_COMMENT.code) {
                showMessage(messages.FAIL_SAVE_COMMENT.message);
            } else if (result.code === messages.EMPTY_POST_ID.code) {
                showMessage(messages.EMPTY_POST_ID.message);
            } else {
                showMessage(messages.COMMON_SERVER_ERROR_MSG.message);
            }
        })
        .catch(error => handleResponseError(error, request));
}
