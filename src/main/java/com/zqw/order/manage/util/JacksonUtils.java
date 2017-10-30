package com.zqw.order.manage.util;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

public class JacksonUtils {
    private static ObjectMapper om = new ObjectMapper();
    /**
     * 将对象转换为json格式字符串
     *
     * @param
     * @return json string
     */
    public static String toJSON(Object obj) throws Exception{
        ObjectMapper om = new ObjectMapper();
        String json = om.writeValueAsString(obj);
        return json;

    }

    /**
     *
     * 将json形式字符串转换为java实体类
     *
     */
    public static <T> T parse(String jsonStr, Class<T> clazz) throws Exception{
        T readValue = null;
        readValue = om.readValue(jsonStr, clazz);
        return readValue;
    }

    public static <T> List<T> parseList(String jsonStr, Class<T> clazz) throws Exception{
        JavaType javaType = getCollectionType(ArrayList.class, clazz);
        List<T> list =  om.readValue(jsonStr, javaType);   //这里不需要强制转换
        return  list;
    }
    public static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        return om.getTypeFactory().constructParametricType(collectionClass, elementClasses);
        }
}
