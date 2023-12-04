package com.shoppingmall.service;

import com.shoppingmall.constant.FileType;
import com.shoppingmall.domain.Post;
import com.shoppingmall.domain.PostFiles;
import com.shoppingmall.dto.request.FileRequestDto;
import com.shoppingmall.dto.request.PostRequestDto;
import com.shoppingmall.dto.request.SearchRequestDto;
import com.shoppingmall.dto.response.*;
import com.shoppingmall.exception.FailSaveFileException;
import com.shoppingmall.exception.FailSavePostException;
import com.shoppingmall.exception.FailUpdateFilesException;
import com.shoppingmall.exception.FailUpdatePostException;
import com.shoppingmall.repository.CommentMapper;
import com.shoppingmall.repository.FileMapper;
import com.shoppingmall.repository.PostMapper;
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
                .map(PostService::addPostFiles)
                .collect(Collectors.toList());

        return new PagingResponseDto<>(posts, pagination);
    }

    private static PostResponseDto addPostFiles(Post post) {
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

    private List<CommentResponseDto> getCommentsByPostId(Long postId) {
        return commentMapper.getComments(postId)
                .stream()
                .filter(Objects::nonNull)
                .map(CommentResponseDto::new)
                .collect(Collectors.toList());
    }

    private List<PostFileResponseDto> getFilesByPostId(Long postId) {
        return fileMapper.getFilesByPostId(postId)
                .stream()
                .filter(Objects::nonNull)
                .map(PostFileResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public Long savePost(PostRequestDto postRequestDto) {
        Post post = new Post(postRequestDto);
        int responseCode = postMapper.savePost(post);
        if (responseCode <= 0) {
            throw new FailSavePostException();
        }

        List<FileRequestDto> fileRequestDtos;
        if (!postRequestDto.getFiles().isEmpty()) {
            fileRequestDtos = fileHandlerHelper.uploadFiles(postRequestDto.getFiles(), postRequestDto.getFileType());
            if (saveFiles(post.getPostId(), fileRequestDtos) == 0) {
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
        int responseCode = postMapper.updatePostById(new Post(postRequestDto));
        if (responseCode == 0) {
            throw new FailUpdatePostException();
        }

        if (!isEmptyFiles(postRequestDto.getFiles())) {
            updateFilesByPostId(postRequestDto);
        }
        return responseCode;
    }

    private void updateFilesByPostId(PostRequestDto postRequestDto) {
        try {
            if (updateFilesByPostId(postRequestDto.getPostId(), postRequestDto.getFiles()) == 0) {
                throw new FailUpdateFilesException();
            }
        } catch (FailUpdateFilesException e) {
            log.error("File updated: " + postRequestDto.getFiles().toString());
        }
    }

    private int updateFilesByPostId(Long postId, List<MultipartFile> files) {
        List<FileResponseDto> fileResponseDtos = getFileResponseDtos(postId);
        fileHandlerHelper.deleteFiles(fileResponseDtos);

        int responseCode = fileMapper.deleteFilesByPostId(postId);
        if (responseCode > 0) {
            List<FileRequestDto> fileRequestDtos = fileHandlerHelper.uploadFiles(files, FileType.POSTS);
            fileRequestDtos.forEach(fileRequestDto -> fileRequestDto.setPostId(postId));
            fileMapper.saveFiles(fileRequestDtos);
        }
        return responseCode;
    }

    private boolean isEmptyFiles(List<MultipartFile> files) {
        return (files.isEmpty());
    }

    @Transactional
    public int deletePost(long postId) {
        int responseCode = postMapper.deletePostById(postId);

        if (responseCode > 0) {
            List<FileResponseDto> fileResponseDtos = getFileResponseDtos(postId);
            if (!CollectionUtils.isEmpty(fileResponseDtos)) {
                fileHandlerHelper.deleteFiles(fileResponseDtos);
                responseCode = fileMapper.deleteUpdateFilesByPostId(postId);
            }
        }

        return responseCode;
    }

    private List<FileResponseDto> getFileResponseDtos(long postId) {
        return fileMapper.getFilesByPostId(postId)
                .stream()
                .map(FileResponseDto::new)
                .collect(Collectors.toList());
    }
}
