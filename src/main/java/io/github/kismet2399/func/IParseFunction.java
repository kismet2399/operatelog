package io.github.kismet2399.func;

/**
 * 自定义函数接口
 *
 * @author kismet
 */
public interface IParseFunction {

    /**
     * 实体操作前json
     *
     * @return 操作前json
     */
    default String beforeJson() {
        return "{}";
    }

    String apply(Object value);
}
