package com.shoppingmall.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchRequestDto extends PagingRequestDto {
    private String searchKeyword;           // 검색 키워드
    private String searchType;              // 검색 유형
}
