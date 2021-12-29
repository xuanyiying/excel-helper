package com.yiying.excelhelper.core.write;


import com.yiying.excelhelper.core.model.ExcelHeadProperty;
import com.yiying.excelhelper.core.model.SelectProperty;

import java.util.List;

/**
 * @Description:
 * @Author: yiying.xuan
 * @Date: 2021/6/25
 */
public abstract class AbstractDataSelector implements DataSelector {


    @Override
    public List<ExcelHeadProperty> select(List<SelectProperty> definitions,
                                          List<ExcelHeadProperty> headProperties) {
        for (SelectProperty definition : definitions) {
            for (ExcelHeadProperty property : headProperties) {
                if (property.getColumn() == definition.getExcelColumn()) {
                    List<String> values = selectValues(property, definition);
                    property.setSelectedValues(values);
                }
            }
        }
        return headProperties;
    }

    /**
     * 从数据库查找下拉框的值
     * @param property
     * @param definition
     * @return
     */
    protected abstract List<String> selectValues(ExcelHeadProperty property, SelectProperty definition);
}
