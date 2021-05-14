package io.shulie.tro.channel.router.zk;


/**
 * 通过 zk 维持心跳的节点，如果成功在 zk 上注册节点，则表示存活，否则不存活。
 *
 * @author pamirs
 */
public interface ZkHeartbeatNode extends Lifecycle {

    /**
     * 获取心跳节点路径，如果未创建节点，返回 <code>null</code>
     * @return
     */
    String getPath();

    /**
     * 设置心跳节点上的数据，如果节点存活，会触发更新 zk，非存活时会保存在内存中
     * @param data 设置zk数据
     * @throws Exception 更新zk异常
     */
    void setData(byte[] data) throws Exception;

    /**
     * 获取当前心跳节点在内存中缓存的心跳数据
     * @return
     */
    byte[] getData();

    /**
     * 返回当前是否存活
     * @return
     */
    boolean isAlive();

    /**
     * 开始心跳节点
     * @throws Exception
     */
    @Override
    void start() throws Exception;

    /**
     * 删除节点，停止心跳
     * @throws Exception 操作zk异常
     */
    @Override
    void stop() throws Exception;

    /**
     * 检查当前是否在运行状态
     * @return 运行装填
     */
    @Override
    boolean isRunning();
}
