package io.github.kismet2399.evaluator;

import io.github.kismet2399.expression.ExpressionRootObject;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.context.expression.CachedExpressionEvaluator;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 操作日志的SpEL Evaluator
 *
 * @author kismet
 */
public class OperateLogExpressionEvaluator extends CachedExpressionEvaluator {

    /**
     * shared param discoverer since it caches data internally
     */
    private final ParameterNameDiscoverer paramNameDiscoverer = new DefaultParameterNameDiscoverer();

    private final Map<ExpressionKey, Expression> expressionCache = new ConcurrentHashMap<>(64);

    private final Map<AnnotatedElementKey, Method> targetMethodCache = new ConcurrentHashMap<>(64);

    /**
     * 执行el表达式解析 获取结构
     *
     * @param joinPoint join点
     * @param condition 条件
     * @return 解析结果
     */
    public String executeStringExpression(JoinPoint joinPoint, String condition) {
        if (StringUtils.isEmpty(condition)) {
            return null;
        }
        return executeExpression(joinPoint.getTarget(), joinPoint.getArgs(),
                joinPoint.getTarget().getClass(),
                ((MethodSignature) joinPoint.getSignature()).getMethod(), condition, String.class);
    }

    /**
     * 执行el表达式解析 获取结构
     *
     * @param joinPoint 切点对象
     * @param condition 条件表达式
     * @return 解析结果
     */
    public Object executeObjectExpression(JoinPoint joinPoint, String condition) {
        if (StringUtils.isEmpty(condition)) {
            return null;
        }
        return executeExpression(joinPoint.getTarget(), joinPoint.getArgs(),
                joinPoint.getTarget().getClass(),
                ((MethodSignature) joinPoint.getSignature()).getMethod(), condition, Object.class);
    }

    private <T> T executeExpression(Object object, Object[] args, Class<?> clazz, Method method, String condition, Class<T> vClazz) {
        if (args == null) {
            return null;
        }
        // 获取并缓存方法
        Method targetMethod = getTargetMethod(clazz, method);
        ExpressionRootObject root = new ExpressionRootObject(object, args);
        EvaluationContext evaluationContext = new MethodBasedEvaluationContext(root, targetMethod, args, this.paramNameDiscoverer);

        AnnotatedElementKey methodKey = new AnnotatedElementKey(method, clazz);
        // 执行SpEL表达式的核心方法
        return getExpression(this.expressionCache, methodKey, condition).getValue(evaluationContext, vClazz);
    }


    private Method getTargetMethod(Class<?> targetClass, Method method) {
        AnnotatedElementKey methodKey = new AnnotatedElementKey(method, targetClass);
        Method targetMethod = this.targetMethodCache.get(methodKey);
        if (targetMethod == null) {
            targetMethod = AopUtils.getMostSpecificMethod(method, targetClass);
            this.targetMethodCache.put(methodKey, targetMethod);
        }
        return targetMethod;
    }

}
