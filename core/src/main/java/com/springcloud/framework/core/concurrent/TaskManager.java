package com.springcloud.framework.core.concurrent;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.*;

/**
 * @author zhangkun
 * @version 1.0
 * @date 2017/8/24
 * @description
 *
 */

public class TaskManager {
    protected static LinkedBlockingDeque<Runnable> queue = new LinkedBlockingDeque<Runnable>(getQueueSize());

    protected static ThreadFactory threadFactory = new BasicThreadFactory.Builder().namingPattern("DisposeThread-%d").build();
    protected static RejectedExecutionHandler rejectedExecutionHandler = RejectedHandlerFactory.getHandler(DiscardOldestPolicy.class.getName());

    protected static ListeningExecutorService pool =
            MoreExecutors.listeningDecorator(
            new ThreadPoolExecutor(
                    getPoolCoreSize(),
                    getPoolMaxSize(),
                    getPoolWaitTime(),
                    TimeUnit.MICROSECONDS,
                    queue,
                    threadFactory,
                    rejectedExecutionHandler));

    private static int getQueueSize() {
        return 10;
    }

    private static int getPoolCoreSize() {
        return 10;
    }

    private static int getPoolMaxSize() {
        return 10;
    }

    private static long getPoolWaitTime() {
        return 1000;
    }


    public static void addTask(AbstractTask task) {
        pool.submit(task);
    }
    
    public static void addTask(Runnable task) {
        pool.submit(task);
    }
    
}
