package com.yiying.excelhelper.model;


import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.HeadFontStyle;
import com.yiying.excelhelper.core.annoation.CustomExcelHead;
import com.yiying.excelhelper.core.annoation.DataType;
import com.yiying.excelhelper.core.annoation.TableName;
import com.yiying.excelhelper.core.model.Model;
import lombok.Data;
import org.apache.poi.ss.usermodel.Font;

import java.time.LocalDate;


/**
 *
 * @Author: yiying.xuan
 */
@Data
@TableName("t_user")
public class ExcelDataModel implements Model {

    @ExcelProperty(value = ExcelConstants.NAME, index = 0)
    @HeadFontStyle(color = Font.COLOR_RED,fontHeightInPoints = 11)
    private String name;

    @ExcelProperty(value = ExcelConstants.SEX, index = 1)
    @HeadFontStyle(color = Font.COLOR_RED,fontHeightInPoints = 11)
    @CustomExcelHead(type = DataType.SElECT)
    private String sex;

    @ExcelProperty(value = ExcelConstants.AGE, index = 2)
    @HeadFontStyle(color = Font.COLOR_RED,fontHeightInPoints = 11)
    private Integer age;

    @ExcelProperty(value = ExcelConstants.BIRTHDAY, index = 3)
    @DateTimeFormat(ExcelConstants.DATE_FORMAT)
    private LocalDate birthday;

    @ExcelProperty(value = ExcelConstants.MARITAL_STATUS, index = 4)
    @CustomExcelHead(type = DataType.SElECT)
    private String maritalStatus;

    @ExcelProperty(value = ExcelConstants.PHONE_NUMBER, index = 5)
    private String phoneNumber;

    @ExcelProperty(value = ExcelConstants.CERTIFICATE_NUM, index = 6)
    private String idCardNum;

    @ExcelProperty(value = ExcelConstants.EMAIL, index = 7)
    private String email;

    @ExcelProperty(value = ExcelConstants.NOTE, index = 8)
    private String note;

    @ExcelProperty(value = ExcelConstants.NATION, index = 9)
    @CustomExcelHead(type = DataType.SElECT)
    private String nation;

    @ExcelProperty(value = ExcelConstants.ADDRESS, index = 10)
    private String address;
}
