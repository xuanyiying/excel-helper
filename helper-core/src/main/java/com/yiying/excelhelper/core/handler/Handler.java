package com.yiying.excelhelper.core.handler;

import com.yiying.excelhelper.core.read.ExcelReadContext;

/**
 * @Description:
 * @Author: yiying.xuan
 * @Date: 2021/9/29
 */
public interface Handler<T> {
    /**
     * 处理批量导入数据
     *
     * @param readContext
     */
    void handle(ExcelReadContext<T> readContext);

    /**
     * 处理单条数据
     * @param data
     * @throws Exception
     */
    void handle(T data) throws Exception;
}