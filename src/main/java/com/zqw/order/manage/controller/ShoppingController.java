package com.zqw.order.manage.controller;

import com.zqw.order.manage.domain.p.ClothSize;
import com.zqw.order.manage.domain.p.Goods;
import com.zqw.order.manage.domain.p.Style;
import com.zqw.order.manage.entity.PageException;
import com.zqw.order.manage.service.SpecialServiceImpl;
import com.zqw.order.manage.service.api.ClothSizeService;
import com.zqw.order.manage.service.api.GoodsService;
import com.zqw.order.manage.service.api.StyleService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.*;

/**
 * @author: zhuquanwen
 * @date: 2017/11/9 14:03
 * @desc:
 */
@Controller
public class ShoppingController extends BaseController {
    @Value("${pic_address}")
    private String picAddress;
    @Value("${server.context-path")
    private String contextPath;
    private static String GOODS_DESCRIPTOR = "GOODS_DESCRIPTOR";
    private static String USER_INFO = "USER_INFO";
    private static String GOODS_ID = "GOODS_ID";
    private static String GOODS_NAME = "GOODS_NAME";
    private static String VIEW_IMGS = "VIEW_IMGS";
    private static String CLOTH_SIZE_COMBOBOX = "CLOTH_SIZE_COMBOBOX";
    private static String STYLE_COMBOBOX = "STYLE_COMBOBOX";
    private static String CONTEXT_PATH = "CONTEXT_PATH";
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private ClothSizeService clothSizeService;
    @Autowired
    private StyleService styleService;
    @Autowired
    private SpecialServiceImpl specialServiceImpl;
//    @GetMapping(value={"/toShopping"})
//    public String toShopping(@PathVariable String userInfo){
//        System.out.println(userInfo);
//        return "shopping";
//    }
    @GetMapping(value={"/toShopping/{userInfo}/{id}"})
    public ModelAndView toShoppingUserInfo(HttpServletRequest request,
                                           @PathVariable String userInfo, @PathVariable Long id) throws PageException{
        //通过ID获得图片信息，库存信息等。
        ModelAndView mav = new ModelAndView("shopping");
        String contextPath = request.getContextPath();
        try {
            Cookie[] cookies = request.getCookies();
            if(cookies != null){
                for (Cookie cookie:
                     cookies) {
                    String name = cookie.getName();
                    if("shopping".equals(name)){
                        if("shopping".equals(cookie.getValue())){
                            String redirect = "redirect:" + "/search";
                            mav.setViewName(redirect);
                            return mav;
                        }
                    }

                }
            }
            Map<String, Object> map = new HashMap<String, Object>();

//        map.put("title","阿迪2017明星同款套装免费送");
            Goods goods = goodsService.findOne(id);

            String path = request.getRealPath("/shopping");
            path += "/" + id;
            List<String> imgs = new ArrayList<String>();
            for (int i = 1; i <= 10; i++) {
                String picPath = path + "/" + i + ".jpg";
                File file = new File(picPath);
                if (!file.exists()) {
                    //如果不存在，从保存图片的地方拷贝一份过来
                    String picPath1 = picAddress + "/shopping/" + id + "/" + i + ".jpg";
                    File file1 = new File(picPath1);
                    if (file1.exists()) {
                        FileInputStream fis = new FileInputStream(file1);
                        FileUtils.copyInputStreamToFile(fis, new File(picPath));
                        if (fis != null) {
                            fis.close();
                        }
                        String src = "/shopping/" + id + "/" + i + ".jpg";
                        imgs.add(src);
                    }
                }else{
                    String src = "/shopping/" + id + "/" + i + ".jpg";
                    imgs.add(src);
                }
            }
            mav.addObject(VIEW_IMGS, imgs);
            mav.addObject(GOODS_NAME, goods.getName());
            mav.addObject(USER_INFO, userInfo);
//            List<ClothSize> clothSizeList = clothSizeService.findAll();
            String sql = "select t1.sum,t1.goods_id,t4.name,t2.name as clothSize,t2.id  from t_stock t1,t_cloth_size t2,t_goods t4 where t1.cloth_size_id =" +
                    "  t2.id  and t1.goods_id = t4.id and t4.id= @id";
            sql = sql.replace("@id" , id.toString());
            List<Map> maps = specialServiceImpl.nativeQueryToMapList(sql);
            List<ClothSize> clothSizeList = new ArrayList<ClothSize>();
            Map mapx = new HashMap();
            if(maps != null){
                for (int i = maps.size()-1; i >= 0 ; i--) {
                    ClothSize style = new ClothSize();
                    BigInteger bi = (BigInteger) maps.get(i).get("id");
                    if(mapx.get(bi) == null){
                        style.setId(bi.longValue() );
                        style.setName((String) maps.get(i).get("clothSize"));
                        clothSizeList.add(style);
                        mapx.put(bi,"xcc");
                    }
                }

            }
//            List<Style> styleList = styleService.findAll();
            String sqlx = "select t1.sum,t1.goods_id,t4.name,t2.name as style,t2.id  from t_stock t1,t_style t2,t_goods t4 where t1.style_id =" +
                    "  t2.id  and t1.goods_id = t4.id and t4.id= @id";
            sqlx = sqlx.replace("@id" , id.toString());
            List<Style> styleList = new ArrayList<Style>();
            List<Map> maps1 = specialServiceImpl.nativeQueryToMapList(sqlx);
            Map mapy = new HashMap();
            if(maps1 != null){
                for (int i = maps1.size()-1; i >= 0 ; i--) {
                    Style style = new Style();
                    BigInteger bi = (BigInteger) maps1.get(i).get("id");
                    if(mapy.get(bi) == null){
                        style.setId(bi.longValue());
                        style.setName((String) maps1.get(i).get("style"));
                        styleList.add(style);
                        mapy.put(bi,"erer");
                    }
                }
            }
            mav.addObject(CLOTH_SIZE_COMBOBOX, clothSizeList);
            mav.addObject(STYLE_COMBOBOX, styleList);
            mav.addObject("topTitle", goods.getDescriptor());
            mav.addObject(CONTEXT_PATH, contextPath);
        }catch (Exception e){
            e.printStackTrace();
            throw new PageException();
        }
        return mav;
    }

