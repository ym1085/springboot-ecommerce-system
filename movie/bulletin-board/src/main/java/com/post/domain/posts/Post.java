package com.post.domain.posts;

import com.post.web.dto.request.PostRequestSaveDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Getter
@Setter
@NoArgsConstructor // https://akku-dev.tistory.com/34
public class Post {
    private Long postId;
    private Long memberId;
    private String title;
    private String content;
    private int readCnt;
    private String fixedYn;
    private String delYn;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private LocalDateTime deleteDate;

    /* 글 등록 시 Dto -> VO */
    public Post(PostRequestSaveDto postRequestSaveDto) {
        this.memberId = postRequestSaveDto.getMemberId(); // FIXME 나중에는 진짜 회원 번호 가져와서 셋팅
        this.title = postRequestSaveDto.getTitle();
        this.content = postRequestSaveDto.getContent();
        this.fixedYn = postRequestSaveDto.getFixedYn();
    }
}
