package com.zqw.order.manage.domain.p;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author: zhuquanwen
 * @date: 2017/11/9 10:06
 * @desc:
 */
public interface UrlPathDao extends JpaRepository<UrlPath, Long>, JpaSpecificationExecutor<UrlPath> {
}
