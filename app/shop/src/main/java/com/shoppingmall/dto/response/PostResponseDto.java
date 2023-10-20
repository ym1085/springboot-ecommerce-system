package com.shoppingmall.dto.response;

import com.shoppingmall.domain.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author      :   ymkim
 * @since       :   2023. 05. 20
 * @description :   게시글 조회 후 게시글 반환 시 사용
 */
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
    private LocalDateTime deleteDate;
    private List<CommentResponseDto> comments;

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
}
