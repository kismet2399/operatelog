package com.kismet.operatelog.starter.aspect;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author kismet
 */
@Data
@Builder
public class OperateLogDTO implements Serializable {

    /**
     * 操作类型（增删查改）
     */
    private String operateType;

    /**
     * 操作日志
     */
    private String logContent;

    /**
     * 业务主键
     */
    private String bizNo;

    /**
     * 操作方法
     */
    private String method;

    /**
     * 控制生成操作日志的执行时机
     * true  : 在拦截业务方法执行之前
     * false : 在拦截业务方法执行之后
     */
    private boolean executeBefore;

    /**
     * 操作用户id
     */
    private Long userId;

    /**
     * 操作用户名称
     */
    private String username;

    /**
     * 操作用户账号
     */
    private String account;

    /**
     * 操作时间
     */
    private Date operateTime;

    /**
     * 操作应用(SAAS平台、嗨家校)
     */
    private String appName;

    /**
     * 机构id
     */
    private Long orgId;

    /**
     * 租户id
     */
    private Long tenantId;

    /**
     * 参数
     */
    private String params;

    /**
     * 操作前时间json 默认为空
     */
    private String beforeJson;
}
