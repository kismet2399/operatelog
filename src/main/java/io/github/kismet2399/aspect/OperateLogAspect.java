package io.github.kismet2399.aspect;

import io.github.kismet2399.annotation.OperateLog;
import io.github.kismet2399.common.AppEnum;
import io.github.kismet2399.common.OperateTypeEnum;
import io.github.kismet2399.context.BeforeJsonHandler;
import io.github.kismet2399.context.OperateLogContextUtil;
import io.github.kismet2399.evaluator.LogTemplateResult;
import io.github.kismet2399.evaluator.OperateLogExpressionEvaluator;
import io.github.kismet2399.expression.FuncInput;
import io.github.kismet2399.expression.FunctionTemplate;
import io.github.kismet2399.expression.IExpression;
import io.github.kismet2399.expression.SpELTemplate;
import io.github.kismet2399.func.IFunctionService;
import io.github.kismet2399.func.ParseFunctionFactory;
import io.github.kismet2399.service.ILogSaveService;
import io.github.kismet2399.support.BraceResult;
import io.github.kismet2399.support.BraceUtils;
import io.github.kismet2399.support.BraceValidResult;
import io.github.kismet2399.support.MethodExecuteResult;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 操作日志切面
 *
 * @author kismet
 */
@Aspect
@Order
@Component
@Slf4j
public class OperateLogAspect implements ApplicationContextAware {
    @Autowired
    private OperateLogExpressionEvaluator expressionEvaluator;
    @Autowired
    private IFunctionService iFunctionService;
    @Autowired
    private ILogSaveService iLogSaveService;
    @Autowired
    private OperateLogContextUtil operateLogContextUtil;
    @Autowired(required = false)
    private BeforeJsonHandler beforeJsonHandler;
    @Autowired
    private  ParseFunctionFactory parseFunctionFactory;

    private static ApplicationContext applicationContext;

    @Around("@annotation(operateLog)")
    public Object saveSysLog(ProceedingJoinPoint point, OperateLog operateLog) throws Throwable {
        Object ret = null;
        MethodExecuteResult methodExecuteResult = new MethodExecuteResult(true, null, "");
        LogTemplateResult logTemplateResult = null;
        boolean executeBefore = operateLog.executeBefore();
        OperateLogDTO instance = null;


        try {
            // 1、解析操作日志模板和表达式
            logTemplateResult = parseOperateLog(point, operateLog);

            if (executeBefore) {
                // 2、在业务方法执行之前生成操作日志
                instance = executeExpressionAndFillPlaceholder(logTemplateResult);
            }
        } catch (Exception e) {
            log.error("operate log parse before biz function exception", e);
        }

        try {
            // 3、执行业务方法
            ret = point.proceed();
        } catch (Exception e) {
            methodExecuteResult = new MethodExecuteResult(false, e, e.getMessage());
        }

        try {
            // 只有业务方法执行成功才构造和记录操作日志
            if (methodExecuteResult.isExecuteOk()) {
                if (!executeBefore) {
                    // 4、执行表达式并填充日志模板占位符
                    instance = executeExpressionAndFillPlaceholder(logTemplateResult);
                }
                // 5、存储操作日志，需要业务方自行定义
                if (instance != null) {
                    iLogSaveService.storeOperateLog(instance);
                }
            }
        } catch (Exception e) {
            // 构造和存储日志过程中错误不要影响业务
            log.error("operate log build and store exception", e);
        }

        // 6、抛出业务方法的exception
        if (methodExecuteResult.getThrowable() != null) {
            throw methodExecuteResult.getThrowable();
        }

        return ret;

    }

    /**
     * 解析操作日志模板
     */
    private LogTemplateResult parseOperateLog(ProceedingJoinPoint point, OperateLog operateLog) {


        // 解析业务no
        String condition = operateLog.bizNo();
        String bizNo = operateLogContextUtil.bizNo();
        // bizNo 不存在才重新获取 为了适配bizNo不在参数中的情况
        if (StringUtils.isEmpty(bizNo) && !StringUtils.isEmpty(condition)) {
            bizNo = expressionEvaluator.executeStringExpression(point, condition);
        }
        // 解析日志模板和表达式
        LogTemplateResult logTemplateResult = parseLogTemplateResult(operateLog.description());

        logTemplateResult.setOperateLog(operateLog);
        logTemplateResult.setBizNo(bizNo);
        logTemplateResult.setPoint(point);

        return logTemplateResult;
    }

    /**
     * 解析LogContent
     *
     * @param content
     * @return
     */
    private LogTemplateResult parseLogTemplateResult(String content) {
        BraceValidResult braceValidResult = BraceUtils.isBraceValid(content);
        // 如果没有{}或者左右括号不匹配，说明日志content里面不包含表达式
        if (!braceValidResult.hasBrace()) {
            return new LogTemplateResult(false);
        }
        List<IExpression> expressions = new ArrayList<>();
        StringBuilder contentTemplate = new StringBuilder();
        // 找出所有的{}之间的内容，并输出contentTemplate
        // eg: content = "修改了订单的配送员：从【{queryOldUser{#request.getDeliveryOrderNo()}}】, 修改到【{deveryUser{#request.userId}}】";
        // out:
        //    queryOldUser{#request.getDeliveryOrderNo()}
        //    deveryUser{#request.userId}
        //    contentTemplate = 修改了订单的配送员：从【%s】, 修改到【%s】
        BraceResult braceResult = null;
        while (null != (braceResult = BraceUtils.findBraceResult(content))) {
            // 构造contentTemplate
            contentTemplate.append(content, 0, braceResult.getLeftBraceIndex());
            contentTemplate.append("%s");
            // 截取剩余未处理完的字符串
            content = content.substring(braceResult.getMatchedRightBraceIndex() + 1);
            // 构造并添加IExpression
            expressions.add(parseExpression(braceResult.getBetweenBraceContent()));
        }
        contentTemplate.append(content);
        return new LogTemplateResult(true, expressions, contentTemplate.toString());
    }

