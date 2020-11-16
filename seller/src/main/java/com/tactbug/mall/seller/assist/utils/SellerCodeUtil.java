package com.tactbug.mall.seller.assist.utils;

import com.tactbug.mall.common.utils.SnowFlakeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SellerCodeUtil {

    private static RedisUtil redisUtil;

    @Autowired
    public void setRedisUtil(RedisUtil redisUtil){
        SellerCodeUtil.redisUtil = redisUtil;
    }

    public static Long nextId(){
        SnowFlakeFactory snowFlakeFactory = new SnowFlakeFactory(redisUtil.getMachineId());
        return snowFlakeFactory.nextId();
    }
}
