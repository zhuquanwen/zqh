package com.zqw.order.manage.service;

import com.zqw.order.manage.domain.p.*;
import com.zqw.order.manage.service.api.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class StockServiceImpl implements StockService {
    @Autowired
    private StockDao stockDao;
    @Override
    public Stock save(Stock stock) {
        return stockDao.save(stock);
    }

    @Override
    public List<Stock> save(List<Stock> t) {
        return stockDao.save(t);
    }

    @Override
    public void delete(Stock stock) {
        stockDao.delete(stock);
    }

    @Override
    public void deleteInBatch(List<Stock> stocks) {
        stockDao.deleteInBatch(stocks);
    }

    @Override
    public Page<Stock> findUsePage(Pageable pageable) {
        return stockDao.findAll(pageable);
    }

    @Override
    public Stock findOne(Long id) {
        return stockDao.findOne(id);
    }

    @Override
    public List<Stock> findAll() {
        return stockDao.findAll();
    }

    @Override
    public Page<Stock> findByPageAndParams(Stock stock, Pageable pageable) {
        Specification<Stock> spec = new Specification<Stock>() {        //查询条件构造
            @Override
            public Predicate toPredicate(Root<Stock> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Path<Goods> goods = root.get("goods");
                Path<ClothSize> clothSize = root.get("clothSize");
                Path<Style> style = root.get("style");

                List<Predicate> pres = new ArrayList<Predicate>();
                if(stock.getGoods() != null){
                    Predicate p1 = cb.equal(goods, stock.getGoods());
                    pres.add(p1);
                }
                if(stock.getClothSize() != null){
                    Predicate p1 = cb.equal(clothSize, stock.getClothSize());
                    pres.add(p1);
                }
                if(stock.getStyle() != null){
                    Predicate p1 = cb.equal(style, stock.getStyle());
                    pres.add(p1);
                }

                Predicate[] presArray =  pres.toArray(new Predicate[pres.size()]);
                Predicate p = cb.and(presArray);
                return p;
            }
        };
        return stockDao.findAll(spec, pageable);
    }
}
