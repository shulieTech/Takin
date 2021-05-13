package io.shulie.tro.channel.router.zk;

import java.util.List;

/**
 * 监视 ZooKeeper 节点的子节点的增加、删除更新，并把内容缓存在内存中。
 * 创建方式：{@link ZkClient#createPathChildrenCache(String)}
 *
 * @author pamirs
 */
public interface ZkPathChildrenCache extends Lifecycle {

    /**
     * 获取缓存在内存中的子节点，如果节点不存在，返回 <code>null</code>
     *
     * @return
     */
    List<String> getChildren();

    /**
     * 获取监视的节点路径
     * @return
     */
    String getPath();

    /**
     * 开始监视更新，第一次更新异步的方式进行，第一次更新会触发回调
     * @throws Exception
     */
    @Override
    void start() throws Exception;

    /**
     * 停止监视更新，回调会被清空
     */
    @Override
    void stop() throws Exception;

    /**
     * 手动刷新，不会触发回调
     *
     * @throws Exception
     */
    void refresh() throws Exception;


    /**
     * 删除目录路径
     * @throws Exception
     */
    void delete() throws Exception;

    /**
     * 检查当前是否在运行状态
     * @return 运行状态
     */
    @Override
    boolean isRunning();
}
