package com.kismet.operatelog.starter.expression;

import com.kismet.operatelog.starter.evaluator.OperateLogExpressionEvaluator;
import com.kismet.operatelog.starter.func.IFunctionService;
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
