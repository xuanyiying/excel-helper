package com.yiying.excelhelper.core.handler;

import cn.hutool.core.collection.CollUtil;
import com.yiying.excelhelper.core.model.Model;
import com.yiying.excelhelper.core.read.ExcelReadContext;
import com.yiying.excelhelper.core.task.SingleTableInsertTask;
import com.yiying.excelhelper.core.util.ThreadPools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author yiying
 */
public abstract class AbstractHandler implements Handler {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractHandler.class);

    private static final int BATCH_INSERT_MAX_SIZE = 10000;
    public JdbcTemplate jdbcTemplate;

    protected List slice(List<Model> data) {
        List<List<Model>> subLists = new ArrayList<>();
        if (CollUtil.isEmpty(data)) {
            return new ArrayList();
        }
        int size = data.size();
        int nThread = getThreadNum(size);
        int end;
        int start;
        for (int i = 0; i < nThread; i++) {
            start = i * nThread;
            end = (i == nThread - 1) ? size : nThread * (i + 1);
            subLists.add(CollUtil.sub(data, start, end));
        }
        return subLists;
    }

    protected void submitTask(ExcelReadContext<Model> readContext) {
        List<Model> data = readContext.getData();
        int threadNum = getThreadNum(data.size());
        ThreadPoolExecutor poolExecutor = ThreadPools.newThreadPoolExecutor(threadNum);
        CountDownLatch latch = new CountDownLatch(threadNum);
        List<List<Model>> slices = slice(data);
        try {
            slices.forEach(slice -> {
                poolExecutor.submit(new SingleTableInsertTask<>(slice, jdbcTemplate));
                latch.countDown();
            });
            latch.await();
        } catch (InterruptedException e) {
            LOGGER.error("{}", e);
        }
    }

    protected int getThreadNum(int size) {
        int nThread = size / BATCH_INSERT_MAX_SIZE;
        nThread = (size % BATCH_INSERT_MAX_SIZE == 0) ? nThread : nThread + 1;
        return Math.min(nThread, ThreadPools.getMaxPoolSize());
    }
}
