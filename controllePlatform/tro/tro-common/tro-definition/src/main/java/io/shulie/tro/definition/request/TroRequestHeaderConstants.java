package io.shulie.tro.definition.request;

/**
 * @author shiyajian
 * create: 2020-09-21
 */
public final class TroRequestHeaderConstants {

    private TroRequestHeaderConstants() { /* no instance */ }

    /**
     * 压测请求的 Header 头
     */
    public static final String CLUSTER_TEST_HEADER_KEY = "User-Agent";

    /**
     * 压测请求对应的值
     * 注意：Agent 里面，目前单词拼写错误，为"Perfomance"，此处将错就错的一直下去了
     */
    public static final String CLUSTER_TEST_HEADER_VALUE = "PerfomanceTest";

    /**
     * 流量的客户ID，用于数据隔离，客户ID+场景ID+任务ID构成唯一值
     */
    public static final String CLUSTER_TEST_CUSTOMER_HEADER_VALUE = "Tro-Customer-ID";

    /**
     * 压测任务的场景ID，用于数据隔离，客户ID+场景ID+任务ID构成唯一值
     */
    public static final String CLUSTER_TEST_SCENE_HEADER_VALUE = "Tro-Scene-ID";

    /**
     * 压测任务的ID，用于数据隔离，客户ID+场景ID+任务ID构成唯一值
     */
    public static final String CLUSTER_TEST_TASK_HEADER_VALUE = "Tro-Task-ID";


}
