package com.zqw.order.manage.service.api;

import com.zqw.order.manage.entity.BasePageResult;
import com.zqw.order.manage.model.Spread;

/**
 * @author: zhuquanwen
 * @date: 2017/11/14 9:24
 * @desc:
 */
public interface SpreadService {
    BasePageResult getSpread(Spread spread, String publishUrl) throws Exception;
    BasePageResult getSpread(Spread spread) throws Exception;
    String getUsernameByPath(String path) throws Exception;
}
