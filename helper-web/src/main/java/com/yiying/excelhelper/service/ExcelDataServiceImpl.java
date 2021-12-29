package com.yiying.excelhelper.service;


import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.yiying.excelhelper.core.read.DefaultReadListener;
import com.yiying.excelhelper.core.write.ExcelTemplateBuilder;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * @Description:
 * @Author: yiying.xuan
 * @Date: 2021/6/29
 */
@Service
public class ExcelDataServiceImpl implements ExcelDataService {
    final DefaultReadListener readListener;

    final ExcelTemplateBuilder templateBuilder;

    public ExcelDataServiceImpl(DefaultReadListener readListener, ExcelTemplateBuilder templateBuilder) {
        this.readListener = readListener;
        this.templateBuilder = templateBuilder;
    }

    /**
     * 读取excel 数据并作处理（保存到数据库）
     *
     * @param inputStream
     * @param modelClass
     */
    @Override
    public void read(InputStream inputStream,
                     Class<?> modelClass) {
        EasyExcel.read(inputStream, modelClass, readListener).sheet().doRead();
    }

    /**
     * @param outputStream
     * @param modelClass
     */
    @Override
    public void exportExcelTemplate(OutputStream outputStream, String fileName, Class<?> modelClass)
            throws Exception {
        templateBuilder.model(modelClass).templateName(fileName)
                .excelType(ExcelTypeEnum.XLSX).build();
    }

    /**
     * 导出Excel数据
     *
     * @param outputStream
     * @param data
     * @throws Exception
     */
    @Override
    public void write(OutputStream outputStream, Class<?> modelClass,List<?> data) {
        EasyExcel.write(outputStream, modelClass)
                .excelType(ExcelTypeEnum.XLSX)
                .sheet("sheet")
                .doWrite(data);
    }

}
