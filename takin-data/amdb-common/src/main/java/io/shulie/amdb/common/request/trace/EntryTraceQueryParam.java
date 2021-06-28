package io.shulie.amdb.common.request.trace;

import lombok.Data;

import java.util.Set;

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
    String taskId;
    Integer pageSize;
    Integer currentPage;
    Set<String> traceIdList;

    @Override
    public String toString() {
        return "EntryTraceQueryParam{" +
                "clusterTest='" + clusterTest + '\'' +
                ", resultType='" + resultType + '\'' +
                ", rpcType='" + rpcType + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", methodName='" + methodName + '\'' +
                ", entranceList='" + entranceList + '\'' +
                ", appName='" + appName + '\'' +
                ", fieldNames='" + fieldNames + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", taskId='" + taskId + '\'' +
                ", pageSize=" + pageSize +
                ", currentPage=" + currentPage +
                ", traceIdList=" + traceIdList +
                '}';
    }
}