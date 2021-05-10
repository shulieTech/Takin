package io.shulie.surge.data.common.lifecycle;

/**
 * 可被监听生命周期变化
 *
 * @author pamirs
 */
public interface LifecycleObservable<T extends Lifecycle> {

    void addObserver(LifecycleObserver<T> observer);
}
