package com.tactbug.mall.stock.aggregate.function;


@FunctionalInterface
public interface IterateChildren<P, T> {
    void accept(P parent, T target);
}
