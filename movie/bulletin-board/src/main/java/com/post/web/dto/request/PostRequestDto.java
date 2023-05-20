package com.post.web.dto.request;

import lombok.Getter;
import lombok.Setter;

/**
 * @author      :   ymkim
 * @since       :   2023. 05. 20
 * @description :   게시글 조회시 사용되는 Dto
 */
@Getter
@Setter
public class PostRequestDto {
    private Long id;
    private String title;
    private String content;
    private int readCnt;
    private String fixedYn;
    private String delYn;
}
