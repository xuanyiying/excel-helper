package com.yiying.excelhelper.core.read;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.read.metadata.holder.ReadRowHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 *
 * @Description:
 * @Author: yiying.xuan
 * @Date: 2021/6/28
 */
public class DefaultReadListener<T> extends AnalysisEventListener<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultReadListener.class);

    private final ThreadLocal<ExcelReadContext> excelReadContext = new ThreadLocal<>();

    public DefaultReadListener(ExcelReadContext readContext) {
        this.excelReadContext.set(readContext);
    }
    public void setReadListener(ExcelReadContext readContext) {
        this.excelReadContext.set(readContext);
    }

    /**
     * When analysis one row trigger invoke function.
     *
     * @param data    one row value. Is is same as {@link AnalysisContext#readRowHolder()}
     * @param context
     */
    @Override
    public void invoke(T data, AnalysisContext context) {
        if (Objects.isNull(data)) {
            return;
        }
        ReadRowHolder rowHolder = context.readRowHolder();
        Integer row = rowHolder.getRowIndex();
        ExcelReadContext readContext = this.excelReadContext.get();
        try {
            readContext.validate(data,row);
            if (CollUtil.isEmpty(readContext.getValidatedInfos(data))){
                readContext.doCacheData(data);
            }
            readContext.handle();
        } catch (Exception e) {
            LOGGER.error("读取Excel，解析数据遇到异常 data:{} \n error: {}", data, e);
        }
    }

    /**
     * if have something to do after all analysis
     *
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        ExcelReadContext readContext = this.excelReadContext.get();
        readContext.handle();
    }
}