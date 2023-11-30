package com.kismet.operatelog.starter.service;

import com.kismet.operatelog.starter.aspect.OperateLogDTO;

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
