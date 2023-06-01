package com.multi.posts.service.impl;

import com.multi.posts.constant.ResponseCode;
import com.multi.posts.domain.Post;
import com.multi.posts.dto.request.FileRequestDto;
import com.multi.posts.dto.request.PostRequestDto;
import com.multi.posts.dto.request.SearchRequestDto;
import com.multi.posts.dto.resposne.FileResponseDto;
import com.multi.posts.dto.resposne.PagingResponseDto;
import com.multi.posts.dto.resposne.PostResponseDto;
import com.multi.utils.Pagination;
import com.multi.posts.repository.FileMapper;
import com.multi.posts.repository.PostMapper;
import com.multi.posts.service.PostService;
import com.multi.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostServiceImpl implements PostService {

    private final PostMapper postMapper;
    private final FileMapper fileMapper;
    private final FileUtils fileUtils;

    @Transactional(readOnly = true)
    @Override
    public PagingResponseDto<PostResponseDto> getPosts(SearchRequestDto searchRequestDto) {
        int totalRecordCount = postMapper.count(searchRequestDto);
        if (totalRecordCount < 1) {
            return new PagingResponseDto<>(Collections.emptyList(), null);
        }

        Pagination pagination = new Pagination(totalRecordCount, searchRequestDto);
        searchRequestDto.setPagination(pagination);

        List<PostResponseDto> posts = postMapper.getPosts(searchRequestDto)
                .stream()
                .filter(Objects::nonNull)
                .map(post -> new PostResponseDto(post))
                .collect(Collectors.toList());

        return new PagingResponseDto<>(posts, pagination);
    }

    @Transactional(readOnly = true)
    @Override
    public PostResponseDto getPostById(Long postId) {
        return postMapper.getPostById(postId)
                .map(post -> new PostResponseDto(post))
                .orElse(new PostResponseDto());
    }

    @Transactional
    @Override
    public Long savePost(PostRequestDto postRequestDto) {
        Post post = new Post(postRequestDto);
        postMapper.savePost(post);
        return post.getPostId();
    }

    /**
     * Todo: 파일 수정 부분은 기존 파일 지우고, 사용자가 올린 파일 다시 올리는데 변경 필요할 것 같음
     * @param postRequestDto
     * @return
     */
    @Transactional
    @Override
    public int updatePost(PostRequestDto postRequestDto) {
        Long successId = postMapper.updatePostById(new Post(postRequestDto));
        if (successId == null || successId == 0) {
            return ResponseCode.FAIL.getResponseCode();
        }

        List<MultipartFile> files = postRequestDto.getFiles();
        return updateFilesById(postRequestDto.getPostId(), files); // 파일이 존재하는 경우 해당 게시글에 매핑 된 파일 정보를 일단 전부 삭제 후 업데이트
    }

    private int updateFilesById(Long postId, List<MultipartFile> files) {
        if (!isFileSizeOverThanZero(files)) {
            return ResponseCode.FAIL.getResponseCode();
        }

        List<FileResponseDto> fileResponseDtos = getFileResponseDtos(postId);
        fileUtils.deleteFiles(fileResponseDtos);

        int successId = fileMapper.deleteFilesById(postId);
        if (successId == 0) {
            return ResponseCode.FAIL.getResponseCode();
        } else if (successId > 0) {
            List<FileRequestDto> fileRequestDtos = fileUtils.uploadFiles(files);
            setFileInfoPostId(postId, fileRequestDtos);
            fileMapper.saveFiles(fileRequestDtos); // 클라이언트가 요청 한 파일 신규 저장
        }
        return ResponseCode.SUCCESS.getResponseCode();
    }

    private boolean isFileSizeOverThanZero(List<MultipartFile> files) {
        return (files.size() > 0);
    }

    private void setFileInfoPostId(Long postId, List<FileRequestDto> fileRequestDtos) {
        fileRequestDtos.stream()
                .filter(Objects::nonNull)
                .forEach(fileRequestDto -> fileRequestDto.setPostId(postId));
    }

    @Transactional
    @Override
    public int deletePost(long postId) {
        Long deletedPostId = postMapper.deletePostById(postId);
        log.debug("deletedPostId = {}", deletedPostId);
        if (deletedPostId == 0) {
            return ResponseCode.FAIL.getResponseCode();
        }

        List<FileResponseDto> fileResponseDtos = getFileResponseDtos(postId);

        // empty라는 것은 해당 게시글에 매핑된 파일이 없다는 의미
        // 게시글 삭제 성공으로 판단 후 성공 코드 반환
        if (CollectionUtils.isEmpty(fileResponseDtos)) {
            return ResponseCode.SUCCESS.getResponseCode();
        }

        fileUtils.deleteFiles(fileResponseDtos);
        return fileMapper.deleteUpdateFilesById(postId);
    }

    private List<FileResponseDto> getFileResponseDtos(long postId) {
        return fileMapper.getFiles(postId).stream()
                .filter(Objects::nonNull)
                .map(file -> new FileResponseDto(file))
                .collect(Collectors.toList());
    }
}
