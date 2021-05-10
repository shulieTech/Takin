package io.shulie.surge.data.common.lifecycle;

/**
 * 组件可停止。
 * 需要先在  注册。
 *
 * @author pamirs
 */
public interface Stoppable {
    /**
     * 停止运行。如果已经停止，则应该不会有任何效果。
     * 建议实现使用同步方式执行。
     */
    void stop() throws Exception;
}
