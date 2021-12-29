package com.yiying.excelhelper.core.write;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.excel.write.handler.AbstractSheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import com.yiying.excelhelper.core.model.ExcelHeadProperty;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;

import java.util.List;

/**
 * @Author: yiying.xuan
 * @date: 2021/6/25 10:52
 */
public class DropDownArrowWriteHandler extends AbstractSheetWriteHandler {
    private static final int DROP_DOWN_ARROW_MAX_SIZE = 60;
    private List<ExcelHeadProperty> headProperties;

    public DropDownArrowWriteHandler(List<ExcelHeadProperty> headProperties) {
        this.headProperties = headProperties;
    }

    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        Sheet sheet = writeSheetHolder.getSheet();
        for (ExcelHeadProperty property : headProperties) {
            handleDropDownArrow(property, sheet);
        }
    }

    /**
     * @param column
     * @return String
     * @Description return excel column label A-Z-AA-ZZ
     */
    private String getExcelLine(int column) {
        String line = "";
        int first = column / 26;
        int second = column % 26;
        if (first > 0) {
            line = (char) ('A' + first - 1) + "";
        }
        line += (char) ('A' + second) + "";
        return line;
    }

    /**
     * @param property
     * @param sheet
     * @Description handle excel cell drop down arrow
     */
    private void handleDropDownArrow(ExcelHeadProperty property, Sheet sheet) {
        List<?> selectedValues = property.getSelectedValues();
        int column = property.getColumn();
        DataValidation validation;
        DataValidationConstraint constraint;
        DataValidationHelper helper = sheet.getDataValidationHelper();
        String sheetName = property.getHeadTitle() + "下拉框内容";
        if (CollUtil.isEmpty(selectedValues)) {
            return;
        }
        CellRangeAddressList rangeList = new CellRangeAddressList(1,
                65536, column, column);
        String[] values = selectedValues.toArray(new String[selectedValues.size()]);
        // 如果下拉值总数大于DROP_DOWN_ARROW_MAX_SIZE，则使用一个新sheet存储，避免生成的导入模板下拉值获取不到
        if (values.length > DROP_DOWN_ARROW_MAX_SIZE) {
            //1.创建一个隐藏的sheet 名称为 property.getHeadTitle() + "下拉框内容";
            Workbook workbook = sheet.getWorkbook();
            Sheet hiddenSheet = workbook.createSheet(sheetName);
            for (int i = 0, length = values.length; i < length; i++) {
                hiddenSheet.createRow(i).createCell(column).setCellValue(values[i]);
            }
            Name categoryName = workbook.createName();
            categoryName.setNameName(sheetName);
            String excelLine = getExcelLine(column);
            String refers = sheetName + "!$" + excelLine + "$1:$" + excelLine + "$" + (values.length + 1);
            // 将刚才设置的sheet引用到你的下拉列表中
            categoryName.setRefersToFormula(refers);
            // 设置存储下拉列值得sheet为隐藏
            int hiddenIndex = workbook.getSheetIndex(sheetName);
            if (!workbook.isSheetHidden(hiddenIndex)) {
                workbook.setSheetHidden(hiddenIndex, true);
            }
            constraint = helper.createFormulaListConstraint(refers);
        } else {
            constraint = helper.createExplicitListConstraint(values);
        }
        validation = helper.createValidation(constraint, rangeList);
        sheet.addValidationData(validation);
        // 处理Excel兼容性问题
        if (validation instanceof XSSFDataValidation) {
            validation.setSuppressDropDownArrow(true);
            validation.setShowErrorBox(true);
        } else {
            validation.setSuppressDropDownArrow(false);
        }
        // 阻止输入非下拉选项的值
        validation.setErrorStyle(DataValidation.ErrorStyle.STOP);
        validation.setShowErrorBox(true);
        validation.setSuppressDropDownArrow(true);
        validation.createErrorBox("提示", "此值与单元格定义格式不一致");
        validation.createPromptBox("填写说明：",
                "填写内容只能为下拉数据集中的数据，其他值可能将会导致无法入库");
        sheet.addValidationData(validation);
    }
}
