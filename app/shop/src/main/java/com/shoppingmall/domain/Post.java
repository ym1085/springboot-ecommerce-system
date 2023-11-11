package com.shoppingmall.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.shoppingmall.dto.request.PostRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
@Setter
@NoArgsConstructor // https://akku-dev.tistory.com/34 | https://okky.kr/questions/848320 | https://okky.kr/questions/211064
public class Post {
    private Long postId;
    private Long memberId;
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
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime deleteDate;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    List<PostFiles> postFiles = new ArrayList<>();

    public Post(PostRequestDto postRequestDto) {
        this.postId = postRequestDto.getPostId();
        this.memberId = postRequestDto.getMemberId();
        this.title = postRequestDto.getTitle();
        this.content = postRequestDto.getContent();
        this.categoryId = postRequestDto.getCategoryId();
        this.fixedYn = postRequestDto.getFixedYn();
    }
}
