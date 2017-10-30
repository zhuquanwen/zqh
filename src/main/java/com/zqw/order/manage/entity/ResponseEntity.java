package com.zqw.order.manage.entity;

public class ResponseEntity<T> {
    private Integer status;
    private String info;
    private T data;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public ResponseEntity(Integer status, String info) {
        this.status = status;
        this.info = info;
    }
}
