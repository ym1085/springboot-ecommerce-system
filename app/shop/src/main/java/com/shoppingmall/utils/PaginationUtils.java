package com.shoppingmall.utils;

import com.shoppingmall.dto.request.SearchRequestDto;
import lombok.Getter;

@Getter
public class PaginationUtils {
    private int totalRecordCount;       // 전체 데이터 갯수 -> COUNT(*) 쿼리의 결과
    private int totalPageCount;         // 페이지 하단에 출력할 전체 페이지 수 -> << < 1, 2, 3, 4, 5.. > >>
    private int startPage;              // 첫 페이지 번호
    private int endPage;                // 끝 페이지 번호
    private int limitStart;             // LIMIT 시작 위치
    private boolean existsPrevPage;     // 이전 페이지 존재 여부
    private boolean existsNextPage;     // 다음 페이지 존재 여부

    public PaginationUtils(int totalRecordCount, SearchRequestDto searchRequestDto) {
        this.totalRecordCount = totalRecordCount;
        if (totalRecordCount > 0) {
            calculation(searchRequestDto);
        }
    }

    /**
     * 페이지 정보 셋팅
     * @param searchRequestDto
     * page             :   현재 페이지 번호
     * recordSize       :   페이징 당 출력할 게시글 개수
     * pageSize         :   화면 하단에 출력할 페이지 사이즈, 5로 지정 -> 1 ~ 5까지, 10로 지정 -> 1 ~ 10 페이지 정보
     * searchKeyword    :   검색 키워드
     * searchType       :   검색 유형
     * pagination       :   페이지네이션 정보
     */
    public void calculation(SearchRequestDto searchRequestDto) {
        setTotalPageCount(searchRequestDto); // [1], [2], [3], [4], .... 설정
        if (searchRequestDto.getPageNo() > this.totalPageCount) {
            searchRequestDto.setPageNo(this.totalPageCount);
        }
        setStartPage(searchRequestDto);
        setEndPage(searchRequestDto);

        if (this.endPage > this.totalPageCount) {
            this.endPage = this.totalPageCount;
        }
        setLimitStart(searchRequestDto); // LIMIT 시작 위치 계산
        checkExistsPrevPage(); // 이전 페이지 존재 여부 확인
        checkExistsNextPage(searchRequestDto); // 다음 페이지 존재 여부 확인
    }

    // 전체 페이지 수 계산 -> ((전체 데이터 갯수 - 1) / 페이징 당 출력할 게시글 개수)) + 1
    // * ((50 - 1) / 10) + 1
    public void setTotalPageCount(SearchRequestDto searchRequestDto) {
        this.totalPageCount = ((this.totalRecordCount - 1) / searchRequestDto.getRecordSizePerPage()) + 1;
    }

    // 시작 페이지 번호 셋팅
    // ...... 1페이지
    // ((1 - 1) / 10) * 10 + 1) => 1
    // ((2 - 1) / 10) * 10 + 1) => 1
    // ((3 - 1) / 10) * 10 + 1) => 1
    // ((4 - 1) / 10) * 10 + 1) => 1
    // ((10 - 1) / 10) * 10 + 1) => 1
    // ...... 2페이지
    // ((11 - 1) / 10) * 10 + 1) => 11
    public void setStartPage(SearchRequestDto searchRequestDto) {
        this.startPage = ((searchRequestDto.getPageNo() - 1) / searchRequestDto.getPageSize()) * searchRequestDto.getPageSize() + 1;
    }

    // 끝 페이지 번호 셋팅
    // ...... 1페이지
    // (1 + 10) - 1) => 10
    // (2 + 10) - 1) => 11
    // (3 + 10) - 1) => 12
    // (4 + 10) - 1) => 13
    // (10 + 10) - 1) => 19
    // ...... 2페이지
    // (11 + 10) - 1) => 20
    public void setEndPage(SearchRequestDto searchRequestDto) {
        this.endPage = (this.startPage + searchRequestDto.getPageSize()) - 1;
    }

    // LIMIT 시작 위치 계산
    // (1 - 1) * 10) => 0(offset)
    // (2 - 1) * 10) => 10(offset)
    // (3 - 1) * 10) => 20(offset)
    // (4 - 1) * 10) => 0
    public void setLimitStart(SearchRequestDto searchRequestDto) {
        this.limitStart = (searchRequestDto.getPageNo() - 1) * searchRequestDto.getRecordSizePerPage();
    }

    // 이전 페이지 존재 여부 확인
    public void checkExistsPrevPage() {
        this.existsPrevPage = this.startPage != 1;
    }

    // 다음 페이지 존재 여부 확인
    public void checkExistsNextPage(SearchRequestDto searchRequestDto) {
        this.existsNextPage = (this.endPage * searchRequestDto.getRecordSizePerPage()) < this.totalRecordCount;
    }
}
