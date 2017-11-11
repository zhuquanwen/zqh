package com.zqw.order.manage.service.api;

import com.zqw.order.manage.domain.p.Stock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StockService extends BaseService<Stock> {
    Page<Stock> findByPageAndParams(Stock stock, Pageable pageable);
}
