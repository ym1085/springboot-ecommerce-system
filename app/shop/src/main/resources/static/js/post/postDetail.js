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
            showMessage(messages.NOT_FOUND_POST_ID.message);
            return false;
        }
        if (!this.isValidTitle(this.title.value)) {
            showMessage(messages.NOT_FOUND_POST_TITLE.message);
            this.title.focus();
            return false;
        }
        if (!this.isValidContent(this.content.value)) {
            showMessage(messages.NOT_FOUND_POST_CONTENT.message);
            this.content.focus();
            return false;
        }
        if (isEmpty(this.fixedYn.value)) {
            showMessage(messages.NOT_FOUND_POST_FIXED_YN.message);
            return false;
        }
        return true;
    },

    updatePosts: function (postId) {
        if (!confirm(`게시글을 수정 하시겠습니까?`)) {
            return;
        }

        if (this.validatePostInfo()) {
            const formData = new FormData();
            const fileObj = document.getElementById('file');
            for (let i = 0; i < fileObj.files.length; i++) {
                formData.append('files', fileObj.files[i]);
            }
            formData.append('title', this.title.value);
            formData.append('content', this.content.value);
            formData.append('fixedYn', this.fixedYn.checked ? 'Y' : 'N');

            const request = queryBuilder
                .createQueryBuilder()
                .baseUrl('/api/v1/post/{postId}')
                .method('PUT')
                .pathVariable({ postId: postId })
                .requestBody(formData)
                .build();

            const response = fetchTemplate
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
            .baseUrl('/api/v1/post/{postId}')
            .method('DELETE')
            .contentType('application/json')
            .pathVariable({ postId: postId })
            .build();

        const response = fetchTemplate
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

    createFileDownloadElement: function (file, filename) {
        const downloadFileUrl = window.URL.createObjectURL(file); // 해당 file을 가르키는 url 생성
        const downloadFileElement = document.createElement('a');
        document.body.appendChild(downloadFileElement);
        downloadFileElement.download = filename; // a tag에 download 속성을 줘서 클릭할 때 다운로드가 일어날 수 있도록 하기
        downloadFileElement.href = downloadFileUrl; // href에 url 달아주기
        downloadFileElement.click(); // 코드 상 클릭을 해줘서 다운로드 트리거_

        document.body.removeChild(downloadFileElement); // 쓰임을 다한 a 태그 삭제
        window.URL.revokeObjectURL(downloadFileUrl); // 쓰임을 다한 url 객체 삭제
    },

    downloadAttachedFile: function (element, postFileId) {
        if (isEmpty(postFileId)) {
            showMessage(messages.NOT_FOUND_POST_FILES_ID.message);
            return;
        }

        const request = queryBuilder
            .createQueryBuilder()
            .baseUrl('/api/v1/download/{domain}/{postFileId}')
            .method('GET')
            .pathVariable({ domain: 'posts', postFileId: postFileId })
            .build();

        // https://developer-alle.tistory.com/435
        const response = fetchTemplate
            .sendFetchRequest(request)
            .then(response => {
                if (response.status === 200) {
                    const contentDisposition = response.headers.get('Content-Disposition');
                    const filename = contentDisposition.match(/filename="(.+)"/)[1];

                    return response.blob().then(file => ({
                        file: file,
                        filename: filename,
                        status: response.status,
                    }));
                } else {
                    showMessage(messages.FAIL_DOWNLOAD_FILES.message);
                }
            })
            .then(({ file, filename, status }) => {
                if (status === 200) {
                    showMessage(messages.SUCCESS_DOWNLOAD_FILES.message);
                    this.createFileDownloadElement(file, filename);
                } else {
                    showMessage(messages.FAIL_DOWNLOAD_FILES.message);
                }
            })
            .catch(error => handleResponseError(error, request));
    },

    createGzipFileDownloadElement: function (file, filename) {
        const downloadFileUrl = window.URL.createObjectURL(file);
        const downloadFileElement = document.createElement('a');
        downloadFileElement.style.display = 'none';
        downloadFileElement.href = downloadFileUrl;

        downloadFileElement.download = filename;
        document.body.appendChild(downloadFileElement);

        downloadFileElement.click();
        window.URL.revokeObjectURL(downloadFileUrl);
    },

    downloadAttachedFilesByCompressed: function (postId) {
        if (isEmpty(postId)) {
            showMessage(messages.NOT_FOUND_POST_ID.message);
            return;
        }

        const request = queryBuilder
            .createQueryBuilder()
            .baseUrl('/api/v1/download/compress/{domain}/{postId}')
            .method('GET')
            .pathVariable({ domain: 'posts', postId: postId })
            .build();

        const response = fetchTemplate
            .sendFetchRequest(request)
            .then(response => {
                if (response.status === 200) {
                    const contentDisposition = response.headers.get('Content-Disposition');
                    const filename = contentDisposition.match(/filename="(.+)"/)[1];

                    return response.blob().then(file => ({
                        file: file,
                        filename: filename,
                        status: response.status,
                    }));
                } else {
                    showMessage(messages.FAIL_DOWNLOAD_FILES.message);
                }
            })
            .then(({ file, filename, status }) => {
                if (status === 200) {
                    showMessage(messages.SUCCESS_GZIP_DOWNLOAD_FILES.message);
                    this.createGzipFileDownloadElement(file, filename);
                } else {
                    showMessage(messages.FAIL_DOWNLOAD_FILES.message);
                }
            })
            .catch(error => handleResponseError(error, request));
    },
};

const commentInfo = {
    renderComment: function (comment, parentElement) {
        // 대댓글 작성 영역
        const executeReplyTemplate = document.createElement('div');
        executeReplyTemplate.setAttribute('id', 'executeReplyTemplate');
        executeReplyTemplate.style.display = 'none';

        const executeReplySubInfoTemplate = document.createElement('ul');
        executeReplySubInfoTemplate.setAttribute('id', 'executeReplySubInfoTemplate');

        const executeReplySubInfoContentTemplate = document.createElement('li');

        // 입력창(input) 생성
        const input = document.createElement('input');
        input.type = 'text';

        // 대댓글 작성 버튼 생성
        const saveButton = document.createElement('button');
        saveButton.type = 'button';
        saveButton.textContent = '대댓글 작성';
        saveButton.setAttribute('onclick', `commentInfo.saveCommentReply(this)`);
        saveButton.setAttribute('data-post-id', `${comment.postId}`);

        // 취소 버튼 생성
        const cancelButton = document.createElement('button');
        cancelButton.type = 'button';
        cancelButton.textContent = '취소';
        cancelButton.setAttribute('onclick', `commentInfo.hideReplyToCommentEditBox(this)`);

        // 요소들을 DOM에 추가
        executeReplySubInfoContentTemplate.appendChild(input);
        executeReplySubInfoContentTemplate.appendChild(saveButton);
        executeReplySubInfoContentTemplate.appendChild(cancelButton);

        executeReplySubInfoTemplate.appendChild(executeReplySubInfoContentTemplate);
        executeReplyTemplate.appendChild(executeReplySubInfoTemplate);

        //////////////////////////////////////////////////////////////////////////////////////////

        // 댓글 수정 영역 <div></div>
        const commentTemplate = document.createElement('div');
        commentTemplate.setAttribute('data-comment-id', `${comment.commentId}`);
        commentTemplate.setAttribute('id', 'commentTemplate');
        commentTemplate.style.display = 'none';

        // 입력 필드
        const inputFieldTemplate = document.createElement('input');
        inputFieldTemplate.setAttribute('type', 'text');
        inputFieldTemplate.setAttribute('id', 'commentTemplateContent');
        inputFieldTemplate.value = comment.content; // Thymeleaf의 th:value 대체

        // 수정 완료 버튼
        const editButtonTemplate = document.createElement('button');
        editButtonTemplate.textContent = '수정 완료';
        editButtonTemplate.setAttribute('data-post-id', `${comment.postId}`);
        editButtonTemplate.setAttribute('type', 'button');
        editButtonTemplate.setAttribute('onclick', `commentInfo.updateComment(this, ${comment.commentId})`);

        // 삭제 버튼
        const deleteButtonTemplate = document.createElement('button');
        deleteButtonTemplate.textContent = '삭제';
        deleteButtonTemplate.setAttribute('data-post-id', `${comment.postId}`);
        deleteButtonTemplate.setAttribute('style', 'margin-right: 10px;');
        deleteButtonTemplate.setAttribute('type', 'button');
        deleteButtonTemplate.setAttribute('onclick', `commentInfo.deleteComments(this, ${comment.commentId})`);

        // 취소 버튼
        const cancelButtonTemplate = document.createElement('button');
        cancelButtonTemplate.textContent = '취소';
        cancelButtonTemplate.setAttribute('type', 'button');
        cancelButtonTemplate.setAttribute('onclick', `commentInfo.hideCommentEditBox(this, ${comment.commentId})`);

        // 댓글 달기 버튼
        const replyButtonTemplate = document.createElement('button');
        replyButtonTemplate.textContent = '댓글 달기';
        replyButtonTemplate.setAttribute('type', 'button');
        replyButtonTemplate.setAttribute('onclick', `commentInfo.showReplyToCommentEditBox(this)`);

        // 댓글 수정 영역에 요소들 추가
        commentTemplate.appendChild(inputFieldTemplate);
        commentTemplate.appendChild(editButtonTemplate);
        commentTemplate.appendChild(cancelButtonTemplate);
        commentTemplate.appendChild(replyButtonTemplate);

        // 댓글 영역 <div></div>
        const commentInfo = document.createElement('div');
        commentInfo.setAttribute('data-comment-id', `${comment.commentId}`);
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
        deleteButtonInfo.setAttribute('data-post-id', `${comment.postId}`);
        deleteButtonInfo.setAttribute('style', 'margin-right: 10px;');
        deleteButtonInfo.setAttribute('type', 'button');
        deleteButtonInfo.setAttribute('onclick', `commentInfo.deleteComments(this, ${comment.commentId})`);

        // 답글달기 버튼
        const replyButtonInfo = document.createElement('button');
        replyButtonInfo.textContent = '댓글 달기';
        replyButtonInfo.setAttribute('style', 'margin-right: 10px;');
        replyButtonInfo.setAttribute('type', 'button');
        replyButtonInfo.setAttribute('onclick', `commentInfo.showReplyToCommentEditBox(this)`);

        // 댓글 영역에 댓글 내용 + 버튼 추가
        commentInfo.appendChild(commentSpanInfo);
        commentInfo.appendChild(editButtonInfo);
        commentInfo.appendChild(deleteButtonInfo);
        commentInfo.appendChild(replyButtonInfo);

        // 댓글 랜더링
        parentElement.appendChild(commentTemplate);
        parentElement.appendChild(commentInfo);
        parentElement.appendChild(executeReplyTemplate);
    },

    renderReplyComment: function (comment, parentElement) {
        // 대댓글 작성 영역
        const executeReplyTemplate = document.createElement('div');
        executeReplyTemplate.setAttribute('id', 'executeReplyTemplate');
        executeReplyTemplate.style.display = 'none';

        const executeReplySubInfoTemplate = document.createElement('ul');
        executeReplySubInfoTemplate.setAttribute('id', 'executeReplySubInfoTemplate');

        const executeReplySubInfoContentTemplate = document.createElement('li');

        // 입력창(input) 생성
        const input = document.createElement('input');
        input.type = 'text';

        // 대댓글 작성 버튼 생성
        const saveButton = document.createElement('button');
        saveButton.type = 'button';
        saveButton.textContent = '대댓글 작성';
        saveButton.setAttribute('onclick', `commentInfo.saveCommentReply(this)`);
        saveButton.setAttribute('data-post-id', `${comment.postId}`);

        // 취소 버튼 생성
        const cancelButton = document.createElement('button');
        cancelButton.type = 'button';
        cancelButton.textContent = '취소';
        cancelButton.setAttribute('onclick', `commentInfo.hideReplyToCommentEditBox(this)`);

        // 요소들을 DOM에 추가
        executeReplySubInfoContentTemplate.appendChild(input);
        executeReplySubInfoContentTemplate.appendChild(saveButton);
        executeReplySubInfoContentTemplate.appendChild(cancelButton);

        executeReplySubInfoTemplate.appendChild(executeReplySubInfoContentTemplate);
        executeReplyTemplate.appendChild(executeReplySubInfoTemplate);

        //////////////////////////////////////////////////////////////////////////////////////////

        // 대댓글 수정 영역
        const commentReplyTemplate = document.createElement('div');
        commentReplyTemplate.setAttribute('id', 'commentReplyTemplate');
        commentReplyTemplate.setAttribute('data-parent-id', comment.parentId);
        commentReplyTemplate.style.display = 'none';

        // ul > li
        const commentReplySubInfoTemplate = document.createElement('ul');
        commentReplySubInfoTemplate.setAttribute('id', 'commentReplySubInfoTemplate');

        const commentReplySubInfoContentsTemplate = document.createElement('li');

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
        cancelButtonEdit.setAttribute('onclick', `commentInfo.hideCommentReplyEditBox(this, ${comment.commentId})`);

        // 삭제 버튼
        const deleteButtonEdit = document.createElement('button');
        deleteButtonEdit.textContent = '삭제';
        deleteButtonEdit.setAttribute('data-post-id', `${comment.postId}`);
        deleteButtonEdit.setAttribute('type', 'button');
        deleteButtonEdit.setAttribute('onclick', `commentInfo.deleteCommentReply(this, ${comment.commentId})`);

        // 대댓글 수정 영역에 요소들 추가
        commentReplySubInfoContentsTemplate.appendChild(editInput);
        commentReplySubInfoContentsTemplate.appendChild(editButtonEdit);
        commentReplySubInfoContentsTemplate.appendChild(cancelButtonEdit);
        commentReplySubInfoContentsTemplate.appendChild(deleteButtonEdit);

        commentReplySubInfoTemplate.appendChild(commentReplySubInfoContentsTemplate);
        commentReplyTemplate.appendChild(commentReplySubInfoTemplate);

        //////////////////////////////////////////////////////////////////////////////////////////

        // 대댓글 영역
        const commentReply = document.createElement('div');
        commentReply.setAttribute('id', 'commentReply');
        commentReply.setAttribute('data-parent-id', comment.parentId);

        // 대댓글을 계층 구조 방식으로 그리기 위한 태그 영역
        const commentReplySubInfo = document.createElement('ul');
        commentReplySubInfo.setAttribute('id', 'commentReplySubInfo');
        const commentReplySubInfoContents = document.createElement('li');

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
        editButton.setAttribute('onclick', `commentInfo.showCommentReplyEditBox(this, ${comment.commentId})`);

        // 삭제 버튼
        const deleteButton = document.createElement('button');
        deleteButton.textContent = '삭제';
        deleteButton.setAttribute('data-post-id', `${comment.postId}`);
        deleteButton.setAttribute('style', 'margin-right: 10px;');
        deleteButton.setAttribute('type', 'button');
        deleteButton.setAttribute('onclick', `commentInfo.deleteCommentReply(this, ${comment.commentId})`);

        // 대댓글 영역에 수정 + 삭제 버튼 추가
        commentReplySubInfoContents.appendChild(commentSpan);
        commentReplySubInfoContents.appendChild(editButton);
        commentReplySubInfoContents.appendChild(deleteButton);

        commentReplySubInfo.appendChild(commentReplySubInfoContents);
        commentReply.appendChild(commentReplySubInfo);

        // 대댓글 랜더링
        parentElement.appendChild(executeReplyTemplate);
        parentElement.appendChild(commentReplyTemplate);
        parentElement.appendChild(commentReply);
    },

    renderComments: function (comments) {
        const commentList = document.getElementById('commentList');
        const commentContent = document.getElementById('commentContent');

        // 모든 자식 노드 삭제 : https://hianna.tistory.com/722
        commentList.replaceChildren();
        console.log(`comments => ${JSON.stringify(comments)}`);

        comments.forEach(comment => {
            if (!comment.path || !comment.path.includes('-')) {
                this.renderComment(comment, commentList);
            } else {
                this.renderReplyComment(comment, commentList);
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
            showMessage(messages.NOT_FOUND_COMMENT_CONTENT.message);
            focus(commentContent);
            return;
        } else if (commentContent.value.length > 300) {
            showMessage(messages.OVER_LENGTH_COMMENT.message);
            focus(commentContent);
            return;
        }

        if (isEmpty(postId)) {
            showMessage(messages.NOT_FOUND_POST_ID.message);
            return;
        }

        const request = queryBuilder
            .createQueryBuilder()
            .baseUrl('/api/v1/post/{postId}/comments')
            .method('POST')
            .contentType('application/json')
            .pathVariable({ postId: postId })
            .requestBody({
                content: commentContent.value,
            })
            .build();

        const response = fetchTemplate
            .sendFetchRequest(request)
            .then(response => response.json())
            .then(result => {
                if (result.code === messages.SUCCESS_SAVE_COMMENT.code) {
                    showMessage(messages.SUCCESS_SAVE_COMMENT.message);
                    this.renderComments(result.result);
                } else if (result.code === messages.FAIL_SAVE_COMMENT.message) {
                    showMessage(messages.FAIL_SAVE_COMMENT.message);
                } else if (result.code === messages.NOT_FOUND_POST_ID.message) {
                    showMessage(messages.NOT_FOUND_POST_ID.message);
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

    showCommentReplyEditBox: function (element) {
        const commentReply = element.closest('#commentReply');
        commentReply.style.display = 'none';
        commentReply.previousElementSibling.style.display = 'block';
    },

    hideCommentEditBox: function (element) {
        const commentTemplate = element.closest('#commentTemplate');
        commentTemplate.style.display = 'none';
        commentTemplate.nextElementSibling.style.display = 'block';
    },

    hideCommentReplyEditBox: function (element) {
        const commentReplyTemplate = element.closest('#commentReplyTemplate');
        commentReplyTemplate.style.display = 'none';
        commentReplyTemplate.nextElementSibling.style.display = 'block';
    },

    updateComment: function (element, commentId) {
        const postId = element.getAttribute('data-post-id');
        // console.log(`postId => ${postId}, commentId => ${commentId}`);
        if (isEmpty(postId)) {
            showMessage(messages.NOT_FOUND_POST_ID.message);
            return;
        }

        const commentTemplateContent = element.previousElementSibling; // 댓글 내용
        if (isEmpty(commentTemplateContent.value)) {
            showMessage(messages.NOT_FOUND_COMMENT_CONTENT.message);
            return;
        }

        if (!confirm('댓글을 수정하시겠습니까?')) {
            return;
        }

        const request = queryBuilder
            .createQueryBuilder()
            .baseUrl('/api/v1/post/{postId}/comments')
            .method('PUT')
            .contentType('application/json')
            .pathVariable({ postId: postId })
            .requestBody({
                commentId: commentId,
                content: commentTemplateContent.value,
            })
            .build();

        const response = fetchTemplate
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

    deleteComments: function (element, commentId) {
        const postId = element.getAttribute('data-post-id');
        if (isEmpty(postId)) {
            showMessage(messages.NOT_FOUND_POST_ID.message);
            return;
        }

        if (!confirm('댓글을 삭제하시겠습니까?')) {
            return;
        }

        const request = queryBuilder
            .createQueryBuilder()
            .baseUrl('/api/v1/post/comments')
            .method('DELETE')
            .contentType('application/json')
            .queryString({
                commentId: commentId,
                postId: postId, // 댓글 정보 조회시 사용
            })
            .build();

        const response = fetchTemplate
            .sendFetchRequest(request)
            .then(response => response.json())
            .then(result => {
                if (result.code === messages.SUCCESS_DELETE_COMMENT.code) {
                    showMessage(messages.SUCCESS_DELETE_COMMENT.message);
                    this.renderComments(result.result);
                } else if (result.code === messages.FAIL_DELETE_COMMENT.message) {
                    showMessage(messages.FAIL_DELETE_COMMENT.message);
                } else {
                    showMessage(messages.COMMON_SERVER_ERROR_MSG.message);
                }
            })
            .catch(error => handleResponseError(error, request));
    },

    deleteCommentsReply: function (element, commentId) {
        const postId = element.getAttribute('data-post-id');
        if (isEmpty(postId)) {
            showMessage(messages.NOT_FOUND_POST_ID.message);
            return;
        }

        if (!confirm('댓글을 삭제하시겠습니까?')) {
            return;
        }

        const request = queryBuilder
            .createQueryBuilder()
            .baseUrl('/api/v1/post/comments')
            .method('DELETE')
            .contentType('application/json')
            .queryString({
                commentId: commentId,
                postId: postId, // 댓글 정보 조회시 사용
            })
            .build();

        const response = fetchTemplate
            .sendFetchRequest(request)
            .then(response => response.json())
            .then(result => {
                if (result.code === messages.SUCCESS_DELETE_COMMENT.code) {
                    showMessage(messages.SUCCESS_DELETE_COMMENT.message);
                    this.renderComments(result.result);
                } else if (result.code === messages.FAIL_DELETE_COMMENT.message) {
                    showMessage(messages.FAIL_DELETE_COMMENT.message);
                } else {
                    showMessage(messages.COMMON_SERVER_ERROR_MSG.message);
                }
            })
            .catch(error => handleResponseError(error, request));
    },

    deleteCommentReply: function (element, commentId) {
        const postId = element.getAttribute('data-post-id');
        if (isEmpty(postId)) {
            showMessage(messages.NOT_FOUND_POST_ID.message);
            return;
        }

        if (!confirm('대댓글을 삭제하시겠습니까?')) {
            return;
        }

        const request = queryBuilder
            .createQueryBuilder()
            .baseUrl('/api/v1/post/comments/reply')
            .method('DELETE')
            .contentType('application/json')
            .queryString({
                commentId: commentId,
                postId: postId,
            })
            .build();

        const response = fetchTemplate
            .sendFetchRequest(request)
            .then(response => response.json())
            .then(result => {
                if (result.code === messages.SUCCESS_DELETE_COMMENT.code) {
                    showMessage(messages.SUCCESS_DELETE_COMMENT.message);
                    this.renderComments(result.result);
                } else if (result.code === messages.FAIL_DELETE_COMMENT.message) {
                    showMessage(messages.FAIL_DELETE_COMMENT.message);
                } else {
                    showMessage(messages.COMMON_SERVER_ERROR_MSG.message);
                }
            })
            .catch(error => handleResponseError(error, request));
    },

    showReplyToCommentEditBox: function (element, commentId) {
        const commentInfo = element.closest('#commentInfo');
        commentInfo.nextElementSibling.style.display = 'block';
    },

    hideReplyToCommentEditBox: function (element, commentId) {
        const executeReplyTemplate = element.closest('#executeReplyTemplate');
        executeReplyTemplate.style.display = 'none';
        executeReplyTemplate.previousElementSibling.style.display = 'block';
    },

    saveCommentReply: function (element) {
        const commentInfo = element.closest('#executeReplyTemplate').previousElementSibling;
        const parentId = commentInfo.getAttribute('data-comment-id');
        const commentContent = element.previousElementSibling;
        const postId = element.getAttribute('data-post-id');
        if (isEmpty(postId)) {
            showMessage(messages.NOT_FOUND_POST_ID.message);
            return;
        }

        if (isEmpty(commentContent.value)) {
            showMessage(messages.NOT_FOUND_COMMENT_CONTENT.message);
            commentContent.focus();
            return;
        }

        if (!confirm('대댓글을 작성하시겠습니까?')) {
            return;
        }

        const request = queryBuilder
            .createQueryBuilder()
            .baseUrl('/api/v1/post/{postId}/comments')
            .method('POST')
            .contentType('application/json')
            .pathVariable({ postId: postId })
            .requestBody({
                content: commentContent.value,
                parentId: parentId,
            })
            .build();

        const response = fetchTemplate
            .sendFetchRequest(request)
            .then(response => response.json())
            .then(result => {
                if (result.code === messages.SUCCESS_SAVE_COMMENT.code) {
                    showMessage(messages.SUCCESS_SAVE_COMMENT.message);
                    this.renderComments(result.result);
                } else if (result.code === messages.FAIL_SAVE_COMMENT.message) {
                    showMessage(messages.FAIL_SAVE_COMMENT.message);
                } else if (result.code === messages.NOT_FOUND_POST_ID.message) {
                    showMessage(messages.NOT_FOUND_POST_ID.message);
                } else {
                    showMessage(messages.COMMON_SERVER_ERROR_MSG.message);
                }
            })
            .catch(error => handleResponseError(error, request));
    },
};
