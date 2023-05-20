package com.post.web.dto.resposne;

import com.post.domain.posts.Post;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * @author      :   ymkim
 * @since       :   2023. 05. 20
 * @description :   게시글 조회 후 게시글 반환 시 사용
 */
@Getter
public class PostResponseDto {
    private Long postId;
    private String title;
    private String content;
    private int readCnt;
    private String fixedYn;
    private String delYn;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private LocalDateTime deleteDate;

    public PostResponseDto() { }

    public PostResponseDto(Post post) {
        this.postId = post.getPostId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.readCnt = post.getReadCnt();
        this.fixedYn = post.getFixedYn();
        this.delYn = post.getDelYn();
        this.createDate = post.getCreateDate();
        this.updateDate = post.getUpdateDate();
        this.deleteDate = post.getDeleteDate();
    }
}
