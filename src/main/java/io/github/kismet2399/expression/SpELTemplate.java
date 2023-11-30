package io.github.kismet2399.expression;

import io.github.kismet2399.evaluator.OperateLogExpressionEvaluator;
import io.github.kismet2399.func.IFunctionService;
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
