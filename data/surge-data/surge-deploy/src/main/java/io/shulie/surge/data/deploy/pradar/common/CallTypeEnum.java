package io.shulie.surge.data.deploy.pradar.common;

/**
 * @Author: xingchen
 * @ClassName: CallTypeEnum
 * @Package: io.shulie.surge.data.deploy.pradar.common
 * @Date: 2020/12/320:36
 * @Description:
 */
public enum CallTypeEnum {
    CALL_HTTP("call-http"),
    CALL_DUBBO("call-dubbo"),
    CALL_DB("call-db"),
    CAll_CACHE("call-cace"),
    CALL_ROCKETMQ("call-rocketmq"),
    CALL_ACTIVEMQ("call-activemq"),
    CALL_KAFKA("call-kafka"),
    CALL_RABBITMQ("call-rabbitmq"),
    CALL_IBMMQ("call-ibmmq");

    private String value;
    CallTypeEnum(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

