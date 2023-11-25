const URL_BY_POST_ID = '/api/v1/post/{id}';

document.addEventListener('DOMContentLoaded', function () {
    postInfo.init();
});

const postInfo = {
    postId: null,
    title: null,
    content: null,
    fixedYn: null,

    init: function () {
        this.postId = document.getElementById('postId');
        this.title = document.getElementById('title');
        this.content = document.getElementById('content');
        this.fixedYn = document.getElementById('fixedYn');
    },

    isValidPostId: function (postId) {
        return !isEmpty(postId) && !isNaN(postId) && !isNotNumericRegExp(postId);
    },

    isValidTitle: function (title) {
        return !isEmpty(title) && title.length <= 20;
    },

    isValidContent: function (content) {
        return !isEmpty(content) && content.length <= 1000;
    },

    validatePostInfo: function () {
        if (!this.isValidPostId(this.postId.value)) {
            showMessage(messages.EMPTY_POST_ID.message);
            return false;
        }
        if (!this.isValidTitle(this.title.value)) {
            showMessage(messages.EMPTY_POST_TITLE.message);
            return false;
        }
        if (!this.isValidContent(this.content.value)) {
            showMessage(messages.EMPTY_POST_CONTENT.message);
            return false;
        }
        if (isEmpty(this.fixedYn.value)) {
            showMessage(messages.EMPTY_POST_FIXED_YN.message);
            return false;
        }
        return true;
    },

    updatePosts: function (postId) {
        if (!confirm(`게시글을 수정 하시겠습니까?`)) {
            return;
        }

        if (this.validatePostInfo()) {
            const request = queryBuilder
                .createQueryBuilder()
                .url('/api/v1/post/{id}')
                .method('PUT')
                .contentType('application/json')
                .pathVariable({
                    id: postId,
                })
                .requestBody({
                    title: this.title.value,
                    content: this.content.value,
                    fixedYn: this.fixedYn.value === 'on' ? 'Y' : 'N',
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
    },

    goListPage: function () {
        const queryString = new URLSearchParams(location.search);
        if (queryString.toString()) {
            location.href = '/post' + '?' + new URLSearchParams(location.search).toString();
        } else {
            location.href = '/post';
        }
    },

    deletePosts: function (postId) {
        if (!confirm('게시글을 삭제하시겠습니까?')) {
            return;
        }

        const request = queryBuilder
            .createQueryBuilder()
            .url('/api/v1/post/{id}')
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
    },
};

const commentInfo = {
    renderComment: function (comment, parentElement) {
        // 댓글 수정 영역 <div></div>
        const commentEdit = document.createElement('div');
        commentEdit.setAttribute('id', 'commentEdit');
        commentEdit.style.display = 'none';

        // 입력 필드
        const inputFieldEdit = document.createElement('input');
        inputFieldEdit.setAttribute('type', 'text');
        inputFieldEdit.setAttribute('id', 'commentEditContent');
        inputFieldEdit.value = comment.content; // Thymeleaf의 th:value 대체

        // 수정 완료 버튼
        const editButtonEdit = document.createElement('button');
        editButtonEdit.textContent = '수정 완료';
        editButtonEdit.setAttribute('data-post-id', `${comment.postId}`);
        editButtonEdit.setAttribute('type', 'button');
        editButtonEdit.setAttribute('onclick', `commentInfo.updateComment(${comment.commentId})`);

        // 취소 버튼
        const cancelButtonEdit = document.createElement('button');
        cancelButtonEdit.textContent = '취소';
        cancelButtonEdit.setAttribute('type', 'button');
        cancelButtonEdit.setAttribute('onclick', `commentInfo.cancelCommentEdit(this, ${comment.commentId})`);

        // 답글달기 버튼
        const replyButtonEdit = document.createElement('button');
        replyButtonEdit.textContent = '답글달기';
        replyButtonEdit.setAttribute('type', 'button');
        replyButtonEdit.setAttribute('onclick', `commentInfo.showReplyToCommentEditBox(${comment.commentId})`);

        // 댓글 수정 영역에 요소들 추가
        commentEdit.appendChild(inputFieldEdit);
        commentEdit.appendChild(editButtonEdit);
        commentEdit.appendChild(cancelButtonEdit);
        commentEdit.appendChild(replyButtonEdit);

        // 댓글 영역 <div></div>
        const commentInfo = document.createElement('div');
        commentInfo.setAttribute('id', 'commentInfo');

        // 댓글 내용
        const commentSpanInfo = document.createElement('span');
        commentSpanInfo.textContent = comment.content;
        commentSpanInfo.setAttribute('style', 'margin-right: 10px;');
        commentSpanInfo.setAttribute('id', 'comment');
        commentSpanInfo.setAttribute('data-comment-id', comment.commentId);

        // 수정 버튼
        const editButtonInfo = document.createElement('button');
        editButtonInfo.textContent = '수정';
        editButtonInfo.setAttribute('style', 'margin-right: 10px;');
        editButtonInfo.setAttribute('type', 'button');
        editButtonInfo.setAttribute('onclick', `commentInfo.showCommentEditBox(this, ${comment.commentId})`);

        // 삭제 버튼
        const deleteButtonInfo = document.createElement('button');
        deleteButtonInfo.textContent = '삭제';
        deleteButtonInfo.setAttribute('style', 'margin-right: 10px;');
        deleteButtonInfo.setAttribute('type', 'button');
        deleteButtonInfo.setAttribute('onclick', `commentInfo.deleteComment(${comment.commentId})`);

        // 답글달기 버튼
        const replyButtonInfo = document.createElement('button');
        replyButtonInfo.textContent = '답글달기';
        replyButtonInfo.setAttribute('style', 'margin-right: 10px;');
        replyButtonInfo.setAttribute('type', 'button');
        replyButtonInfo.setAttribute('onclick', `commentInfo.showReplyToCommentEditBox(${comment.commentId})`);

        // 댓글 영역에 댓글 내용 + 버튼 추가
        commentInfo.appendChild(commentSpanInfo);
        commentInfo.appendChild(editButtonInfo);
        commentInfo.appendChild(deleteButtonInfo);
        commentInfo.appendChild(replyButtonInfo);

        // 댓글 랜더링
        parentElement.appendChild(commentEdit);
        parentElement.appendChild(commentInfo);
    },

    renderNestedComment: function (comment, parentElement) {
        // 대댓글 영역 <div></div>
        const nestedComment = document.createElement('div');
        nestedComment.setAttribute('id', 'nestedComment');
        nestedComment.setAttribute('data-parent-id', comment.parentId);

        // 대댓글을 계층 구조 방식으로 그리기 위한 태그 영역
        const nestedCommentSubInfo = document.createElement('ul');
        nestedCommentSubInfo.setAttribute('id', 'nestedCommentSubInfo');
        const nestedCommentSubInfoItem = document.createElement('li');

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
        editButton.setAttribute('onclick', `commentInfo.showNestedCommentEditBody(this, ${comment.commentId})`);

        // 삭제 버튼
        const deleteButton = document.createElement('button');
        deleteButton.textContent = '삭제';
        deleteButton.setAttribute('style', 'margin-right: 10px;');
        deleteButton.setAttribute('type', 'button');
        deleteButton.setAttribute('onclick', `commentInfo.deleteNestedComment(${comment.commentId})`);

        // 대댓글 영역에 수정 + 삭제 버튼 추가
        nestedCommentSubInfoItem.appendChild(commentSpan);
        nestedCommentSubInfoItem.appendChild(editButton);
        nestedCommentSubInfoItem.appendChild(deleteButton);

        nestedCommentSubInfo.appendChild(nestedCommentSubInfoItem);
        nestedComment.appendChild(nestedCommentSubInfo);

        // 대댓글 수정 영역 <ul></ul>
        const nestedCommentSubEdit = document.createElement('ul');
        nestedCommentSubEdit.setAttribute('id', 'nestedCommentSubEdit');
        nestedCommentSubEdit.style.display = 'none';

        const nestedCommentSubEditItem = document.createElement('li');

        // 수정 입력 필드
        const editInput = document.createElement('input');
        editInput.setAttribute('type', 'text');
        editInput.value = comment.content; // 기존 대댓글의 내용

        // 수정 완료 버튼
        const editButtonEdit = document.createElement('button');
        editButtonEdit.textContent = '수정 완료';
        editButtonEdit.setAttribute('data-post-id', `${comment.postId}`);
        editButtonEdit.setAttribute('type', 'button');
        editButtonEdit.setAttribute('onclick', `commentInfo.updateComment(this, ${comment.commentId})`);

        // 취소 버튼
        const cancelButtonEdit = document.createElement('button');
        cancelButtonEdit.textContent = '취소';
        cancelButtonEdit.setAttribute('type', 'button');
        cancelButtonEdit.setAttribute('onclick', `commentInfo.cancelNestedCommentEdit(this, ${comment.commentId})`);

        // 삭제 버튼
        const deleteButtonEdit = document.createElement('button');
        deleteButtonEdit.textContent = '삭제';
        deleteButtonEdit.setAttribute('type', 'button');
        deleteButtonEdit.setAttribute('onclick', `commentInfo.deleteNestedComment(${comment.commentId})`);

        // 대댓글 수정 영역에 요소들 추가
        nestedCommentSubEditItem.appendChild(editInput);
        nestedCommentSubEditItem.appendChild(editButtonEdit);
        nestedCommentSubEditItem.appendChild(cancelButtonEdit);
        nestedCommentSubEditItem.appendChild(deleteButtonEdit);

        nestedCommentSubEdit.appendChild(nestedCommentSubEditItem);
        nestedComment.appendChild(nestedCommentSubEdit);

        // 대댓글 랜더링
        parentElement.appendChild(nestedComment);
    },

    renderComments: function (comments) {
        const commentList = document.getElementById('commentList');
        const commentContent = document.getElementById('commentContent');

        // 모든 자식 노드 삭제 : https://hianna.tistory.com/722
        commentList.replaceChildren();

        comments.forEach(comment => {
            if (!comment.path || !comment.path.includes('-')) {
                this.renderComment(comment, commentList);
            } else {
                this.renderNestedComment(comment, commentList);
            }
        });

        // 입력 필드 초기화
        if (commentContent) {
            commentContent.value = '';
        }
    },

    saveComment: function (postId) {
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
            .url('/api/v1/post/{postId}/comments')
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
                    this.renderComments(result.result);
                } else if (result.code === messages.FAIL_SAVE_COMMENT.message) {
                    showMessage(messages.FAIL_SAVE_COMMENT.message);
                } else if (result.code === messages.EMPTY_POST_ID.message) {
                    showMessage(messages.EMPTY_POST_ID.message);
                } else {
                    showMessage(messages.COMMON_SERVER_ERROR_MSG.message);
                }
            })
            .catch(error => handleResponseError(error, request));
    },

    showCommentEditBox: function (element) {
        const commentInfo = element.closest('#commentInfo');
        commentInfo.style.display = 'none';
        commentInfo.previousElementSibling.style.display = 'block';
    },

    showNestedCommentEditBody: function (element) {
        const nestedCommentSubInfo = element.closest('#nestedCommentSubInfo');
        nestedCommentSubInfo.style.display = 'none';
        nestedCommentSubInfo.previousElementSibling.style.display = 'block';
    },

    cancelCommentEdit: function (element) {
        const closest = element.closest('#commentEdit');
        closest.style.display = 'none';
        closest.nextElementSibling.style.display = 'block';
    },

    cancelNestedCommentEdit: function (element) {
        const nestedCommentSubEdit = element.closest('#nestedCommentSubEdit');
        nestedCommentSubEdit.style.display = 'none';
        nestedCommentSubEdit.nextElementSibling.style.display = 'block';
    },

    updateComment: function (element, commentId) {
        const postId = element.getAttribute('data-post-id');
        if (isEmpty(postId)) {
            showMessage(messages.EMPTY_POST_ID.message);
            return;
        }

        const commentEditContent = document.getElementById('commentEditContent');
        if (isEmpty(commentEditContent.value)) {
            showMessage(messages.EMPTY_COMMENT_CONTENT.message);
            return;
        }

        if (!confirm('댓글을 수정하시겠습니까?')) {
            return;
        }

        const request = queryBuilder
            .createQueryBuilder()
            .url('/api/v1/post/{postId}/comments')
            .method('PUT')
            .contentType('application/json')
            .pathVariable({
                postId: postId,
            })
            .requestBody({
                commentId: commentId,
                content: commentEditContent.value,
            })
            .build();

        const response = commonFetchTemplate
            .sendFetchRequest(request)
            .then(response => response.json())
            .then(result => {
                if (result.code === messages.SUCCESS_UPDATE_COMMENT.code) {
                    showMessage(messages.SUCCESS_UPDATE_COMMENT.message);
                    this.renderComments(result.result);
                } else if (result.code === messages.FAIL_UPDATE_COMMENT.message) {
                    showMessage(messages.FAIL_UPDATE_COMMENT.message);
                } else {
                    showMessage(messages.COMMON_SERVER_ERROR_MSG.message);
                }
            })
            .catch(error => handleResponseError(error, request));
    },

    showReplyToCommentEditBox: function (element, commentId) {
        console.log(`commentId => ${commentId}`);

        nestedCommentSubEdit;
    },
};
