package com.zqw.order.manage.service.api;

import com.zqw.order.manage.domain.p.Goods;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GoodsService extends BaseService<Goods>{
    Page<Goods> findByPageAndParams(Goods goods, Pageable pageable);
    Goods save(Goods goods);
    Goods findByName(String name);
    List<Goods> findAll();
}
