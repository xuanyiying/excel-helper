package com.yiying.excelhelper.core.write;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.HeadFontStyle;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.yiying.excelhelper.core.model.ExcelHeadProperty;
import com.yiying.excelhelper.core.model.SelectProperty;
import com.yiying.excelhelper.core.annoation.CustomExcelHead;
import com.yiying.excelhelper.core.annoation.DataType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static org.apache.poi.ss.usermodel.Font.COLOR_RED;


/**
 * @Description:
 * @Author: yiying.xuan
 * @Date: 2021/6/25
 */
@Component
public class ExcelTemplateBuilder {

    public static final String DATE_FORMAT = "yyyy-mm-dd";

    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelTemplateBuilder.class);

    public static final String REQUIRED_COMMENTS = "此项为必填项";

    List<SelectProperty> needSelectProperties;

    private Class<?> model;

    private String templateName;

    private ExcelTypeEnum excelType;

    final DataSelector dataSelector;

    public ExcelTemplateBuilder(DataSelector dataSelector) {
        this.dataSelector = dataSelector;
    }

    public ExcelTemplateBuilder model(Class<?> model) {
        this.model = model;
        return this;
    }

    public String getTemplateName() {
        return templateName;
    }

    public ExcelTemplateBuilder templateName(String templateName) {
        this.templateName = templateName;
        return this;
    }

    public ExcelTypeEnum getExcelType() {
        return excelType;
    }

    public ExcelTemplateBuilder excelType(ExcelTypeEnum excelType) {
        this.excelType = excelType;
        return this;
    }


    private List<ExcelHeadProperty> headProperties;


    private void init() throws Exception {
        if (Objects.isNull(model)) {
            return;
        }
        if (Objects.isNull(excelType)) {
            excelType = ExcelTypeEnum.XLSX;
        }
        if (StrUtil.isEmpty(templateName)) {
            templateName = Objects.requireNonNull(this.getClass().getClassLoader().getResource("/")).getPath()
                    + "普通健康体检模板" + excelType;
        }
        needSelectProperties = new LinkedList<>();
        headProperties = new LinkedList<>();
        Field[] propertyFields = model.getDeclaredFields();
        for (Field field : propertyFields) {
            handleAnnotation(field);
        }
    }

    /**
     * @param field
     * @throws Exception
     */
    private void handleAnnotation(Field field) throws Exception {
        ExcelHeadProperty excelHeadProperty = new ExcelHeadProperty();
        CustomExcelHead customProperty = field.getAnnotation(CustomExcelHead.class);
        ExcelProperty property = field.getAnnotation(ExcelProperty.class);
        HeadFontStyle headFontStyle = field.getAnnotation(HeadFontStyle.class);
        DateTimeFormat dateProperty = field.getAnnotation(DateTimeFormat.class);
        String headTitle = property.value()[0];
        excelHeadProperty.setHeadTitle(headTitle);
        excelHeadProperty.setColumn(property.index());
        if (null != headFontStyle && headFontStyle.color() == COLOR_RED) {
            excelHeadProperty.setComments(REQUIRED_COMMENTS);
        }
        if (null != dateProperty) {
            String dateFormat = dateProperty.value();
            if (StrUtil.isEmpty(dateFormat)) {
                dateFormat = DATE_FORMAT;
            }
            excelHeadProperty.setDateFormat(dateFormat);
        }
        handleProperty(excelHeadProperty, customProperty, property, field, headTitle);
        headProperties.add(excelHeadProperty);
    }

    private void handleProperty(ExcelHeadProperty excelHeadProperty, CustomExcelHead customProperty,
                                ExcelProperty property, Field field, String headTitle) throws Exception {
        if (Objects.nonNull(customProperty)) {
            String comment = customProperty.comment();
            if (!StrUtil.isEmpty(comment)) {
                excelHeadProperty.setComments(comment);
            }
            DataType dataType = customProperty.type();
            if (DataType.SElECT.equals(dataType)) {
                List<String> values = CommonData.get(headTitle);
                if (CollUtil.isNotEmpty(values)) {
                    excelHeadProperty.setSelectedValues(CommonData.get(headTitle));
                    return;
                }
                String querySql = customProperty.querySql();
                String tableName = customProperty.tableName();
                String columnName = customProperty.columnName();
                boolean notNull = StrUtil.isNotEmpty(querySql)
                        || (StrUtil.isNotEmpty(tableName) && StrUtil.isNotEmpty(columnName));
                if (notNull) {
                    SelectProperty definition =
                            new SelectProperty(tableName, columnName, querySql, property.index());
                    needSelectProperties.add(definition);
                } else {
                    LOGGER.warn("Field :" + field.getName() + "->" + property.value()[0] + "->" + property.index());
                    throw new Exception("When CustomExcelProperty's type is DataType.SELECT_DB," +
                            " need to specify the database table name and column name, " +
                            "or specify the SQL of all the values.");
                }
            }
        }
    }

    public void build() throws Exception {
        File file = new File(templateName);
        if (file.exists()) {
            file.delete();
        }
        init();
        headProperties = dataSelector.select(needSelectProperties, headProperties);
        DropDownArrowWriteHandler dropDownArrowWriteHandler = new DropDownArrowWriteHandler(headProperties);
        CommentWriteHandler commentWriteHandler = new CommentWriteHandler(headProperties);
        EasyExcel.write(templateName, model)
                .registerWriteHandler(dropDownArrowWriteHandler)
                .registerWriteHandler(commentWriteHandler)
                .sheet("sheet")
                .doWrite(Collections.singletonList(model.newInstance()));
    }
}
