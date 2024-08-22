package com.shoppingmall.dto.request;

import com.shoppingmall.utils.PaginationUtils;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PagingRequestDto {
    private int pageNo;                     // 현재 페이지 번호
    private int recordSizePerPage;          // 페이징 당 출력할 게시글 개수
    private int pageSize;                   // 화면 하단에 출력할 페이지 사이즈 ---> 5로 지정 -> 1 ~ 5까지, 10로 지정 -> 1 ~ 10 페이지 정보
    private PaginationUtils pagination;     // 페이지네이션 정보

    private int categoryId;

    public PagingRequestDto() {
        this.pageNo = 1;
        this.recordSizePerPage = 10;
        this.pageSize = 10;
    }

    public int getOffset() {
        return (this.pageNo - 1) * recordSizePerPage;
    }
}
