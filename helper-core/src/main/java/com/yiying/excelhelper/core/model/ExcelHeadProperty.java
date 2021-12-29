package com.yiying.excelhelper.core.model;


import java.util.List;

/**
 * @Author: yiying.xuan
 */
public class ExcelHeadProperty {
    /**
     * excel head title
     */
    String headTitle;
    /**
     * the index of row, start with 0
     */
    int row;
    /**
     * the index of column, start with 0
     */
    int column;
    /**
     * cell comments
     */
    String comments;
    /**
     * values of the drop down box
     */
    List<?> selectedValues;
    /**
     * 日期格式
     */
    String dateFormat;

    public String getHeadTitle() {
        return headTitle;
    }

    public void setHeadTitle(String headTitle) {
        this.headTitle = headTitle;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public List<?> getSelectedValues() {
        return selectedValues;
    }

    public void setSelectedValues(List<?> selectedValues) {
        this.selectedValues = selectedValues;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }
    @Override
    public String toString() {
        return "ExcelHeadDefinition{" +
                "headTitle='" + headTitle + '\'' +
                ", row=" + row +
                ", column=" + column +
                ", comments='" + comments + '\'' +
                ", selectedValues=" + selectedValues +
                ", dateFormat='" + dateFormat + '\'' +
                '}';
    }

}
