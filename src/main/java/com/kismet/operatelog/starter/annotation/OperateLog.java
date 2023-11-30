package com.kismet.operatelog.starter.annotation;

import com.kismet.operatelog.starter.common.AppEnum;
import com.kismet.operatelog.starter.common.OperateTypeEnum;
import com.kismet.operatelog.starter.context.BeforeJsonHandler;

import java.lang.annotation.*;

/**
 * 操作日志自定义注解
 *
 * @author kismet
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperateLog {

    /**
     * 操作日志模版
     *
     * @return
     */
    String description() default "";

    /**
     * 业务no，可以是SpEL表达式
     */
    String bizNo() default "";

    /**
     * 操作类型
     *
     * @return
     */
    OperateTypeEnum operateType();

    /**
     * 所属应用
     *
     * @return
     */
    AppEnum appName() default AppEnum.DEFAULT;

    /**
     * 控制生成操作日志的执行时机
     * true  : 在拦截业务方法执行之前
     * false : 在拦截业务方法执行之后
     */
    boolean executeBefore() default false;

    /**
     * beforeJson实体操作bean或者自定义函数接口
     *
     * @return
     */
    String serviceBean() default "";

    /**
     * beforeJson操作实体查询 是否自定义
     *
     * @return
     */
    boolean beforeJsonCustom() default false;

    /**
     * 是否设置实体beforeJson,必须在bizNo不为空 且serviceBean都不为空时才会生效
     * 查询逻辑由 实现BeforeJsonHandler实现或者serviceBean指定的自定义函数接口实现
     *
     * @return
     * @see BeforeJsonHandler
     */
    boolean before() default true;
}
