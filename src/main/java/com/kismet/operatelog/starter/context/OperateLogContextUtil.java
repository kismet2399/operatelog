package com.kismet.operatelog.starter.context;

/**
 * 上下文工具
 *
 * @author kismet
 */
public interface OperateLogContextUtil {

    Long tenantId();

    Long orgId();

    Long userId();

    String userName();

    String account();

    String bizNo();
}
