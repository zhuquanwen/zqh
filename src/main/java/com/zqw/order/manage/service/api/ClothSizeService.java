package com.zqw.order.manage.service.api;

import com.zqw.order.manage.domain.p.ClothSize;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClothSizeService extends BaseService<ClothSize> {
    Page<ClothSize> findByPageAndParams(ClothSize clothSize, Pageable pageable);
    ClothSize findByName(String name);
}
