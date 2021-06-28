package io.shulie.amdb.common.enums;

public final class RpcType {

    /**
     * web server类型
     */
    static public final int TYPE_WEB_SERVER = 0;

    /**
     * rpc
     */
    static public final int TYPE_RPC = 1;

    /**
     * 消息
     */
    static public final int TYPE_MQ = 3;

    /**
     * db
     */
    static public final int TYPE_DB = 4;

    /**
     * 缓存
     */
    static public final int TYPE_CACHE = 5;

    /**
     * 索引
     */
    static public final int TYPE_SEARCH = 6;

    /**
     * job
     */
    static public final int TYPE_JOB = 7;

    /**
     * 文件
     */
    static public final int TYPE_FS = 8;

    /**
     * 本地方法
     */
    static public final int TYPE_LOCAL = 9;

    /**
     * 未知
     */
    static public final int TYPE_UNKNOW = -1;

}