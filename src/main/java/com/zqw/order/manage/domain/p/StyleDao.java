package com.zqw.order.manage.domain.p;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author: zhuquanwen
 * @date: 2017/11/10 17:12
 * @desc:
 */
public interface StyleDao extends JpaRepository<Style, Long>, JpaSpecificationExecutor<Style> {
    Style findByName(String name);
    List<Style> findAll();
}
