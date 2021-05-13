package io.shulie.tro.channel.router.zk;

import com.netflix.curator.framework.CuratorFramework;
import com.netflix.curator.framework.recipes.cache.PathChildrenCacheEvent;

/**
 * @author: Hengyu
 * @className: ZkChildListener
 * @date: 2021/1/7 2:43 下午
 * @description:
 */
public interface ZkChildListener {

    /**
     * zk监听子节点通知
     * @param client 客户端连接对象
     * @param event 事件对象
     */
    public void call(CuratorFramework client, PathChildrenCacheEvent event);
}