    @GetMapping("/shoppingEdit")
    public ModelAndView editSpread(HttpServletRequest request, HttpSession session, Long id) throws PageException {
//        String path = request.getRealPath("/shopping");
        ModelAndView mav = new ModelAndView();
        try {

            mav.setViewName(this.validateSession(request, session, "editShoppingPage", clothSizeService, styleService));
            Goods goods = goodsService.findOne(id);
            mav.addObject("topTitle", goods.getDescriptor());
            String path = request.getRealPath("/shopping");
            path += "/" + id;
            for (int i = 1; i <= 10; i++) {
                String picPath = path + "/" + i + ".jpg";
                File file = new File(picPath);
                if (!file.exists()) {
                    //如果不存在，从保存图片的地方拷贝一份过来
                    String picPath1 = picAddress + "/shopping/" + id + "/" + i + ".jpg";
                    File file1 = new File(picPath1);
                    if (file1.exists()) {
                        FileInputStream fis = new FileInputStream(file1);
                        FileUtils.copyInputStreamToFile(fis, new File(picPath));
                        if(fis != null){
                            fis.close();
                        }
                    }
                }
                mav.addObject("img" + i, "shopping/" + id + "/" + i + ".jpg");
            }
            mav.addObject("action", "shoppingEditImg?id=" + id);
//        mav.addObject(HIDDEN_FLAG, "spread");
        }catch (Exception e){
            e.printStackTrace();
            throw new PageException();
        }
        return mav;
    }
    @PostMapping("/shoppingEditImg")
    public ModelAndView editImg(HttpServletRequest request, HttpSession session, Long id) throws PageException {
        ModelAndView mav = new ModelAndView();
        try {
            String path = request.getRealPath("/shopping");
            File file = new File(path + "/" +id);
            if(!file.exists()){
                file.mkdir();
            }
            path += "/" +id;
            request.setCharacterEncoding("UTF-8");
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

            /** 页面控件的文件流* */
            MultipartFile multipartFile = null;
            Map map = multipartRequest.getFileMap();
            int j = 1;
            for (Iterator i = map.keySet().iterator(); i.hasNext(); ) {
                Object obj = i.next();
                multipartFile = (MultipartFile) map.get(obj);
                InputStream inputStream;
                String filename = multipartFile.getOriginalFilename();
                if(!StringUtils.isEmpty(filename)){
                    inputStream = multipartFile.getInputStream();
                    String picPath = path + "/" + j + ".jpg";
                    File tmpFile = new File(picPath);
                    FileUtils.copyInputStreamToFile(inputStream, tmpFile);
                    if(inputStream != null ){
                        inputStream.close();
                    }
                    String picPath1 = picAddress + "/shopping/" + id + "/" + j + ".jpg";
                    FileInputStream fis = new FileInputStream(tmpFile);
                    FileUtils.copyInputStreamToFile(fis, new File(picPath1));
                    if(fis != null){
                        fis.close();
                    }
                }
                j++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new PageException();
        }
        mav.setViewName("redirect:shoppingEdit?id=" + id);
        return mav;
    }
}
