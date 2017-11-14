package com.zqw.order.manage.service;

import com.zqw.order.manage.entity.BasePageResult;
import com.zqw.order.manage.model.Spread;
import com.zqw.order.manage.service.api.GoodsService;
import com.zqw.order.manage.service.api.SpreadService;
import com.zqw.order.manage.util.BeanMapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author: zhuquanwen
 * @date: 2017/11/14 9:24
 * @desc:
 */
@Service
//@DependsOn({"specialService"})
public class SpreadServiceImpl extends SpecialServiceImpl implements SpreadService{
    @Autowired
    private GoodsService goodsService;
//    @Autowired()
//    @Qualifier(value="entityManagerPrimary")
//    private EntityManager entityManager;
    @Override
    public BasePageResult getSpread(Spread spread) throws Exception {
        BasePageResult bpr = new BasePageResult();
        List<Spread> spreadList = new ArrayList<Spread>();
        Integer page = spread.getPage();
        Integer size = spread.getSize();
        Integer begin = page * size;
        Integer end = size;
        String sql = " select t3.name as userName, t3.real_name,t3.path as url,t4.name as goodsName, t4.id as goodsId " +
                " from (select t1.name,t1.real_name,t2.path from " +
                " t_employee t1 LEFT JOIN t_url_path t2 on t1.id = t2.employee_id" +
                " where t2.use_flag = '1' @condition1 ) t3  join t_goods t4 where 1 =1 @condition2 " +
                " order by t3.name  limit @begin,@end";
        String sqlCount =  " select count(*) as count " +
                " from (select t1.name,t1.real_name,t2.path from " +
                " t_employee t1 LEFT JOIN t_url_path t2 on t1.id = t2.employee_id" +
                " where t2.use_flag = '1' @condition1 ) t3  join t_goods t4 where 1 =1 @condition2 ";

        BigInteger goodsId = spread.getGoodsId();
        String userName = spread.getUserName();
        if(StringUtils.isEmpty(goodsId) || goodsId.compareTo(BigInteger.valueOf(-1L)) == 0){
            sql = sql.replace("@condition2", " ");
            sqlCount = sqlCount.replace("@condition2", " ");
        }else{
            sql = sql.replace("@condition2", "and t4.id = " + goodsId.toString());
            sqlCount = sqlCount.replace("@condition2", "and t4.id = " + goodsId.toString());
        }
        if(StringUtils.isEmpty(userName)){
            sql = sql.replace("@condition1", " ");
            sqlCount = sqlCount.replace("@condition1", " ");
        }else {
            sql = sql.replace("@condition1", "and t1.name = '" + userName + "'");
            sqlCount = sqlCount.replace("@condition1", "and t1.name = '" + userName + "'");
        }
        sql = sql.replace("@begin", begin.toString());
        sql = sql.replace("@end", end.toString());
        List<Map> spreadMaps = this.nativeQueryToMapList(sql);
        for (Map map: spreadMaps){
            Spread spreadResult = BeanMapUtils.map2Bean(map,Spread.class);
            spreadList.add(spreadResult);
        }
        List<Map> countMaps = this.nativeQueryToMapList(sqlCount);
        Map countMap = countMaps.get(0);
        Long totalElements = Long.parseLong(countMap.get("count").toString());
        bpr.setTotalCount(totalElements);
        bpr.setResult(spreadList);
        return bpr;
    }

}
