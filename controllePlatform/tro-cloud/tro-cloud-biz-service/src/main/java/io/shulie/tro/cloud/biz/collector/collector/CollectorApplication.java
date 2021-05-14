/*
 * Copyright 2021 Shulie Technology, Co.Ltd
 * Email: shulie@shulie.io
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.shulie.tro.cloud.biz.collector.collector;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import io.shulie.tro.cloud.common.bean.collector.Constants;
import io.shulie.tro.cloud.common.bean.collector.EventMetrics;
import io.shulie.tro.cloud.common.bean.collector.ResponseMetrics;
import io.shulie.tro.cloud.biz.collector.bean.ServerStatusInfo;
import io.shulie.tro.cloud.common.utils.GsonUtil;
import io.shulie.tro.cloud.common.utils.IPUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author <a href="tangyuhan@shulie.io">yuhan.tang</a>
 * @package: io.shulie.tro.collector
 * @Date 2020-04-17 17:20
 */
@Slf4j
@RestController
@RequestMapping("/api/collector")
public class CollectorApplication {

    @Autowired
    private CollectorService collectorService;

    @RequestMapping("/receive")
    public ResponseEntity<String> receive(@RequestParam("sceneId") Long sceneId,
        @RequestParam("reportId") Long reportId,
        @RequestParam(value = "customerId", required = false) Long customerId,
        @RequestBody List<Map> metrics,
        HttpServletRequest request) {
        try {
            if (sceneId == null || reportId == null) {
                return ResponseEntity.ok("唯一标示不能为空");
            }
            if (null == metrics || metrics.size() < 1) {
                return ResponseEntity.ok("metrics数据为空");
            }

            // 分类
            List<ResponseMetrics> responseMetrics =
                metrics.stream().filter(metric -> Constants.METRICS_TYPE_RESPONSE.equals(metric.get("type"))).map(
                    metric -> GsonUtil.gsonToBean(GsonUtil.gsonToString(metric), ResponseMetrics.class)).collect(
                    Collectors.toList());
            List<EventMetrics> eventMetrics =
                metrics.stream().filter(metric -> Constants.METRICS_TYPE_EVENTS.equals(metric.get("type"))).map(
                    metric -> GsonUtil.gsonToBean(GsonUtil.gsonToString(metric), EventMetrics.class)).collect(
                    Collectors.toList());

            long time = System.currentTimeMillis();
            if (responseMetrics.size() > 0) {
                long timestamp = responseMetrics.get(0).getTimestamp();
                log.debug("【Collector-metrics-debug】{}-{}-{}:receive metrics data:{}",sceneId, reportId,customerId,GsonUtil.gsonToString(responseMetrics));
                log.info("【Collector-metrics】{}-{}-{}: receive metrics data:{},metrics time:{},elapsed time:{}",
                    sceneId, reportId,customerId, responseMetrics.size(), timestamp, (System.currentTimeMillis() - time));
                collectorService.collector(sceneId, reportId, customerId, responseMetrics);
                collectorService.statisticalIp(sceneId, reportId, customerId, timestamp, IPUtils.getIP(request));
            }
            if (eventMetrics.size() > 0) {
                log.info("Collector-metrics-event】{}-{}-{}:{}", sceneId, reportId, customerId,
                    GsonUtil.gsonToString(eventMetrics));
                collectorService.verifyEvent(sceneId, reportId, customerId, eventMetrics);
            }
            return ResponseEntity.ok("success");
        } catch (Exception e) {
            log.error("【Collector-metrics-Error】: {}", e.getMessage(), e);
            return ResponseEntity.ok(e.getMessage());
        }
    }

    @RequestMapping("/system/receive")
    public ResponseEntity<String> receive(HttpServletRequest request, @RequestBody ServerStatusInfo serverInfoMap) {
        try {
            if (null == serverInfoMap) {
                return ResponseEntity.ok("上传的数据为空");
            }
            String ip = IPUtils.getIP(request);
            log.info("【Collector-Server】ip:{},receive ip:{}, data:{}", ip, serverInfoMap.getIp(),
                serverInfoMap.toString());
            //            collectorService.collector(scenId, reportId, metrics);
            return ResponseEntity.ok("success");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.ok(e.getMessage());
        }
    }
}
