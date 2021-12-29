package com.yiying.excelhelper.core.write;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.util.CollectionUtils;
import com.alibaba.excel.write.handler.AbstractRowWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import com.yiying.excelhelper.core.model.ExcelHeadProperty;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;

import java.util.List;

/**
 * @Description:
 * @Author: yiying.xuan
 * @Date: 2021/10/14
 */
public class CommentWriteHandler extends AbstractRowWriteHandler {

    private  List<ExcelHeadProperty> headProperties;

    public CommentWriteHandler(List<ExcelHeadProperty> headProperties) {
        this.headProperties = headProperties;
    }

    @Override
    public void afterRowDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row,
                                Integer relativeRowIndex, Boolean isHead) {
        if (isHead && !CollectionUtils.isEmpty(headProperties)) {
            Sheet sheet = writeSheetHolder.getSheet();
            for (ExcelHeadProperty property : headProperties) {
                handleComments(property, sheet, row.getCell(0));
                handleDateTime(sheet, property);
            }
        }
    }
    /**
     * 处理批注
     */
    private void handleComments(ExcelHeadProperty property, Sheet sheet, Cell cell) {
        String text = property.getComments();
        if (!StrUtil.isEmpty(text) && cell.getColumnIndex() == property.getColumn()) {
            Drawing drawing = sheet.createDrawingPatriarch();
            XSSFClientAnchor anchor = new XSSFClientAnchor(0, 0, 0, 0,
                    (short) property.getColumn(), 0, (short) 5, 5);
            Comment comment = drawing.createCellComment(anchor);
            comment.setString(new XSSFRichTextString(text));
            cell.setCellComment(comment);
        }
    }

    /**
     * 处理时间
     * @param sheet
     * @param property
     */
    private void handleDateTime(Sheet sheet, ExcelHeadProperty property) {
        if (!StrUtil.isEmpty(property.getDateFormat())) {
            CellRangeAddressList cellRangeAddressList = new CellRangeAddressList(-1, -1,
                    property.getColumn(), property.getColumn());
            DataValidationHelper helper = sheet.getDataValidationHelper();
            DataValidationConstraint constraint = helper.createDateConstraint(DataValidationConstraint.OperatorType.BETWEEN,
                    "Date(1900, 1, 1)", "Date(2099, 12, 31)", property.getDateFormat());
            DataValidation dataValidation = helper.createValidation(constraint, cellRangeAddressList);
            // Whether to display an error box when entering an invalid value
            dataValidation.setShowErrorBox(true);
            // Whether to verify the input data
            dataValidation.setSuppressDropDownArrow(true);
            // 设置无效值时 是否弹出提示框
            dataValidation.setShowPromptBox(true);
            // 设置无效值时的提示框内容 createErrorBox
            dataValidation.createPromptBox("非法日期格式", "请输入[yyyy-MM-dd]格式日期！");
            sheet.addValidationData(dataValidation);
        }
    }
}
