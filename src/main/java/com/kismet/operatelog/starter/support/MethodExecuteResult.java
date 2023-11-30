package com.kismet.operatelog.starter.support;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author kismet
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MethodExecuteResult {
    /**
     * 是否成功
     */
    boolean executeOk;
    /**
     * 异常
     */
    private Throwable throwable;
    /**
     * 错误信息
     */
    private String throwableMsg;
}
