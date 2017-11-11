package com.zqw.order.manage.service.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BaseService<T> {
    T save(T t);
    List<T> save(List<T> t);
    void delete(T t);
    void deleteInBatch(List<T> ts);
    Page<T> findUsePage(Pageable pageable);
    T findOne(Long id);

}
