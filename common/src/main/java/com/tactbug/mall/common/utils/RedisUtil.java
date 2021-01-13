package com.tactbug.mall.common.utils;

import com.tactbug.mall.common.exceptions.TactRedisException;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
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

@Setter
@Component
@Slf4j
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

    private String applicationName;

    static {
        try {
            localIp = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.error(ExceptionUtil.getMessage(e));
            e.printStackTrace();
        }
    }

    public void tryLock(String methodKey) {
        RLock lock = redissonClient.getLock(methodKey);
        try {
            lock.tryLock(REDISSON_WAIT_TIME, REDISSON_LEASE_TIME, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.error(ExceptionUtil.getMessage(e));
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
        //使用redis hash进行存储
        HashOperations<String, Object, Object> hashOperations = stringRedisTemplate.opsForHash();

        //1. 判断当前IP是否有已经存在的标识，有则返回
        if (hashOperations.hasKey(MACHINE_ID_YIELD, localIp)){
            Object id = hashOperations.get(MACHINE_ID_YIELD, localIp);
            assert id != null;
            return Long.valueOf(id.toString());
        }

        //生成id的分布式锁标记，防止当前实例重复生成
        String idGetterLock = applicationName + ":" + MACHINE_ID_GETTER_METHOD;

        //上分布式锁
        tryLock(idGetterLock);
        Map<Object, Object> entries = hashOperations.entries(MACHINE_ID_YIELD);

        //2. 如果当前标识位数超过集群最大长度限制则抛出异常
        if (entries.size() >= MAX_MACHINE_ID){
            unlock(idGetterLock);
            throw new TactRedisException("集群数量超出限制:[" + MAX_MACHINE_ID + "]");
        }

        //3. 判断redis中是否存在已经生成的标识，如果没有则生成当前IP标识，初始标识1
        if (entries.isEmpty()){
            hashOperations.put(MACHINE_ID_YIELD, localIp, "1");
            unlock(idGetterLock);
            return 1L;
        }

        //4. 如果redis中已经存在标记则以累加填充的方式为当前IP生成合适的标识
        Collection<Object> values = entries.values();
        List<Long> keyIds = values.stream()
                .map(v -> Long.valueOf(v.toString()))
                .sorted()
                .collect(Collectors.toList());
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
