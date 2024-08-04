package com.shoppingmall.service;

import com.shoppingmall.constant.DirPathType;
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
import com.shoppingmall.vo.PagingResponse;
import com.shoppingmall.vo.Post;
import com.shoppingmall.vo.PostFiles;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import static com.shoppingmall.common.code.failure.file.FileFailureCode.FAIL_SAVE_FILES;
import static com.shoppingmall.common.code.failure.file.FileFailureCode.FAIL_UPLOAD_FILES;
import static com.shoppingmall.common.code.failure.post.PostFailureCode.NOT_FOUND_POST;
import static com.shoppingmall.common.code.failure.post.PostFailureCode.FAIL_SAVE_POST;

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
        if (postMapper.savePost(postSaveRequestDto) == 0) {
            log.error(FAIL_SAVE_POST.getMessage());
            throw new PostException(FAIL_SAVE_POST);
        }

        try {
            if (!postSaveRequestDto.getFiles().isEmpty()) {
                List<FileSaveRequestDto> baseFileSaveRequestDto = fileHandlerHelper.uploadFiles(postSaveRequestDto.getFiles(), postSaveRequestDto.getDirPathType());
                if (saveFiles(postSaveRequestDto.getPostId(), baseFileSaveRequestDto) < 1) {
                    log.error(FAIL_SAVE_FILES.getMessage());
                    throw new FileException(FAIL_SAVE_FILES);
                }
            }
        } catch (FileException e) {
            // 예외 발생 시 서버의 특정 경로에 업로드 된 파일을 삭제해야 하기에, 추가
            // 파일 업로드 성공 ----> 파일 정보 DB 저장(ERROR!! 발생) ----> Transaction Rollback ----> 이미 업로드한 파일은 지워줘야 함
            List<PostFiles> postFiles = getFileResponseDtos(postSaveRequestDto.getPostId());
            fileHandlerHelper.deleteFiles(postFiles);
            throw e; // 현재 트랜잭션 롤백
        }
        return postSaveRequestDto.getPostId();
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

        if (!isEmptyFiles(postUpdateRequestDto.getFiles())) {
            if (updateFilesByPostId(postUpdateRequestDto.getPostId(), postUpdateRequestDto.getFiles()) < 1) {
                log.error(FAIL_UPLOAD_FILES.getMessage());
                throw new FileException(FAIL_UPLOAD_FILES);
            }
        }
    }

    /**
     * Todo: 기존 파일을 DELETE 하고 새로운 파일을 INSERT 하는 Flow는 수정 필요
     */
    private int updateFilesByPostId(Integer postId, List<MultipartFile> files) {
        if (postFileMapper.countFilesByPostId(postId) > 0) {
            if (postFileMapper.deleteFilesByPostId(postId) > 0) { // DB의 파일 정보 삭제
                log.info("success delete posts files from database");
                fileHandlerHelper.deleteFiles(getFileResponseDtos(postId)); // 서버 특정 경로에 존재하는 파일 삭제
            } else {
                log.error("fail delete posts files from database");
            }
        }

        List<FileSaveRequestDto> fileRequestDtos = fileHandlerHelper.uploadFiles(files, DirPathType.posts);
        fileRequestDtos.forEach(fileRequestDto -> fileRequestDto.setId(postId));
        return postFileMapper.saveFiles(fileRequestDtos);
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
        return (files.isEmpty());
    }

    private List<PostFiles> getFileResponseDtos(Integer postId) {
        return postFileMapper.getFilesByPostId(postId);
    }
}
