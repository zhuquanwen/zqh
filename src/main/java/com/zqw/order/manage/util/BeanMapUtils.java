package com.zqw.order.manage.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;

public class BeanMapUtils {
    public static <T, K, V> T map2Bean(Map<K, V> mp, Class<T> beanCls)
            throws Exception{
        T t = null;


            BeanInfo beanInfo = Introspector.getBeanInfo(beanCls);
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

            t = beanCls.newInstance();

            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();

                if (mp.containsKey(key)) {
                    Object value = mp.get(key);
                    Method setter = property.getWriteMethod();// Java中提供了用来访问某个属性的
                    // getter/setter方法
                    setter.invoke(t, value);
                }
            }


        return t;
    }
    public static <T, K, V> Map<String, Object> bean2Map(T bean, Map<String, Object> mp)
            throws Exception {

        if (bean == null) {
            return null;
        }


            BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();

                if (!key.equals("class")) {

                    Method getter = property.getReadMethod();// Java中提供了用来访问某个属性的
                    // getter/setter方法
                    Object value;

                    value = getter.invoke(bean);
                    mp.put(key, value);
                }

            }


        return mp;

    }

}
