package com.zqw.order.manage.service;

import com.zqw.order.manage.domain.p.Qrcode;
import com.zqw.order.manage.domain.p.QrcodeDao;
import com.zqw.order.manage.service.api.QrcodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QrcodeServiceImpl implements QrcodeService {
    @Autowired
    private QrcodeDao qrcodeDao;
    @Override
    public Qrcode findByGoodsId(Long goodsId) {
        return qrcodeDao.findByGoodsId(goodsId);
    }

    @Override
    public Qrcode save(Qrcode qrcode) {
        return qrcodeDao.save(qrcode);
    }

    @Override
    public List<Qrcode> save(List<Qrcode> t) {
        return qrcodeDao.save(t);
    }

    @Override
    public void delete(Qrcode qrcode) {
        qrcodeDao.delete(qrcode);
    }

    @Override
    public void deleteInBatch(List<Qrcode> qrcodes) {
        qrcodeDao.deleteInBatch(qrcodes);
    }

    @Override
    public Page<Qrcode> findUsePage(Pageable pageable) {
        return qrcodeDao.findAll(pageable);
    }

    @Override
    public Qrcode findOne(Long id) {
        return qrcodeDao.findOne(id);
    }

    @Override
    public List<Qrcode> findAll() {
        return qrcodeDao.findAll();
    }
}
