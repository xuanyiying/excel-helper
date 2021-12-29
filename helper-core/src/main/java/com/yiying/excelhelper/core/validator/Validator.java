package com.yiying.excelhelper.core.validator;


import com.yiying.excelhelper.core.read.ExcelReadContext;

/**
 * @Description:
 * @Author: yiying.xuan
 * @Date: 2021/7/26
 */
public interface Validator<T> {

    /**
     * 数据校验
     * @param data
     * @param row
     * @param readContext
     */
     void validate(T data, int row, ExcelReadContext<T> readContext);
}
