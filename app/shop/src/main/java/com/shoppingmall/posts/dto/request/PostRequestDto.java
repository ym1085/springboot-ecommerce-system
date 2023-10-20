package com.shoppingmall.posts.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * @author      :   ymkim
 * @since       :   2023. 05. 20
 * @description :   게시글 조회, 수정, 삭제에 사용되는 DTO
 */

@NoArgsConstructor
@Getter
@Setter // setter 지우자..
public class PostRequestDto {
    private Long postId;
    private Long memberId;

    @NotEmpty(message = "제목은 반드시 입력되어야 합니다. 다시 한번 시도해주세요.")
    @Size(max = 20, message = "제목은 20자를 초과할 수 없습니다. 다시 한번 시도해주세요.")
    private String title;

    @NotEmpty(message = "내용은 반드시 입력되어야 합니다. 다시 한번 시도해주세요.")
    @Size(max = 250, message = "내용은 250자를 초과할 수 없습니다. 다시 한번 시도해주세요.")
    private String content;

    private String fixedYn;

    private List<MultipartFile> files = new ArrayList<>();

    private int categoryId;

    @Builder
    public PostRequestDto(Long postId, Long memberId, String title, String content, String fixedYn, int categoryId) {
        this.postId = postId;
        this.memberId = memberId;
        this.title = title;
        this.content = content;
        this.fixedYn = fixedYn;
        this.categoryId = categoryId;
    }
}
