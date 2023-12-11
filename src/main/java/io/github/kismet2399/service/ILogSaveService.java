package io.github.kismet2399.service;

import io.github.kismet2399.aspect.OperateLogDTO;

/**
 * 操作日志存储service
 *
 * @author kismet
 */
public interface ILogSaveService {

    /**
     * 存储操作日志
     *
     * @param dto 操作日志数据传输对象
     */
    void storeOperateLog(OperateLogDTO dto);

}
