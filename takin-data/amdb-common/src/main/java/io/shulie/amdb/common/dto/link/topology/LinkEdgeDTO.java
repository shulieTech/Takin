package io.shulie.amdb.common.dto.link.topology;

import lombok.Data;

@Data
public class LinkEdgeDTO {
    String sourceId;
    String targetId;
    String eagleId;
    String eagleType;
    String eagleTypeGroup;
    String serverAppName;
    //Object extendInfo;
    //服务
    String service;
    //方法
    String method;
    //扩展信息
    String extend;
    //应用名称
    String appName;
    //RPC类型
    String rpcType;
    //日志类型
    String logType;
    //中间件名称
    String middlewareName;
}
