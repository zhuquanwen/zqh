package com.zqw.order.manage.controller;

import com.zqw.order.manage.domain.p.Order;
import com.zqw.order.manage.entity.BasePageResult;
import com.zqw.order.manage.entity.ResponseEntity;
import com.zqw.order.manage.service.ExcelReadService;
import com.zqw.order.manage.service.api.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@SuppressWarnings("Duplicates")
@Controller
public class OrderController {
    @Autowired
    private OrderService orderService;

    @RequestMapping(value ="/index")
    public String index(){
        return "indexPage";
    }


    //处理文件上传
    @RequestMapping(value="/importOrder", method = RequestMethod.POST)
    public @ResponseBody String uploadImg(HttpServletRequest request) throws Exception{
        request.setCharacterEncoding("UTF-8");

        Map<String, Object> json = new HashMap<String, Object>();
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

        /** 页面控件的文件流* */
        MultipartFile multipartFile = null;
        Map map =multipartRequest.getFileMap();
        for (Iterator i = map.keySet().iterator(); i.hasNext();) {
            Object obj = i.next();
            multipartFile=(MultipartFile) map.get(obj);
            /** 获取文件的后缀* */
            String filename = multipartFile.getOriginalFilename();
            System.out.println(filename);
        }

        //返回json
        return "uploadimg success";
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
//            Page<Order> page = orderService.findUsePage(pageable);
            Page<Order> page = orderService.findByPageAndParams(order,pageable);
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
