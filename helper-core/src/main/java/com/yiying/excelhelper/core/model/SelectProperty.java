package com.yiying.excelhelper.core.model;

/**
 * @Description:
 * @Author: yiying.xuan
 * @Date: 2021/6/25
 */

public class SelectProperty {
    private String tableName;
    private String tableColumn;
    private String querySql;
    private int excelColumn;

    public SelectProperty(String tableName, String tableColumn, String querySql, int excelColumn) {
        this.tableName = tableName;
        this.tableColumn = tableColumn;
        this.querySql = querySql;
        this.excelColumn = excelColumn;
    }

    public String getQuerySql() {
        return querySql;
    }

    public SelectProperty(String tableName, String columnName, int excelColumn) {
        this.tableColumn = columnName;
        this.tableName = tableName;
        this.excelColumn = excelColumn;
    }

    public String getTableName() {
        return tableName;
    }

    public String getTableColumn() {
        return tableColumn;
    }

    public int getExcelColumn() {
        return excelColumn;
    }
}
