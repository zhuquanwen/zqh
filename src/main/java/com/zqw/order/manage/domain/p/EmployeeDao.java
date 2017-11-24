package com.zqw.order.manage.domain.p;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author: zhuquanwen
 * @date: 2017/11/9 10:05
 * @desc:
 */
public interface EmployeeDao extends JpaRepository<Employee,Long>, JpaSpecificationExecutor<Employee>{
    Employee findByName(String name);
}
