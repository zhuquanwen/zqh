package com.zqw.order.manage.controller;

import com.zqw.order.manage.domain.p.Order;
import com.zqw.order.manage.domain.p.Stock;
import com.zqw.order.manage.entity.AjaxException;
import com.zqw.order.manage.entity.BasePageResult;
import com.zqw.order.manage.entity.PageException;
import com.zqw.order.manage.entity.ResponseEntity;
import com.zqw.order.manage.service.ExcelExportService;
import com.zqw.order.manage.service.ExcelReadService;
import com.zqw.order.manage.service.SpecialServiceImpl;
import com.zqw.order.manage.service.api.*;
import com.zqw.order.manage.util.EncodeUtils;
import com.zqw.order.manage.util.JacksonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@SuppressWarnings("Duplicates")
@Controller
public class OrderController extends BaseController {
    @Autowired
    private StockService stockService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ClothSizeService clothSizeService;
    @Autowired
    private StyleService styleService;
    @Autowired
    private ExcelReadService excelReadService;
    @Autowired
    private SpreadService spreadService;
    @Autowired
    private ExcelExportService excelExportService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private SpecialServiceImpl specialServiceImpl;

    @Value("${usr_pwd}")
    private String usrPwd;
    @Value("${cookie_timeout}")
    private int cookieTimeout;
    private Map<String,Map<String,String>> usrPwdMap = new HashMap<String,Map<String,String>>();


    @RequestMapping("/turnIndex")
    public ModelAndView turnIndex(HttpSession session,String username, String password){
        ModelAndView mav = new ModelAndView();
        String message = "0";
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
                message= "1";
                message = "用户不存在";
                mav.setViewName("login");
            }else{
                Map<String, String> map = usrPwdMap.get(username);
                String pwd = map.get("password");
                if(! pwd.equals(password)){
                    message= "2";
                    message = "密码错误";
                    mav.setViewName("login");
                }else{
                    //记录session
                    session.setAttribute(USER_SESSION, EncodeUtils.aesEncrypt(username,encodeKey));

                    //跳转到刚才点击的页面上去
                    String uri = (String) session.getAttribute(TARGET_URL);
                    if(uri == null){
                        mav.setViewName("redirect:/order");
                    }else{
                        mav.setViewName("redirect:" + uri);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            message= "3";
            message = "登录出错";
            mav.setViewName("login");

        }
        session.setAttribute(LOGIN_MESSAGE, message);
        return mav;
    }

    @RequestMapping(value ="/login")
    public String login() throws PageException{
        return "login";
    }

    @RequestMapping(value="/search")
    public String search() throws PageException{
        return "getCourier";
    }

    @RequestMapping(value ="/order")
    public ModelAndView index(HttpSession session,HttpServletRequest request) throws PageException{
        ModelAndView mav = new ModelAndView();
        mav.setViewName(this.validateSession(request,session,"orderPage", clothSizeService, styleService));
        mav.addObject(HIDDEN_FLAG, "order");
        return mav;

    }

    @RequestMapping(value="/orderExport",method = RequestMethod.GET)
    public void  orderExport1(HttpServletRequest request,HttpServletResponse response,
                                                    Order order) throws Exception {
//        Order order = null;
//
//        order = JacksonUtils.parse(data, Order.class);

//        ResponseEntity re = new ResponseEntity(HttpStatus.OK.value(), "导出成功");
        if (order.getDirection() == null) {
            order.setDirection("asc");
        }
        if (order.getField() == null) {
            order.setField("id");
        }
        Sort sort = new Sort(order.getDirection(), order.getField());
        Pageable pageable = new PageRequest(0, Integer.MAX_VALUE, sort);
//            Page<Order> page = orderService.findUsePage(pageable);
        Page<Order> page = orderService.findByPageAndParams(order, pageable);
        excelExportService.exportOrder(response,page.getContent());
//        return re;
    }

    @RequestMapping(value="/orderExport",method = RequestMethod.POST)
    public @ResponseBody ResponseEntity orderExport(HttpServletRequest request,HttpServletResponse response,
                                                    @RequestParam(name="order") String data) throws Exception {
        Order order = null;

        order = JacksonUtils.parse(data, Order.class);

        ResponseEntity re = new ResponseEntity(HttpStatus.OK.value(), "导出成功");
        if (order.getDirection() == null) {
            order.setDirection("asc");
        }
        if (order.getField() == null) {
            order.setField("id");
        }
        Sort sort = new Sort(order.getDirection(), order.getField());
        Pageable pageable = new PageRequest(0, Integer.MAX_VALUE, sort);
//            Page<Order> page = orderService.findUsePage(pageable);
        Page<Order> page = orderService.findByPageAndParams(order, pageable);
        excelExportService.exportOrder(response,page.getContent());
        return re;
    }

    //处理文件上传
    @Transactional
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
                if(orderList != null){
                    for (Order order: orderList) {
                        ResponseEntity re = new ResponseEntity(200,"ok");
                        re = this.check(request.getServletContext(),re,order);
                        if(re.getStatus() != 200){
                            json.put("newVersionName", null);
                            json.put("fileMd5", null);
                            json.put("message", "导入失败,请确保商品具有相应库存");
                            json.put("status", false);
                            json.put("filePath", null);
                            return json;

                        }
                    }
                    for (Order o:
                         orderList) {
                       this.handlerOrderString(o);
                    }
                    orderService.save(orderList);
                }


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
            json.put("message", "导入失败,请确保EXCEL数据格式正确");
            json.put("status", false);
            json.put("filePath", null);
        }

