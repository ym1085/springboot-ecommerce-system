package com.post.web.dto.request;

import com.post.utils.Pagination;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchRequestDto {
    private int pageNo;                 // 현재 페이지 번호
    private int recordSizePerPage;      // 페이징 당 출력할 게시글 개수
    private int pageSize;               // 화면 하단에 출력할 페이지 사이즈 ---> 5로 지정 -> 1 ~ 5까지, 10로 지정 -> 1 ~ 10 페이지 정보
    private String searchKeyword;       // 검색 키워드
    private String searchType;          // 검색 유형
    private Pagination pagination;      // 페이지네이션 정보

    private int categoryId;

    public SearchRequestDto() {
        this.pageNo = 1;
        this.recordSizePerPage = 10;
        this.pageSize = 10;
    }

    public int getOffset() {
        return (this.pageNo - 1) * recordSizePerPage;
    }
}
