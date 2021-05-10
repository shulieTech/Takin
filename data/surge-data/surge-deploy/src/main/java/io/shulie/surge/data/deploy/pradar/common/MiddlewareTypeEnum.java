package io.shulie.surge.data.deploy.pradar.common;

public enum MiddlewareTypeEnum {
    DEFAULT(""),
    APP("APP"),
    HTTP("HTTP"),
    DUBBO("DUBBO"),
    MYSQL("MYSQL"),
    ORACLE("ORACLE"),
    SQLSERVER("SQLSERVER"),
    CASSANDRA("CASSANDRA"),
    MONGODB("MONGODB"),
    HBASE("HBASE"),
    HESSIAN("HESSIAN"),
    CACHE("CACHE"),
    REDIS("REDIS"),
    MEMCACHE("MEMCACHE"),
    ROCKETMQ("ROCKETMQ"),
    KAFKA("KAFKA"),
    ACTIVEMQ("ACTIVEMQ"),
    IBMMQ("IBMMQ"),
    RABBITMQ("RABBITMQ"),
    ES("ES"),
    ELASTICJOB("ELASTIC-JOB"),
    OSS("OSS"),
    UNKNOWN("UNKNOWN");

    String type;

    MiddlewareTypeEnum(String type) {
        this.type = type;
    }

    public static MiddlewareTypeEnum getNodeType(String middlewareName) {
        if (middlewareName == null || "".equals(middlewareName.trim())) {
            return MiddlewareTypeEnum.UNKNOWN;
        }
        if (middlewareName.toLowerCase().contains("http")) {
            middlewareName = "http";
        }
        switch (middlewareName.toLowerCase()) {
            case "app":
                return MiddlewareTypeEnum.APP;
            case "dubbo":
            case "apache-dubbo":
                return MiddlewareTypeEnum.DUBBO;
            case "apache-rocketmq":
            case "rocketmq":
            case "ons":
                return MiddlewareTypeEnum.ROCKETMQ;
            case "apache-kafka":
            case "kafka":
            case "sf-kafka":
                return MiddlewareTypeEnum.KAFKA;
            case "apache-activemq":
            case "activemq":
                return MiddlewareTypeEnum.ACTIVEMQ;
            case "ibmmq":
                return MiddlewareTypeEnum.IBMMQ;
            case "rabbitmq":
                return MiddlewareTypeEnum.RABBITMQ;
            case "hbase":
            case "aliyun-hbase":
                return MiddlewareTypeEnum.HBASE;
            case "hessian":
                return MiddlewareTypeEnum.HESSIAN;
            case "tfs":
                return MiddlewareTypeEnum.OSS;
            case "http":
            case "undertow":
            case "tomcat":
            case "jetty":
            case "jdk-http":
            case "netty-gateway":
            case "webflux":
            case "okhttp":
                return MiddlewareTypeEnum.HTTP;
            case "oss":
                return MiddlewareTypeEnum.OSS;
            case "mysql":
                return MiddlewareTypeEnum.MYSQL;
            case "oracle":
                return MiddlewareTypeEnum.ORACLE;
            case "sqlserver":
                return MiddlewareTypeEnum.SQLSERVER;
            case "cassandra":
                return MiddlewareTypeEnum.CASSANDRA;
            case "mongodb":
                return MiddlewareTypeEnum.MONGODB;
            case "elasticsearch":
                return MiddlewareTypeEnum.ES;
            case "redis":
                return MiddlewareTypeEnum.REDIS;
            case "memcache":
                return MiddlewareTypeEnum.MEMCACHE;
            case "cache":
                return MiddlewareTypeEnum.CACHE;
            case "search":
                return MiddlewareTypeEnum.ES;
            case "elastic-job":
                return MiddlewareTypeEnum.ELASTICJOB;
            default:
                return buildEnum(middlewareName.toUpperCase());
        }
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static MiddlewareTypeEnum buildEnum(String type) {
        MiddlewareTypeEnum defaultEnum = DEFAULT;
        defaultEnum.setType(type);
        return defaultEnum;
    }
}
