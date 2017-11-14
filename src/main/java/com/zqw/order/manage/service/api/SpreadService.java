package com.zqw.order.manage.service.api;

import com.zqw.order.manage.entity.AjaxException;
import com.zqw.order.manage.model.Spread;

import java.util.List;

/**
 * @author: zhuquanwen
 * @date: 2017/11/14 9:24
 * @desc:
 */
public interface SpreadService {
    List<Spread> getSpread(Spread spread) throws AjaxException;
}
