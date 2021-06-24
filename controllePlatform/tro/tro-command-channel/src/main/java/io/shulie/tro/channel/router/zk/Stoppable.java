package io.shulie.tro.channel.router.zk;

/**
 * @Description
 * @author: HengYu
 * @mail guohaozhu@shulie.io
 * @Date 2020/12/29 20:11
 */
public interface Stoppable {

    /**
     * 停止运行。如果已经停止，则应该不会有任何效果。
     * 建议实现使用同步方式执行。
     * @throws Exception
     */
    void stop() throws Exception;
}
