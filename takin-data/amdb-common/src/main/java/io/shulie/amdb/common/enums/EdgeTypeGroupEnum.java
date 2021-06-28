package io.shulie.amdb.common.enums;

import lombok.Getter;

@Getter
@Deprecated
public enum EdgeTypeGroupEnum {
    /**
     * DUBBO
     */
    DUBBO("DUBBO"),
    /**
     * HTTP
     */
    HTTP("HTTP"),
    /**
     * DB
     */
    DB("DB"),
    /**
     * CACHE
     */
    CACHE("CACHE"),
    /**
     * MQ
     */
    MQ("MQ"),
    /**
     * SEARCH
     */
    SEARCH("SEARCH"),
    /**
     * OSS
     */
    OSS("OSS"),
    /**
     * JOB
     */
    JOB("JOB"),
    /**
     * UNKNOWN
     */
    UNKNOWN("UNKNOWN");

    String type;

    EdgeTypeGroupEnum(String type) {
        this.type = type;
    }

    public static EdgeTypeGroupEnum getEdgeTypeEnum(String middlewareName) {
        if (middlewareName == null || "".equals(middlewareName.trim())) {
            return EdgeTypeGroupEnum.UNKNOWN;
        }
        switch (middlewareName.toLowerCase()) {
            case "dubbo":
            case "apache-dubbo":
                return EdgeTypeGroupEnum.DUBBO;
            case "apache-rocketmq":
            case "rocketmq":
            case "ons":
            case "apache-kafka":
            case "kafka":
            case "apache-activemq":
            case "activemq":
            case "ibmmq":
            case "rabbitmq":
            case "sf-kafka":
                return EdgeTypeGroupEnum.MQ;
            case "hbase":
            case "aliyun-hbase":
            case "hessian":
            case "mysql":
            case "oracle":
            case "sqlserver":
            case "cassandra":
            case "mongodb":
                return EdgeTypeGroupEnum.DB;
            case "tfs":
            case "oss":
                return EdgeTypeGroupEnum.OSS;
            case "http":
            case "undertow":
            case "tomcat":
            case "virtual":
            case "jetty":
            case "jdk-http":
            case "weblogic":
            case "okhttp":
                return EdgeTypeGroupEnum.HTTP;
            case "elasticsearch":
            case "search":
                return EdgeTypeGroupEnum.SEARCH;
            case "redis":
            case "memcache":
            case "cache":
            case "google-guava":
                return EdgeTypeGroupEnum.CACHE;
            case "elastic-job":
                return EdgeTypeGroupEnum.JOB;
            default:
                if (middlewareName.toLowerCase().contains("http")) {
                    return EdgeTypeGroupEnum.HTTP;
                }
                return EdgeTypeGroupEnum.UNKNOWN;
        }
    }

}
