package io.shulie.surge.data.runtime.digest;

import io.shulie.surge.data.common.lifecycle.Stoppable;

import java.io.Serializable;

/**
 * 实时数据处理
 *
 * @author pamirs
 */
public interface DataDigester<T extends Serializable> extends Stoppable {
    /**
     * 处理数据
     *
     * @param context
     */
    void digest(DigestContext<T> context);

    /**
     * 多线程执行的线程数，默认为 1
     *
     * @return 线程数
     */
    int threadCount();

}
