package com.zqw.order.manage.controller;

import com.zqw.order.manage.domain.p.ClothSize;
import com.zqw.order.manage.domain.p.Goods;
import com.zqw.order.manage.domain.p.Stock;
import com.zqw.order.manage.domain.p.Style;
import com.zqw.order.manage.entity.AjaxException;
import com.zqw.order.manage.entity.BasePageResult;
import com.zqw.order.manage.entity.PageException;
import com.zqw.order.manage.entity.ResponseEntity;
import com.zqw.order.manage.service.api.ClothSizeService;
import com.zqw.order.manage.service.api.GoodsService;
import com.zqw.order.manage.service.api.StockService;
import com.zqw.order.manage.service.api.StyleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class StockController extends BaseController {
    @Autowired
    private ClothSizeService clothSizeService;
    @Autowired
    private StyleService styleService;
    @Autowired
    private StockService stockService;
    @Autowired
    private GoodsService goodsService;
    @GetMapping("/stock")
    public ModelAndView turnGoods(HttpServletRequest request, HttpSession session) throws PageException {
        ModelAndView mav = new ModelAndView();
        mav.setViewName(this.validateSession(request, session,"stockPage",clothSizeService,styleService));
        mav.addObject(HIDDEN_FLAG, "stock");
        return mav;
    }
    @PostMapping(value = "/stock")
    @ResponseBody
    public BasePageResult<Stock> goods(HttpSession session, Stock stock) throws AjaxException {
        BasePageResult bpr = new BasePageResult();
        try {

            if (stock.getDirection() == null) {
                stock.setDirection("asc");
            }
            if (stock.getField() == null) {
                stock.setField("id");
            }
            Sort sort = new Sort(stock.getDirection(), stock.getField());
            Pageable pageable = new PageRequest(stock.getPage(), stock.getSize(), sort);

            if(!StringUtils.isEmpty(stock.getGoodsName())){
                Goods goods = goodsService.findByName(stock.getGoodsName());
                if(goods != null){
                    stock.setGoods(goods);
                }else {
                    bpr.setResult(new ArrayList<Stock>());
                    bpr.setTotalCount(0L);
                    return bpr;
                }
            }
            Page<Stock> page = stockService.findByPageAndParams(stock, pageable);
            List<Stock> stockList = page.getContent();
            stockList.forEach(stock1 -> {
                Goods goods = stock1.getGoods();
                ClothSize clothSize = stock1.getClothSize();
                Style style = stock1.getStyle();
                stock1.setGoodsName(goods != null ? goods.getName() : null);
                stock1.setClothSizeName(clothSize != null ? clothSize.getName() : null);
                stock1.setStyleName(style != null ? style.getName() : null);
            });
            bpr.setResult(page.getContent());
            bpr.setTotalCount(page.getTotalElements());
        }catch (Exception e){
            e.printStackTrace();
            throw new AjaxException();
        }
        return bpr;
    }

    @PostMapping(value = "/stock/save")
    public @ResponseBody
    ResponseEntity saveStock(HttpSession session, Stock stock){
        ServletContext sc = session.getServletContext();
        Long id = stock.getId();

        ResponseEntity re = new ResponseEntity(HttpStatus.OK.value(),"操作成功");
        try{
            Long goodsId = stock.getGoodsId();
            Long clothSizeId = stock.getClothSizeId();
            Long styleId = stock.getStyleId();
            Goods goods = goodsService.findOne(goodsId);
            ClothSize clothSize = clothSizeService.findOne(clothSizeId);
            Style style = styleService.findOne(styleId);
            stock.setGoods(goods);
            stock.setStyle(style);
            stock.setClothSize(clothSize);
            if(stock.getId() == null){
                Pageable pageable = new PageRequest(0, 5);
                Page<Stock> stockPage = stockService.findByPageAndParams(stock, pageable);
                if(stockPage != null && stockPage.getContent() != null && stockPage.getContent().size() != 0){
                    //数据库中已经有了，不能新增了
                    re.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                    re.setInfo("已经有此记录，只能修改库存，不能新增");
                    return re;
                }
            }


            //保存key，为了锁
            if(id == null){
                stock = stockService.save(stock);
                String key = "stockKey" + stock.getId();
                sc.setAttribute(key, key);
            }else{
                String key = (String) sc.getAttribute("stockKey" + id);
                if(key == null){
                    key = "stockKey" + id;
                    sc.setAttribute(key, key);
                }
                synchronized (key){
                    stock = stockService.save(stock);
                }
            }

            re.setData(stock);
        }catch (Exception e){
            e.printStackTrace();
            re.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            re.setInfo("操作出错");
        }
        return re;
    }

    @GetMapping(value = "/stock/{id}")
    public @ResponseBody ResponseEntity getOrderById(@PathVariable Long id){
        ResponseEntity re = new ResponseEntity(HttpStatus.OK.value(),"操作成功");
        try{
            Stock stock = stockService.findOne(id);
            re.setData(stock);
        }catch (Exception e){
            e.printStackTrace();
            re.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            re.setInfo("操作出错");
        }
        return re;
    }
    @GetMapping(value = "/stock/delete/{id}")
    public @ResponseBody ResponseEntity deleteOrder(HttpSession session, @PathVariable Long id){
        ServletContext sc = session.getServletContext();
        ResponseEntity re = new ResponseEntity(HttpStatus.OK.value(),"操作成功");
        try{
            Stock stock = stockService.findOne(id);
//            stock.setId(id);
            String key = (String) sc.getAttribute("stockKey" + id);
            if(key == null){
                key = "stockKey" + id;
                sc.setAttribute(key, key);
            }
            synchronized (key){
                stockService.delete(stock);
                sc.removeAttribute("stockKey" + id);
            }

            re.setData(null);
        }catch (Exception e){
            e.printStackTrace();
            re.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            re.setInfo("操作出错");
        }
        return re;
    }
}
