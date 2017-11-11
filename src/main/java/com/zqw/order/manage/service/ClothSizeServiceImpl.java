package com.zqw.order.manage.service;

import com.zqw.order.manage.domain.p.ClothSize;
import com.zqw.order.manage.domain.p.ClothSizeDao;
import com.zqw.order.manage.service.api.ClothSizeService;
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
public class ClothSizeServiceImpl implements ClothSizeService {
    @Autowired
    private ClothSizeDao clothSizeDao;
    @Transactional
    @Override
    public ClothSize save(ClothSize clothSize) {
        return clothSizeDao.save(clothSize);
    }
    @Transactional
    @Override
    public List<ClothSize> save(List<ClothSize> t) {
        return clothSizeDao.save(t);
    }
    @Transactional
    @Override
    public void delete(ClothSize clothSize) {
        clothSizeDao.delete(clothSize);
    }
    @Transactional
    @Override
    public void deleteInBatch(List<ClothSize> clothSizes) {
        clothSizeDao.deleteInBatch(clothSizes);
    }
    @Transactional
    @Override
    public Page<ClothSize> findUsePage(Pageable pageable) {
        return clothSizeDao.findAll(pageable);
    }
    @Transactional
    @Override
    public ClothSize findOne(Long id) {
        return clothSizeDao.findOne(id);
    }

    @Override
    public List<ClothSize> findAll() {
        return clothSizeDao.findAll();
    }

    @Transactional
    @Override
    public Page<ClothSize> findByPageAndParams(ClothSize clothSize, Pageable pageable) {
        Specification<ClothSize> spec = new Specification<ClothSize>() {        //查询条件构造
            @Override
            public Predicate toPredicate(Root<ClothSize> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Path<String> name = root.get("name");

                List<Predicate> pres = new ArrayList<Predicate>();
                if(!StringUtils.isEmpty(clothSize.getName())){
                    Predicate p1 = cb.like(name, "%" + clothSize.getName() + "%");
                    pres.add(p1);
                }
                Predicate[] presArray =  pres.toArray(new Predicate[pres.size()]);
                Predicate p = cb.and(presArray);
                return p;
            }
        };
        return clothSizeDao.findAll(spec, pageable);
    }

    @Transactional
    @Override
    public ClothSize findByName(String name) {
        return clothSizeDao.findByName(name);
    }
}