    /**
     * 从funcContent解析出自定义函数template
     * <p>
     * 1、自定义函数
     * eg1: content = queryOldUser{#request.getDeliveryOrderNo()}
     * out:
     * functionName = queryOldUser
     * functionInput = #request.getDeliveryOrderNo()
     * 2、SpEl表达式
     * eg2: content = #request.userName
     * out:
     * spElExpression = #request.userName
     *
     * @param content
     * @return
     */
    private IExpression parseExpression(String content) {
        BraceResult braceResult = BraceUtils.findBraceResult(content);
        if (null != braceResult) {
            // 自定义函数
            String functionName = content.substring(0, braceResult.getLeftBraceIndex());
            String input = content.substring(braceResult.getLeftBraceIndex() + 1, braceResult.getMatchedRightBraceIndex());
            return new FunctionTemplate(functionName, new FuncInput(input));
        } else {
            // SpEl表达式
            return new SpELTemplate(content);
        }
    }

    /**
     * 执行表达式并填充日志模板占位符
     */
    private OperateLogDTO executeExpressionAndFillPlaceholder(LogTemplateResult logTemplateResult) {
        if (logTemplateResult == null) {
            return null;
        }
        String logContent = null;
        ProceedingJoinPoint point = logTemplateResult.getPoint();
        OperateLog operateLog = logTemplateResult.getOperateLog();

        // 如果日志LogContentTemplate里面包含表达式的话
        if (logTemplateResult.isHasExpression()) {
            String logContentTemplate = logTemplateResult.getContentTemplate();
            List<String> expressionResult = new ArrayList<>();
            // 执行自定义函数
            for (IExpression expression : logTemplateResult.getExpressions()) {
                // 保存执行的结果
                expressionResult.add(expression.execute(point, expressionEvaluator, iFunctionService));
            }
            // 构造最终的日志content
            logContent = String.format(logContentTemplate, expressionResult.toArray());
        } else {
            // 普通logContent
            logContent = operateLog.description();
        }

        String appName;
        if (AppEnum.DEFAULT.equals(operateLog.appName())) {
            appName = getApplicationName();
        } else {
            appName = operateLog.appName().getName();
        }
        String operationType = operateLog.operateType().getOperationName();
        // 获取当前请求上下文中的当前用户信息
        Long userId = operateLogContextUtil.userId();
        String userName = operateLogContextUtil.userName();
        String account = operateLogContextUtil.account();
        Long tenantId = operateLogContextUtil.tenantId();
        Long orgId = operateLogContextUtil.orgId();

        // 获取切点方法入参列表
        StringBuilder s = new StringBuilder();
        Object[] objArray = point.getArgs();
        for (Object o : objArray) {
            // 文件信息不存储
            if (o == null || o instanceof ServletResponse || o instanceof MultipartFile || o instanceof HttpSession) {
                continue;
            }
            s.append("|");
            s.append(o);
        }
        // 构造操作日志实例
        // 查询操作前json
        String beforeJson = null;
        String bizNo = logTemplateResult.getBizNo();
        if (StringUtils.isEmpty(bizNo)) {
            bizNo = operateLogContextUtil.bizNo();
        }
        boolean before = operateLog.before();
        String serviceBean = operateLog.serviceBean();
        if (beforeJsonHandler != null && !StringUtils.isEmpty(bizNo) && !StringUtils.isEmpty(serviceBean) && before) {
            if (operateLog.beforeJsonCustom()) {
                beforeJson = parseFunctionFactory.getFunction(serviceBean).apply(bizNo);
            }else {
                beforeJson = beforeJsonHandler.beforeJson(serviceBean, bizNo);
            }
        }
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        Method method = methodSignature.getMethod();
        OperateLogDTO logDTO = OperateLogDTO.builder()
                .userId(userId)
                .username(userName)
                .account(account)
                .operateType(operationType)
                .logContent(logContent)
                .operateTime(new Date())
                .appName(appName)
                .orgId(orgId)
                .tenantId(tenantId)
                .bizNo(logTemplateResult.getBizNo())
                .beforeJson(beforeJson)
                .executeBefore(operateLog.executeBefore())
                .method(method.getDeclaringClass().getSimpleName() + "#" + method.getName())
                .params(s.toString()).build();

        try {
            if (OperateTypeEnum.ADD_OR_UPDATE.getOperationName().equals(operationType)) {
                // 业务no 为空即为新增否则为更新
                if (StringUtils.isEmpty(logTemplateResult.getBizNo())) {
                    logDTO.setOperateType(OperateTypeEnum.ADD.getOperationName());
                } else {
                    logDTO.setOperateType(OperateTypeEnum.UPDATE.getOperationName());
                }

            }
        } catch (Exception e) {
            log.error("获取操作类型异常", e);
        }

        return logDTO;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        OperateLogAspect.applicationContext = applicationContext;
    }

    public static String getApplicationName() {
        if (null == applicationContext) {
            return null;
        }
        return applicationContext.getEnvironment().getProperty("spring.application.name");
    }

}
