package com.zqw.order.manage.test;

import java.util.concurrent.Callable;

public class Test {
    public static void main(String[] args) {
        Callable<String> call = () -> "hello";
        try {
            call.call();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
