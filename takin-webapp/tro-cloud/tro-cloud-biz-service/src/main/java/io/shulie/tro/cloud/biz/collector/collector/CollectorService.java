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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import io.shulie.tro.cloud.biz.output.scenemanage.SceneManageWrapperOutput;
import io.shulie.tro.cloud.common.bean.collector.EventMetrics;
import io.shulie.tro.cloud.common.bean.collector.ResponseMetrics;
import io.shulie.tro.cloud.common.bean.scenemanage.SceneManageQueryOpitons;
import io.shulie.tro.cloud.common.constants.CollectorConstants;
import io.shulie.tro.cloud.common.utils.CollectorUtil;
import io.shulie.tro.cloud.common.bean.scenemanage.UpdateStatusBean;
import io.shulie.tro.cloud.common.constants.ScheduleConstants;
import io.shulie.tro.cloud.common.enums.scenemanage.SceneManageStatusEnum;
import io.shulie.tro.cloud.common.utils.GsonUtil;
import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Author <a href="tangyuhan@shulie.io">yuhan.tang</a>
 * @package: io.shulie.tro.cloud.poll.poll
 * @Date 2020-04-20 14:38
 */
@Slf4j
@Service
public class CollectorService extends AbstractIndicators {

    public static final String METRICS_EVENTS_STARTED = "started";
    public static final String METRICS_EVENTS_ENDED = "ended";

    private static final Map<String, List<String>> cacheTasks = new ConcurrentHashMap<>();

    /**
     * 记录时间
     */
    // todo 本地缓存，之后都需要改成redis 解决redis锁问题，以及redis 缓存延迟问题
    public static Map<String,Integer> events = Maps.newConcurrentMap();

