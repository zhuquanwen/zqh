package com.zqw.order.manage.service;

import com.zqw.order.manage.domain.p.Style;
import com.zqw.order.manage.domain.p.StyleDao;
import com.zqw.order.manage.service.api.StyleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
@Service
public class StyleServiceImpl implements StyleService {
    @Autowired
    private StyleDao styleDao;
    @Transactional
    @Override
    public Style save(Style style) {
        return styleDao.save(style);
    }
    @Transactional
    @Override
    public List<Style> save(List<Style> t) {
        return styleDao.save(t);
    }
    @Transactional
    @Override
    public void delete(Style style) {
        styleDao.delete(style);
    }
    @Transactional
    @Override
    public void deleteInBatch(List<Style> styles) {
        styleDao.deleteInBatch(styles);
    }
    @Transactional
    @Override
    public Page<Style> findUsePage(Pageable pageable) {
        return styleDao.findAll(pageable);
    }
    @Transactional
    @Override
    public Style findOne(Long id) {
        return styleDao.findOne(id);
    }

    @Override
    public List<Style> findAll() {
        return styleDao.findAll();
    }

    @Transactional
    @Override
    public Page<Style> findByPageAndParams(Style style, Pageable pageable) {
        Specification<Style> spec = new Specification<Style>() {        //查询条件构造
            @Override
            public Predicate toPredicate(Root<Style> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Path<String> name = root.get("name");

                List<Predicate> pres = new ArrayList<Predicate>();
                if(!StringUtils.isEmpty(style.getName())){
                    Predicate p1 = cb.like(name, "%" + style.getName() + "%");
                    pres.add(p1);
                }

                Predicate[] presArray =  pres.toArray(new Predicate[pres.size()]);
                Predicate p = cb.and(presArray);
                return p;
            }
        };
        return styleDao.findAll(spec, pageable);
    }
    @Transactional
    @Override
    public Style findByName(String name) {
        return styleDao.findByName(name);
    }
}
