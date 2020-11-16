package com.tactbug.mall.common.vo;

import lombok.Getter;

@Getter
public class PageArguments {
    private final Integer pageNumber;
    private final Integer pageSize;

    public PageArguments(Integer pageNumber, Integer pageSize){
        if (null == pageNumber || pageNumber <= 0){
            this.pageNumber = 1;
        }else {
            this.pageNumber = pageNumber;
        }
        if (null == pageSize || pageSize <= 0){
            this.pageSize = 10;
        }else {
            this.pageSize = pageSize;
        }
    }
}
