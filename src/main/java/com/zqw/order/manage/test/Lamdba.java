package com.zqw.order.manage.test;

import java.util.function.Consumer;
import java.util.function.Function;

public class Lamdba {
    public static void main(String[] args) {

        new Thread(() -> {
            Function<String,Integer> f1 = (a) -> a.length();
            System.out.println(f1.apply("wegewg"));
            Consumer<String> consumer = a -> {
                System.out.println(a);
            };
            consumer.accept("we");
        }).start();
    }
}
