package com.yiying.excelhelper.core.validator;


import com.yiying.excelhelper.core.model.Model;
import com.yiying.excelhelper.core.read.ExcelReadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author: yiying.xuan
 * @Date: 2021/9/28
 *
 */
@Component
public class DataValidator extends AbstractValidator<Model> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataValidator.class);


    /**
     * @param data
     * @param row
     * @param readContext
     */
    @Override
    public void validate(Model data, int row, ExcelReadContext<Model> readContext) {
        validateRequiredCell(data, row, readContext);
    }
}
