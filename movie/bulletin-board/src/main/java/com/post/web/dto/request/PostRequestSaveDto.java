package com.post.web.dto.request;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * @author      :   ymkim
 * @since       :   2023. 05. 20
 * @description :   게시글 조회시 사용되는 Dto
 */
@Getter
public class PostRequestSaveDto {
    private Long postId;
    private Long memberId = 1L; //FIXME TEST용으로 1L 회원 번호 넣어놨음, 나중에 지워주세요

    @NotEmpty
    @Size(max = 20)
    private String title;

    @NotEmpty
    @Size(max = 250)
    private String content;

    @NotEmpty
    private String fixedYn;

    private List<MultipartFile> files = new ArrayList<>();

    @Builder
    public PostRequestSaveDto(Long postId, Long memberId, String title, String content, String fixedYn) {
        this.postId = postId;
        this.memberId = memberId;
        this.title = title;
        this.content = content;
        this.fixedYn = fixedYn;
    }
}
