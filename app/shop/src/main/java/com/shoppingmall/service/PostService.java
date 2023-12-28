package com.shoppingmall.service;

import com.shoppingmall.constant.FileType;
import com.shoppingmall.vo.Post;
import com.shoppingmall.vo.PostFiles;
import com.shoppingmall.dto.request.FileRequestDto;
import com.shoppingmall.dto.request.PostRequestDto;
import com.shoppingmall.dto.request.SearchRequestDto;
import com.shoppingmall.dto.response.*;
import com.shoppingmall.exception.*;
import com.shoppingmall.mapper.CommentMapper;
import com.shoppingmall.mapper.FileMapper;
import com.shoppingmall.mapper.PostMapper;
import com.shoppingmall.utils.FileHandlerHelper;
import com.shoppingmall.utils.PaginationUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PostService {

    private final PostMapper postMapper;
    private final FileMapper fileMapper;
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
        List<PostFileResponseDto> postFileResponseDtos = new ArrayList<>();
        for (PostFiles postFile : post.getPostFiles()) {
            if (postFile.getPostFileId() == null) {
                postResponseDto.addPostFiles(Collections.emptyList());
                continue;
            }
            postFileResponseDtos.add(PostFileResponseDto.toDto(postFile));
            postResponseDto.addPostFiles(postFileResponseDtos);
        }
        return postResponseDto;
    }

    public PostResponseDto getPostById(Long postId) {
        if (postId != null) {
            int responseCode = postMapper.increasePostByPostId(postId);
            if (responseCode == 0) {
                throw new FailUpdatePostReadCountException();
            }
        }

        PostResponseDto postResponseDto = postMapper.getPostByPostId(postId)
                .map(PostResponseDto::toDto)
                .orElse(new PostResponseDto());

        postResponseDto.addComments(getCommentsByPostId(postId));
        postResponseDto.addPostFiles(getFilesByPostId(postId));
        return postResponseDto;
    }

    private List<CommentResponseDto> getCommentsByPostId(Long postId) {
        return commentMapper.getComments(postId)
                .stream()
                .map(CommentResponseDto::toDto)
                .collect(Collectors.toList());
    }

    private List<PostFileResponseDto> getFilesByPostId(Long postId) {
        return fileMapper.getFilesByPostId(postId)
                .stream()
                .map(PostFileResponseDto::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public Long savePost(PostRequestDto postRequestDto) {
        Post post = postRequestDto.toEntity();
        int responseCode = postMapper.savePost(post);
        if (responseCode == 0) {
            throw new FailSavePostException();
        }

        List<FileRequestDto> fileRequestDtos;
        if (!postRequestDto.getFiles().isEmpty()) {
            fileRequestDtos = fileHandlerHelper.uploadFiles(postRequestDto.getFiles(), postRequestDto.getFileType());
            responseCode = saveFiles(post.getPostId(), fileRequestDtos);
            if (responseCode == 0) {
                throw new FailSaveFileException();
            }
        }

        return post.getPostId();
    }

    public int saveFiles(Long postId, List<FileRequestDto> files) {
        if (CollectionUtils.isEmpty(files) || files.get(0) == null || postId == null) {
            throw new FailSaveFileException();
        }
        for (FileRequestDto file : files) {
            if (file == null) {
                continue;
            }
            file.setPostId(postId);
        }
        return fileMapper.saveFiles(files);
    }

    @Transactional
    public int updatePost(PostRequestDto postRequestDto) {
        int responseCode = postMapper.updatePost(postRequestDto.toEntity());
        if (responseCode == 0) {
            throw new FailUpdatePostException();
        }

        if (!isEmptyFiles(postRequestDto.getFiles())) {
            responseCode = updateFilesByPostId(postRequestDto.getPostId(), postRequestDto.getFiles());
            if (responseCode == 0) {
                throw new FailUpdateFilesException();
            }
        }

        return responseCode;
    }

    /**
     * Todo: 기존 파일을 DELETE 하고 새로운 파일을 INSERT 하는 FLOW는 수정이 필요
     */
    private int updateFilesByPostId(Long postId, List<MultipartFile> files) {
        fileHandlerHelper.deleteFiles(getFileResponseDtos(postId)); // 서버 특정 경로에 존재하는 파일 삭제

        int filesCount = fileMapper.countFilesByPostId(postId);
        if (filesCount > 0) {
            int responseCode = (fileMapper.deleteFilesByPostId(postId)); // DB의 파일 정보 삭제
            if (responseCode > 0) {
                log.info("success delete files from database");
            } else {
                log.info("fail delete files from database");
            }
        }

        List<FileRequestDto> fileRequestDtos = fileHandlerHelper.uploadFiles(files, FileType.POSTS);
        fileRequestDtos.forEach(fileRequestDto -> fileRequestDto.setPostId(postId));
        int responseCode = fileMapper.saveFiles(fileRequestDtos);

        return responseCode;
    }

    @Transactional
    public int deletePost(Long postId) {
        int responseCode = postMapper.deletePostByPostId(postId);

        if (responseCode > 0) {
            List<FileResponseDto> fileResponseDtos = getFileResponseDtos(postId);
            if (!CollectionUtils.isEmpty(fileResponseDtos)) {
                fileHandlerHelper.deleteFiles(fileResponseDtos);
                responseCode = fileMapper.deleteUpdateFilesByPostId(postId);
            }
        }

        return responseCode;
    }

    private boolean isEmptyFiles(List<MultipartFile> files) {
        return (files.isEmpty());
    }

    private List<FileResponseDto> getFileResponseDtos(long postId) {
        return fileMapper.getFilesByPostId(postId)
                .stream()
                .map(FileResponseDto::toDto)
                .collect(Collectors.toList());
    }
}
