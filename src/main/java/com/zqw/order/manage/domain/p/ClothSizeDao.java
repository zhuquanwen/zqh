package com.zqw.order.manage.domain.p;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author: zhuquanwen
 * @date: 2017/11/10 17:10
 * @desc:
 */
public interface ClothSizeDao extends JpaRepository<ClothSize, Long>, JpaSpecificationExecutor<ClothSize> {
    ClothSize findByName(String name);
}
