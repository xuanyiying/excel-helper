package com.yiying.excelhelper.core.util;

import cn.hutool.core.util.ReflectUtil;
import com.yiying.excelhelper.core.annoation.NotTableField;
import com.yiying.excelhelper.core.annoation.TableName;


import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author yiying
 */
public class SqlUtil {
    private static final Pattern LINE_PATTERN = Pattern.compile("[A-Z]");
    private static final String UNDERLINE = "_";

    public static String column(String property) {
        Matcher matcher = LINE_PATTERN.matcher(property);
        while (matcher.find()) {
            String old = matcher.group();
            String name = matcher.group().toLowerCase();
            property = property.replace(old, UNDERLINE + name);
        }
        if (property.startsWith(UNDERLINE)) {
            property = property.substring(1);
        }
        return property.toUpperCase();
    }

    public static <E> String buildBatchInsertSql(List<E> data) {
        StringBuilder sqlBuf = new StringBuilder();
        Class<?> clazz = data.get(0).getClass();
        TableName annotation = clazz.getAnnotation(TableName.class);
        String tableName = Objects.nonNull(annotation) ? annotation.value() : clazz.getSimpleName();
        Field[] fields = ReflectUtil.getFields(clazz);
        sqlBuf.append(" INSERT INTO ").append(tableName).append("( ");
        for (Field field : fields) {
            if (Objects.isNull(field.getAnnotation(NotTableField.class))) {
                sqlBuf.append(column(field.getName())).append(",");
            }
        }
        sqlBuf.append(")").append(" VALUES (");
        for (E item : data) {
            Object[] values = ReflectUtil.getFieldsValue(item);
            for (Object value : values) {
                if (value instanceof String) {
                    sqlBuf.append("'" + value + "'").append(",");
                } else {
                    sqlBuf.append(value).append(",");
                }
            }
            sqlBuf.append("),(");
        }
        sqlBuf.append(")");
        return sqlBuf.toString().replace(",)", ")")
                .replace(",()", "");
    }
}
