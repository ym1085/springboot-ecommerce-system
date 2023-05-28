package com.post.dto.resposne;

import com.post.utils.Pagination;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PagingResponseDto<T> {
    private List<T> result = new ArrayList<>();
    private Pagination pagination;

    public PagingResponseDto(List<T> result, Pagination pagination) {
        this.result.addAll(result);
        this.pagination = pagination;
    }
}
