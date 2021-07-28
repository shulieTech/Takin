package io.shulie.tro.web.app.controller.perfomanceanaly;

import java.util.List;

import io.shulie.tro.web.app.constant.AgentUrls;
import io.shulie.tro.web.app.exception.ExceptionCode;
import io.shulie.tro.web.app.exception.TroWebException;
import io.shulie.tro.web.app.request.perfomanceanaly.TraceManageCreateRequest;
import io.shulie.tro.web.app.request.perfomanceanaly.TraceManageDeployQueryRequest;
import io.shulie.tro.web.app.request.perfomanceanaly.TraceManageQueryListRequest;
import io.shulie.tro.web.app.response.perfomanceanaly.TraceManageCreateResponse;
import io.shulie.tro.web.app.response.perfomanceanaly.TraceManageResponse;
import io.shulie.tro.web.app.service.perfomanceanaly.TraceManageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhaoyong
 */
@RestController
@RequestMapping(value = AgentUrls.PREFIX_URL)
@Api(tags = "方法追踪")
public class TraceManageController {

    @Autowired
    private TraceManageService traceManageService;

    @PostMapping("/traceManage/createTraceManage")
    @ApiOperation(value = "创建方法追踪，获取trace方法追踪凭证")
    public TraceManageCreateResponse createTraceManage(@RequestBody TraceManageCreateRequest traceManageCreateRequest)
        throws Exception {
        return traceManageService.createTraceManage(traceManageCreateRequest);
    }

    @GetMapping("/traceManage/queryTraceManageDeploy")
    @ApiOperation(value = "查询方法追踪树状结构信息")
    public TraceManageResponse queryTraceManageDeploy(@ApiParam(value = "传入traceManageId 查询所有信息") Long id,
                                                      @ApiParam(value = "追踪凭证id") String sampleId) {
        TraceManageDeployQueryRequest traceManageDeployQueryRequest = new TraceManageDeployQueryRequest();
        traceManageDeployQueryRequest.setId(id);
        traceManageDeployQueryRequest.setSampleId(sampleId);
        return traceManageService.queryTraceManageDeploy(traceManageDeployQueryRequest);
    }

    @GetMapping("/traceManage/queryTraceManageList")
    @ApiOperation(value = "查询方法追踪列表信息")
    public List<TraceManageResponse> queryTraceManageList(@ApiParam(value = "报告id") Long reportId,
                                                          @ApiParam(value = "进程名称") String processName) {
        if (StringUtils.isEmpty(processName)){
            throw new TroWebException(ExceptionCode.TRACE_MANAGE_PARAM_VALID_ERROR,"报告id为空");
        }
        if (reportId == null){
            throw new TroWebException(ExceptionCode.TRACE_MANAGE_PARAM_VALID_ERROR,"进程名称为空");
        }

        TraceManageQueryListRequest traceManageQueryListRequest = new TraceManageQueryListRequest();
        traceManageQueryListRequest.setReportId(reportId);
        String[] split = StringUtils.split(processName, "|");
        traceManageQueryListRequest.setAgentId(split[1]);
        return traceManageService.queryTraceManageList(traceManageQueryListRequest);
    }



}
