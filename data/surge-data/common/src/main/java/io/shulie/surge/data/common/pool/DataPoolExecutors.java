package io.shulie.surge.data.common.pool;


import java.util.concurrent.*;

/**
 * 默认的连接池
 */
public class DataPoolExecutors {
    /**
     * 无队列线程池
     *
     * @param coreSize
     * @return
     */
    public static ExecutorService newDefaultNoQueueExecutors(int coreSize, int maxPoolSize, int keepAliveTime, TimeUnit timeUnit, ThreadFactory threadFactory, RejectedExecutionHandler rejectedExecutionHandler) {

        BlockingQueue blockingQueue = new SynchronousQueue<>();
        if (rejectedExecutionHandler != null) {
            return new ThreadPoolExecutor(coreSize, maxPoolSize, keepAliveTime,
                    timeUnit, blockingQueue, threadFactory, rejectedExecutionHandler);
        }
        if (rejectedExecutionHandler != null)
        {
            return new ThreadPoolExecutor(coreSize, maxPoolSize, keepAliveTime,
                    timeUnit, blockingQueue, threadFactory, rejectedExecutionHandler);
        }

        return new ThreadPoolExecutor(coreSize, maxPoolSize, keepAliveTime,
                timeUnit, blockingQueue, threadFactory);
    }


    /**
     * 有队列队列线程池
     *
     * @param coreSize
     * @return
     */
    public static ExecutorService newDefaultQueueExecutors(int coreSize, int maxSize, int keepAliveTime, int queueSize, TimeUnit timeUnit, ThreadFactory threadFactory, RejectedExecutionHandler rejectedExecutionHandler) {

        BlockingQueue blockingQueue = new ArrayBlockingQueue(queueSize);
        if (rejectedExecutionHandler != null)
        {
            return new ThreadPoolExecutor(coreSize, maxSize, keepAliveTime,
                    timeUnit, blockingQueue, threadFactory, rejectedExecutionHandler);
        }

        return  new ThreadPoolExecutor(coreSize, maxSize, keepAliveTime,
                timeUnit, blockingQueue, threadFactory);
    }


}

