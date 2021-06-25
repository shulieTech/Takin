package io.shulie.tro.channel.router.zk;

/**
 * @Description
 * @Author xiaobin.zfb
 * @mail xiaobin@shulie.io
 * @Date 2020/8/7 8:30 下午
 */
public interface Lifecycle extends Stoppable {

    /**
     * 开始运行
     * @throws Exception
     */
    void start() throws Exception;


    /**
     * 检查当前是否在运行状态
     * @return 运行状态
     */
    boolean isRunning();
}