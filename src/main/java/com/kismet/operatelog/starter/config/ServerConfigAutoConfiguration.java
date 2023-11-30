package com.kismet.operatelog.starter.config;

import com.kismet.operatelog.starter.context.DefaultOperateLogContextUtil;
import com.kismet.operatelog.starter.context.OperateLogContextUtil;
import com.kismet.operatelog.starter.evaluator.OperateLogExpressionEvaluator;
import com.kismet.operatelog.starter.func.DefaultFunctionServiceImpl;
import com.kismet.operatelog.starter.func.IFunctionService;
import com.kismet.operatelog.starter.func.ParseFunctionFactory;
import com.kismet.operatelog.starter.service.DefaultILogSaveService;
import com.kismet.operatelog.starter.service.ILogSaveService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 自动装配
 *
 * @author kismet
 */
@Configuration
@ComponentScan({"com/kismet/operatelog/starter"})
public class ServerConfigAutoConfiguration {

    @Bean
    public ParseFunctionFactory parseFunctionFactory() {
        return new ParseFunctionFactory();
    }

    @Bean
    @ConditionalOnMissingBean(IFunctionService.class)
    public IFunctionService functionService(ParseFunctionFactory parseFunctionFactory) {
        return new DefaultFunctionServiceImpl(parseFunctionFactory);
    }

    @Bean
    public OperateLogExpressionEvaluator operateLogExpressionEvaluator() {
        return new OperateLogExpressionEvaluator();
    }

    @Bean
    @ConditionalOnMissingBean(ILogSaveService.class)
    public ILogSaveService operateLogStoreService() {
        return new DefaultILogSaveService();
    }

    @Bean
    @ConditionalOnMissingBean(OperateLogContextUtil.class)
    public OperateLogContextUtil operateLogContextUtil() {
        return new DefaultOperateLogContextUtil();
    }
}
