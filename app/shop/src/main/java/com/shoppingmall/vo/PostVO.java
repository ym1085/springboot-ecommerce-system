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
public class PostVO {
    private Long postId;
    private Long memberId;
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
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private LocalDateTime deleteDate;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    List<PostFilesVO> postFiles = new ArrayList<>();
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    List<CommentVO> comments = new ArrayList<>();

    @Builder
    public PostVO(Long postId, Long memberId, String title, String content, int categoryId,
                  String categoryName, String writer, int readCnt, String fixedYn, String delYn,
                  LocalDateTime createDate, LocalDateTime updateDate, LocalDateTime deleteDate,
                  List<PostFilesVO> postFiles, List<CommentVO> comments) {
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
