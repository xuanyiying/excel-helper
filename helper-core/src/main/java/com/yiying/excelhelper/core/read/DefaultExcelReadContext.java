package com.yiying.excelhelper.core.read;


import cn.hutool.core.collection.CollUtil;
import com.yiying.excelhelper.core.handler.Handler;
import com.yiying.excelhelper.core.model.DataValidatedInfo;
import com.yiying.excelhelper.core.validator.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @Description:
 * @Author: yiying.xuan
 * @Date: 2021/7/12
 */
@Component
public class DefaultExcelReadContext<Model> implements ExcelReadContext<Model> {

     private static final Logger LOGGER = LoggerFactory.getLogger(DefaultExcelReadContext.class);
    /**
     * 数据验证器
     */
    private List<Validator> validators;
    /**
     * 数据处理器
     */
    private Vector<Handler> handlers;
    /**
     * 数据缓存
     */
    private List<Model> dataCaches;
    /**
     * 数据检验异常信息
     */
    private ConcurrentMap<Model, List<DataValidatedInfo>> validatedInfos;

    public DefaultExcelReadContext() {
        this.handlers = new Vector<>();
        this.dataCaches = new LinkedList<>();
        this.validators = new Vector<>();
        this.validatedInfos = new ConcurrentHashMap<>();
    }

    @Override
    public void registerHandlers(Handler... handler) {
        CollUtil.addAll(handlers, handler);
    }

    @Override
    public void handle() {
        if (CollUtil.isEmpty(handlers)) {
            return;
        }
       handlers.forEach(handler -> handler.handle(this));
    }

    @Override
    public void validate(Model data, Integer row) {
        if (CollUtil.isEmpty(validators)) {
            return;
        }
        validators.forEach(validator -> validator.validate(data,row,this));
    }


    @Override
    public void doCacheData(Model data) {
        dataCaches.add(data);
    }

    @Override
    public List<Model> getData() {
        return dataCaches;
    }

    @Override
    public List<DataValidatedInfo> getValidatedInfos(Model data) {
        List<DataValidatedInfo> infos = this.validatedInfos.get(data);
        if (Objects.isNull(validatedInfos)) {
            infos = new LinkedList<>();
            validatedInfos.put(data, infos);
        }
        return infos;
    }

    @Override
    public List<DataValidatedInfo> getAllValidatedInfos() {
        Collection<List<DataValidatedInfo>> values = this.validatedInfos.values();
        if (CollUtil.isEmpty(values)) {
            return new LinkedList<>();
        }
        return values.stream().reduce(new LinkedList<>(), (all, item) -> {
            all.addAll(item);
            return all;
        });
    }

    @Override
    public void registerValidators(Validator... validator) {
        CollUtil.addAll(validators, validator);
    }

}
