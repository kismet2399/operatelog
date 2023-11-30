package com.kismet.operatelog.starter.expression;

import com.kismet.operatelog.starter.evaluator.OperateLogExpressionEvaluator;
import com.kismet.operatelog.starter.func.IFunctionService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * SpEL的模板
 *
 * @author kismet
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpELTemplate implements IExpression {

    /**
     * SpEL表达式
     */
    private String spElExpression;

    @Override
    public String execute(ProceedingJoinPoint point,
                          OperateLogExpressionEvaluator expressionEvaluator,
                          IFunctionService iFunctionService) {

        return expressionEvaluator.executeStringExpression(point, spElExpression);
    }
}
