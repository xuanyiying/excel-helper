package com.yiying.excelhelper.core.read;

import com.yiying.excelhelper.core.handler.Handler;
import com.yiying.excelhelper.core.model.DataValidatedInfo;
import com.yiying.excelhelper.core.validator.Validator;

import java.util.List;

/**
 * @Description:
 * @Author: yiying.xuan
 * @Date: 2021/10/8
 */
public interface ExcelReadContext<T> {
    /**
     * 注册数据处理器到上下文
     * @param Handlers
     */
    void registerHandlers(Handler...Handlers);

    /**
     *
     * @throws Exception
     */
    void handle();
    /**
     * 数据校验
     * @param data
     * @param row
     */
    void validate(T data, Integer row);
    /**
     * 缓存数据
     * @param data
     */
    void doCacheData(T data);

    /**
     * 注册数据验证器
     * @param validators
     */
    void registerValidators(Validator...validators);

    /**
     * 获取校验异常信息
     * @param data
     * @return
     */
    List<DataValidatedInfo> getValidatedInfos(T data);
    /**
     * 获取缓存的数据
     * @return
     */
    List<T> getData();

    /**
     * 获取所有的验证信息
     * @return
     */
    List<DataValidatedInfo> getAllValidatedInfos();
}
