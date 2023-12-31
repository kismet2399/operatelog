package io.github.kismet2399.func;

import org.springframework.beans.factory.annotation.Autowired;

import java.text.MessageFormat;
import java.util.Map;


/**
 * @author kismet
 */
public class ParseFunctionFactory {

    @Autowired(required = false)
    private Map<String, IParseFunction> allFunctionMap;

    /**
     * 获取自定义函数
     *
     * @param functionName 函数名称
     * @return 自定义函数
     */
    public IParseFunction getFunction(String functionName) {
        IParseFunction function = allFunctionMap.get(functionName);
        if (null == function) {
            throw new UnsupportedOperationException(MessageFormat.format("please implement custom function=[{0}]!", functionName));
        }
        return function;
    }
}
