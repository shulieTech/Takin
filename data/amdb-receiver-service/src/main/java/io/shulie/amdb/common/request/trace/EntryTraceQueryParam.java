package io.shulie.amdb.common.request.trace;

import lombok.Data;

@Data
public class EntryTraceQueryParam {
    String clusterTest;
    String resultType;
    String rpcType;
    String serviceName;
    String methodName;
    String entranceList;
    String appName;
    String fieldNames;
    Long startTime;
    Long endTime;
    Integer pageSize;
    Integer currentPage;
}