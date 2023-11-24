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

const renderComment = (comment, parentElement) => {
    const commentListArea = document.createElement('commentListArea');
    // 댓글 영역 <div></div>
    const commentInfo = document.createElement('div');
    commentInfo.setAttribute('id', 'commentInfo');

    // 댓글 내용
    const commentSpan = document.createElement('span');
    commentSpan.textContent = comment.content;
    commentSpan.setAttribute('style', 'margin-right: 10px;');
    commentSpan.setAttribute('id', 'comment');
    commentSpan.setAttribute('data-comment-id', comment.commentId);

    // 수정 버튼
    const editButton = document.createElement('button');
    editButton.textContent = '수정';
    editButton.setAttribute('style', 'margin-right: 10px;');
    editButton.setAttribute('type', 'button');
    editButton.setAttribute('onclick', `showEditBox(${comment.commentId})`);

    // 삭제 버튼
    const deleteButton = document.createElement('button');
    deleteButton.textContent = '삭제';
    deleteButton.setAttribute('style', 'margin-right: 10px;');
    deleteButton.setAttribute('type', 'button');
    deleteButton.setAttribute('onclick', `deleteComment(${comment.commentId})`);

    // 답글달기 버튼
    const replyButton = document.createElement('button');
    replyButton.textContent = '답글달기';
    replyButton.setAttribute('style', 'margin-right: 10px;');
    replyButton.setAttribute('type', 'button');
    replyButton.setAttribute('onclick', `replyToComment(${comment.commentId})`);

    // 댓글 영역에 댓글 내용 + 버튼 추가
    commentInfo.appendChild(commentSpan);
    commentInfo.appendChild(editButton);
    commentInfo.appendChild(deleteButton);
    commentInfo.appendChild(replyButton);

    // 댓글 랜더링
    commentListArea.appendChild(commentInfo);
    parentElement.appendChild(commentListArea);
};

const renderNestedComment = (comment, parentElement) => {
    const commentListArea = document.createElement('commentListArea');
    // 대댓글 영역 <div></div>
    const nestedComment = document.createElement('div');
    nestedComment.setAttribute('id', 'nestedComment');
    nestedComment.setAttribute('data-parent-id', comment.parentId);

    // 대댓글을 계층 구조 방식으로 그리기 위한 태그 영역
    const commentList = document.createElement('ul');
    const commentItem = document.createElement('li');

    // 대댓글 내용
    const commentSpan = document.createElement('span');
    commentSpan.textContent = comment.content;
    commentSpan.setAttribute('style', 'margin-right: 10px;');
    commentSpan.style.marginRight = '10px';

    // 수정 버튼
    const editButton = document.createElement('button');
    editButton.textContent = '수정';
    editButton.setAttribute('style', 'margin-right: 10px;');
    editButton.setAttribute('type', 'button');
    editButton.setAttribute('onclick', `editComment(${comment.commentId})`);

    // 삭제 버튼
    const deleteButton = document.createElement('button');
    deleteButton.textContent = '삭제';
    deleteButton.setAttribute('style', 'margin-right: 10px;');
    deleteButton.setAttribute('type', 'button');
    deleteButton.setAttribute('onclick', `deleteComment(${comment.commentId})`);

    // 대댓글 영역에 수정 + 삭제 버튼 추가
    commentItem.appendChild(commentSpan);
    commentItem.appendChild(editButton);
    commentItem.appendChild(deleteButton);

    commentList.appendChild(commentItem);
    nestedComment.appendChild(commentList);

    // 대댓글 랜더링
    commentListArea.appendChild(nestedComment);
    parentElement.appendChild(commentListArea);
};

const renderCommentFirstChild = comments => {
    const commentList = document.getElementById('commentList');
    const commentContent = document.getElementById('commentContent');

    // 모든 자식 노드 삭제 : https://hianna.tistory.com/722
    commentList.replaceChildren();

    comments.forEach(comment => {
        if (!comment.path || !comment.path.includes('-')) {
            renderComment(comment, commentList);
        } else {
            renderNestedComment(comment, commentList);
        }
    });

    // 입력 필드 초기화
    if (commentContent) {
        commentContent.value = '';
    }
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
            } else if (result.code === messages.FAIL_SAVE_COMMENT.message) {
                showMessage(messages.FAIL_SAVE_COMMENT.message);
            } else if (result.code === messages.EMPTY_POST_ID.message) {
                showMessage(messages.EMPTY_POST_ID.message);
            } else {
                showMessage(messages.COMMON_SERVER_ERROR_MSG.message);
            }
        })
        .catch(error => handleResponseError(error, request));
}
