package com.zqw.order.manage.service;

import com.zqw.order.manage.service.api.SpecialService;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;
@Service
public class SpecialServiceImpl implements SpecialService{
//   @Autowired
   @PersistenceContext
   @Qualifier(value="entityManagerPrimary")
   private EntityManager entityManager;

    @Override
    public List<Map> nativeQueryToMapList(String sql) {
        Query query = entityManager.createNativeQuery(sql);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map> rows = query.getResultList();

        entityManager.close();
        return rows;
    }
}
