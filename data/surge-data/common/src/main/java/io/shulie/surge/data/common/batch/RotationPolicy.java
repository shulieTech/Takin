package io.shulie.surge.data.common.batch;

/**
 * 滚动策略
 *
 * @author vincent
 */
public interface RotationPolicy {


    /**
     * 确定是否需要滚动
     * @param offset
     * @return
     */
    boolean mark(long offset);

    /**
     * 执行完成的重置动作
     */
    void reset();

    /**
     * 滚动策略拷贝
     *
     * @return
     */
    RotationPolicy copy();
}
