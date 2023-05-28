package com.post.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class CommentRequestDto {
    private Long postId;
    private Long memberId; // security에서 값 받아서 셋팅 할거임

    @NotNull(message = "댓글 번호는 필수 입력 값입니다. 다시 한번 시도해주세요.")
    private Long parentId;

    @NotEmpty(message = "댓글 내용은 반드시 입력되어야 합니다. 다시 한번 시도해주세요.")
    @Size(max = 50, message = "댓글 내용은 50자를 초과할 수 없습니다. 다시 한번 시도해주세요.")
    private String content;

    @Builder
    public CommentRequestDto(Long postId, Long memberId, Long parentId, String content) {
        this.postId = postId;
        this.memberId = memberId;
        this.parentId = parentId;
        this.content = content;
    }
}
