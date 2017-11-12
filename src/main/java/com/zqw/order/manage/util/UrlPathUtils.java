package com.zqw.order.manage.util;

import java.util.UUID;

public class UrlPathUtils {
    public static String createNewPath(String username){
        String result = username;
        result += UUID.randomUUID();
        return result;
    }
}
