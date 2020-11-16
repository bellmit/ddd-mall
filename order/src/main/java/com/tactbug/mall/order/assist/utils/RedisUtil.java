package com.tactbug.mall.order.assist.utils;

import com.tactbug.mall.common.exceptions.TactRedisException;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class RedisUtil {

    private static String localIp;
    private static Long machineId;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedissonClient redissonClient;
    private static final int REDISSON_WAIT_TIME = 1;
    private static final int REDISSON_LEASE_TIME = 3;
    private static final int MAX_MACHINE_ID = 1024;
    private static final String MACHINE_ID_GETTER_METHOD = "getMachineId";
    private static final String MACHINE_ID_YIELD = "machineId";

    @Value("${spring.application.name}")
    private String applicationName;

    static {
        try {
            localIp = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void tryLock(String methodKey) {
        RLock lock = redissonClient.getLock(methodKey);
        try {
            lock.tryLock(REDISSON_WAIT_TIME, REDISSON_LEASE_TIME, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new TactRedisException(
                    "分布式锁获取异常: ipAddr:[" + localIp + "], "
                    + "application:[" + applicationName + "], "
                    + "method:[" + methodKey + "], "
                    + "ThreadId:[" + Thread.currentThread().getId() + "]"
            );
        }
    }

    public void unlock(String methodKey){
        RLock lock = redissonClient.getLock(methodKey);
        lock.unlock();
    }

    public Long getMachineId(){
        if (null == RedisUtil.machineId){
            RedisUtil.machineId = machineIdGetter();
        }
        return RedisUtil.machineId;
    }

    private Long machineIdGetter(){
        HashOperations<String, Object, Object> hashOperations = stringRedisTemplate.opsForHash();
        if (hashOperations.hasKey(MACHINE_ID_YIELD, localIp)){
            Object id = hashOperations.get(MACHINE_ID_YIELD, localIp);
            assert id != null;
            return Long.valueOf(id.toString());
        }
        Map<Object, Object> entries = hashOperations.entries(MACHINE_ID_YIELD);
        String idGetterLock = applicationName + ":" + MACHINE_ID_GETTER_METHOD;
        if (entries.isEmpty()){
            tryLock(idGetterLock);
            hashOperations.put(MACHINE_ID_YIELD, localIp, ""+1);
            unlock(idGetterLock);
            return 1L;
        }
        if (entries.size() >= MAX_MACHINE_ID){
            throw new TactRedisException("集群数量超出限制:[" + MAX_MACHINE_ID);
        }
        Collection<Object> values = entries.values();
        List<Long> keyIds = values.stream()
                .map(v -> Long.valueOf(v.toString()))
                .sorted()
                .collect(Collectors.toList());
        tryLock(idGetterLock);
        Long id = 1L;
        for (Long i :
                keyIds) {
            if (id.equals(i)) {
                id += 1;
            }else {
                break;
            }
        }
        hashOperations.put(MACHINE_ID_YIELD, localIp, ""+id);
        unlock(idGetterLock);
        return id;
    }
}
