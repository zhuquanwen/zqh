package com.zqw.order.manage.service.api;

import com.zqw.order.manage.domain.p.Qrcode;

public interface QrcodeService extends BaseService<Qrcode> {
    Qrcode findByGoodsId(Long goodsId);
}
