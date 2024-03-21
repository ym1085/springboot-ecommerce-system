package com.shoppingmall.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.shoppingmall.vo.Post;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostResponseDto {
    private Integer postId;
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
    private List<CommentResponseDto> comments = new ArrayList<>();
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<FileResponseDto> postFiles = new ArrayList<>();

    public static PostResponseDto toDto(Post post) {
        return PostResponseDto.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .content(post.getContent())
                .categoryId(post.getCategoryId())
                .categoryName(post.getCategoryName())
                .writer(post.getWriter())
                .readCnt(post.getReadCnt())
                .fixedYn(post.getFixedYn())
                .delYn(post.getDelYn())
                .createDate(post.getCreateDate())
                .updateDate(post.getUpdateDate())
                .deleteDate(post.getDeleteDate())
                .build();
    }

    public void addComments(List<CommentResponseDto> commentResponseDto) {
        this.comments = commentResponseDto;
    }

    public void addPostFiles(List<FileResponseDto> fileResponseDtos) {
        this.postFiles = fileResponseDtos;
    }
}
