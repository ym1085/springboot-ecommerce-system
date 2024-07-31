package com.shoppingmall.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class Post {
    private Integer postId;
    private Integer memberId;
    private String title;
    private String content;
    private Integer categoryId;
    private String categoryName;
    private String writer;
    private Integer readCnt;
    private String fixedYn;
    private String delYn;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime createDate;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime updateDate;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime deleteDate;

    // 게시글에 매핑된 파일 목록
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    List<PostFiles> postFiles = new ArrayList<>();

    // 게시글에 매핑된 댓글 목록
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    List<Comment> comments = new ArrayList<>();

    @Builder
    public Post(Integer postId, Integer memberId, String title, String content, Integer categoryId, String categoryName, String writer, Integer readCnt, String fixedYn, String delYn, LocalDateTime createDate, LocalDateTime updateDate, LocalDateTime deleteDate, List<PostFiles> postFiles, List<Comment> comments) {
        this.postId = postId;
        this.memberId = memberId;
        this.title = title;
        this.content = content;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.writer = writer;
        this.readCnt = readCnt;
        this.fixedYn = fixedYn;
        this.delYn = delYn;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.deleteDate = deleteDate;
        this.postFiles = postFiles;
        this.comments = comments;
    }
}
