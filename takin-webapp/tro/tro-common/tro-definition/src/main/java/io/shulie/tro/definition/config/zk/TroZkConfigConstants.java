package io.shulie.tro.definition.config.zk;

/**
 * Agent 和控制台交互的时候，如果通过zk拿参数，路径在这里
 * @author shiyajian
 * create: 2020-12-09
 */
public final class TroZkConfigConstants {

    private TroZkConfigConstants() { /* no instance */ }

    public static final String NAME_SPACE = "/tro/config";

    /**
     * 白名单，按应用名保存
     */
    public static final String ALLOW_LIST_PARENT_PATH = "/allow_list";

    /**
     * 黑名单
     */
    public static final String BLOCK_LIST_PARENT_PATH = "/block_list";

    /**
     * 影子库表，按应用名保存
     */
    public static final String SHADOW_DB_PARENT_PATH = "/shadow_db";

    /**
     * 影子job，按应用名保存
     */
    public static final String SHADOW_JOB_PARENT_PATH = "/shadow_job";

    /**
     * 挡板信息，按应用名保存
     */
    public static final String LINK_GUARD_PARENT_PATH = "/guard";

    /**
     * 全局压测开关
     */
    public static final String CLUSTER_TEST_SWITCH_PATH = "/switch/cluster_test";

    /**
     * 全局白名单开关
     */
    public static final String ALLOW_LIST_SWITCH_PATH = "/switch/allow_list";
}
