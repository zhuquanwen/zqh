package com.zqw.order.manage.service.api;

import com.zqw.order.manage.domain.p.Order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.List;

public interface OrderService extends BaseService<Order> {
    Order save(Order order);
    List<Order> save(List<Order> orderList);
    void delete(Order order);
    void deleteInBatch(List<Order> orders);
    Page<Order> findUsePage(Pageable pageable);
    Order findOne(Long id);
    Page<Order> findByPageAndParams(Order order,Pageable pageable);
    List<Order> findByPhoneNum(String phoneNum);
}
