package com.shoppingmall.vo.response;

import com.shoppingmall.utils.PaginationUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PagingResponse<T> {
    private List<T> data = new ArrayList<>();
    private PaginationUtils pagination;

    public PagingResponse(List<T> data, PaginationUtils pagination) {
        this.data.addAll(data);
        this.pagination = pagination;
    }
}
