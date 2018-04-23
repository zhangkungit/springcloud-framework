package com.springcloud.framework.core.concurrent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author zhangkun
 * @version 1.0
 * @date 2017/8/24
 * @description
 */
public class DiscardOldestPolicy implements RejectedExecutionHandler {
    private static final Logger logger = LoggerFactory.getLogger(DiscardOldestPolicy.class);

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        logger.error("rejectedExecution......");
    }
}
