package com.zqw.order.manage.service;

import com.zqw.order.manage.domain.p.*;
import com.zqw.order.manage.entity.BasePageResult;
import com.zqw.order.manage.model.Spread;
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
public class SpreadServiceImpl /*extends SpecialServiceImpl*/ implements SpreadService{
    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private EmployeeDao employeeDao;
//    @Autowired()
//    @Qualifier(value="entityManagerPrimary")
//    private EntityManager entityManager;

    public BasePageResult getSpread(Spread spread, String publishUrl) throws Exception{
        BasePageResult bpr = new BasePageResult();
        Integer page = spread.getPage();
        Integer size = spread.getSize();
        BigInteger goodsId = spread.getGoodsId();
        List<Goods> goodsList = goodsDao.findAll();
        List<Employee> employeeList = employeeDao.findAll();
        String userName = spread.getUserName();
        List<Spread> spreadList  = new ArrayList<Spread>();
        if(StringUtils.isEmpty(userName) && (BigInteger.valueOf(-1).compareTo(goodsId) == 0)){
            for (int i = 0 ; i< employeeList.size(); i++){
                Employee employee = employeeList.get(i);
                String username = employee.getName();
                String path = username;
                List<UrlPath> urlPathList = employee.getUrlPathList();
                for (UrlPath urlPath:
                     urlPathList) {
                    String flag = urlPath.getUseFlag();
                    if("1".equals(flag)){
                        path = urlPath.getPath();
                        break;
                    }
                }
                for (int j =0;j< goodsList.size();j++){
                    Goods goods = goodsList.get(j);
                    String goodsName = goods.getName();
                    BigInteger id = BigInteger.valueOf(goods.getId());
                    Spread spread1 = new Spread();
                    spread1.setGoodsId(id);
                    spread1.setGoodsName(goodsName);
                    spread1.setUserName(username);
                    String urlPath = publishUrl +
                            "/toShopping/" + path + "/" + id;
                    spread1.setUrl(urlPath);
                    spreadList.add(spread1);
                }
            }
        }else if(StringUtils.isEmpty(userName) && !(BigInteger.valueOf(-1).compareTo(goodsId) == 0)){

            long id = goodsId.longValue();
            Goods goods = goodsDao.findOne(id);
            for (int i = 0 ; i< employeeList.size(); i++) {
                Employee employee = employeeList.get(i);
                String username = employee.getName();
                String path = username;
                List<UrlPath> urlPathList = employee.getUrlPathList();
                for (UrlPath urlPath :
                        urlPathList) {
                    String flag = urlPath.getUseFlag();
                    if ("1".equals(flag)) {
                        path = urlPath.getPath();
                        break;
                    }
                }
                String goodsName = goods.getName();
                Spread spread1 = new Spread();
                spread1.setGoodsId(goodsId);
                spread1.setGoodsName(goodsName);
                spread1.setUserName(username);
                String urlPath = publishUrl +
                        "/toShopping/" + path + "/" + id;
                spread1.setUrl(urlPath);
                spreadList.add(spread1);

            }

        }else if(!StringUtils.isEmpty(userName) && !(BigInteger.valueOf(-1).compareTo(goodsId) == 0)){
            Goods goods = goodsDao.findOne(goodsId.longValue());
            Employee employee = employeeDao.findByName(userName);

            if(employee != null){
                List<UrlPath> urlPathList = employee.getUrlPathList();
                String path = userName;
                for (UrlPath urlPath :
                        urlPathList) {
                    String flag = urlPath.getUseFlag();
                    if ("1".equals(flag)) {
                        path = urlPath.getPath();
                        break;
                    }
                }
                String goodsName = goods.getName();
                Spread spread1 = new Spread();
                spread1.setGoodsId(goodsId);
                spread1.setGoodsName(goodsName);
                spread1.setUserName(employee.getName());
                String urlPath = publishUrl +
                        "/toShopping/" + path + "/" + goodsId;
                spread1.setUrl(urlPath);
                spreadList.add(spread1);
            }
        }else if(!StringUtils.isEmpty(userName) && (BigInteger.valueOf(-1).compareTo(goodsId) == 0)){
            Employee employee = employeeDao.findByName(userName);

            if(employee != null){
                List<UrlPath> urlPathList = employee.getUrlPathList();
                String path = userName;
                for (UrlPath urlPath :
                        urlPathList) {
                    String flag = urlPath.getUseFlag();
                    if ("1".equals(flag)) {
                        path = urlPath.getPath();
                        break;
                    }
                }
                for (int i = 0; i < goodsList.size(); i++) {
                    Goods goods = goodsList.get(i);
                    String goodsName = goods.getName();
                    BigInteger id = BigInteger.valueOf(goods.getId());
                    Spread spread1 = new Spread();
                    spread1.setGoodsId(id);
                    spread1.setGoodsName(goodsName);
                    spread1.setUserName(employee.getName());
                    String urlPath = publishUrl +
                            "/toShopping/" + path + "/" + id;
                    spread1.setUrl(urlPath);
                    spreadList.add(spread1);
                }


            }
        }
        Integer begin = page * size;
        Integer end = (page+1) * size - 1 ;
        List<Spread> spreadList1 = new ArrayList<Spread>();
        for (int i = begin ; i <= end && i< spreadList.size(); i++){
            spreadList1.add(spreadList.get(i));
        }
        bpr.setResult(spreadList1);
        bpr.setTotalCount(Long.valueOf(spreadList.size()));
        return bpr;
    }

//    @Override
//    public BasePageResult getSpread(Spread spread) throws Exception {
//        BasePageResult bpr = new BasePageResult();
//        List<Spread> spreadList = new ArrayList<Spread>();
//        Integer page = spread.getPage();
//        Integer size = spread.getSize();
//        Integer begin = page * size;
//        Integer end = size;
//        String sql = " select t3.name as userName, t3.real_name,t3.path as url,t4.name as goodsName, t4.id as goodsId " +
//                " from (select t1.name,t1.real_name,t2.path from " +
//                " t_employee t1 LEFT JOIN t_url_path t2 on t1.id = t2.employee_id" +
//                " where t2.use_flag = '1' @condition1 ) t3  join t_goods t4 where 1 =1 @condition2 " +
//                " order by t3.name  limit @begin,@end";
//        String sqlCount =  " select count(*) as count " +
//                " from (select t1.name,t1.real_name,t2.path from " +
//                " t_employee t1 LEFT JOIN t_url_path t2 on t1.id = t2.employee_id" +
//                " where t2.use_flag = '1' @condition1 ) t3  join t_goods t4 where 1 =1 @condition2 ";
//
//        BigInteger goodsId = spread.getGoodsId();
//        String userName = spread.getUserName();
//        if(StringUtils.isEmpty(goodsId) || goodsId.compareTo(BigInteger.valueOf(-1L)) == 0){
//            sql = sql.replace("@condition2", " ");
//            sqlCount = sqlCount.replace("@condition2", " ");
//        }else{
//            sql = sql.replace("@condition2", "and t4.id = " + goodsId.toString());
//            sqlCount = sqlCount.replace("@condition2", "and t4.id = " + goodsId.toString());
//        }
//        if(StringUtils.isEmpty(userName)){
//            sql = sql.replace("@condition1", " ");
//            sqlCount = sqlCount.replace("@condition1", " ");
//        }else {
//            sql = sql.replace("@condition1", "and t1.name = '" + userName + "'");
//            sqlCount = sqlCount.replace("@condition1", "and t1.name = '" + userName + "'");
//        }
//        sql = sql.replace("@begin", begin.toString());
//        sql = sql.replace("@end", end.toString());
//        List<Map> spreadMaps = this.nativeQueryToMapList(sql);
//        for (Map map: spreadMaps){
//            Spread spreadResult = BeanMapUtils.map2Bean(map,Spread.class);
//            spreadList.add(spreadResult);
//        }
//        List<Map> countMaps = this.nativeQueryToMapList(sqlCount);
//        Map countMap = countMaps.get(0);
//        Long totalElements = Long.parseLong(countMap.get("count").toString());
//        bpr.setTotalCount(totalElements);
//        bpr.setResult(spreadList);
//        return bpr;
//    }
//
//    @Override
//    public String getUsernameByPath(String path) throws Exception {
//        String username = null;
//        String sql ="select t2.name as username from t_url_path t1 left join t_employee t2 on t1.employee_id" +
//                "=t2.id where t1.path = '@path'";
//        sql = sql.replace("@path", path);
//        List<Map> maps = this.nativeQueryToMapList(sql);
//        if (maps != null && maps.size() > 0) {
//            username = (String) maps.get(0).get("username");
//        }
//        return username;
//    }

}
