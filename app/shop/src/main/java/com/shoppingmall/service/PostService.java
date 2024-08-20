package com.shoppingmall.service;

import com.shoppingmall.constant.FileType;
import com.shoppingmall.dto.request.FileSaveRequestDto;
import com.shoppingmall.dto.request.PostSaveRequestDto;
import com.shoppingmall.dto.request.PostUpdateRequestDto;
import com.shoppingmall.dto.request.SearchRequestDto;
import com.shoppingmall.exception.FileException;
import com.shoppingmall.exception.PostException;
import com.shoppingmall.mapper.PostFileMapper;
import com.shoppingmall.mapper.PostMapper;
import com.shoppingmall.utils.FileHandlerHelper;
import com.shoppingmall.utils.PaginationUtils;
import com.shoppingmall.vo.response.PagingResponse;
import com.shoppingmall.vo.Post;
import com.shoppingmall.vo.PostFiles;
import io.jsonwebtoken.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import static com.shoppingmall.common.code.failure.file.FileFailureCode.*;
import static com.shoppingmall.common.code.failure.post.PostFailureCode.FAIL_SAVE_POST;
import static com.shoppingmall.common.code.failure.post.PostFailureCode.NOT_FOUND_POST;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PostService {
    private final PostMapper postMapper;
    private final PostFileMapper postFileMapper;
    private final FileHandlerHelper fileHandlerHelper;

    public PagingResponse<Post> getPosts(SearchRequestDto searchRequestDto) {
        int totalRecordCount = postMapper.count(searchRequestDto);
        if (totalRecordCount < 1) {
            return new PagingResponse<>(Collections.emptyList(), null);
        }
        PaginationUtils pagination = new PaginationUtils(totalRecordCount, searchRequestDto);
        searchRequestDto.setPagination(pagination);

        List<Post> posts = postMapper.getPosts(searchRequestDto);
        return new PagingResponse<>(posts, pagination);
    }

    @Transactional
    public Post getPostById(Integer postId) {
        if (postId == null) {
            throw new PostException(NOT_FOUND_POST);
        }
        Post post = postMapper.getPostByPostId(postId).orElseThrow(() -> new NoSuchElementException("게시글이 존재하지 않습니다."));
        postMapper.increasePostByPostId(post.getPostId());
        return post;
    }
    
    @Transactional
    public int savePost(PostSaveRequestDto postSaveRequestDto) {
        if (postMapper.savePost(postSaveRequestDto) < 1) {
            log.error(FAIL_SAVE_POST.getMessage());
            throw new PostException(FAIL_SAVE_POST);
        }
        handlePostFiles(postSaveRequestDto);
        return postSaveRequestDto.getPostId();
    }

    /**
     * 서버 && DB에 파일 저장
     */
    private void handlePostFiles(PostSaveRequestDto postSaveRequestDto) {
        if (CollectionUtils.isEmpty(postSaveRequestDto.getFiles())) {
            log.error(FAIL_SAVE_FILES.getMessage());
            return;
        }

        try {
            String categoryName = postMapper.getCategoryNameByPostCategoryId(postSaveRequestDto.getCategoryId());
            List<FileSaveRequestDto> fileSaveRequestDtos = fileHandlerHelper.uploadFilesToServer(postSaveRequestDto.getFiles(), FileType.posts, categoryName);
            int result = saveFiles(postSaveRequestDto.getPostId(), fileSaveRequestDtos);
            log.info("DB 파일 저장 결과 = {}", result);
        } catch (IOException | FileException e) {
            log.error("File processing error: {}", e.getMessage());
            rollbackUploadedFiles(postSaveRequestDto.getPostId()); // 업로드된 파일을 서버 상에서 DELETE
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage());
            rollbackUploadedFiles(postSaveRequestDto.getPostId()); // 업로드된 파일을 서버 상에서 DELETE
            throw e;
        }
    }

    private void rollbackUploadedFiles(Integer postId) {
        List<PostFiles> postFiles = getFileResponseDtos(postId);
        fileHandlerHelper.deleteFiles(postFiles);
    }

    public int saveFiles(Integer postId, List<FileSaveRequestDto> files) {
        if (CollectionUtils.isEmpty(files)) {
            return 0;
        }

        for (FileSaveRequestDto file : files) {
            if (file == null) {
                continue;
            }
            file.setId(postId);
        }
        return postFileMapper.saveFiles(files);
    }

    @Transactional
    public void updatePost(PostUpdateRequestDto postUpdateRequestDto) {
        if (postMapper.updatePost(postUpdateRequestDto) < 1) {
            throw new RuntimeException();
        }

        if (isEmptyFiles(postUpdateRequestDto.getFiles())) {
            throw new FileException(FAIL_UPDATE_FILES);
        }

        int updateFilesCount = updateFilesByPostId(postUpdateRequestDto);
        if (updateFilesCount < 1) {
            log.error(FAIL_UPLOAD_FILES.getMessage());
            throw new FileException(FAIL_UPLOAD_FILES);
        }
    }

    /**
     * 게시글 업데이트 시 파일이 첨부되어 있는 경우, 기존 파일은 전부 삭제하고 새로 업로드를 진행 한다
     * TODO: 아래 플로우는 개선이 필요한 것으로 보임
     */
    private int updateFilesByPostId(PostUpdateRequestDto postUpdateRequestDto) {
        Integer postId = postUpdateRequestDto.getPostId();
        List<MultipartFile> files = postUpdateRequestDto.getFiles();
        if (postId == null || CollectionUtils.isEmpty(files)) {
            return 0;
        }
        int deleteCount = deleteExistingFiles(postId);
        log.info("[uploadFilesByPostId] 삭제된 파일 개수 = {}", deleteCount);

        return saveUpdatedNewFiles(postUpdateRequestDto);
    }

    private int saveUpdatedNewFiles(PostUpdateRequestDto postUpdateRequestDto) {
        String categoryName = postMapper.getCategoryNameByPostCategoryId(postUpdateRequestDto.getCategoryId());
        List<FileSaveRequestDto> fileSaveRequestDtos = fileHandlerHelper.uploadFilesToServer(postUpdateRequestDto.getFiles(), FileType.posts, categoryName);
        fileSaveRequestDtos.forEach(fileRequestDto -> fileRequestDto.setId(postUpdateRequestDto.getPostId()));
        return postFileMapper.saveFiles(fileSaveRequestDtos);
    }

    private int deleteExistingFiles(Integer postId) {
        int deleteCount = postFileMapper.deleteFilesByPostId(postId);
        if (deleteCount > 0) {
            log.info("[uploadFilesByPostId] 파일 삭제에 성공하였습니다. postId = {}", postId);
            fileHandlerHelper.deleteFiles(getFileResponseDtos(postId)); // 서버 특정 경로에 존재하는 파일 삭제
        } else {
            log.error("[uploadFilesByPostId] 파일 삭제에 실패하였습니다. postId = {}", postId);
        }
        return deleteCount;
    }

    @Transactional
    public void deletePost(Integer postId) {
        if (postMapper.deletePostByPostId(postId) <= 0) {
            return;
        }

        List<PostFiles> fileResponseDtos = getFileResponseDtos(postId);
        if (CollectionUtils.isEmpty(fileResponseDtos)) {
            return;
        }

        if (postFileMapper.deleteFilesByPostId(postId) > 0) {
            fileHandlerHelper.deleteFiles(fileResponseDtos);
        }
    }

    private boolean isEmptyFiles(List<MultipartFile> files) {
        return CollectionUtils.isEmpty(files);
    }

    private List<PostFiles> getFileResponseDtos(Integer postId) {
        return postFileMapper.getFilesByPostId(postId);
    }
}
