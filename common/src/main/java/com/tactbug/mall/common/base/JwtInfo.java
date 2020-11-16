package com.tactbug.mall.common.base;

/**
 * packageName：com.tactbug.mall.common.enums
 * ClassName  ：JwtEnum
 * description：
 * createDate ：2020/7/15 8:17
 * creator    ：tactBug
 */
public interface JwtInfo {
    String getSubject();
    String getSign();
    Long getValidMillis();
}
