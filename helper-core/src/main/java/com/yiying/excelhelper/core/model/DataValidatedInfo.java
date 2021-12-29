package com.yiying.excelhelper.core.model;


/**
 * @Description:
 * @Author: yiying.xuan
 * @Date: 2021/6/28
 */
public class DataValidatedInfo {
    private String errorMsg;

    private int row;

    private int column;

    public DataValidatedInfo(Builder builder) {
        this.errorMsg = builder.errorMsg;
        this.row = builder.row;
        this.column = builder.column;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
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

    public static Builder getBuilder(){
        return new Builder();
    }
    public static class Builder {
        private String errorMsg;
        private int row;
        private int column;

        public Builder row(int row) {
            this.row = row;
            return this;
        }

        public  Builder errorMsg(String errorMsg) {
            this.errorMsg = errorMsg;
            return this;
        }

        public Builder column(int column) {
            this.column = column;
            return this;
        }

        public DataValidatedInfo build() {
            return new DataValidatedInfo(this);
        }
    }

}
