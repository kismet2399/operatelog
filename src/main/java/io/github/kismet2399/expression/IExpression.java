package io.github.kismet2399.expression;

import io.github.kismet2399.evaluator.OperateLogExpressionEvaluator;
import io.github.kismet2399.func.IFunctionService;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * 表达式
 *
 * @author kismet
 */
public interface IExpression {

    /**
     * 执行表达式
     *
     * @param point
     * @param expressionEvaluator
     * @param iFunctionService
     * @return
     */
    String execute(ProceedingJoinPoint point,
                   OperateLogExpressionEvaluator expressionEvaluator,
                   IFunctionService iFunctionService);

}
