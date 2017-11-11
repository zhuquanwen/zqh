package com.zqw.order.manage.domain.p;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author: zhuquanwen
 * @date: 2017/11/10 17:06
 * @desc:
 */
public interface GoodsDao extends JpaRepository<Goods, Long>, JpaSpecificationExecutor<Goods> {
    Goods findByName(String name);
    List<Goods> findAll();
}
