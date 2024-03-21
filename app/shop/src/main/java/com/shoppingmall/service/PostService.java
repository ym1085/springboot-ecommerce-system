package com.shoppingmall.service;

import com.shoppingmall.common.response.ErrorCode;
import com.shoppingmall.constant.DirPathType;
import com.shoppingmall.dto.request.*;
import com.shoppingmall.dto.response.*;
import com.shoppingmall.exception.FailSaveFileException;
import com.shoppingmall.exception.FailSavePostException;
import com.shoppingmall.exception.FailUpdateFilesException;
import com.shoppingmall.mapper.CommentMapper;
import com.shoppingmall.mapper.PostFileMapper;
import com.shoppingmall.mapper.PostMapper;
import com.shoppingmall.utils.FileHandlerHelper;
import com.shoppingmall.utils.PaginationUtils;
import com.shoppingmall.vo.PostFiles;
import com.shoppingmall.vo.Post;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PostService {

    private final PostMapper postMapper;
    private final PostFileMapper postFileMapper;
    private final FileHandlerHelper fileHandlerHelper;
    private final CommentMapper commentMapper;

    public PagingResponseDto<PostResponseDto> getPosts(SearchRequestDto searchRequestDto) {
        int totalRecordCount = postMapper.count(searchRequestDto);
        if (totalRecordCount < 1) {
            return new PagingResponseDto<>(Collections.emptyList(), null);
        }

        PaginationUtils pagination = new PaginationUtils(totalRecordCount, searchRequestDto);
        searchRequestDto.setPagination(pagination);

        List<PostResponseDto> posts = postMapper.getPosts(searchRequestDto)
                .stream()
                .filter(Objects::nonNull)
                .map(PostService::addPostFiles)
                .collect(Collectors.toList());

        return new PagingResponseDto<>(posts, pagination);
    }

    private static PostResponseDto addPostFiles(Post post) {
        PostResponseDto postResponseDto = PostResponseDto.toDto(post);
        List<FileResponseDto> postFileResponseDtos = new ArrayList<>();

        // 파일이 사이즈가 0인 경우에 대한 방어로직
        if (post.getPostFiles() == null || post.getPostFiles().isEmpty()) {
            return postResponseDto;
        }

        for (PostFiles postFile : post.getPostFiles()) {
            if (postFile.getPostFileId() == null || !StringUtils.hasText(postFile.getOriginFileName())) {
                postResponseDto.addPostFiles(Collections.emptyList());
                continue;
            }
            postFileResponseDtos.add(PostFileResponseDto.toDto(postFile));
            postResponseDto.addPostFiles(postFileResponseDtos);
        }
        return postResponseDto;
    }

    @Transactional
    public PostResponseDto getPostById(Integer postId) {
        if (postId != null) {
            int responseCode = postMapper.increasePostByPostId(postId);
            if (responseCode == 0) {
                throw new RuntimeException();
            }
        }

        PostResponseDto postResponseDto = postMapper.getPostByPostId(postId)
                .map(PostResponseDto::toDto)
                .orElse(new PostResponseDto());

        postResponseDto.addComments(getCommentsByPostId(postId));
        postResponseDto.addPostFiles(getFilesByPostId(postId));
        return postResponseDto;
    }

    private List<CommentResponseDto> getCommentsByPostId(Integer postId) {
        return commentMapper.getComments(postId)
                .stream()
                .map(CommentResponseDto::toDto)
                .collect(Collectors.toList());
    }

    private List<FileResponseDto> getFilesByPostId(Integer postId) {
        return postFileMapper.getFilesByPostId(postId)
                .stream()
                .map(PostFileResponseDto::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public int savePost(PostSaveRequestDto postSaveRequestDto) {
        Post post = postSaveRequestDto.toEntity();
        int responseCode = postMapper.savePost(post);
        if (responseCode == 0) {
            log.error("[Occurred Exception] Error Message = {}", ErrorCode.SAVE_POST.getMessage());
            throw new FailSavePostException(ErrorCode.SAVE_POST);
        }

        try {
            if (!postSaveRequestDto.getFiles().isEmpty()) {
                List<FileSaveRequestDto> baseFileSaveRequestDto = fileHandlerHelper.uploadFiles(postSaveRequestDto.getFiles(), postSaveRequestDto.getDirPathType());
                responseCode = saveFiles(post.getPostId(), baseFileSaveRequestDto);
                if (responseCode == 0) {
                    log.error("[Occurred Exception] Error Message = {}", ErrorCode.SAVE_FILES);
                    throw new FailSaveFileException(ErrorCode.SAVE_FILES);
                }
            }
        } catch (RuntimeException e) {
            // 예외 발생 시 서버의 특정 경로에 업로드 된 파일을 삭제해야 하기에, 추가
            // 파일 업로드 성공 ----> 파일 정보 DB 저장(ERROR!! 발생) ----> Transaction Rollback ----> 이미 업로드한 파일은 지워줘야 함
            List<FileResponseDto> fileResponseDtos = getFileResponseDtos(postSaveRequestDto.getPostId());
            fileHandlerHelper.deleteFiles(fileResponseDtos);
            throw e; // 현재 트랜잭션 롤백
        }
        return post.getPostId();
    }

    public int saveFiles(Integer postId, List<FileSaveRequestDto> files) {
        if (CollectionUtils.isEmpty(files) || files.get(0) == null || postId == null) {
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
    public int updatePost(PostUpdateRequestDto postUpdateRequestDto) {
        int responseCode = postMapper.updatePost(postUpdateRequestDto.toEntity());
        if (responseCode == 0) {
            throw new RuntimeException();
        }

        if (!isEmptyFiles(postUpdateRequestDto.getFiles())) {
            responseCode = updateFilesByPostId(postUpdateRequestDto.getPostId(), postUpdateRequestDto.getFiles());
            if (responseCode == 0) {
                log.error("[Occurred Exception] Error Message = {}", ErrorCode.UPLOAD_FILES.getMessage());
                throw new FailUpdateFilesException(ErrorCode.UPLOAD_FILES);
            }
        }

        return responseCode;
    }

    /**
     * Todo: 기존 파일을 DELETE 하고 새로운 파일을 INSERT 하는 FLOW는 수정이 필요
     */
    private int updateFilesByPostId(Integer postId, List<MultipartFile> files) {
        if (postFileMapper.countFilesByPostId(postId) > 0) {
            int responseCode = postFileMapper.deleteFilesByPostId(postId); // DB의 파일 정보 삭제
            if (responseCode > 0) {
                log.info("success delete posts files from database");
                fileHandlerHelper.deleteFiles(getFileResponseDtos(postId)); // 서버 특정 경로에 존재하는 파일 삭제
            } else {
                log.info("fail delete posts files from database");
            }
        }

        List<FileSaveRequestDto> fileRequestDtos = fileHandlerHelper.uploadFiles(files, DirPathType.posts);
        fileRequestDtos.forEach(fileRequestDto -> fileRequestDto.setId(postId));
        return postFileMapper.saveFiles(fileRequestDtos);
    }

    @Transactional
    public void deletePost(Integer postId) {
        int responseCode = postMapper.deletePostByPostId(postId);

        if (responseCode <= 0) {
            return;
        }

        List<FileResponseDto> fileResponseDtos = getFileResponseDtos(postId);
        if (CollectionUtils.isEmpty(fileResponseDtos)) {
            return;
        }

        responseCode = postFileMapper.deleteFilesByPostId(postId);
        if (responseCode > 0) {
            fileHandlerHelper.deleteFiles(fileResponseDtos);
        }
    }

    private boolean isEmptyFiles(List<MultipartFile> files) {
        return (files.isEmpty());
    }

    private List<FileResponseDto> getFileResponseDtos(Integer postId) {
        return postFileMapper.getFilesByPostId(postId)
                .stream()
                .map(PostFileResponseDto::toDto)
                .collect(Collectors.toList());
    }
}
