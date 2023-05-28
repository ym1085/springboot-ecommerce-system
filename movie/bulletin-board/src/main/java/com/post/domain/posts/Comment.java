package com.post.domain.posts;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class Comment {
    private Long commentId;
    private Long parentId;
    private Long postId;
    private String content;
    private Long memberId;
    private String delYn;
    private String path;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    /*public Comment(CommentRequestDto commentRequestDto) {

    }*/
}
