package io.shulie.amdb.common.enums;

import lombok.Getter;

@Getter
public enum NodeTypeEnum {
    /**
     * APP
     */
    APP("APP"),
    /**
     * GATEWAY
     */
    GATEWAY("GATEWAY"),
    /**
     * DB
     */
    DB("DB"),
    /**
     * MYSQL
     */
    MYSQL("MYSQL"),
    /**
     * POSTGRESQL
     */
    POSTGRESQL("POSTGRESQL"),
    /**
     * ORACLE
     */
    ORACLE("ORACLE"),
    /**
     * SQLSERVER
     */
    SQLSERVER("SQLSERVER"),
    /**
     * CASSANDRA
     */
    CASSANDRA("CASSANDRA"),
    /**
     * MONGODB
     */
    MONGODB("MONGODB"),
    /**
     * HBASE
     */
    HBASE("HBASE"),
    /**
     * HESSIAN
     */
    HESSIAN("HESSIAN"),
    /**
     * CACHE
     */
    CACHE("CACHE"),
    /**
     * RDS
     */
    RDS("RDS"),
    /**
     * REDIS
     */
    REDIS("REDIS"),
    /**
     * MEMCACHE
     */
    MEMCACHE("MEMCACHE"),
    /**
     * MQ
     */
    MQ("MQ"),
    /**
     * ROCKETMQ
     */
    ROCKETMQ("ROCKETMQ"),
    /**
     * KAFKA
     */
    KAFKA("KAFKA"),
    /**
     * ACTIVEMQ
     */
    ACTIVEMQ("ACTIVEMQ"),
    /**
     * IBMMQ
     */
    IBMMQ("IBMMQ"),
    /**
     * RABBITMQ
     */
    RABBITMQ("RABBITMQ"),
    /**
     * OTHER
     */
    OTHER("OTHER"),
    /**
     * ES
     */
    ES("ES"),
    /**
     * OSS
     */
    OSS("OSS"),
    /**
     * VIRTUAL
     */
    VIRTUAL("VIRTUAL"),
    /**
     * UNKNOWN
     */
    UNKNOWN("UNKNOWN");

    String type;

    NodeTypeEnum(String type) {
        this.type = type;
    }

    public static NodeTypeEnum getNodeType(String middlewareName) {
        if (middlewareName == null || "".equals(middlewareName.trim())) {
            return NodeTypeEnum.APP;
        }
        switch (middlewareName.toLowerCase()) {
            case "app":
                return NodeTypeEnum.APP;
            case "dubbo":
            case "apache-dubbo":
                return NodeTypeEnum.APP;
            case "apache-rocketmq":
            case "rocketmq":
            case "ons":
                return NodeTypeEnum.ROCKETMQ;
            case "apache-kafka":
            case "kafka":
            case "sf-kafka":
                return NodeTypeEnum.KAFKA;
            case "apache-activemq":
            case "activemq":
                return NodeTypeEnum.ACTIVEMQ;
            case "ibmmq":
                return NodeTypeEnum.IBMMQ;
            case "rabbitmq":
                return NodeTypeEnum.RABBITMQ;
            case "hbase":
            case "aliyun-hbase":
                return NodeTypeEnum.HBASE;
            case "hessian":
                return NodeTypeEnum.HESSIAN;
            case "tfs":
                return NodeTypeEnum.OSS;
            case "http":
            case "undertow":
            case "tomcat":
            case "weblogic":
            case "jetty":
                return NodeTypeEnum.APP;
            case "oss":
                return NodeTypeEnum.OSS;
            case "mysql":
                return NodeTypeEnum.MYSQL;
            case "postgresql":
                return NodeTypeEnum.POSTGRESQL;
            case "oracle":
                return NodeTypeEnum.ORACLE;
            case "sqlserver":
                return NodeTypeEnum.SQLSERVER;
            case "cassandra":
                return NodeTypeEnum.CASSANDRA;
            case "mongodb":
                return NodeTypeEnum.MONGODB;
            case "elasticsearch":
                return NodeTypeEnum.ES;
            case "redis":
                return NodeTypeEnum.REDIS;
            case "memcache":
                return NodeTypeEnum.MEMCACHE;
            case "cache":
            case "google-guava":
                return NodeTypeEnum.CACHE;
            case "search":
                return NodeTypeEnum.ES;
            case "virtual":
                return NodeTypeEnum.VIRTUAL;
            default:
                return NodeTypeEnum.APP;
        }
    }

    public static String convertMiddlewareName(String oldMiddlewareName) {
        if (oldMiddlewareName == null) {
            return oldMiddlewareName;
        }
        if (oldMiddlewareName.toLowerCase().contains("netty")) {
            return "http";
        }
        if (oldMiddlewareName.toLowerCase().contains("jetty")) {
            return "http";
        }
        if (oldMiddlewareName.toLowerCase().contains("http")) {
            return "http";
        }
        if (oldMiddlewareName.toLowerCase().contains("dubbo")) {
            return "dubbo";
        }
        if (oldMiddlewareName.toLowerCase().contains("kafka")) {
            return "kafka";
        }
        if (oldMiddlewareName.toLowerCase().contains("rocketmq")) {
            return "rocketmq";
        }
        if (oldMiddlewareName.toLowerCase().contains("cache")) {
            return "cache";
        }
        return oldMiddlewareName;
    }
}
