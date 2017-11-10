package com.zqw.order.manage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: zhuquanwen
 * @date: 2017/11/9 14:03
 * @desc:
 */
@Controller
public class ShoppingController {
//    @GetMapping(value={"/toShopping"})
//    public String toShopping(@PathVariable String userInfo){
//        System.out.println(userInfo);
//        return "shopping";
//    }

    @GetMapping(value={"/toShopping/{userInfo}/{id}"})
    public ModelAndView toShoppingUserInfo(@PathVariable String userInfo, @PathVariable Integer id){
        //通过ID获得图片信息，库存信息等。
        ModelAndView mav = new ModelAndView("shopping");
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("title","阿迪2017明星同款套装免费送");
        mav.getModel().put("topTitle","阿迪2017明星同款套装免费送");
        return mav;
    }
}
