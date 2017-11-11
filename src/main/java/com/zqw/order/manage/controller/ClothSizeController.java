package com.zqw.order.manage.controller;

import com.zqw.order.manage.domain.p.ClothSize;
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
public class ClothSizeController extends BaseController {
    @Autowired
    private ClothSizeService clothSizeService;
    @Autowired
    private StyleService styleService;
    @GetMapping("/clothSize")
    public ModelAndView turnGoods(HttpSession session, HttpServletRequest request) throws PageException {
        ModelAndView mav = new ModelAndView();
        mav.setViewName(this.validateSession(request,session,"clothSizePage",clothSizeService,styleService));
        mav.addObject(HIDDEN_FLAG, "clothSize");
        return mav;
    }
    @PostMapping(value = "/clothSize")
    @ResponseBody
    public BasePageResult<ClothSize> goods(HttpSession session, ClothSize clothSize) throws AjaxException {
        BasePageResult bpr = new BasePageResult();
        try {

            if (clothSize.getDirection() == null) {
                clothSize.setDirection("asc");
            }
            if (clothSize.getField() == null) {
                clothSize.setField("id");
            }
            Sort sort = new Sort(clothSize.getDirection(), clothSize.getField());
            Pageable pageable = new PageRequest(clothSize.getPage(), clothSize.getSize(), sort);

            Page<ClothSize> page = clothSizeService.findByPageAndParams(clothSize, pageable);
            bpr.setResult(page.getContent());
            bpr.setTotalCount(page.getTotalElements());
        }catch (Exception e){
            e.printStackTrace();
            throw new AjaxException();
        }
        return bpr;
    }

    @PostMapping(value = "/clothSize/save")
    public @ResponseBody
    ResponseEntity saveOrder(ClothSize clothSize){
        ResponseEntity re = new ResponseEntity(HttpStatus.OK.value(),"操作成功");
        try{
            clothSize = clothSizeService.save(clothSize);
            re.setData(clothSize);
        }catch (Exception e){
            e.printStackTrace();
            re.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            re.setInfo("操作出错");
        }
        return re;
    }

    @GetMapping(value = "/clothSize/{id}")
    public @ResponseBody ResponseEntity getOrderById(@PathVariable Long id){
        ResponseEntity re = new ResponseEntity(HttpStatus.OK.value(),"操作成功");
        try{
            ClothSize clothSize = clothSizeService.findOne(id);
            re.setData(clothSize);
        }catch (Exception e){
            e.printStackTrace();
            re.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            re.setInfo("操作出错");
        }
        return re;
    }
    @GetMapping(value = "/clothSize/delete/{id}")
    public @ResponseBody ResponseEntity deleteOrder(@PathVariable Long id){
        ResponseEntity re = new ResponseEntity(HttpStatus.OK.value(),"操作成功");
        try{

            ClothSize clothSize1 = clothSizeService.findOne(id);
            if(clothSize1 != null && "默认".equals(clothSize1.getName())){
                re.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                re.setInfo("默认尺码不允许删除");
                return re;
            }

            ClothSize clothSize = new ClothSize();
            clothSize.setId(id);
            clothSizeService.delete(clothSize);
            re.setData(null);
        }catch (Exception e){
            e.printStackTrace();
            re.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            re.setInfo("操作出错");
        }
        return re;
    }
    @PostMapping(value = "/clothSizeAll")
    @ResponseBody
    public ResponseEntity<List<ClothSize>> clothSizeAll() /*throws AjaxException*/ {
        ResponseEntity<List<ClothSize>> re = new ResponseEntity<List<ClothSize>>(HttpStatus.OK.value(),"操作成功");
        List<ClothSize> clothSizeList = new ArrayList<ClothSize>();
        try {
            clothSizeList = clothSizeService.findAll();
            re.setData(clothSizeList);
        }catch (Exception e){
            e.printStackTrace();
//            throw new AjaxException();
            re.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            re.setInfo("查询尺码列表出错");
        }
        return re;
    }
}
