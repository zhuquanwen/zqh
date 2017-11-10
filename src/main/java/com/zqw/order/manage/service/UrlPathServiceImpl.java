package com.zqw.order.manage.service;

import com.zqw.order.manage.domain.p.UrlPathDao;
import com.zqw.order.manage.service.api.UrlPathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: zhuquanwen
 * @date: 2017/11/9 10:27
 * @desc:
 */
@Service
public class UrlPathServiceImpl implements UrlPathService{
    @Autowired
    private UrlPathDao urlPathDao;
}
