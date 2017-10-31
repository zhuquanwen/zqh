package com.zqw.order.manage.entity;

/**
 * @author: zhuquanwen
 * @date: 2017/10/31 16:51
 * @desc:
 */
public class BasePageResult<T> {
    private T result;
    private Long totalCount;

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }
}
