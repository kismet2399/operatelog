package com.kismet.operatelog.starter.common;

import lombok.Getter;

/**
 * 操作类型
 *
 * @author kismet
 */
@Getter
public enum OperateTypeEnum {

    /**
     * 新增操作
     */
    ADD("新增"),

    /**
     * 删除操作
     */
    DELTE("删除"),

    /**
     * 查询操作
     */
    QUERY("查询"),

    /**
     * 修改操作
     */
    UPDATE("修改"),

    /**
     * 新增或者修改
     */
    ADD_OR_UPDATE("新增或修改"),

    /**
     * 上传操作
     */
    UPLOAD("上传"),

    /**
     * 下载操作
     */
    DOWNLOAD("下载"),

    /**
     * 登录成功
     */
    LOGIN("登录");

    private final String operationName;

    OperateTypeEnum(String operationName) {
        this.operationName = operationName;
    }

}
