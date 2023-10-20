package com.shoppingmall.dto.response;

import com.shoppingmall.common.utils.PaginationUtils;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PagingResponseDto<T> {
    private List<T> result = new ArrayList<>();
    private PaginationUtils pagination;

    public PagingResponseDto(List<T> result, PaginationUtils pagination) {
        this.result.addAll(result);
        this.pagination = pagination;
    }
}
