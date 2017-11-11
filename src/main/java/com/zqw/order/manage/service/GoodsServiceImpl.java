package com.zqw.order.manage.service;

import com.zqw.order.manage.domain.p.Goods;
import com.zqw.order.manage.domain.p.GoodsDao;
import com.zqw.order.manage.service.api.GoodsService;
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
public class GoodsServiceImpl implements GoodsService {
    @Autowired
    private GoodsDao goodsDao;
    @Transactional
    @Override
    public Page<Goods> findByPageAndParams(Goods goods, Pageable pageable) {
        Specification<Goods> spec = new Specification<Goods>() {        //查询条件构造
            @Override
            public Predicate toPredicate(Root<Goods> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Path<String> name = root.get("name");
                Path<String> descriptor = root.get("descriptor");

                List<Predicate> pres = new ArrayList<Predicate>();
                if(!StringUtils.isEmpty(goods.getName())){
                    Predicate p1 = cb.like(name, "%" + goods.getName() + "%");
                    pres.add(p1);
                }
                if(!StringUtils.isEmpty(goods.getDescriptor())){
                    Predicate p2 = cb.like(descriptor, "%" + goods.getDescriptor() + "%");
                    pres.add(p2);
                }

                Predicate[] presArray =  pres.toArray(new Predicate[pres.size()]);
                Predicate p = cb.and(presArray);
                return p;
            }
        };
        return goodsDao.findAll(spec, pageable);
    }
    @Transactional
    @Override
    public Goods save(Goods goods) {
        return goodsDao.save(goods);

    }

    @Override
    public Goods findByName(String name) {
        return goodsDao.findByName(name);
    }

    @Override
    public List<Goods> findAll() {
        return goodsDao.findAll();
    }

    @Override
    public List<Goods> save(List<Goods> t) {
        return goodsDao.save(t);
    }

    @Override
    public void delete(Goods goods) {
        goodsDao.delete(goods);
    }

    @Override
    public void deleteInBatch(List<Goods> goods) {
        goodsDao.deleteInBatch(goods);
    }

    @Override
    public Page<Goods> findUsePage(Pageable pageable) {
        return goodsDao.findAll(pageable);
    }

    @Override
    public Goods findOne(Long id) {
        return goodsDao.findOne(id);
    }
}
