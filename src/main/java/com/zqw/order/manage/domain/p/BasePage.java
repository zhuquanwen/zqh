package com.zqw.order.manage.domain.p;


import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

import javax.persistence.Transient;
import java.io.Serializable;

public class BasePage implements Serializable {
    @Transient
    private Integer page;
    @Transient
    private Integer size;

    @Transient
    private Sort.Direction direction;

    @Transient
    private String field;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }



    public String getField() {
        return field;
    }

    public void setField(String field) {
        if(StringUtils.isEmpty(field)){
            this.field = "_id";
        }else{
            this.field = field;
        }

    }

    public Sort.Direction getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        if(direction == null){
            direction = "desc";
        }

        if("desc".equals(direction)){
            this.direction = Sort.Direction.DESC;
        }else {
            this.direction = Sort.Direction.ASC;
        }

    }
}
