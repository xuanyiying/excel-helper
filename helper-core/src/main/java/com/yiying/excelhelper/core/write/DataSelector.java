package com.yiying.excelhelper.core.write;

import com.yiying.excelhelper.core.model.ExcelHeadProperty;
import com.yiying.excelhelper.core.model.SelectProperty;

import java.util.List;

/**
 * @description:
 * @Author: yiying.xuan
 * @date: 2021/7/5
 */
public interface DataSelector {
    /**
     * select data from  database
     * @return
     * @param definitions
     * @param headProperties
     */
    List<ExcelHeadProperty> select(List<SelectProperty> definitions, List<ExcelHeadProperty> headProperties);
}
