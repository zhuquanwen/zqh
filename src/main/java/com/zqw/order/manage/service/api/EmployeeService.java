package com.zqw.order.manage.service.api;

import com.zqw.order.manage.domain.p.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author: zhuquanwen
 * @date: 2017/11/9 10:29
 * @desc:
 */
public interface EmployeeService extends BaseService<Employee>{
    Page<Employee> findByPageAndParams(Employee employee, Pageable pageable);
}
