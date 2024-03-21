package com.shoppingmall.dto.request;

import com.shoppingmall.vo.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentSaveRequestDto {

    private Integer postId;
    private Integer memberId;
    private Integer parentId;
    private Integer commentId;
    private String content;

    public Comment toEntity() {
        return Comment.builder()
                .postId(postId)
                .memberId(memberId)
                .parentId(parentId)
                .commentId(commentId)
                .content(content)
                .build();
    }

}

