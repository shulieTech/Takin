package io.shulie.surge.data.runtime.supplier;


import io.shulie.surge.data.common.lifecycle.Lifecycle;
import io.shulie.surge.data.common.lifecycle.LifecycleObservable;
import io.shulie.surge.data.runtime.processor.DataQueue;

/**
 * 数据提供者，必须在 start()、update() 前 {@link #setQueue(DataQueue) 设置好队列}。
 * 支持 start() 运行之后，继续动态 update()
 * @author pamirs
 */
public interface Supplier extends Lifecycle, LifecycleObservable<Supplier> {
    /**
     * 开始获取数据
     * @throws IllegalStateException Queue 尚未设置时抛出此异常
     * @throws Exception
     */
    void start() throws Exception;

    /**
     * 停止获取数据
     * @throws Exception
     */
    void stop() throws Exception;

    /**
     * 设置使用的队列
     * @param queue
     * @throws IllegalStateException 已经执行了 {@link #start()} 时抛出此异常
     */
    void setQueue(DataQueue queue) throws Exception;




}
