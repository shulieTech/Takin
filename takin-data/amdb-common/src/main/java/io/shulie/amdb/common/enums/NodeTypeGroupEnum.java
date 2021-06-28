package io.shulie.amdb.common.enums;

import lombok.Getter;

@Getter
public enum NodeTypeGroupEnum {
    /**
     * APP
     */
    APP("APP"),
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
     * OTHER
     */
    OTHER("OTHER");

    String type;

    NodeTypeGroupEnum(String type) {
        this.type = type;
    }

    public static NodeTypeGroupEnum getNodeType(String middlewareName) {
        if (middlewareName == null || "".equals(middlewareName.trim())) {
            return NodeTypeGroupEnum.APP;
        }
        switch (middlewareName.toLowerCase()) {
            case "app":
            case "dubbo":
            case "apache-dubbo":
            case "http":
            case "undertow":
            case "weblogic":
            case "tomcat":
            case "jetty":
                return NodeTypeGroupEnum.APP;
            case "apache-rocketmq":
            case "rocketmq":
            case "ons":
            case "apache-kafka":
            case "kafka":
            case "sf-kafka":
            case "apache-activemq":
            case "activemq":
            case "ibmmq":
            case "rabbitmq":
                return NodeTypeGroupEnum.MQ;
            case "hbase":
            case "aliyun-hbase":
            case "hessian":
            case "mysql":
            case "oracle":
            case "sqlserver":
            case "cassandra":
            case "postgresql":
            case "mongodb":
                return NodeTypeGroupEnum.DB;
            case "tfs":
            case "oss":
                return NodeTypeGroupEnum.OSS;
            case "elasticsearch":
            case "search":
                return NodeTypeGroupEnum.DB;
            case "redis":
            case "memcache":
            case "cache":
            case "google-guava":
                return NodeTypeGroupEnum.CACHE;
            default:
                return NodeTypeGroupEnum.OTHER;
        }
    }
}
