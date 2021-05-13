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

package io.shulie.tro.cloud.biz.collector;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import com.google.common.collect.Lists;
import io.shulie.tro.cloud.common.bean.collector.Metrics;
import io.shulie.tro.cloud.common.bean.collector.SendMetricsEvent;
import io.shulie.tro.cloud.biz.collector.collector.AbstractIndicators;
import io.shulie.tro.cloud.common.bean.scenemanage.UpdateStatusBean;
import io.shulie.tro.cloud.common.constants.CollectorConstants;
import io.shulie.tro.cloud.common.enums.scenemanage.SceneManageStatusEnum;
import io.shulie.tro.cloud.common.redis.RedisClientUtils;
import io.shulie.tro.cloud.common.utils.CollectorUtil;
import io.shulie.tro.cloud.common.bean.task.TaskResult;
import io.shulie.tro.cloud.common.constants.ScheduleConstants;
import com.google.common.collect.Maps;
import io.shulie.tro.cloud.common.influxdb.InfluxDBUtil;
import io.shulie.tro.cloud.common.influxdb.InfluxWriter;
import io.shulie.tro.eventcenter.Event;
import io.shulie.tro.eventcenter.EventCenterTemplate;
import io.shulie.tro.eventcenter.annotation.IntrestFor;
import io.shulie.tro.eventcenter.entity.TaskConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Author <a href="tangyuhan@shulie.io">yuhan.tang</a>
 * @package: io.shulie.tro.cloud.poll.poll
 * @Date 2020-04-20 22:13
 */
@Slf4j
@Component
public class PushWindowDataScheduled extends AbstractIndicators {

    @Autowired
    private InfluxWriter influxWriter;

    @Autowired
    private EventCenterTemplate eventCenterTemplate;

    @Autowired
    private RedisClientUtils redisClientUtils;


    // todo 之后改成分布式，需要注意，redis 读写锁问题
    /**
     * 用于时间窗口 记忆
     */
    // todo 本地缓存，之后都需要改成redis 解决redis锁问题，以及redis 缓存延迟问题

    private static Map<String,Long> timeWindowMap = Maps.newConcurrentMap();


    public void sendMetrics(Metrics metrics) {
        Event event = new Event();
        event.setEventName("metricsData");
        event.setExt(metrics);
        eventCenterTemplate.doEvents(event);
    }

    @IntrestFor(event = "started")
    public void doStartScheduleTaskEvent(Event event) {
        log.info("PushWindowDataScheduled，从调度中心收到压测任务启动成功事件");
        Object object = event.getExt();
        TaskResult taskBean = (TaskResult)object;
        String taskKey = getTaskKey(taskBean.getSceneId(), taskBean.getTaskId(), taskBean.getCustomerId());
        /**
         * 压测时长 + 预热时长 + 五分钟 7天
         */
        long taskTimeout = 7L * 24 * 60 * 60;
        Map<String, Object> extMap = taskBean.getExtendMap();
        List<String> refList = Lists.newArrayList();
        if (MapUtils.isNotEmpty(extMap)) {
            refList.addAll((List)extMap.get("businessActivityBindRef"));
        }
        ArrayList<String> transation = new ArrayList<>(refList);
        transation.add("all");
        String redisKey = String.format(CollectorConstants.REDIS_PRESSURE_TASK_KEY, taskKey);
        redisTemplate.opsForValue().set(redisKey, transation, taskTimeout, TimeUnit.SECONDS);
        log.info("PushWindowDataScheduled Create Redis Key = {}, expireDuration={}min, refList={} Success....",
            redisKey, taskTimeout, refList);
    }

    // todo 没有用到
    @IntrestFor(event = "stop")
    public void doStopTaskEvent(Event event) {
        TaskConfig taskConfig = (TaskConfig)event.getExt();
        delTask(taskConfig.getSceneId(), taskConfig.getTaskId(), taskConfig.getCustomerId());
    }

    /**
     * 删除 拉取数据
     */
    @IntrestFor(event = "finished")
    public void doDeleteTaskEvent(Event event) {
        try {
            log.info("通知PushWindowDataScheduled模块，从调度中心收到压测任务结束事件");
            TaskResult taskResult = (TaskResult)event.getExt();
            delTask(taskResult.getSceneId(), taskResult.getTaskId(), taskResult.getCustomerId());
        } catch (Exception e) {
            log.error("【PushWindowDataScheduled】处理finished事件异常={}", e.getMessage(), e);
        }
    }

    private void delTask(Long sceneId, Long reportId, Long customerId) {
        if (null != sceneId && null != reportId) {
            String taskKey = getTaskKey(sceneId, reportId, customerId);
            redisTemplate.delete(String.format(CollectorConstants.REDIS_PRESSURE_TASK_KEY, taskKey));
        }
    }

