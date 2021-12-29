package com.yiying.excelhelper.config;

import cn.hutool.core.collection.CollUtil;
import com.yiying.excelhelper.core.handler.Handler;
import com.yiying.excelhelper.core.read.DefaultExcelReadContext;
import com.yiying.excelhelper.core.read.DefaultReadListener;
import com.yiying.excelhelper.core.validator.Validator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
/**
 * @author yiying
 */
@Configuration
public class BeanConfig {

    /**
     * 构建DefaultExcelReadContext
     * @param context
     * @return
     */
    DefaultExcelReadContext excelReadContext(ApplicationContext context) {
        DefaultExcelReadContext excelReadContext = new DefaultExcelReadContext();
        Map<String, Handler> handlers = context.getBeansOfType(Handler.class);
        if (CollUtil.isNotEmpty(handlers)) {
            handlers.values().forEach(handler -> excelReadContext.registerHandlers(handler));
        }
        Map<String, Validator> validators = context.getBeansOfType(Validator.class);
        if (CollUtil.isNotEmpty(validators)) {
            validators.values().forEach(validator -> excelReadContext.registerValidators(validator));
        }
        return excelReadContext;
    }

    /**
     *  构建 DefaultReadListener
     * @param context
     * @return
     */
    @Bean
    DefaultReadListener readListener(ApplicationContext context) {
        return new DefaultReadListener(excelReadContext(context));
    }
}
