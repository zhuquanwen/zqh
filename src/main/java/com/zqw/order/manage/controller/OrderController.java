package com.zqw.order.manage.controller;

import com.zqw.order.manage.domain.p.Order;
import com.zqw.order.manage.entity.ResponseEntity;
import com.zqw.order.manage.service.api.OrderService;
import com.zqw.order.manage.util.JacksonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("Duplicates")
@RestController
public class OrderController {
    @Autowired
    private OrderService orderService;
    @RequestMapping(value = "/order", method = RequestMethod.PUT,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity saveOrder( @RequestBody  String data){
        ResponseEntity re = new ResponseEntity(HttpStatus.OK.value(),"操作成功");
        try{
            List<Order> orders = JacksonUtils.parseList(data, Order.class);
            orders = orderService.save(orders);
            re.setData(orders);
        }catch (Exception e){
            e.printStackTrace();
            re.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            re.setInfo("操作出错");
        }
        return re;
    }
    @RequestMapping(value = "/order", method = RequestMethod.DELETE)
    public ResponseEntity deleteOrder(@RequestParam String ids){
        ResponseEntity re = new ResponseEntity(HttpStatus.OK.value(),"操作成功");
        try{
            String[] strs = ids.split(",");
            List<Order> orders = new ArrayList<Order>();
            List<String> idList = Arrays.asList(strs);
            idList.forEach(id -> {
                Order order = new Order();
                order.setId(Integer.parseInt(id));
                orders.add(order);
            });
            orderService.deleteInBatch(orders);
            re.setData(null);
        }catch (Exception e){
            e.printStackTrace();
            re.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            re.setInfo("操作出错");
        }
        return re;
    }
    @RequestMapping(value = "/order", method = RequestMethod.POST)
    public ResponseEntity getOrder(@RequestBody String data){
        ResponseEntity re = new ResponseEntity(HttpStatus.OK.value(),"操作成功");
        try{
            Order order = JacksonUtils.parse(data, Order.class);
            Sort sort = new Sort(order.getDirection(), order.getField());
            Pageable pageable = new PageRequest(order.getPage(), order.getSize(), sort);
            Page<Order> page = orderService.findUsePage(pageable);
            re.setData(page);
        }catch (Exception e){
            e.printStackTrace();
            re.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            re.setInfo("操作出错");
        }
        return re;
    }
    @RequestMapping(value = "/order/{id}", method = RequestMethod.POST)
    public ResponseEntity getOrderById(@PathVariable Integer id){
        ResponseEntity re = new ResponseEntity(HttpStatus.OK.value(),"操作成功");
        try{
           Order order = orderService.findOne(id);
           re.setData(order);
        }catch (Exception e){
            e.printStackTrace();
            re.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            re.setInfo("操作出错");
        }
        return re;
    }

}
