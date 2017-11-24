package com.zqw.order.manage.controller;

import com.zqw.order.manage.entity.AjaxException;
import com.zqw.order.manage.entity.BasePageResult;
import com.zqw.order.manage.entity.PageException;
import com.zqw.order.manage.model.Spread;
import com.zqw.order.manage.service.api.SpreadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class SpreadController extends BaseController {
    @Value("${publish_url}")
    private String publishUrl;
    @Autowired
    private SpreadService spreadService;
    @GetMapping("/spread")
    public ModelAndView turnGoods(HttpServletRequest request, HttpSession session) throws PageException {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("spreadPage");
        mav.addObject(HIDDEN_FLAG, "spread");
        return mav;
    }


    @PostMapping(value = "/spread")
    @ResponseBody
    public BasePageResult<List<Spread>> spread(HttpSession session, Spread spread) throws AjaxException {
        BasePageResult<List<Spread>> bpr = null;
        try{
//            bpr = spreadService.getSpread(spread);
//            List<Spread> spreadList = bpr.getResult();
//            spreadList.forEach(spread1 -> {
//                String path = publishUrl;
//                path += "/toShopping/" + spread1.getUrl() + "/" + spread1.getGoodsId();
//                spread1.setUrl(path);
//            });
            bpr = spreadService.getSpread(spread, publishUrl);
        }catch (Exception e){
            e.printStackTrace();
            throw new AjaxException();
        }
        return bpr;
    }
}
