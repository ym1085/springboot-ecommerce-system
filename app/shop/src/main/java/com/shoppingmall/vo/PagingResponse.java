package com.shoppingmall.vo;

import com.shoppingmall.utils.PaginationUtils;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PagingResponse<T> {
    private List<T> data = new ArrayList<>();
    private PaginationUtils pagination;

    public PagingResponse(List<T> data, PaginationUtils pagination) {
        this.data.addAll(data);
        this.pagination = pagination;
    }
}
