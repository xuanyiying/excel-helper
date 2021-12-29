package com.yiying.excelhelper.core.task;

import com.yiying.excelhelper.core.util.SqlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

/**
 * @author yiying
 */
public class SingleTableInsertTask<E> extends AbstractInsertTask<E> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SingleTableInsertTask.class);

    public SingleTableInsertTask(List<E> data, JdbcTemplate jdbcTemplate) {
        super(data, jdbcTemplate);
    }

    @Override
    public void run() {
        try {
            jdbcTemplate.batchUpdate(SqlUtil.buildBatchInsertSql(data));
        } catch (Exception e) {
            LOGGER.error("{}", e);
        }
    }
}
