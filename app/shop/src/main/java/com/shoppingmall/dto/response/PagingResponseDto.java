package com.shoppingmall.dto.response;

import com.shoppingmall.utils.PaginationUtils;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PagingResponseDto<T> {
    private List<T> data = new ArrayList<>();
    private PaginationUtils pagination;

    public PagingResponseDto(List<T> data, PaginationUtils pagination) {
        this.data.addAll(data);
        this.pagination = pagination;
    }
}
