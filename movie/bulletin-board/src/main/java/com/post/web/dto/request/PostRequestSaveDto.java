package com.post.web.dto.request;

import lombok.Getter;
import lombok.Setter;
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
@Setter
public class PostRequestSaveDto {
    private Long postId;
    private Long memberId;

    @NotEmpty
    @Size(max = 20)
    private String title;

    @NotEmpty
    @Size(max = 250)
    private String content;

    @NotEmpty
    private String fixedYn;

    private List<MultipartFile> files = new ArrayList<>();
}
