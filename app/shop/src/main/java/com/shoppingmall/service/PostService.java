package com.shoppingmall.service;

import com.shoppingmall.common.MessageCode;
import com.shoppingmall.constant.FileType;
import com.shoppingmall.domain.Post;
import com.shoppingmall.domain.PostFiles;
import com.shoppingmall.dto.request.FileRequestDto;
import com.shoppingmall.dto.request.PostRequestDto;
import com.shoppingmall.dto.request.SearchRequestDto;
import com.shoppingmall.dto.response.*;
import com.shoppingmall.exception.FailUpdateFilesException;
import com.shoppingmall.repository.CommentMapper;
import com.shoppingmall.repository.FileMapper;
import com.shoppingmall.repository.PostMapper;
import com.shoppingmall.utils.FileHandlerHelper;
import com.shoppingmall.utils.PaginationUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostService {

    private final PostMapper postMapper;
    private final FileMapper fileMapper;
    private final FileHandlerHelper fileHandlerHelper;
    private final CommentMapper commentMapper;

    @Transactional(readOnly = true)
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
                .map(post -> {
                    PostResponseDto postResponseDto = new PostResponseDto(post);
                    List<PostFileResponseDto> postFileResponseDtos = new ArrayList<>();
                    for (PostFiles postFile : post.getPostFiles()) {
                        if (postFile.getPostFileId() == null) {
                            postResponseDto.addPostFiles(Collections.emptyList());
                            continue;
                        }
                        postFileResponseDtos.add(new PostFileResponseDto(postFile));
                        postResponseDto.addPostFiles(postFileResponseDtos);
                    }
                    return postResponseDto;
                }).collect(Collectors.toList());

        return new PagingResponseDto<>(posts, pagination);
    }

    @Transactional(readOnly = true)
    public PostResponseDto getPostById(Long postId) {
        PostResponseDto postResponseDto = postMapper.getPostById(postId)
                .map(PostResponseDto::new)
                .orElse(new PostResponseDto());

        postResponseDto.addComments(getCommentsByPostId(postId));
        postResponseDto.addPostFiles(getFilesByPostId(postId));
        return postResponseDto;
    }

    private List<PostFileResponseDto> getFilesByPostId(Long postId) {
        return fileMapper.getFilesByPostId(postId)
                .stream()
                .filter(Objects::nonNull)
                .map(PostFileResponseDto::new)
                .collect(Collectors.toList());
    }

    private List<CommentResponseDto> getCommentsByPostId(Long postId) {
        return commentMapper.getComments(postId)
                .stream()
                .filter(Objects::nonNull)
                .map(CommentResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public Long savePost(PostRequestDto postRequestDto) {
        Post post = new Post(postRequestDto);
        postMapper.savePost(post);
        return post.getPostId();
    }

    @Transactional
    public MessageCode updatePost(PostRequestDto postRequestDto) {
        // Todo: 방어 로직 허술, 아래 로직 수정 필요
        Long result = postMapper.updatePostById(new Post(postRequestDto));
        if (result == 0) {
            return MessageCode.FAIL_UPDATE_POST;
        }

        List<MultipartFile> files;
        if (!isEmptyFiles(postRequestDto.getFiles())) {
            try {
                int fileUpdatedResult = updateFilesByPostId(postRequestDto.getPostId(), postRequestDto.getFiles());
                if (fileUpdatedResult == 0) {
                    // Fixme: 추후 MessageCode 값에 넣어서 code 값을 반환하도록 수정
                    throw new FailUpdateFilesException("파일 삭제 및 업데이트에 실패 하였습니다.");
                }
            } catch (FailUpdateFilesException e) {
                log.error("File updated: " + postRequestDto.getFiles().toString());
            }
        }

        return MessageCode.SUCCESS_UPDATE_POST;
    }

    private int updateFilesByPostId(Long postId, List<MultipartFile> files) {
        List<FileResponseDto> fileResponseDtos = getFileResponseDtos(postId);
        fileHandlerHelper.deleteFiles(fileResponseDtos); // 서버 특정 경로의 파일 삭제

        // Todo: DB 파일 삭제, 아래 로직 수정 필요
        int result = fileMapper.deleteFilesByPostId(postId);
        if (result > 0) {
            log.info("uploadFile is exists, result = {}", result);
            List<FileRequestDto> fileRequestDtos = fileHandlerHelper.uploadFiles(files, FileType.POSTS);
            setFileInfoPostId(postId, fileRequestDtos);
            fileMapper.saveFiles(fileRequestDtos);
        }
        return MessageCode.SUCCESS.getCode();
    }

    private boolean isEmptyFiles(List<MultipartFile> files) {
        return (files.isEmpty());
    }

    private void setFileInfoPostId(Long postId, List<FileRequestDto> fileRequestDtos) {
        fileRequestDtos.stream()
                .forEach(fileRequestDto -> fileRequestDto.setPostId(postId));
    }

    @Transactional
    public MessageCode deletePost(long postId) {
        Long deletedPostId = postMapper.deletePostById(postId);
        if (deletedPostId == 0) {
            return MessageCode.NOT_FOUND_POST_ID;
        }

        List<FileResponseDto> fileResponseDtos = getFileResponseDtos(postId);
        fileHandlerHelper.deleteFiles(fileResponseDtos);

        return (fileMapper.deleteUpdateFilesByPostId(postId) > 0)
                ? MessageCode.SUCCESS_DELETE_FILES
                : MessageCode.FAIL_DELETE_FILES;
    }

    private List<FileResponseDto> getFileResponseDtos(long postId) {
        return fileMapper.getFilesByPostId(postId)
                .stream()
                .map(FileResponseDto::new)
                .collect(Collectors.toList());
    }
}
