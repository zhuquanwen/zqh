package com.zqw.order.manage.service;

import com.zqw.order.manage.domain.p.Order;
import com.zqw.order.manage.domain.p.OrderDao;
import com.zqw.order.manage.service.api.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


import java.util.List;
@Service
public class OrderServiceImpl implements OrderService{
    @Autowired
    private OrderDao orderDao;


    @Transactional
    @Override
    public Order save(Order order) {
        return orderDao.save(order);

    }
    @Transactional
    @Override
    public List<Order> save(List<Order> orderList) {
        return orderDao.save(orderList);
    }

    @Override
    public void delete(Order order) {
        orderDao.delete(order);
    }
    @Override
    public void deleteInBatch(List<Order> orders) {
        orderDao.deleteInBatch(orders);
    }

    @Override
    public Page<Order> findUsePage(Pageable pageable) {
        Page<Order> page = orderDao.findAll(pageable);
        return page;
    }

    @Override
    public Order findOne(Long id) {
        return orderDao.findOne(id);
    }
}
