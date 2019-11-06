package com.cloud.spring;

import java.util.concurrent.TimeUnit;

public class EchoMethod {
    /**
     * 模拟阻塞方法
     *
     * @param str
     * @param delay
     * @param timeUnit
     * @return
     */
    public static String echoAfterTime(String str, int delay, TimeUnit timeUnit) {
        try {
            timeUnit.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return str;
    }

}
