package com.zqw.order.manage.service;

import com.zqw.order.manage.domain.p.UrlPath;
import com.zqw.order.manage.domain.p.UrlPathDao;
import com.zqw.order.manage.service.api.UrlPathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: zhuquanwen
 * @date: 2017/11/9 10:27
 * @desc:
 */
@Service
public class UrlPathServiceImpl implements UrlPathService{
    @Autowired
    private UrlPathDao urlPathDao;

    @Override
    public UrlPath save(UrlPath urlPath) {
        return urlPathDao.save(urlPath);
    }

    @Override
    public List<UrlPath> save(List<UrlPath> t) {
        return urlPathDao.save(t);
    }

    @Override
    public void delete(UrlPath urlPath) {
        urlPathDao.delete(urlPath);
    }

    @Override
    public void deleteInBatch(List<UrlPath> urlPaths) {
        urlPathDao.deleteInBatch(urlPaths);
    }

    @Override
    public Page<UrlPath> findUsePage(Pageable pageable) {
        return urlPathDao.findAll(pageable);
    }

    @Override
    public UrlPath findOne(Long id) {
        return urlPathDao.findOne(id);
    }

    @Override
    public List<UrlPath> findAll() {
        return urlPathDao.findAll();
    }
}
