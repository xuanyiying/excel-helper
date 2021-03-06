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
        String sheetName = property.getHeadTitle() + "???????????????";
        if (CollUtil.isEmpty(selectedValues)) {
            return;
        }
        CellRangeAddressList rangeList = new CellRangeAddressList(1,
                65536, column, column);
        String[] values = selectedValues.toArray(new String[selectedValues.size()]);
        // ???????????????????????????DROP_DOWN_ARROW_MAX_SIZE?????????????????????sheet?????????????????????????????????????????????????????????
        if (values.length > DROP_DOWN_ARROW_MAX_SIZE) {
            //1.?????????????????????sheet ????????? property.getHeadTitle() + "???????????????";
            Workbook workbook = sheet.getWorkbook();
            Sheet hiddenSheet = workbook.createSheet(sheetName);
            for (int i = 0, length = values.length; i < length; i++) {
                hiddenSheet.createRow(i).createCell(column).setCellValue(values[i]);
            }
            Name categoryName = workbook.createName();
            categoryName.setNameName(sheetName);
            String excelLine = getExcelLine(column);
            String refers = sheetName + "!$" + excelLine + "$1:$" + excelLine + "$" + (values.length + 1);
            // ??????????????????sheet??????????????????????????????
            categoryName.setRefersToFormula(refers);
            // ???????????????????????????sheet?????????
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
        // ??????Excel???????????????
        if (validation instanceof XSSFDataValidation) {
            validation.setSuppressDropDownArrow(true);
            validation.setShowErrorBox(true);
        } else {
            validation.setSuppressDropDownArrow(false);
        }
        // ?????????????????????????????????
        validation.setErrorStyle(DataValidation.ErrorStyle.STOP);
        validation.setShowErrorBox(true);
        validation.setSuppressDropDownArrow(true);
        validation.createErrorBox("??????", "???????????????????????????????????????");
        validation.createPromptBox("???????????????",
                "??????????????????????????????????????????????????????????????????????????????????????????");
        sheet.addValidationData(validation);
    }
}
