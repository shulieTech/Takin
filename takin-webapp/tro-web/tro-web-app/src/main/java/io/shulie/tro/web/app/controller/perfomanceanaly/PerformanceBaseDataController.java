package io.shulie.tro.web.app.controller.perfomanceanaly;

import java.util.concurrent.atomic.AtomicInteger;

import io.shulie.tro.web.app.constant.AgentUrls;
import io.shulie.tro.web.app.convert.performace.PerformanceBaseReqConvert;
import io.shulie.tro.web.app.input.PerformanceBaseDataCreateInput;
import io.shulie.tro.web.app.req.perfomanceanaly.PerformanceBaseDataReq;
import io.shulie.tro.web.app.service.perfomanceanaly.PerformanceBaseDataService;
import io.shulie.tro.web.app.service.perfomanceanaly.ThreadAnalyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName PerformanceBaseDataController
 * @Description
 * @Author qianshui
 * @Date 2020/11/4 下午2:49
 */
@RestController
@RequestMapping(value = AgentUrls.PREFIX_URL)
@Api(tags = "性能分析数据")
public class PerformanceBaseDataController {

    @Value("${performance.base.agent.frequency:1}")
    private String frequency;
    @Autowired
    private PerformanceBaseDataService performanceBaseDataService;
    @Autowired
    private ThreadAnalyService threadAnalyService;

    private static AtomicInteger integer = new AtomicInteger();
    @PostMapping(value = AgentUrls.PERFORMANCE_BASE_URL)
    @ApiOperation(value = "接收agent写入数据")
    public void receivePerformanceBaseData(@RequestBody PerformanceBaseDataReq req) {
        if(integer.get() > 100000000) {
            integer.set(0);
        }
        if(integer.getAndIncrement() % Integer.parseInt(frequency) == 0) {
            PerformanceBaseDataCreateInput input = PerformanceBaseReqConvert.INSTANCE.reqToInput(req);
            performanceBaseDataService.cache(input);
        }
    }

    /**
     * 秒级别
     * @param second
     */
    @GetMapping("/clear")
    @ApiOperation(value = "清理数据接口")
    public String clear(@RequestParam("second") Integer second) {
        performanceBaseDataService.clearData(Integer.valueOf(second));
        threadAnalyService.clearData(Integer.valueOf(second));
        return "清理成功";
    }

}
