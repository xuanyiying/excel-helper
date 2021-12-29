package com.yiying.excelhelper.core.write;

import cn.hutool.core.util.StrUtil;
import com.yiying.excelhelper.core.model.ExcelHeadProperty;
import com.yiying.excelhelper.core.model.SelectProperty;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * @author wangying
 */
@Component
public class JdbcDataSelector extends AbstractDataSelector {

    final JdbcTemplate jdbcTemplate;

    public JdbcDataSelector(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    protected List<String> selectValues(ExcelHeadProperty property, SelectProperty definition) {
        if (definition.getExcelColumn() != property.getColumn()) {
            return Collections.emptyList();
        }
        String querySql = definition.getQuerySql();
        String tableColumn = definition.getTableColumn();
        String tableName = definition.getTableName();
        if (StrUtil.isEmpty(querySql)
                && StrUtil.isNotEmpty(tableColumn)
                && StrUtil.isNotEmpty(tableName)) {
            querySql = "select distinct " + tableColumn + " from " + tableName;
        }
        return StrUtil.isEmpty(querySql) ? Collections.emptyList() :
                jdbcTemplate.queryForList(querySql, String.class);
    }

}