        return json;
    }

    private ResponseEntity check(ServletContext sc, ResponseEntity re, Order order){
        if(StringUtils.isEmpty(order.getClothSize())){
            re.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            re.setInfo("此尺码未在系统录入");
            return re;
        }
        if(StringUtils.isEmpty(order.getStyle())){
            re.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            re.setInfo("此款式未在系统录入");
            return re;
        }
        if(StringUtils.isEmpty(order.getGoodsName())){
            re.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            re.setInfo("此商品未在系统录入");
            return re;
        }
        String sql = "select t1.sum,t1.id  from t_stock t1,t_cloth_size t2,t_style t3,t_goods t4 where t1.cloth_size_id =\n" +
                "  t2.id and t1.style_id = t3.id and t1.goods_id = t4.id and t2.name = '@clothSizeName' and\n" +
                "  t3.name = '@styleName' and t4.name = '@goodsName'";
        sql = sql.replace("@clothSizeName", order.getClothSize());
        sql = sql.replace("@styleName", order.getStyle());
        sql = sql.replace("@goodsName", order.getGoodsName());
        List<Map> maps = specialServiceImpl.nativeQueryToMapList(sql);
        if(maps == null || maps.size() <= 0){
            re.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            re.setInfo("未查到此商品的库存");
            return re;
        }
        Long id = Long.parseLong(maps.get(0).get("id") + "");
        String key = (String) sc.getAttribute("stockKey" + id);
        if(key == null){
            key = "stockKey" + id;
            sc.setAttribute(key, key);
        }
        synchronized (key){
            Stock stock = stockService.findOne(id);
            if(0 == stock.getSum()){
                re.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                re.setInfo("此商品没有库存");
                return re;
            }
            stock.setSum(stock.getSum() - 1);
            stockService.save(stock);
            sc.removeAttribute("stockKey" + id);
        }
        return re;
    }

    private void handlerOrderString(Order order){
        if(order.getOrderDate() != null && order.getOrderDate().indexOf(".") != -1){
            order.setOrderDate(order.getOrderDate().substring(0,order.getOrderDate().indexOf(".")));
        }
        if(order.getGoodsName() != null && order.getGoodsName().indexOf(".") != -1){
            order.setGoodsName(order.getGoodsName().substring(0,order.getGoodsName().indexOf(".")));
        }
        if(order.getSpreadUserName() != null && order.getSpreadUserName().indexOf(".") != -1){
            order.setSpreadUserName(order.getSpreadUserName().substring(0,order.getSpreadUserName().indexOf(".")));
        }
        if(order.getAddress() != null && order.getAddress().indexOf(".") != -1){
            order.setAddress(order.getAddress().substring(0,order.getAddress().indexOf(".")));
        }
        if(order.getCardType() != null && order.getCardType().indexOf(".") != -1){
            order.setCardType(order.getCardType().substring(0,order.getCardType().indexOf(".")));
        }
        if(order.getClothSize() != null && order.getClothSize().indexOf(".") != -1){
            order.setClothSize(order.getClothSize().substring(0,order.getClothSize().indexOf(".")));
        }
        if(order.getCourierNum() != null && order.getCourierNum().indexOf(".") != -1){
            order.setCourierNum(order.getCourierNum().substring(0,order.getCourierNum().indexOf(".")));
        }
        if(order.getName() != null && order.getName().indexOf(".") != -1){
            order.setName(order.getName().substring(0,order.getName().indexOf(".")));
        }
        if(order.getPhoneNum() != null && order.getPhoneNum().indexOf(".") != -1){
            order.setPhoneNum(order.getPhoneNum().substring(0,order.getPhoneNum().indexOf(".")));
        }
        if(order.getStyle() != null && order.getStyle().indexOf(".") != -1){
            order.setStyle(order.getStyle().substring(0,order.getStyle().indexOf(".")));
        }
    }

    @Transactional
    @RequestMapping(value = "/order/save", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity saveOrder(HttpServletRequest request,
                                                  HttpServletResponse response, Order  order){
        ServletContext sc = request.getServletContext();
        ResponseEntity re = new ResponseEntity(HttpStatus.OK.value(),"操作成功");
        try{
//            List<Order> orders = JacksonUtils.parseList(data, Order.class);
            if(order.getOrderDate() == null){
                Date date = new Date();
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String orderDate = df.format(date);
                order.setOrderDate(orderDate);
            }

            //处理推广人
            String userInfo = order.getUserINfo();
            if(!StringUtils.isEmpty(userInfo)){
                String spreadUserName = spreadService.getUsernameByPath(userInfo);
                order.setSpreadUserName(spreadUserName);
            }

            //减少库存
//            Goods goods = goodsService.findByName(order.getGoodsName());
//            if(goods == null){
//                re.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
//                re.setInfo("此商品未在系统录入");
//                return re;
//            }
//            Style style = styleService.findByName(order.getClothSize());
//            if(style == null){
//                re.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
//                re.setInfo("此款式未在系统录入");
//                return re;
//            }
//            ClothSize clothSize = clothSizeService.findByName(order.getClothSize());
//            if(clothSize == null){
//                re.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
//                re.setInfo("此尺码未在系统录入");
//                return re;
//            }
            if(order.getId() == null){
                re = this.check(sc, re, order);
                if(200 != re.getStatus()){
                    return re;
                }
            }
            this.handlerOrderString(order);
            order = orderService.save(order);
            re.setData(order);
            Cookie cookie = new Cookie("shopping", "shopping");
            cookie.setMaxAge(60*60*24*cookieTimeout);
            cookie.setPath("/");
            response.addCookie(cookie);

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

    @RequestMapping(value = "/order/phoneNum/{phoneNum}", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity getOrderByPhoneNum(@PathVariable String phoneNum){
        ResponseEntity re = new ResponseEntity(HttpStatus.OK.value(),"操作成功");
        try{
            String data = "";
            List<Order> orders = orderService.findByPhoneNum(phoneNum);
            if(orders == null || orders.size() == 0){
                data = "物流查询与官方数据延迟2-5天,请粉丝们耐心等待";
            }else{
                if(orders.size() == 1){
                    data = "查询到的单号为:";
                }else{
                    data = "查询到" + orders.size() + "个单号,分别为:";
                }
                data += "<strong>";
                for (Order order: orders) {
                    data += StringUtils.isEmpty(order.getCourierNum()) || "".equals(order.getCourierNum())
                            ? "未发货," : order.getCourierNum() + ",";
                }
                data = data.substring(0,data.length()-1);
                data += "</strong>";
            }
            re.setData(data);
        }catch (Exception e){
            e.printStackTrace();
            re.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            re.setInfo("操作出错");
        }
        return re;
    }

}
