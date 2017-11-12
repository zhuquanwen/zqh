package com.zqw.order.manage.controller;

import com.zqw.order.manage.domain.p.Goods;
import com.zqw.order.manage.domain.p.Stock;
import com.zqw.order.manage.entity.AjaxException;
import com.zqw.order.manage.entity.BasePageResult;
import com.zqw.order.manage.entity.PageException;
import com.zqw.order.manage.entity.ResponseEntity;
import com.zqw.order.manage.service.api.ClothSizeService;
import com.zqw.order.manage.service.api.GoodsService;
import com.zqw.order.manage.service.api.StyleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class GoodsController extends BaseController {
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private ClothSizeService clothSizeService;
    @Autowired
    private StyleService styleService;
    @GetMapping("/goods")
    public ModelAndView turnGoods(HttpServletRequest request, HttpSession session) throws PageException {
        ModelAndView mav = new ModelAndView();
        mav.setViewName(this.validateSession(request,session,"goodsPage",clothSizeService,styleService));
        mav.addObject(HIDDEN_FLAG, "goods");
        return mav;
    }

    @PostMapping(value = "/goodsAll")
    @ResponseBody
    public ResponseEntity<List<Goods>> goodsAll() /*throws AjaxException*/ {
        ResponseEntity<List<Goods>> re = new ResponseEntity<List<Goods>>(HttpStatus.OK.value(),"操作成功");
        List<Goods> goodsList = new ArrayList<Goods>();
        try {
            goodsList = goodsService.findAll();
            re.setData(goodsList);
        }catch (Exception e){
            e.printStackTrace();
//            throw new AjaxException();
            re.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            re.setInfo("查询商品列表出错");
        }
       return re;
    }

    @PostMapping(value = "/goods")
    @ResponseBody
    public BasePageResult<Goods> goods(HttpSession session, Goods goods) throws AjaxException {
        BasePageResult bpr = new BasePageResult();
        try {

            if (goods.getDirection() == null) {
                goods.setDirection("asc");
            }
            if (goods.getField() == null) {
                goods.setField("id");
            }
            Sort sort = new Sort(goods.getDirection(), goods.getField());
            Pageable pageable = new PageRequest(goods.getPage(), goods.getSize(), sort);

            Page<Goods> page = goodsService.findByPageAndParams(goods, pageable);
            List<Goods> goodsList = page.getContent();
            goodsList.forEach(goods1 -> {
                List<Stock> stockList = goods1.getStockList();
                Long sum = 0L;
                for (Stock stock: stockList) {
                    Long sum1 = stock.getSum();
                    if(sum1 != null){
                        sum += sum1;
                    }
                }
                goods1.setStockSum(sum);
            });
            bpr.setResult(page.getContent());
            bpr.setTotalCount(page.getTotalElements());
        }catch (Exception e){
            e.printStackTrace();
            throw new AjaxException();
        }
        return bpr;
    }

    @PostMapping(value = "/goods/save")
    public @ResponseBody ResponseEntity saveOrder(Goods goods){
        ResponseEntity re = new ResponseEntity(HttpStatus.OK.value(),"操作成功");
        try{
            goods = goodsService.save(goods);
            re.setData(goods);
        }catch (Exception e){
            e.printStackTrace();
            re.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            re.setInfo("操作出错");
        }
        return re;
    }

    @GetMapping(value = "/goods/{id}")
    public @ResponseBody ResponseEntity getOrderById(@PathVariable Long id){
        ResponseEntity re = new ResponseEntity(HttpStatus.OK.value(),"操作成功");
        try{
            Goods goods = goodsService.findOne(id);
            re.setData(goods);
        }catch (Exception e){
            e.printStackTrace();
            re.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            re.setInfo("操作出错");
        }
        return re;
    }
    @GetMapping(value = "/goods/delete/{id}")
    public @ResponseBody ResponseEntity deleteOrder(@PathVariable Long id){
        ResponseEntity re = new ResponseEntity(HttpStatus.OK.value(),"操作成功");
        try{
            Goods goods = new Goods();
            goods.setId(id);
            goodsService.delete(goods);
            re.setData(null);
        }catch (Exception e){
            e.printStackTrace();
            re.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            re.setInfo("操作出错");
        }
        return re;
    }
}
