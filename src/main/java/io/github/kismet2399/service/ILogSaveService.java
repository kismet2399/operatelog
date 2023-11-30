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
     * @param dto
     * @throws Exception
     */
    void storeOperateLog(OperateLogDTO dto);

}
