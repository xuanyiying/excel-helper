package com.yiying.excelhelper.core.task;

import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

/**
 * @author yiying
 */
public abstract class AbstractInsertTask<E> implements Runnable {

    protected List<E> data;

    protected JdbcTemplate jdbcTemplate;

    public AbstractInsertTask(List<E> data, JdbcTemplate jdbcTemplate) {
        this.data = data;
        this.jdbcTemplate = jdbcTemplate;
    }


}
