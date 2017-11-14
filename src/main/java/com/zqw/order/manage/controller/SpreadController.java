package com.zqw.order.manage.controller;

import com.zqw.order.manage.domain.p.Goods;
import com.zqw.order.manage.domain.p.Stock;
import com.zqw.order.manage.entity.AjaxException;
import com.zqw.order.manage.entity.BasePageResult;
import com.zqw.order.manage.entity.PageException;
import com.zqw.order.manage.model.Spread;
import com.zqw.order.manage.service.api.SpreadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
public class SpreadController extends BaseController {
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
    public BasePageResult<Map<String,Object>> spread(HttpSession session, Spread spread) throws AjaxException {
        BasePageResult bpr = new BasePageResult();
//        try {
//            if (goods.getDirection() == null) {
//                goods.setDirection("asc");
//            }
//            if (goods.getField() == null) {
//                goods.setField("id");
//            }
//            Sort sort = new Sort(goods.getDirection(), goods.getField());
//            Pageable pageable = new PageRequest(goods.getPage(), goods.getSize(), sort);
//
//            Page<Goods> page = goodsService.findByPageAndParams(goods, pageable);
//            List<Goods> goodsList = page.getContent();
//            goodsList.forEach(goods1 -> {
//                List<Stock> stockList = goods1.getStockList();
//                Long sum = 0L;
//                for (Stock stock: stockList) {
//                    Long sum1 = stock.getSum();
//                    if(sum1 != null){
//                        sum += sum1;
//                    }
//                }
//                goods1.setStockSum(sum);
//            });
//            bpr.setResult(page.getContent());
//            bpr.setTotalCount(page.getTotalElements());
//        }catch (Exception e){
//            e.printStackTrace();
//            throw new AjaxException();
//        }
        return bpr;
    }
}
