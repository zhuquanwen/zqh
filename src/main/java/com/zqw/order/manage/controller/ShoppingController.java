package com.zqw.order.manage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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

    @GetMapping(value={"/toShopping/{userInfo}"})
    public String toShoppingUserInfo(@PathVariable String userInfo){
        System.out.println(userInfo);
        return "shopping";
    }
}
