package com.yiying.excelhelper.core.validator;


import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.ReflectUtil;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.HeadFontStyle;
import com.yiying.excelhelper.core.model.DataValidatedInfo;
import com.yiying.excelhelper.core.read.ExcelReadContext;
import org.apache.poi.ss.usermodel.Font;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

/**
 * @Description:
 * @Author: yiying.xuan
 * @Date: 2021/10/11
 */
public abstract class AbstractValidator<T>  implements Validator<T> {

    /**
     *
     * @param data
     * @param property
     * @return
     */
    private int getColumn(T data, String property) {
        if (Objects.isNull(data) || StrUtil.isBlank(property)) {
            return -1;
        }
        Field field = ReflectUtil.getField(data.getClass(), property);
        if (Objects.isNull(field)) {
            return -1;
        }
        ExcelProperty annotation = AnnotationUtil.getAnnotation(field, ExcelProperty.class);
        return Objects.isNull(annotation) ? -1 : annotation.index();
    }

    /**
     *
     * @param data
     * @param row
     * @param readContext
     * @param msg
     * @param property
     */
    protected void buildValidatedInfo(T data, int row, ExcelReadContext<T> readContext,
                                      String msg, String property) {
        List<DataValidatedInfo> validatedInfos = readContext.getValidatedInfos(data);
        DataValidatedInfo validatedInfo = DataValidatedInfo.getBuilder()
                .errorMsg(msg).row(row).column(getColumn(data, property)).build();
        validatedInfos.add(validatedInfo);
    }

    /**
     * 校验必填选项
     *
     * @param data
     * @param row
     * @param readContext
     * @throws IllegalAccessException
     */
    protected void validateRequiredCell(T data, Integer row, ExcelReadContext<T> readContext) {
        Class<T> dataClass = (Class<T>) data.getClass();
        Field[] fields = dataClass.getDeclaredFields();
        for (Field property : fields) {
            ExcelProperty excelProperty = property.getAnnotation(ExcelProperty.class);
            String columnName = excelProperty.value()[0];
            int column = excelProperty.index();
            HeadFontStyle headFontStyle = property.getAnnotation(HeadFontStyle.class);
            Object value = ReflectUtil.getFieldValue(data, property);
            if (Objects.nonNull(headFontStyle) && headFontStyle.color() == Font.COLOR_RED) {
                if (Objects.nonNull(value) || StrUtil.isNotBlank(value.toString())) {
                    return;
                }
                String msg = columnName + "为必填项，第" + column + "列第" + row + "行为空";
                buildValidatedInfo(data, row, readContext, msg, property.getName());
            }
        }
    }

}
