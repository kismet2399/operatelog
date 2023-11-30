package io.github.kismet2399.service;

import com.alibaba.fastjson.JSONObject;
import io.github.kismet2399.aspect.OperateLogDTO;
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
