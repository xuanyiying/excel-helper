package com.yiying.excelhelper.core.util;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author: yiying
 * @Date: 2021/10/9
 */

public class ThreadPools {


    public static ThreadPoolExecutor newThreadPoolExecutor(String name) {
        return new ThreadPoolExecutor(1, getCorePoolSize(), 30, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(), getThreadFactory(name));
    }

    public static ThreadPoolExecutor newThreadPoolExecutor(int nThread, String name) {
        return new ThreadPoolExecutor(getCorePoolSize(), nThread, 30, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(), getThreadFactory(name));
    }

    public static int getCorePoolSize() {
        return Runtime.getRuntime().availableProcessors() * 2 + 1;
    }

    public static ThreadPoolExecutor newThreadPoolExecutor(int coreSize, int maxSize, String name) {
        return new ThreadPoolExecutor(coreSize, maxSize, 30, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(), getThreadFactory(name));
    }

    public static int getMaxPoolSize(int total, int size) {
        int nThread = total / size;
        return (total % size == 0) ? nThread : nThread + 1;
    }

    public static <T> List<List<T>> slice(List<T> data, int maxPoolSize, int sliceSize) {
        List<List<T>> subLists = new ArrayList<>();
        if (CollUtil.isEmpty(data)) {
            return new ArrayList();
        }
        int end;
        int start;
        for (int i = 0; i < maxPoolSize; i++) {
            start = i * sliceSize;
            end = (i == maxPoolSize - 1) ? data.size() : sliceSize * (i + 1);
            subLists.add(CollUtil.sub(data, start, end));
        }
        return subLists;
    }

    private static ThreadFactory getThreadFactory(String name) {
        if (StrUtil.isBlank(name)) {
            name = "Default";
        }
        return new CustomizableThreadFactory("Custom-Thread-Pool-" + name + "-");
    }
}

