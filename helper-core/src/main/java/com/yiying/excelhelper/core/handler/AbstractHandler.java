package com.yiying.excelhelper.core.handler;

import com.yiying.excelhelper.core.model.Model;
import com.yiying.excelhelper.core.read.ExcelReadContext;
import com.yiying.excelhelper.core.task.SingleTableInsertTask;
import com.yiying.excelhelper.core.util.ThreadPools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author yiying
 */
public abstract class AbstractHandler implements Handler {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractHandler.class);

    private static final int BATCH_INSERT_MAX_SIZE = 1000;
    public JdbcTemplate jdbcTemplate;


    protected void submitTask(ExcelReadContext<Model> readContext) {
        List<Model> data = readContext.getData();
        int threadNum = ThreadPools.getMaxPoolSize(data.size(), BATCH_INSERT_MAX_SIZE);
        ThreadPoolExecutor poolExecutor = ThreadPools.newThreadPoolExecutor(threadNum, "SingleTableInsertTask");
        CountDownLatch latch = new CountDownLatch(threadNum);
        List<List<Model>> slices = ThreadPools.slice(data, threadNum, BATCH_INSERT_MAX_SIZE);
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
}
