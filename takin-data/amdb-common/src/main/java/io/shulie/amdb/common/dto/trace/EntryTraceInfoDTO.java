package io.shulie.amdb.common.dto.trace;

import lombok.Data;

@Data
public class EntryTraceInfoDTO {
    String serviceName;
    String methodName;
    String appName;
    String remoteIp;
    String port;
    String resultCode;
    long cost;
    long startTime;
    String traceId;
    String response;
    String request;
    String assertResult;
}
