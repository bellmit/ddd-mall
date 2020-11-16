package com.tactbug.mall.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PageResult<T> {
    private List<T> list;
    private Integer pageNumber;
    private Integer pageSize;
    private Long total;
    private Integer totalPages;
}
