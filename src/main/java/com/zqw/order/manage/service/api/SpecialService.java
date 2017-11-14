package com.zqw.order.manage.service.api;

import java.util.List;
import java.util.Map;

public interface SpecialService {
    public List<Map> nativeQueryToMapList(String sql);
}
