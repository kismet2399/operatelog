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
     * @param point               ProceedingJoinPoint       方法连接点
     * @param expressionEvaluator OperateLogExpressionEvaluator   运算表达式评估器
     * @param iFunctionService    IFunctionService           函数服务接口
     * @return String                     执行结果
     */
    String execute(ProceedingJoinPoint point,
                   OperateLogExpressionEvaluator expressionEvaluator,
                   IFunctionService iFunctionService);

}