    /**
     * 计算读取的时间窗口
     *
     * @return
     */
    private Long refreshTimeWindow(String engineName) {
        long timeWindow = 0L;
        String tempTimestamp = ScheduleConstants.TEMP_TIMESTAMP_SIGN + engineName;
        // 插入成功 进行计数
        if (timeWindowMap.containsKey(tempTimestamp)) {
            // 获取下一个5s,并更新redis
            timeWindow = CollectorUtil.addWindowTime(timeWindowMap.get(tempTimestamp));
            timeWindowMap.put(tempTimestamp, timeWindow);
            return timeWindow;
        }
        String startTimeKey = engineName + ScheduleConstants.FIRST_SIGN;
        if (redisTemplate.hasKey(startTimeKey)) {
            // 延迟5s 获取数据
            timeWindow = CollectorUtil.getPushWindowTime(
                CollectorUtil.getTimeWindow((Long)redisTemplate.opsForValue().get(startTimeKey)).getTimeInMillis());
            timeWindowMap.put(tempTimestamp, timeWindow);
        }

        return timeWindow;
    }

    /**
     * 每五秒执行一次
     * 每次从redis中取10秒前的数据
     */
    @Async("collectorSchedulerPool")
    @Scheduled(cron = "0/5 * * * * ? ")
    public void pushData() {
        try {
            Set<String> keys = this.keys(String.format(CollectorConstants.REDIS_PRESSURE_TASK_KEY, "*"));
            for (String sceneReportKey : keys) {
                try {
                    if (lock(sceneReportKey, "collectorSchedulerPool")) {
                        int lastIndex = sceneReportKey.lastIndexOf(":");
                        if (-1 == lastIndex) {
                            unlock(sceneReportKey, "collectorSchedulerPool");
                            continue;
                        }
                        String sceneReportId = sceneReportKey.substring(lastIndex + 1);
                        String[] split = sceneReportId.split("_");
                        Long sceneId = Long.valueOf(split[0]);
                        Long reportId = Long.valueOf(split[1]);
                        Long customerId = null;
                        if (split.length == 3) {
                            customerId = Long.valueOf(split[2]);
                        }
                        String engineName = ScheduleConstants.getEngineName(sceneId, reportId, customerId);

                        // 记录一个redis 时间计数开始时间的时间窗口开始
                        long timeWindow = refreshTimeWindow(engineName);

                        if (timeWindow == 0) {
                            unlock(sceneReportKey, "collectorSchedulerPool");
                            continue;
                        }
                        List<String> transactions = (List<String>)redisTemplate.opsForValue().get(sceneReportKey);
                        if (null == transactions || transactions.size() == 0) {
                            unlock(sceneReportKey, "collectorSchedulerPool");
                            continue;
                        }
                        log.info("【collector metric】{}-{}-{}:{}", sceneId, reportId, customerId, timeWindow);
                        String taskKey = getPressureTaskKey(sceneId, reportId, customerId);
                        // 写入数据
                        writeInfluxDB(transactions, taskKey, timeWindow, sceneId, reportId, customerId);
                        // 读取结束标识   手动收尾
                        String last = String.valueOf(redisTemplate.opsForValue().get(last(taskKey)));
                        if (ScheduleConstants.LAST_SIGN.equals(last)) {
                            // 只需触发一次即可
                            String endTimeKey = engineName + ScheduleConstants.LAST_SIGN;
                            long endTime = CollectorUtil.getEndWindowTime(
                                (Long)redisTemplate.opsForValue().get(endTimeKey));
                            log.info("触发手动收尾操作，当前时间窗口：{},结束时间窗口：{},",timeWindow, endTime);
                            // 比较 endTime timeWindow
                            // 如果结束时间 小于等于当前时间，数据不用补充，
                            // 如果结束时间 大于 当前时间，需要补充期间每5秒的数据 延后5s
                            endTime = CollectorUtil.addWindowTime(endTime);
                            while (endTime > timeWindow) {
                                timeWindow = CollectorUtil.addWindowTime(timeWindow);
                                // 1、确保 redis->influxDB
                                log.info("redis->influxDB，当前时间窗口：{},", timeWindow);
                                writeInfluxDB(transactions, taskKey, timeWindow, sceneId, reportId, customerId);
                            }
                            log.info("本次压测{}-{}-{},metric数据已经全部上报influxDB", sceneId, reportId, customerId);
                            // 清除 SLA配置 清除PushWindowDataScheduled 删除pod job configMap  生成报告
                            Event event = new Event();
                            event.setEventName("finished");
                            event.setExt(new TaskResult(sceneId, reportId, customerId));
                            eventCenterTemplate.doEvents(event);
                            redisTemplate.delete(last(taskKey));
                            // 删除 timeWindowMap 的key
                            String tempTimestamp = ScheduleConstants.TEMP_TIMESTAMP_SIGN + engineName;
                            timeWindowMap.remove(tempTimestamp);
                        }
                        // 超时自动检修，强行触发关闭
                        forceClose(taskKey,timeWindow,sceneId,reportId,customerId);

                    }
                } catch (Exception e) {
                    log.error("【collector】Real-time data analysis for anomalies hashkey:{}, error:{}", sceneReportKey,
                        e.getMessage());
                } finally {
                    unlock(sceneReportKey, "collectorSchedulerPool");
                }
            }
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 超时自动检修，强行触发关闭
     * @param taskKey
     * @param timeWindow
     */
    private void forceClose(String taskKey,Long timeWindow,Long sceneId,Long reportId,Long customerId) {
        Long forceTime = (Long)Optional.ofNullable(redisTemplate.opsForValue().get(forceCloseTime(taskKey))).orElse(0L);
        if(forceTime > 0 && timeWindow >= forceTime) {
            log.info("本次压测{}-{}-{}:触发超时自动检修，强行触发关闭，超时延迟时间-{}，触发时间-{}",
                sceneId,reportId,customerId,forceTime,timeWindow);

            log.info("场景[{}]压测任务已完成,将要开始更新报告{}", sceneId, reportId);
            // 更新压测场景状态  压测引擎运行中,压测引擎停止压测 ---->压测引擎停止压测
            sceneManageService.updateSceneLifeCycle(UpdateStatusBean.build(sceneId, reportId, customerId)
                .checkEnum(SceneManageStatusEnum.ENGINE_RUNNING,SceneManageStatusEnum.STOP).updateEnum(SceneManageStatusEnum.STOP)
                .build());
            // 清除 SLA配置 清除PushWindowDataScheduled 删除pod job configMap  生成报告
            Event event = new Event();
            event.setEventName("finished");
            event.setExt(new TaskResult(sceneId, reportId, customerId));
            eventCenterTemplate.doEvents(event);
            redisTemplate.delete(last(taskKey));
            // 删除 timeWindowMap 的key
            String engineName = ScheduleConstants.getEngineName(sceneId, reportId, customerId);
            String tempTimestamp = ScheduleConstants.TEMP_TIMESTAMP_SIGN + engineName;
            timeWindowMap.remove(tempTimestamp);
        }
    }

    private void writeInfluxDB(List<String> transactions, String taskKey, long timeWindow, Long sceneId, Long reportId,
        Long customerId) {
        long start = System.currentTimeMillis();
        for (String transaction : transactions) {
            Integer count = getIntValue(countKey(taskKey, transaction, timeWindow));
            if (null == count || count < 1) {
                log.error(
                    "【collector metric】【null == count || count < 1】 write influxDB time : {},{}-{}-{}, ", timeWindow,
                    sceneId, reportId, customerId);
                continue;
            }
            Integer failCount = getIntValue(failCountKey(taskKey, transaction, timeWindow));
            Integer saCount = getIntValue(saCountKey(taskKey, transaction, timeWindow));
            Double rt = getDoubleValue(rtKey(taskKey, transaction, timeWindow));
            Double maxRt = getDoubleValue(maxRtKey(taskKey, transaction, timeWindow));
            Double minRt = getDoubleValue(minRtKey(taskKey, transaction, timeWindow));
            Integer activeThreads = getIntValue(activeThreadsKey(taskKey, transaction, timeWindow));
            Double avgTps = getAvgTps(count);
            // 根据pod数量计算平均rt
            String podName = ScheduleConstants.getPodName(sceneId, reportId, customerId);
            String podNum = redisClientUtils.getString(podName);
            Double avgRt = getAvgRt(podNum != null ? Integer.parseInt(podNum) : 1, rt);
            Double saRate = getSaRate(count, saCount);
            Double successRate = getSuccessRate(count, failCount);

            Map<String, String> tags = new HashMap<>();
            tags.put("transaction", transaction);
            Map<String, Object> fields = getInfluxdbFieldMap(count, failCount,
                saCount, rt, maxRt, minRt, avgTps, avgRt, saRate, successRate, activeThreads);

            influxWriter.insert(InfluxDBUtil.getMeasurement(sceneId, reportId, customerId), tags,
                fields, timeWindow);
            try {
                SendMetricsEvent metrics = getSendMetricsEvent(sceneId, reportId,customerId, timeWindow,
                    transaction, count, failCount, maxRt, minRt, avgTps, avgRt,
                    saRate, successRate);
                //未finish，发事件
                String existKey = String.format(CollectorConstants.REDIS_PRESSURE_TASK_KEY,
                    getTaskKey(sceneId, reportId, customerId));
                if (redisTemplate.hasKey(existKey)) {
                    sendMetrics(metrics);
                }
            } catch (Exception e) {
                log.error(
                    "【collector metric】【error】 write influxDB time : {} sceneId : {}, reportId : {},customerId : {}, "
                        + "error:{}",
                    timeWindow, sceneId, reportId, customerId, e.getMessage());
            }
            long end = System.currentTimeMillis();
            log.info(
                "【collector metric】【success】 write influxDB time : {},write time：{} sceneId : {}, reportId : {},"
                    + "customerId : {}",
                timeWindow, (end - start), sceneId, reportId, customerId);

        }
    }

    private Map<String, Object> getInfluxdbFieldMap(Integer count, Integer failCount, Integer saCount, Double rt,
        Double maxRt, Double minRt, Double avgTps, Double avgRt, Double saRate, Double successRate,Integer activeThreads) {
        Map<String, Object> fields = new HashMap<>();
        fields.put("count", count);
        fields.put("fail_count", failCount);
        fields.put("sa_count", saCount);
        fields.put("sum_rt", rt);
        fields.put("max_rt", maxRt);
        fields.put("min_rt", minRt);

        fields.put("avg_tps", avgTps);
        fields.put("avg_rt", avgRt);
        fields.put("sa", saRate);
        fields.put("success_rate", successRate);
        fields.put("active_threads",activeThreads);
        return fields;
    }

    private SendMetricsEvent getSendMetricsEvent(Long sceneId, Long reportId, Long customerId,long timeWindow, String transaction,
        Integer count, Integer failCount, Double maxRt, Double minRt, Double avgTps, Double avgRt, Double saRate,
        Double successRate) {
        SendMetricsEvent metrics = new SendMetricsEvent();
        metrics.setTransaction(transaction);
        metrics.setCount(count);
        metrics.setFailCount(failCount);
        metrics.setAvgTps(avgTps);
        metrics.setAvgRt(avgRt);
        metrics.setSa(saRate);
        metrics.setMaxRt(maxRt);
        metrics.setMinRt(minRt);
        metrics.setSuccessRate(successRate);
        metrics.setTimestamp(timeWindow);
        metrics.setReportId(reportId);
        metrics.setSceneId(sceneId);
        metrics.setCustomerId(customerId);
        return metrics;
    }

    private void scan(String pattern, Consumer<byte[]> consumer) {
        this.redisTemplate.execute((RedisConnection connection) -> {
            try (Cursor<byte[]> cursor = connection.scan(ScanOptions.scanOptions().count(Long.MAX_VALUE).match(pattern)
                .build())) {
                cursor.forEachRemaining(consumer);
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * 获取符合条件的key
     *
     * @param pattern 表达式
     * @return
     */
    public Set<String> keys(String pattern) {
        Set<String> keys = new HashSet<>();
        this.scan(pattern, item -> {
            //符合条件的key
            String key = new String(item, StandardCharsets.UTF_8);
            keys.add(key);
        });
        return keys;
    }

    /**
     * 平均TPS计算
     *
     * @return
     */
    private Double getAvgTps(Integer count) {
        BigDecimal countDecimal = BigDecimal.valueOf(count);
        return countDecimal.divide(BigDecimal.valueOf(CollectorConstants.SEND_TIME), 2, BigDecimal.ROUND_HALF_UP)
            .doubleValue();
    }

    /**
     * 平均RT 计算
     *
     * @return
     */
    private Double getAvgRt(Integer count, Double rt) {
        BigDecimal countDecimal = BigDecimal.valueOf(count);
        BigDecimal rtDecimal = BigDecimal.valueOf(rt);
        return rtDecimal.divide(countDecimal, 2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 事物成功率
     *
     * @return
     */
    private Double getSaRate(Integer count, Integer saCount) {
        BigDecimal countDecimal = BigDecimal.valueOf(count);
        BigDecimal saCountDecimal = BigDecimal.valueOf(saCount);
        return saCountDecimal.divide(countDecimal, 2, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100))
            .doubleValue();
    }

    /**
     * 请求成功率
     *
     * @return
     */
    private Double getSuccessRate(Integer count, Integer failCount) {
        BigDecimal countDecimal = BigDecimal.valueOf(count);
        BigDecimal failCountDecimal = BigDecimal.valueOf(failCount);
        return countDecimal.subtract(failCountDecimal).multiply(BigDecimal.valueOf(100)).divide(countDecimal, 2,
            BigDecimal.ROUND_HALF_UP).doubleValue();
    }

}
