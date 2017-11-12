package com.zqw.order.manage.controller;

import com.zqw.order.manage.entity.PageException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
@Controller
public class SpreadController extends BaseController {
    @GetMapping("/spread")
    public ModelAndView turnGoods(HttpServletRequest request, HttpSession session) throws PageException {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("goodsPage");
        mav.addObject(HIDDEN_FLAG, "goods");
        return mav;
    }
}
