package com.zqw.order.manage.domain.p;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author: zhuquanwen
 * @date: 2017/10/30 17:20
 * @desc:
 */
public interface OrderDao extends JpaRepository<Order,Long>, JpaSpecificationExecutor<Order> {
    List<Order> findByPhoneNum(String phoneNum);

}
