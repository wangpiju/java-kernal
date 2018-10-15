package com.hs3.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * program: java-kernal
 * des:
 * author: Terra
 * create: 2018-08-12 17:35
 **/
public class ThreadFactory {
    public static ExecutorService ES = Executors.newFixedThreadPool(6);
    public static ScheduledExecutorService ES_INTERVAL = Executors.newScheduledThreadPool(6);
}
