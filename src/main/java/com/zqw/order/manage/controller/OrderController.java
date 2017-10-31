package com.zqw.order.manage.controller;

import com.zqw.order.manage.domain.p.Order;
import com.zqw.order.manage.entity.BasePageResult;
import com.zqw.order.manage.entity.ResponseEntity;
import com.zqw.order.manage.service.api.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressWarnings("Duplicates")
@Controller
public class OrderController {
    @Autowired
    private OrderService orderService;

    @RequestMapping(value ="/index")
    public String index(){
        return "indexPage";
    }

    @RequestMapping(value = "/order/save", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity saveOrder( Order  order){
        ResponseEntity re = new ResponseEntity(HttpStatus.OK.value(),"操作成功");
        try{
//            List<Order> orders = JacksonUtils.parseList(data, Order.class);
            Date date = new Date();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String recordDate = df.format(date);

            order.setRecordDate(recordDate);
            order = orderService.save(order);
            re.setData(order);
        }catch (Exception e){
            e.printStackTrace();
            re.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            re.setInfo("操作出错");
        }
        return re;
    }
    @RequestMapping(value = "/order/delete/{id}", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity deleteOrder(@PathVariable Long id){
        ResponseEntity re = new ResponseEntity(HttpStatus.OK.value(),"操作成功");
        try{
//            String[] strs = ids.split(",");
//            List<Order> orders = new ArrayList<Order>();
//            List<String> idList = Arrays.asList(strs);
//            idList.forEach(id -> {
//                Order order = new Order();
//                order.setId(Long.parseLong(id));
//                orders.add(order);
//            });
//            orderService.deleteInBatch(orders);
            Order order = new Order();
            order.setId(id);
            orderService.delete(order);
            re.setData(null);
        }catch (Exception e){
            e.printStackTrace();
            re.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            re.setInfo("操作出错");
        }
        return re;
    }
    @RequestMapping(value = "/order", method = RequestMethod.POST)
    public @ResponseBody BasePageResult<Order> getOrder(Order order){
//        ResponseEntity re = new ResponseEntity(HttpStatus.OK.value(),"操作成功");
        BasePageResult bpr = new BasePageResult();
//        try{
//            Order order = JacksonUtils.parse(data, Order.class);
            if(order.getDirection() == null){
                order.setDirection("asc");
            }
            if(order.getField() == null){
                order.setField("id");
            }
            Sort sort = new Sort(order.getDirection(), order.getField());
            Pageable pageable = new PageRequest(order.getPage(), order.getSize(), sort);
            Page<Order> page = orderService.findUsePage(pageable);
           bpr.setResult(page.getContent());
           bpr.setTotalCount(page.getTotalElements());
//        }catch (Exception e){
//            e.printStackTrace();
////            re.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
////            re.setInfo("操作出错");
//        }
        return bpr;
    }
    @RequestMapping(value = "/order/{id}", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity getOrderById(@PathVariable Long id){
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
