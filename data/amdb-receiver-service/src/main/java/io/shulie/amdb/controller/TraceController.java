package io.shulie.amdb.controller;

import com.pamirs.pradar.log.parser.trace.RpcBased;
import io.shulie.amdb.common.Response;
import io.shulie.amdb.common.dto.trace.EntryTraceInfoDTO;
import io.shulie.amdb.common.request.trace.EntryTraceQueryParam;
import io.shulie.amdb.dto.LogResultDTO;
import io.shulie.amdb.request.query.LogResultRequest;
import io.shulie.amdb.service.TraceService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: cyf
 */
@RestController
@Api(description = "trace查询")
@RequestMapping(value = "/amdb/trace")
/**
 * 调用链查询
 * @Author: xingchen
 * @Date: 2020/11/419:51
 * @Description:
 */
public class TraceController {
    @Autowired
    private TraceService traceService;

    /**
     * 流量明细
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getEntryTraceList", method = RequestMethod.GET)
    public Response<List<EntryTraceInfoDTO>> getLinkDetail(EntryTraceQueryParam param) {
        return traceService.getEntryTraceInfo(param);
    }

    /**
     * 链路详情
     *
     * @param traceId
     * @return
     */
    @RequestMapping(value = "/getTraceDetail", method = RequestMethod.GET)
    public Response<List<RpcBased>> getTraceDetail(String traceId) {
        return Response.success(traceService.getTraceDetail(traceId));
    }

    /**
     * trace日志查询
     *
     * @param logResultRequest
     * @return
     */
    @RequestMapping(value = "/log/query", method = RequestMethod.GET)
    public Response<List<LogResultDTO>> logQuery(LogResultRequest logResultRequest) {
        return Response.success(new ArrayList<>());
    }
}
