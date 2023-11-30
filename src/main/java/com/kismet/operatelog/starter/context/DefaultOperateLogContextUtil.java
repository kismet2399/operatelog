package com.kismet.operatelog.starter.context;


/**
 * 上下文工具
 *
 * @author kismet
 */
public class DefaultOperateLogContextUtil implements OperateLogContextUtil {
    @Override
    public Long tenantId() {
        return 0L;
    }

    @Override
    public Long orgId() {
        return 0L;
    }

    @Override
    public Long userId() {
        return 0L;
    }

    @Override
    public String userName() {
        return "";
    }

    @Override
    public String account() {
        return "";
    }

    @Override
    public String bizNo() {
        return null;
    }
}
