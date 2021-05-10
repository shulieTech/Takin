package io.shulie.surge.data.common.lifecycle;

/**
 * 监听类 T 的对象生命周期事件，处理中不应该抛出异常，
 * 否则可能影响 {@link Lifecycle} 的执行流程。
 *
 * @author pamirs
 */
public interface LifecycleObserver<T extends Lifecycle> {

    void beforeStart(T target);
    void afterStart(T target);
    void beforeStop(T target);
    void afterStop(T target);
}
