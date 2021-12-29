package com.yiying.excelhelper.core.annoation;

import java.lang.annotation.*;


/**
 * @Author: yiying.xuan
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface CustomExcelHead {
    /**
     * Cell content write type { input, select, datetime}
     *
     * @return
     */
    DataType type() default DataType.INPUT;

    /**
     * need to add a comment
     *
     * @return
     */
    String comment() default "";

    /**
     * @return
     */
    String tableName() default "";

    /**
     * @return
     */
    String columnName() default "";

    /**
     *
     * @return
     */
    String querySql() default  "";

}
