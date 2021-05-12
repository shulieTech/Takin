package io.shulie.amdb.service;

import com.pamirs.pradar.log.parser.trace.RpcBased;
import io.shulie.amdb.common.Response;
import io.shulie.amdb.common.request.trace.EntryTraceQueryParam;
import io.shulie.amdb.common.trace.EntryTraceInfoDTO;

import java.util.List;
import java.util.Map;

public interface TraceService {

    Response<List<EntryTraceInfoDTO>> getEntryTraceInfo(EntryTraceQueryParam param);

    List<RpcBased> getTraceDetail(String traceId);

    Response<Map<String,List<RpcBased>>> getTraceInfo(EntryTraceQueryParam param);

}
