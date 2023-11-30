package io.github.kismet2399.config;

import io.github.kismet2399.context.DefaultOperateLogContextUtil;
import io.github.kismet2399.context.OperateLogContextUtil;
import io.github.kismet2399.evaluator.OperateLogExpressionEvaluator;
import io.github.kismet2399.func.DefaultFunctionServiceImpl;
import io.github.kismet2399.func.IFunctionService;
import io.github.kismet2399.func.ParseFunctionFactory;
import io.github.kismet2399.service.DefaultILogSaveService;
import io.github.kismet2399.service.ILogSaveService;
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
