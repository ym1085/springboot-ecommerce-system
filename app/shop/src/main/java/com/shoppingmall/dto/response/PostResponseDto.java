package com.shoppingmall.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.shoppingmall.domain.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
public class PostResponseDto {
    private Long postId;
    private String title;
    private String content;
    private int categoryId;
    private String categoryName;
    private String writer;
    private int readCnt;
    private String fixedYn;
    private String delYn;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime deleteDate;

    private List<CommentResponseDto> comments = new ArrayList<>();
    private List<PostFileResponseDto> postFiles = new ArrayList<>();

    public PostResponseDto(Post post) {
        this.postId = post.getPostId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.categoryId = post.getCategoryId();
        this.categoryName = post.getCategoryName();
        this.writer = post.getWriter();
        this.readCnt = post.getReadCnt();
        this.fixedYn = post.getFixedYn();
        this.delYn = post.getDelYn();
        this.createDate = post.getCreateDate();
        this.updateDate = post.getUpdateDate();
        this.deleteDate = post.getDeleteDate();
    }

    public void addComments(List<CommentResponseDto> commentResponseDto) {
        this.comments = commentResponseDto;
    }

    public void addPostFiles(List<PostFileResponseDto> postFileResponseDto) {
        this.postFiles = postFileResponseDto;
    }
}
