package com.kismet.operatelog.starter.func;

/**
 * 自定义函数接口
 *
 * @author kismet
 */
public interface IParseFunction {

    /**
     * 实体操作前json
     *
     * @return
     */
    default String beforeJson() {
        return "{}";
    }

    String apply(Object value);
}
