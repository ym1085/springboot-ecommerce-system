package com.shoppingmall.dto.request;

import com.shoppingmall.vo.CommentVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentSaveRequestDto {

    private Long postId;
    private Long memberId;
    private Long parentId;
    private Long commentId;
    private String content;

    public CommentVO toEntity() {
        return CommentVO.builder()
                .postId(postId)
                .memberId(memberId)
                .parentId(parentId)
                .commentId(commentId)
                .content(content)
                .build();
    }

}

