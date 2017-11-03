package com.zqw.order.manage.controller;

import com.zqw.order.manage.domain.p.Order;
import com.zqw.order.manage.entity.AjaxException;
import com.zqw.order.manage.entity.BasePageResult;
import com.zqw.order.manage.entity.PageException;
import com.zqw.order.manage.entity.ResponseEntity;
import com.zqw.order.manage.service.ExcelReadService;
import com.zqw.order.manage.service.api.OrderService;
import com.zqw.order.manage.util.EncodeUtils;
import com.zqw.order.manage.util.JacksonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@SuppressWarnings("Duplicates")
@Controller
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private ExcelReadService excelReadService;
    @Value("${usr_pwd}")
    private String usrPwd;
    private Map<String,Map<String,String>> usrPwdMap = new HashMap<String,Map<String,String>>();

    private static String encodeKey = "zhuquanhong";
    private static String USER_SESSION = "USER_SESSION";

    @RequestMapping("/turnIndex")
    public ModelAndView turnIndex(HttpSession session,String username, String password){
        ModelAndView mav = new ModelAndView();
        try{
            if(usrPwdMap.size() == 0){
                String[] strs = usrPwd.split(";");
                for (String str: strs) {
                    Map<String,String> map = new HashMap<String, String>();
                    map.put("username",str.split(",")[0]);
                    map.put("password",str.split(",")[1]);
                    usrPwdMap.put(str.split(",")[0], map);
                }
            }
            if(usrPwdMap.get(username) == null){
                mav.setViewName("login");
                mav.addObject("没有此用户!");
            }else{
                Map<String, String> map = usrPwdMap.get(username);
                String pwd = map.get("password");
                if(! pwd.equals(password)){
                    mav.setViewName("login");
                    mav.addObject("密码错误!");
                }else{
                    //记录session
                    session.setAttribute(USER_SESSION, EncodeUtils.aesEncrypt(username,encodeKey));
                    mav.setViewName("redirect:/index");
                    mav.addObject("登录出错!");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            mav.setViewName("login");
            mav.addObject("登录出错!");
        }
        return mav;
    }

    @RequestMapping(value ="/login")
    public String login() throws PageException{
        return "login";
    }

    @RequestMapping(value ="/index")
    public String index(HttpSession session) throws PageException{
        try{
            String desUsername = (String) session.getAttribute(USER_SESSION);
            if(desUsername == null){
                return "redirect:login";
            }
            String username = EncodeUtils.aesDecrypt(desUsername, encodeKey);
            if(username == null){
                return "redirect:login";
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new PageException();
        }
        return "indexPage";
    }


    //处理文件上传
    @RequestMapping(value="/importOrder", method = RequestMethod.POST)
    public @ResponseBody Map<String,Object> uploadImg(HttpServletRequest request) {
        Map<String, Object> json = new HashMap<String, Object>();


        try{
            request.setCharacterEncoding("UTF-8");
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

            /** 页面控件的文件流* */
            MultipartFile multipartFile = null;
            Map map =multipartRequest.getFileMap();
            for (Iterator i = map.keySet().iterator(); i.hasNext();) {
                Object obj = i.next();
                multipartFile=(MultipartFile) map.get(obj);
//                InputStream inputStream;
//                String path = "";
//                String newVersionName = "";
//                String fileMd5 = "";
//
//                    String filename = multipartFile.getOriginalFilename();
//                    inputStream = multipartFile.getInputStream();
//                    File tmpFile = File.createTempFile(filename,
//                            filename.substring(filename.lastIndexOf(".")));
////                    fileMd5 = Files.hash(tmpFile, Hashing.md5()).toString();
//                    FileUtils.copyInputStreamToFile(inputStream, tmpFile);
////                    FileUpload.
//
//                    tmpFile.delete();

                List<Order> orderList = excelReadService.readExcel(multipartFile);
//                tmpFile.delete();
                orderService.save(orderList);

            }
            json.put("newVersionName", null);
            json.put("fileMd5", null);
            json.put("message", "导入成功");
            json.put("status", true);
            json.put("filePath", null);
            //返回json
        }catch (Exception e){
            e.printStackTrace();
            json.put("newVersionName", null);
            json.put("fileMd5", null);
            json.put("message", "导入失败,请确保EXCEL数据格式正确，且单号与已录入的单号不重复");
            json.put("status", false);
            json.put("filePath", null);
        }

        return json;
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

    @RequestMapping(value = "/order/deleteInBatch", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity deleteOrderInBatch(@RequestParam String orderList){
        ResponseEntity re = new ResponseEntity(HttpStatus.OK.value(),"操作成功");
        try{
            List<Order> orders = JacksonUtils.parseList(orderList, Order.class);
//            String[] strs = ids.split(",");
//            List<Order> orders = new ArrayList<Order>();
//            List<String> idList = Arrays.asList(strs);
//            idList.forEach(id -> {
//                Order order = new Order();
//                order.setId(Long.parseLong(id));
//                orders.add(order);
//            });
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
    public @ResponseBody BasePageResult<Order> getOrder(Order order) throws AjaxException{
        BasePageResult bpr = new BasePageResult();
        try {
//        ResponseEntity re = new ResponseEntity(HttpStatus.OK.value(),"操作成功");

//        try{
//            Order order = JacksonUtils.parse(data, Order.class);
            if (order.getDirection() == null) {
                order.setDirection("asc");
            }
            if (order.getField() == null) {
                order.setField("id");
            }
            Sort sort = new Sort(order.getDirection(), order.getField());
            Pageable pageable = new PageRequest(order.getPage(), order.getSize(), sort);
//            Page<Order> page = orderService.findUsePage(pageable);
            Page<Order> page = orderService.findByPageAndParams(order, pageable);
            bpr.setResult(page.getContent());
            bpr.setTotalCount(page.getTotalElements());
//        }catch (Exception e){
//            e.printStackTrace();
////            re.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
////            re.setInfo("操作出错");
//        }
        }catch (Exception e){
            e.printStackTrace();
            throw new AjaxException();
        }
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
