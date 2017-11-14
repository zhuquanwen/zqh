package com.zqw.order.manage.service;

import com.zqw.order.manage.entity.AjaxException;
import com.zqw.order.manage.model.Spread;
import com.zqw.order.manage.service.api.GoodsService;
import com.zqw.order.manage.service.api.SpreadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: zhuquanwen
 * @date: 2017/11/14 9:24
 * @desc:
 */
@Service
public class SpreadServiceImpl implements SpreadService{
    @Autowired
    private GoodsService goodsService;
    @Override
    public List<Spread> getSpread(Spread spread) throws AjaxException {
        Long goodsId = spread.getGoodsId();
        String userName = spread.getUserName();
        if(goodsId == null){
            throw new AjaxException();
        }
        return null;
    }
}
