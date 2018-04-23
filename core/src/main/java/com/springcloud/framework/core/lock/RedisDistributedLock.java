package com.springcloud.framework.core.lock;

import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

/**
 * Created by Charles on 2016/7/3.
 * 延迟锁，
 */
public class RedisDistributedLock {
    public static boolean lock(ValueOperations<String, String> vo, String lockName, String unlockKey, long expireSeconds) {
        Boolean flag = vo.setIfAbsent(lockName, unlockKey);
        if(!flag){
            return false;
        }
        vo.set(lockName, unlockKey, expireSeconds, TimeUnit.SECONDS);
        return true;
    }

    public static boolean lockAndHold(ValueOperations<String, String> vo, String lockName, String unlockKey, long expireSeconds) {
        Boolean flag = vo.setIfAbsent(lockName, unlockKey);
        if(!flag && !unlockKey.equals(vo.get(lockName))){
            return false;
        }
        vo.set(lockName, unlockKey, expireSeconds, TimeUnit.SECONDS);
        return true;
    }

    public static boolean unlock(ValueOperations<String, String> vo, String lockName, String unlockKey) {
        Object key = vo.get(lockName);
        if(null != key && !key.equals(unlockKey)){
            return false;
        }
        vo.getOperations().delete(lockName);
        return true;
    }
}
