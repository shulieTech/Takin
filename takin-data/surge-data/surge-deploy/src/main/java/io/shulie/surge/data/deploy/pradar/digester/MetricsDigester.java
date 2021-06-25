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

package io.shulie.surge.data.deploy.pradar.digester;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.pamirs.pradar.log.parser.metrics.MetricsBased;
import io.shulie.surge.data.common.aggregation.AggregateSlot;
import io.shulie.surge.data.common.aggregation.DefaultAggregator;
import io.shulie.surge.data.common.aggregation.Scheduler;
import io.shulie.surge.data.common.aggregation.metrics.CallStat;
import io.shulie.surge.data.common.aggregation.metrics.Metric;
import io.shulie.surge.data.deploy.pradar.listener.MetricsResultListener;
import io.shulie.surge.data.runtime.common.remote.DefaultValue;
import io.shulie.surge.data.runtime.common.remote.Remote;
import io.shulie.surge.data.runtime.digest.DataDigester;
import io.shulie.surge.data.runtime.digest.DigestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author: xingchen
 * @ClassName: MetricsDigester
 * @Package: io.shulie.surge.data.deploy.pradar.digester
 * @Date: 2020/11/1614:40
 * @Description:
 */
@Singleton
public class MetricsDigester implements DataDigester<MetricsBased> {
    private static Logger logger = LoggerFactory.getLogger(MetricsDigester.class);

    private static final String METRICS_ID = "tro_pradar";

    @Inject
    @DefaultValue("true")
    @Named("/pradar/config/rt/metricsDisable")
    private Remote<Boolean> monitroDisable;

    @Inject
    private MetricsResultListener metricsResultListener;

    private transient AtomicBoolean isRunning = new AtomicBoolean(false);

    private DefaultAggregator defaultAggregator;

    private Scheduler scheduler = new Scheduler(1);

    @Override
    public void digest(DigestContext<MetricsBased> context) {
        if (monitroDisable.get()) {
            return;
        }
        if (isRunning.compareAndSet(false, true)) {
            try {
                defaultAggregator = new DefaultAggregator(5, 60, scheduler);
                defaultAggregator.start();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        MetricsBased metricsBased = context.getContent();
        if (metricsBased == null) {
            logger.warn("parse MetricsBased is null " + context.getContent());
            return;
        }
        // 拼接唯一值
        String[] tags = new String[]{metricsBased.getAppName(), metricsBased.getEvent(), metricsBased.getType(), String.valueOf(metricsBased.isClusterTest()), metricsBased.getCallEvent(), metricsBased.getCallType()
                , String.valueOf(metricsBased.isEntry()), metricsBased.getAgentId()};
        long timeStamp = metricsBased.getTimestamp();
        AggregateSlot<Metric, CallStat> slot = defaultAggregator.getSlotByTimestamp(timeStamp);
        // 冗余字段信息
        String traceId = metricsBased.getTraceId();
        // 总次数/成功次数/totalRt/错误次数/hitCount/totalQps/totalTps/total
        CallStat callStat = new CallStat(traceId,
                metricsBased.getTotalCount(), metricsBased.getSuccessCount(), metricsBased.getTotalRt(),
                metricsBased.getFailureCount(), metricsBased.getHitCount(), metricsBased.getQps().longValue(),
                metricsBased.getQps().longValue(), 1);

        slot.addToSlot(Metric.of(METRICS_ID, tags, "", new String[]{}), callStat);
        defaultAggregator.addListener(METRICS_ID, metricsResultListener);
    }

    @Override
    public int threadCount() {
        return 1;
    }

    @Override
    public void stop() throws Exception {
        defaultAggregator.stop();
    }
}
