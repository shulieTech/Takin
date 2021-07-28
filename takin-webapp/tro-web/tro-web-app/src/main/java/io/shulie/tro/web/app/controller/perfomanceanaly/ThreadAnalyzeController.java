package io.shulie.tro.web.app.controller.perfomanceanaly;

import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;
import io.shulie.tro.web.app.request.perfomanceanaly.PerformanceAnalyzeRequest;
import io.shulie.tro.web.app.request.perfomanceanaly.ThreadCpuUseRateRequest;
import io.shulie.tro.web.app.request.perfomanceanaly.ThreadListRequest;
import io.shulie.tro.web.app.request.perfomanceanaly.ThreadStackRequest;
import io.shulie.tro.web.app.response.common.SelectListResponse;
import io.shulie.tro.web.app.response.perfomanceanaly.ProcessBaseDataResponse;
import io.shulie.tro.web.app.response.perfomanceanaly.ThreadCpuChartResponse;
import io.shulie.tro.web.app.response.perfomanceanaly.ThreadCpuUseRateChartResponse;
import io.shulie.tro.web.app.response.perfomanceanaly.ThreadListResponse;
import io.shulie.tro.web.app.response.perfomanceanaly.ThreadStackInfoResponse;
import io.shulie.tro.web.app.service.perfomanceanaly.PerformanceBaseDataService;
import io.shulie.tro.web.app.service.perfomanceanaly.ThreadAnalyService;
import io.shulie.tro.web.app.service.report.impl.ReportApplicationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName ThreadAnalyController
 * @Description 线程分析
 * @Author qianshui
 * @Date 2020/11/4 上午11:05
 */
@RestController
@RequestMapping("/api/thread")
@Api(tags = "线程分析")
public class ThreadAnalyzeController {

    @Autowired
    private ReportApplicationService reportApplicationService;

    @Autowired
    private PerformanceBaseDataService performanceBaseDataService;

    @Autowired
    private ThreadAnalyService threadAnalyService;

    @GetMapping("/application")
    @ApiOperation(value = "应用列表")
    public List<SelectListResponse> getApplicationList(Long reportId) {
        List<String> dataList = reportApplicationService.getReportApplication(reportId).getApplicationNames();
        Collections.sort(dataList);
        return convert(dataList);
    }

    @GetMapping("/process")
    @ApiOperation(value = "进程名称列表")
    public List<SelectListResponse> getProcess(Long reportId, String appName) {
        return convert(performanceBaseDataService.getProcessName(reportId, appName));
    }

    @GetMapping("/base")
    @ApiOperation(value = "基础信息")
    public ProcessBaseDataResponse getBaseData(PerformanceAnalyzeRequest request) {
        return threadAnalyService.getBaseData(request);
    }

    @GetMapping("/analyze")
    @ApiOperation(value = "分析图表")
    public List<ThreadCpuChartResponse> getThreadAnalyze(PerformanceAnalyzeRequest request) {
        return threadAnalyService.getThreadAnalyze(request);
    }

    @GetMapping("/list")
    @ApiOperation(value = "线程列表")
    public ThreadListResponse getThreadList(ThreadListRequest request) { return threadAnalyService.getThreadList(request);
    }

    @PostMapping("/getThreadStackInfo")
    @ApiOperation(value = "获取线程栈信息")
    public ThreadStackInfoResponse getThreadStackInfo(@RequestBody ThreadStackRequest request) {
        ThreadStackInfoResponse response = new ThreadStackInfoResponse();
        response.setThreadStack(threadAnalyService.getThreadStackInfo(request.getLink()));
        return response;
    }

    @GetMapping("/cpuUseRate")
    @ApiOperation(value = "线程cpu占用率图表")
    public List<ThreadCpuUseRateChartResponse> getThreadCpuUseRate(ThreadCpuUseRateRequest request) {
        return threadAnalyService.getThreadCpuUseRate(request);
    }

    private List<SelectListResponse> convert(List<String> dataList) {
        if(CollectionUtils.isEmpty(dataList)) {
            return Collections.EMPTY_LIST;
        }
        List<SelectListResponse> responses = Lists.newArrayList();
        dataList.stream().forEach(data -> {
            SelectListResponse temp = new SelectListResponse();
            temp.setLabel(data);
            temp.setValue(data);
            responses.add(temp);
        });
        return responses;
    }
}
