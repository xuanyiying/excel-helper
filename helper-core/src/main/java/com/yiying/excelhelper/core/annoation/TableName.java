package com.yiying.excelhelper.core.annoation;

import java.lang.annotation.*;

/**
 * @Description:
 * @Author: yiying.xuan
 * @Date: 2021/10/14
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface TableName {
    String value() default "";
}
