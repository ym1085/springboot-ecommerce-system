package com.shoppingmall.utils;

import com.shoppingmall.ShopApplication;
import com.shoppingmall.dto.request.SearchRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest(classes = ShopApplication.class)
class PaginationUtilsTest {

    @Test
    // @WithMockUser(roles = "USER")
    @DisplayName("전체 페이지 수 계산 테스트 - 데이터 1000개")
    void testTotalPageCountPer1000() {
        SearchRequestDto searchRequestDto = new SearchRequestDto();
        searchRequestDto.setPageNo(1); // 페이지 번호
        searchRequestDto.setRecordSizePerPage(10); // 페이지 당 출력할 게시글 개수
        searchRequestDto.setPageSize(10); // 화면 하단에 출력할 게시글 개수

        PaginationUtils PaginationUtils = new PaginationUtils(1000, searchRequestDto);

        // ((전체 데이터 갯수 - 1) / 페이징 당 출력할 게시글 개수)) + 1
        // ((1000 - 1) / 10) + 1 => 100
        assertEquals(100, PaginationUtils.getTotalPageCount());
    }

    @Test
    // @WithMockUser(roles = "USER")
    @DisplayName("전체 페이지 수 계산 테스트 - 데이터 1242개")
    void testTotalPageCountPer1242() {
        SearchRequestDto searchRequestDto = new SearchRequestDto();
        searchRequestDto.setPageNo(1); // 페이지 번호
        searchRequestDto.setRecordSizePerPage(10); // 페이지 당 출력할 게시글 개수
        searchRequestDto.setPageSize(10); // 화면 하단에 출력할 게시글 개수

        PaginationUtils PaginationUtils = new PaginationUtils(1242, searchRequestDto);

        // ((전체 데이터 갯수 - 1) / 페이징 당 출력할 게시글 개수)) + 1
        // ((1242 - 1) / 10) + 1 => 124
        assertEquals(125, PaginationUtils.getTotalPageCount()); // 페이지 번호 갯수 1,2,3,4,5...
    }

    @Test
    // @WithMockUser(roles = "USER")
    @DisplayName("시작 번호 테스트 - 페이지 번호 1인 경우")
    void testStartPageByPageNo1() {
        SearchRequestDto searchRequestDto = new SearchRequestDto();
        searchRequestDto.setPageNo(1); // 페이지 번호
        searchRequestDto.setRecordSizePerPage(10); // 페이지 당 출력할 게시글 개수
        searchRequestDto.setPageSize(10); // 화면 하단에 출력할 게시글 개수

        PaginationUtils PaginationUtils = new PaginationUtils(1000, searchRequestDto);

        // ((시작 페이지 번호 - 1) / 화면 화단에 출력할 게시글 개수) * 화면 화단에 출력할 게시글 개수 + 1
        // ((1 - 1) / 10) * 10 + 1 => 1
        assertEquals(1, PaginationUtils.getStartPage());
    }

    @Test
    // @WithMockUser(roles = "USER")
    @DisplayName("시작 번호 테스트 - 페이지 번호 11인 경우")
    void testStartPageByPageNo11() {
        SearchRequestDto searchRequestDto = new SearchRequestDto();
        searchRequestDto.setPageNo(11); // 페이지 번호
        searchRequestDto.setRecordSizePerPage(10); // 페이지 당 출력할 게시글 개수
        searchRequestDto.setPageSize(10); // 화면 하단에 출력할 게시글 개수

        PaginationUtils PaginationUtils = new PaginationUtils(1000, searchRequestDto);

        // ((시작 페이지 번호 - 1) / 화면 화단에 출력할 게시글 개수) * 화면 화단에 출력할 게시글 개수 + 1
        // ((1 - 1) / 10) * 10 + 1 => 1
        assertEquals(11, PaginationUtils.getStartPage());
    }

    @Test
    // @WithMockUser(roles = "USER")
    @DisplayName("끝 번호 테스트 - 페이지 번호 1인 경우")
    void testEndPageByPageNo1() {
        SearchRequestDto searchRequestDto = new SearchRequestDto();
        searchRequestDto.setPageNo(1); // 페이지 번호
        searchRequestDto.setRecordSizePerPage(10); // 페이지 당 출력할 게시글 개수
        searchRequestDto.setPageSize(10); // 화면 하단에 출력할 게시글 개수

        PaginationUtils PaginationUtils = new PaginationUtils(1000, searchRequestDto);

        // (시작 페이지 + 화면 화단에 출력할 게시글 개수) - 1
        // (1 + 10) - 1 => 10
        assertEquals(10, PaginationUtils.getEndPage());
    }

