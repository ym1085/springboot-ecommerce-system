package com.shoppingmall.dto.response;

import com.shoppingmall.vo.Post;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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

    private List<CommentResponseDto> comments = new ArrayList<>();
    private List<PostFileResponseDto> postFiles = new ArrayList<>();

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

    public void addPostFiles(List<PostFileResponseDto> postFileResponseDto) {
        this.postFiles = postFileResponseDto;
    }
}