    public void collector(Long sceneId, Long reportId, Long customerId, List<ResponseMetrics> metrics) {
        String taskKey = getPressureTaskKey(sceneId, reportId, customerId);
        for (ResponseMetrics metric : metrics) {
            try {
                long timeWindow = CollectorUtil.getTimeWindow(metric.getTimestamp()).getTimeInMillis();
                if (validate(timeWindow)) {

                    String transaction = metric.getTransaction();

                    intCumulative(countKey(taskKey, transaction, timeWindow), metric.getCount());
                    intCumulative(failCountKey(taskKey, transaction, timeWindow), metric.getFailCount());
                    intCumulative(saCountKey(taskKey, transaction, timeWindow), metric.getSaCount());
                    intCumulative(activeThreadsKey(taskKey, transaction, timeWindow), metric.getActiveThreads());

                    // 错误信息
                    setError(errorKey(taskKey, transaction, timeWindow), GsonUtil.gsonToString(metric.getErrorInfos()));

                    /**
                     * all指标额外计算，累加所有业务活动的saCount all 为空
                     */
                    intCumulative(saCountKey(taskKey, "all", timeWindow), metric.getSaCount());

                    doubleCumulative(rtKey(taskKey, transaction, timeWindow), metric.getRt());
                    mostValue(maxRtKey(taskKey, transaction, timeWindow), metric.getMaxRt(), 0);
                    mostValue(minRtKey(taskKey, transaction, timeWindow), metric.getMinRt(), 1);
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }




    public synchronized void verifyEvent(Long sceneId, Long reportId, Long customerId, List<EventMetrics> metrics) {
        String engineName = ScheduleConstants.getEngineName(sceneId, reportId, customerId);
        String taskKey = getPressureTaskKey(sceneId, reportId, customerId);

        for (EventMetrics metric : metrics) {
            try {
                // 解决多pod
                Boolean isFirst = METRICS_EVENTS_STARTED.equals(metric.getEventName()) ? true : false;
                Boolean isLast = METRICS_EVENTS_ENDED.equals(metric.getEventName()) ? true : false;
                if (isFirst) {
                    // 超时自动检修，强行触发关闭
                    if(!redisTemplate.hasKey(forceCloseTime(taskKey))) {
                        // 获取压测时长
                        log.info("本次压测{}-{}-{}:记录超时自动检修时间-{}",sceneId, reportId, customerId,metric.getTimestamp());
                        SceneManageWrapperOutput wrapperDTO = sceneManageService.getSceneManage(sceneId, new SceneManageQueryOpitons());
                        setForceCloseTime(forceCloseTime(taskKey),metric.getTimestamp(),wrapperDTO.getPressureTestSecond());
                    }
                    // 取min
                    setMin(engineName + ScheduleConstants.FIRST_SIGN, metric.getTimestamp());
                    //多个pod 解决方案 只要一个pod 过来，状态就是压测引擎已启动，但是会通过redis计数 数据将归属于报告
                    // POD running -- > 压测引擎已启动
                    // 计数 压测引擎实际运行个数
                    events.compute(engineName,(k,v) -> v == null ? 1 : v +1);
                    if(events.get(engineName) == 1) {
                        sceneManageService.updateSceneLifeCycle(UpdateStatusBean.build(sceneId, reportId, customerId)
                            .checkEnum(SceneManageStatusEnum.POD_RUNNING).updateEnum(SceneManageStatusEnum.ENGINE_RUNNING)
                            .build());
                    }
                }
                if (isLast) {
                    // 取max flag 是否更新过
                    setMax(engineName + ScheduleConstants.LAST_SIGN, metric.getTimestamp());

                    if (events.get(engineName) == 1) {
                        // 压测引擎只有一个运行 压测停止
                        log.info("本次压测{}-{}-{}:打入结束标识",sceneId, reportId, customerId);
                        setLast(last(taskKey), ScheduleConstants.LAST_SIGN);
                        notifyEnd(sceneId, reportId, customerId);
                        return;
                    }
                    // 计数 回传标识数量
                    events.compute(ScheduleConstants.TEMP_LAST_SIGN + engineName,(k,v) -> v == null ? 1 : v +1);
                    // 是否是最后一个结束标识 回传个数 == 压测实际运行个数
                    if (isLastSign(ScheduleConstants.TEMP_LAST_SIGN + engineName, engineName)) {
                        // 标识结束标识
                        log.info("本次压测{}-{}-{}:打入结束标识",sceneId, reportId, customerId);
                        setLast(last(taskKey), ScheduleConstants.LAST_SIGN);
                        // 删除临时标识
                        events.remove(ScheduleConstants.TEMP_LAST_SIGN + engineName);
                        // 压测停止
                        notifyEnd(sceneId, reportId, customerId);
                    }
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    private void notifyEnd(Long sceneId, Long reportId, Long customerId) {
        log.info("场景[{}]压测任务已完成,将要开始更新报告{}", sceneId, reportId);
        // 更新压测场景状态  压测引擎运行中,压测引擎停止压测 ---->压测引擎停止压测
        sceneManageService.updateSceneLifeCycle(UpdateStatusBean.build(sceneId, reportId, customerId)
            .checkEnum(SceneManageStatusEnum.ENGINE_RUNNING, SceneManageStatusEnum.STOP)
            .updateEnum(SceneManageStatusEnum.STOP)
            .build());
    }

    private boolean isLastSign(String lastSign, String engineName) {
        if (events.get(lastSign).equals(events.get(engineName))) {
            return true;
        }
        return false;
    }

    /**
     * 统计每个时间窗口pod调用数量
     */
    public void statisticalIp(Long sceneId, Long reportId, Long customerId, long time, String ip) {
        //  todo
        String windosTimeKey = String.format("%s:%s", getPressureTaskKey(sceneId, reportId, customerId), "windosTime");
        String timeInMillis = String.valueOf(CollectorUtil.getTimeWindow(time).getTimeInMillis());
        List<String> ips = null;
        if (redisTemplate.getExpire(windosTimeKey) == -2) {
            ips = new ArrayList<>();
            ips.add(ip);
            redisTemplate.opsForHash().put(windosTimeKey, timeInMillis, ips);
            redisTemplate.expire(windosTimeKey, 60 * 60 * 2, TimeUnit.SECONDS);
        } else {
            ips = (List<String>)redisTemplate.opsForHash().get(windosTimeKey, timeInMillis);
            if (null == ips) {
                ips = new ArrayList<>();
            }
            ips.add(ip);
            redisTemplate.opsForHash().put(windosTimeKey, timeInMillis, ips);
        }

    }

    /**
     * 校验数据是否丢弃
     *
     * @return
     */
    private boolean validate(long time) {
        if ((System.currentTimeMillis() - time) > CollectorConstants.overdueTime) {
            return false;
        }
        return true;
    }

    public boolean cacheCheck(Long scenId, Long reportId, Long customerId, List<String> transactions) {
        String hashKey = getTaskKey(scenId, reportId, customerId);
        List<String> list = cacheTasks.get(hashKey);
        boolean flag = true;
        if (null != list) {
            for (String transaction : transactions) {
                if (!list.contains(transaction)) {
                    list.add(transaction);
                    flag = false;
                }
            }
        } else {
            cacheTasks.put(hashKey, transactions);
            flag = false;
        }
        return flag;
    }
}
