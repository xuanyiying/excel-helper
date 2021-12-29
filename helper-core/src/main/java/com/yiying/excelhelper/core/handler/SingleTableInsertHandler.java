package com.yiying.excelhelper.core.handler;

import com.yiying.excelhelper.core.read.ExcelReadContext;
import com.yiying.excelhelper.core.task.SingleTableInsertTask;
import com.yiying.excelhelper.core.util.ThreadPools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Description:
 * @Author: yiying.xuan
 * @Date: 2021/10/14
 */

@Component
public class SingleTableInsertHandler extends AbstractHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(SingleTableInsertHandler.class);

    protected JdbcTemplate jdbcTemplate;

    public SingleTableInsertHandler(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * 构建数据并保存到数据库
     *
     * @param readContext
     */
    @Override
    public void handle(ExcelReadContext readContext) {
        super.submitTask(readContext);
    }

    /**
     * @param data
     * @throws Exception
     */
    @Override
    public void handle(Object data) {
        ThreadPoolExecutor poolExecutor = ThreadPools.newThreadPoolExecutor();
        LOGGER.info("start insert {} to database",data);
        poolExecutor.submit(new SingleTableInsertTask<>(Arrays.asList(data),jdbcTemplate));
    }
}

