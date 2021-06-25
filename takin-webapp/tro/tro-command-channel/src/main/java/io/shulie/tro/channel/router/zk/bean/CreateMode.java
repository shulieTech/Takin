package io.shulie.tro.channel.router.zk.bean;

/**
 * @author: HengYu
 * @className: CreateMode
 * @date: 2020/12/29 10:20 下午
 * @description: 定义了 znode 节点的创建模式
 */

public enum CreateMode {

    /**
     * The znode will not be automatically deleted upon client's disconnect.
     */
    PERSISTENT,
    /**
     * The znode will not be automatically deleted upon client's disconnect,
     * and its name will be appended with a monotonically increasing number.
     */
    PERSISTENT_SEQUENTIAL,
    /**
     * The znode will be deleted upon the client's disconnect.
     */
    EPHEMERAL,
    /**
     * The znode will be deleted upon the client's disconnect, and its name
     * will be appended with a monotonically increasing number.
     */
    EPHEMERAL_SEQUENTIAL;
}