package com.springcloud.framework.core.concurrent;

import com.google.common.collect.Maps;

import java.util.Map;
import java.util.concurrent.RejectedExecutionHandler;

/**
 * @author zhangkun
 * @version 1.0
 * @date 2017/8/24
 * @description
 */
public class RejectedHandlerFactory {
    private static Map<String, RejectedExecutionHandler> rejectedExecutionHandlerMap = Maps.newHashMap();

    static {
        rejectedExecutionHandlerMap.put(DiscardOldestPolicy.class.getName(), new DiscardOldestPolicy());
    }

    public static RejectedExecutionHandler getHandler(String name) {
        return rejectedExecutionHandlerMap.get(name);
    }
}
