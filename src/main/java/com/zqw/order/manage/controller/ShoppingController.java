package com.zqw.order.manage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author: zhuquanwen
 * @date: 2017/11/9 14:03
 * @desc:
 */
@Controller
public class ShoppingController {
    @GetMapping("/toShopping")
    public String toShopping(){
        return "shopping";
    }
}
