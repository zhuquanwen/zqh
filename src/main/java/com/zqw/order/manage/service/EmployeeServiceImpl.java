package com.zqw.order.manage.service;

import com.zqw.order.manage.domain.p.EmployeeDao;
import com.zqw.order.manage.service.api.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: zhuquanwen
 * @date: 2017/11/9 10:30
 * @desc:
 */
@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private EmployeeDao employeeDao;
}
