package com.zqw.order.manage.controller;

import com.zqw.order.manage.domain.p.Goods;
import com.zqw.order.manage.domain.p.Style;
import com.zqw.order.manage.entity.AjaxException;
import com.zqw.order.manage.entity.BasePageResult;
import com.zqw.order.manage.entity.PageException;
import com.zqw.order.manage.entity.ResponseEntity;
import com.zqw.order.manage.service.api.ClothSizeService;
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
public class StyleController extends BaseController {

    @Autowired
    private ClothSizeService clothSizeService;
    @Autowired
    private StyleService styleService;

    @GetMapping("/style")
    public ModelAndView turnGoods(HttpServletRequest request , HttpSession session) throws PageException {
        ModelAndView mav = new ModelAndView();
        mav.setViewName(this.validateSession(request, session,"stylePage",clothSizeService,styleService));
        mav.addObject(HIDDEN_FLAG, "style");
        return mav;
    }
    @PostMapping(value = "/style")
    @ResponseBody
    public BasePageResult<Goods> goods(HttpSession session, Style style) throws AjaxException {
        BasePageResult bpr = new BasePageResult();
        try {

            if (style.getDirection() == null) {
                style.setDirection("asc");
            }
            if (style.getField() == null) {
                style.setField("id");
            }
            Sort sort = new Sort(style.getDirection(), style.getField());
            Pageable pageable = new PageRequest(style.getPage(), style.getSize(), sort);

            Page<Style> page = styleService.findByPageAndParams(style, pageable);
            bpr.setResult(page.getContent());
            bpr.setTotalCount(page.getTotalElements());
        }catch (Exception e){
            e.printStackTrace();
            throw new AjaxException();
        }
        return bpr;
    }

    @PostMapping(value = "/style/save")
    public @ResponseBody
    ResponseEntity saveOrder(Style style){
        ResponseEntity re = new ResponseEntity(HttpStatus.OK.value(),"操作成功");
        try{
            if("默认".equals(style.getName())){
                re.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                re.setInfo("默认款式不允许新增");
                return re;
            }
            style = styleService.save(style);
            re.setData(style);
        }catch (Exception e){
            e.printStackTrace();
            re.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            re.setInfo("操作出错");
        }
        return re;
    }

    @GetMapping(value = "/style/{id}")
    public @ResponseBody ResponseEntity getOrderById(@PathVariable Long id){
        ResponseEntity re = new ResponseEntity(HttpStatus.OK.value(),"操作成功");
        try{
            Style style = styleService.findOne(id);
            re.setData(style);
        }catch (Exception e){
            e.printStackTrace();
            re.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            re.setInfo("操作出错");
        }
        return re;
    }
    @GetMapping(value = "/style/delete/{id}")
    public @ResponseBody ResponseEntity deleteOrder(@PathVariable Long id){
        ResponseEntity re = new ResponseEntity(HttpStatus.OK.value(),"操作成功");
        try{
            Style style1 = styleService.findOne(id);
            if(style1 != null && "默认".equals(style1.getName())){
                re.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                re.setInfo("默认款式不允许删除");
                return re;
            }
            Style style = new Style();
            style.setId(id);
            styleService.delete(style);
            re.setData(null);
        }catch (Exception e){
            e.printStackTrace();
            re.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            re.setInfo("操作出错");
        }
        return re;
    }
    @PostMapping(value = "/styleAll")
    @ResponseBody
    public ResponseEntity<List<Style>> styleAll() /*throws AjaxException*/ {
        ResponseEntity<List<Style>> re = new ResponseEntity<List<Style>>(HttpStatus.OK.value(),"操作成功");
        List<Style> styleList = new ArrayList<Style>();
        try {
            styleList = styleService.findAll();
            re.setData(styleList);
        }catch (Exception e){
            e.printStackTrace();
//            throw new AjaxException();
            re.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            re.setInfo("查询款式列表出错");
        }
        return re;
    }
}