    @Test
    // @WithMockUser(roles = "USER")
    @DisplayName("끝 번호 테스트 - 페이지 번호 11인 경우")
    void testEndPageByPageNo11() {
        SearchRequestDto searchRequestDto = new SearchRequestDto();
        searchRequestDto.setPageNo(11); // 페이지 번호
        searchRequestDto.setRecordSizePerPage(10); // 페이지 당 출력할 게시글 개수
        searchRequestDto.setPageSize(10); // 화면 하단에 출력할 게시글 개수

        PaginationUtils PaginationUtils = new PaginationUtils(1000, searchRequestDto);

        // (시작 페이지 + 화면 화단에 출력할 게시글 개수) - 1
        // (1 + 10) - 1 => 10
        assertEquals(20, PaginationUtils.getEndPage()); // 11 ~ 20
    }

    @Test
    // @WithMockUser(roles = "USER")
    @DisplayName("MySQL의 LIMIT 구문의 첫번째 인자로 사용될 Limit 테스트 - 페이지 번호 1인 경우")
    void testLimitStartByPageNo1() {
        SearchRequestDto searchRequestDto = new SearchRequestDto();
        searchRequestDto.setPageNo(1); // 페이지 번호
        searchRequestDto.setRecordSizePerPage(10); // 페이지 당 출력할 게시글 개수
        searchRequestDto.setPageSize(10); // 화면 하단에 출력할 게시글 개수

        PaginationUtils PaginationUtils = new PaginationUtils(1000, searchRequestDto);

        // (페이지번호 - 1) * 페이지 당 출력할 게시글 개수
        // (1 - 1) * 10 => 0(idx)
        // (2 - 1) * 10 => 10(idx)
        // (3 - 1) * 10 => 20(idx)
        assertEquals(0, PaginationUtils.getLimitStart());
    }

    @Test
    // @WithMockUser(roles = "USER")
    @DisplayName("MySQL의 LIMIT 구문의 첫번째 인자로 사용될 Limit 테스트 - 페이지 번호 3인 경우")
    void testLimitStartByPageNo3() {
        SearchRequestDto searchRequestDto = new SearchRequestDto();
        searchRequestDto.setPageNo(3); // 페이지 번호
        searchRequestDto.setRecordSizePerPage(10); // 페이지 당 출력할 게시글 개수
        searchRequestDto.setPageSize(10); // 화면 하단에 출력할 게시글 개수

        PaginationUtils PaginationUtils = new PaginationUtils(1000, searchRequestDto);

        // (페이지번호 - 1) * 페이지 당 출력할 게시글 개수
        // (1 - 1) * 10 => 0(idx)
        // (2 - 1) * 10 => 10(idx)
        // (3 - 1) * 10 => 20(idx)
        assertEquals(20, PaginationUtils.getLimitStart());
    }

    @Test
    // @WithMockUser(roles = "USER")
    @DisplayName("시작 페이지 테스트 - pageNo 16로 지정하여 테스트")
    void testPageSizeAndPageNo() {
        SearchRequestDto searchRequestDto = new SearchRequestDto();
        searchRequestDto.setPageNo(16); // 페이지 번호
        searchRequestDto.setRecordSizePerPage(10); // 페이지 당 출력할 게시글 개수
        searchRequestDto.setPageSize(15); // 화면 하단에 출력할 게시글 개수 ----> << < 1, 2, 3, 4, 5... > >>

        PaginationUtils PaginationUtils = new PaginationUtils(1000, searchRequestDto);

        // ((시작 페이지 번호 - 1) / 화면 화단에 출력할 게시글 개수) * 화면 화단에 출력할 게시글 개수 + 1
        // ((16 - 1) / 15) * 15 + 1 => 16
        assertEquals(16, PaginationUtils.getStartPage()); // 페이지 번호 16이 startPage 번호가 되야함
    }

    @Test
    // @WithMockUser(roles = "USER")
    @DisplayName("시작 페이지 테스트 - pageNo 9로 지정하여 테스트")
    void testPageSizeAndPageNo9() {
        SearchRequestDto searchRequestDto = new SearchRequestDto();
        searchRequestDto.setPageNo(16); // 페이지 번호
        searchRequestDto.setRecordSizePerPage(10); // 페이지 당 출력할 게시글 개수
        searchRequestDto.setPageSize(9); // 화면 하단에 출력할 게시글 개수 ----> UI 페이징 ---->   << < 1, 2, 3, 4, 5... > >>

        PaginationUtils PaginationUtils = new PaginationUtils(1000, searchRequestDto);

        // ((시작 페이지 번호 - 1) / 화면 화단에 출력할 게시글 개수) * 화면 화단에 출력할 게시글 개수 + 1
        // ((16 - 1) / 9) * 9 + 1 => 16
        assertEquals(10, PaginationUtils.getStartPage()); // 페이지 번호 10이 startPage 번호가 되야함
    }
}
