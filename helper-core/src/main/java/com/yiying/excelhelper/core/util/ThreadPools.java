package com.yiying.excelhelper.core.util;


import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author: yiying
 * @Date: 2021/10/9
 */

public class ThreadPools {

    public static ThreadPoolExecutor newThreadPoolExecutor() {
        return new ThreadPoolExecutor(getPoolSize(), getPoolSize(), 30, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
    }

    public static ThreadPoolExecutor newThreadPoolExecutor(int nThread) {
        return new ThreadPoolExecutor(getPoolSize(), nThread, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<>());

    }

    private static int getPoolSize() {
        return Runtime.getRuntime().availableProcessors() + 1;
    }

    /**
     * 最佳线程数目 = （线程等待时间与线程CPU时间之比 + 1）* CPU数目
     *
     * @return
     */
    public static int getMaxPoolSize() {
        int cpuNum = Runtime.getRuntime().availableProcessors();
        return 2 * cpuNum + 1;
    }
}

