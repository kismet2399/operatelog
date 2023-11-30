package com.kismet.operatelog.starter.service;

import com.alibaba.fastjson.JSONObject;
import com.kismet.operatelog.starter.aspect.OperateLogDTO;
import lombok.extern.slf4j.Slf4j;

/**
 * 默认的操作日志存储实现
 *
 * @author kismet
 */
@Slf4j
public class DefaultILogSaveService implements ILogSaveService {

    @Override
    public void storeOperateLog(OperateLogDTO dto) {
        log.info("operateLog = {}", JSONObject.toJSONString(dto));
    }

}
