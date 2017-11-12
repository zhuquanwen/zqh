package com.zqw.order.manage.service;

import com.zqw.order.manage.domain.p.Employee;
import com.zqw.order.manage.domain.p.EmployeeDao;
import com.zqw.order.manage.service.api.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: zhuquanwen
 * @date: 2017/11/9 10:30
 * @desc:
 */
@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private EmployeeDao employeeDao;

    @Override
    public Employee save(Employee employee) {
        return employeeDao.save(employee);
    }

    @Override
    public List<Employee> save(List<Employee> t) {
        return employeeDao.save(t);
    }

    @Override
    public void delete(Employee employee) {
        employeeDao.delete(employee);
    }

    @Override
    public void deleteInBatch(List<Employee> employees) {
        employeeDao.deleteInBatch(employees);
    }

    @Override
    public Page<Employee> findUsePage(Pageable pageable) {
        return employeeDao.findAll(pageable);
    }

    @Override
    public Employee findOne(Long id) {
        return employeeDao.findOne(id);
    }

    @Override
    public List<Employee> findAll() {
        return employeeDao.findAll();
    }

    @Override
    public Page<Employee> findByPageAndParams(Employee employee, Pageable pageable) {
        Specification<Employee> spec = new Specification<Employee>() {        //查询条件构造
            @Override
            public Predicate toPredicate(Root<Employee> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Path<String> name = root.get("name");

                List<Predicate> pres = new ArrayList<Predicate>();
                if(!StringUtils.isEmpty(employee.getName())){
                    Predicate p1 = cb.like(name, "%" + employee.getName() + "%");
                    pres.add(p1);
                }


                Predicate[] presArray =  pres.toArray(new Predicate[pres.size()]);
                Predicate p = cb.and(presArray);
                return p;
            }
        };
        return employeeDao.findAll(spec, pageable);
    }
}
