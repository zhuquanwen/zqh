package com.zqw.order.manage.domain.p;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author: zhuquanwen
 * @date: 2017/11/9 11:18
 * @desc:
 */
public interface ShoppingDao extends JpaRepository<Shopping, Long>, JpaSpecificationExecutor<Shopping> {
}
