package com.tactbug.mall.common.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CodeUtil {

    private static RedisUtil redisUtil;

    @Autowired
    public void setRedisUtil(RedisUtil redisUtil){
        CodeUtil.redisUtil = redisUtil;
    }

    public static Long nextId(String applicationName){
        redisUtil.setApplicationName(applicationName);
        SnowFlakeFactory snowFlakeFactory = new SnowFlakeFactory(redisUtil.getMachineId());
        return snowFlakeFactory.nextId();
    }
}
