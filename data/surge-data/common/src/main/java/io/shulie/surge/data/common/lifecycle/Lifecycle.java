package io.shulie.surge.data.common.lifecycle;

/**
 * 控制组件的生命周期
 *
 * @author pamirs
 */
public interface Lifecycle extends Stoppable {

    /**
     * 开始运行
     */
    void start() throws Exception;

    /**
     * 停止运行。如果已经停止，则应该不会有任何效果。
     * 建议实现使用同步方式执行。
     */
    void stop() throws Exception;

    /**
     * 检查当前是否在运行状态
     */
    boolean isRunning();
}
