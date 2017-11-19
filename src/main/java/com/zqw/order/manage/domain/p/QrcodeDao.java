package com.zqw.order.manage.domain.p;

import org.springframework.data.jpa.repository.JpaRepository;

public interface QrcodeDao extends JpaRepository<Qrcode,Long> {
    Qrcode findByGoodsId(Long goodsId);
}
