package io.shulie.tro.web.app.controller.perfomanceanaly;

import io.shulie.tro.web.app.request.perfomanceanaly.MemoryAnalysisRequest;
import io.shulie.tro.web.app.response.perfomanceanaly.DownloadDumpResponse;
import io.shulie.tro.web.app.response.perfomanceanaly.MemoryAnalysisResponse;
import io.shulie.tro.web.app.service.perfomanceanaly.MemoryAnalysisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName MemoryAnalyController
 * @Description 内存分析
 * @Author qianshui
 * @Date 2020/11/4 上午11:05
 */
@RestController
@RequestMapping("/api/memory")
@Api(tags = "内存分析")
public class MemoryAnalyzeController {

    @Autowired
    private MemoryAnalysisService memoryAnalysisService;

    @PostMapping("/dump")
    @ApiOperation(value = "获取内存分析数据")
    public MemoryAnalysisResponse getMemoryDump(@RequestBody MemoryAnalysisRequest request) {
        return memoryAnalysisService.queryMemoryDump(request);

    }

    /**
     * 上传到zk
     */
    @GetMapping("/download/dump")
    @ApiOperation(value = "上传zk")
    public DownloadDumpResponse downloadDump(@RequestParam("agentId") String agentId) throws Throwable {
        return memoryAnalysisService.downloadDump(agentId);
    }

}
