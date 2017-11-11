package com.zqw.order.manage.service.api;

import com.zqw.order.manage.domain.p.Style;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StyleService extends BaseService<Style> {
    Page<Style> findByPageAndParams(Style style, Pageable pageable);
}
