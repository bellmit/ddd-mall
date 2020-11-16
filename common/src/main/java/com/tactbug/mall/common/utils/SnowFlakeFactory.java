package com.tactbug.mall.common.utils;

import com.tactbug.mall.common.exceptions.TactParamsException;

public class SnowFlakeFactory {
    /**
     * ID中41位时间戳的起点 (2020-01-01 00:00:00.00)
     * @apiNote 一般地，选用系统上线的时间
     */
    private static final long startPoint = 1577808000000L;

    /**
     * 序列号位数
     */
    private static final long sequenceBits = 12L;

    /**
     * 机器ID位数
     */
    private static final long machineIdBits = 10L;

    /**
     * 序列号最大值, 4095
     * @apiNote 4095 = 0xFFF,其相当于是序列号掩码
     */
    private final long sequenceMask = -1L^(-1L<<sequenceBits);

    /**
     * 机器ID最大值, 1024
     */
    private final long maxMachineId = -1L^(-1L<< machineIdBits);


    /**
     * 机器ID左移位数, 12
     */
    private final long machineIdShift = sequenceBits;

    /**
     * 时间戳左移位数, 12+10
     */
    private final long timeStampShift = sequenceBits + machineIdBits;

    /**
     * 机器ID, Value Range: [0,1023]
     */
    private long machineId;

    /**
     * 相同毫秒内的序列号, Value Range: [0,4095]
     */
    private long sequence = 0L;

    /**
     * 上一个生成ID的时间戳
     */
    private long lastTimeStamp = -1L;

    /**
     * 构造器
     * @param machineId 机器ID
     */
    public SnowFlakeFactory(Long machineId) {
        if(machineId==null || machineId < 1 || machineId > maxMachineId + 1) {
            throw new TactParamsException("机器ID不在允许范围内(1-1024)");
        }
        this.machineId = machineId - 1;
    }

    /**
     * 获取ID
     * @return
     */
    public synchronized long nextId() {
        long currentTimeStamp = System.currentTimeMillis();
        //当前时间小于上一次生成ID的时间戳，系统时钟被回拨
        if( currentTimeStamp < lastTimeStamp ) {
            throw new RuntimeException("系统时钟被回拨");
        }

        // 当前时间等于上一次生成ID的时间戳,则通过序列号来区分
        if( currentTimeStamp == lastTimeStamp ) {
            // 通过序列号掩码实现只取 (sequence+1) 的低12位结果，其余位全部清零
            sequence = (sequence + 1) & sequenceMask;
            if(sequence == 0) { // 该时间戳下的序列号已经溢出
                // 阻塞等待下一个毫秒,并获取新的时间戳
                currentTimeStamp = getNextMs(lastTimeStamp);
            }
        } else {    // 当前时间大于上一次生成ID的时间戳,重置序列号
            sequence = 0;
        }

        // 更新上次时间戳信息
        lastTimeStamp = currentTimeStamp;

        // 生成此次ID
        return ((currentTimeStamp-startPoint) << timeStampShift)
                | (machineId << machineIdShift)
                | sequence;
    }

    /**
     * 阻塞等待,直到获取新的时间戳(下一个毫秒)
     * @param lastTimeStamp
     * @return
     */
    private long getNextMs(long lastTimeStamp) {
        long timeStamp = System.currentTimeMillis();
        while(timeStamp<=lastTimeStamp) {
            timeStamp = System.currentTimeMillis();
        }
        return timeStamp;
    }
}
