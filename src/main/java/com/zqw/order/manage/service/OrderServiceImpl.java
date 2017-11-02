package com.zqw.order.manage.service;

import com.zqw.order.manage.domain.p.Order;
import com.zqw.order.manage.domain.p.OrderDao;
import com.zqw.order.manage.service.api.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.*;
import javax.transaction.Transactional;


import java.util.ArrayList;
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


    @Override
    public Page<Order> findByPageAndParams(Order order, Pageable pageable) {
        Specification<Order> spec = new Specification<Order>() {        //查询条件构造
            @Override
            public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
            Path<String> phoneNum = root.get("phoneNum");
            Path<String> courierNum = root.get("courierNum");
            Path<String> orderDate = root.get("orderDate");
            List<Predicate> pres = new ArrayList<Predicate>();
            if(!StringUtils.isEmpty(order.getPhoneNum())){
                Predicate p1 = cb.like(phoneNum, "%" + order.getPhoneNum() + "%");
                pres.add(p1);
            }
            if(!StringUtils.isEmpty(order.getCourierNum())){
                Predicate p2 = cb.like(courierNum, "%" + order.getCourierNum() + "%");
                pres.add(p2);
            }
            if(!StringUtils.isEmpty(order.getStartDate()) && !StringUtils.isEmpty(order.getEndDate())){
                Predicate p3 = cb.between(orderDate, order.getStartDate(),order.getEndDate());
                pres.add(p3);
            }else if(!StringUtils.isEmpty(order.getStartDate()) && StringUtils.isEmpty(order.getEndDate())){
                Predicate p3 = cb.greaterThan(orderDate,order.getStartDate());
                pres.add(p3);
            }else if(StringUtils.isEmpty(order.getStartDate()) && !StringUtils.isEmpty(order.getEndDate())){
                Predicate p3 = cb.lessThan(orderDate,order.getEndDate());
                pres.add(p3);
            }
            Predicate[] presArray =  pres.toArray(new Predicate[pres.size()]);
            Predicate p = cb.and(presArray);
            return p;
             }
        };
        return orderDao.findAll(spec, pageable);
    }
}
