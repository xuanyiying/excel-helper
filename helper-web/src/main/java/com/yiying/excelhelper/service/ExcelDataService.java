package com.yiying.excelhelper.service;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * @Description:
 * @Author: yiying.xuan
 * @Date: 2021/6/30
 */
public interface ExcelDataService {

    /**
     * 导出Excel模板
     * @param outputStream
     * @param fileName
     * @param modelClass
     * @throws Exception
     */
    void exportExcelTemplate(OutputStream outputStream, String fileName, Class<?> modelClass) throws Exception;

    /**
     * 导出Excel数据
     * @param outputStream
     * @param modelClass
     * @param data
     * @throws Exception
     */
    void write(OutputStream outputStream,  Class<?> modelClass, List<?> data) throws Exception;

    /**
     *  读取excel数据并作处理（保存到数据库）
     * @param inputStream
     * @param modelClass
     */
    void read(InputStream inputStream, Class<?> modelClass);
}
