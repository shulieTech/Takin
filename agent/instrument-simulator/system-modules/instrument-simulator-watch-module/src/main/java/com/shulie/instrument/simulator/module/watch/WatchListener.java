/**
 * Copyright 2021 Shulie Technology, Co.Ltd
 * Email: shulie@shulie.io
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.shulie.instrument.simulator.module.watch;

import com.shulie.instrument.simulator.api.listener.ext.Advice;
import com.shulie.instrument.simulator.api.listener.ext.AdviceListener;
import com.shulie.instrument.simulator.module.express.ExpressException;
import com.shulie.instrument.simulator.module.express.ExpressFactory;
import com.shulie.instrument.simulator.module.model.watch.WatchView;
import com.shulie.instrument.simulator.module.util.ThreadUtil;
import com.shulie.instrument.simulator.module.util.TraceInfo;
import org.apache.commons.lang.StringUtils;

import java.util.Queue;
import java.util.concurrent.CountDownLatch;

/**
 * @author xiaobin.zfb
 * @since 2020/9/18 3:26 下午
 */
public class WatchListener extends AdviceListener {
    /**
     * 方法执行耗时
     */
    public static final String COST_VARIABLE = "cost";
    public static final String TARGET = "target";
    public static final String TARGET_CLASS = "targetClass";
    public static final String PARAMS = "params";
    public static final String RETURN_OBJ = "returnObj";
    public static final String THROWABLE = "throwExp";
    private final int limits;
    private final String condition;
    private final String express;
    private Queue<Object> watchViews;
    private CountDownLatch latch;

    public WatchListener(CountDownLatch latch, Queue<Object> watchViews, String condition, String express, final int limits) {
        this.latch = latch;
        this.watchViews = watchViews;
        this.condition = condition;
        this.express = express;
        this.limits = limits;
    }


    /**
     * 判断条件是否满足，满足的情况下需要输出结果
     *
     * @param conditionExpress 条件表达式
     * @param advice           当前的advice对象
     * @param cost             本次执行的耗时
     * @return true 如果条件表达式满足
     */
    protected boolean isConditionMet(String conditionExpress, Advice advice, long cost) throws ExpressException {
        return StringUtils.isBlank(conditionExpress)
                || ExpressFactory.threadLocalExpress(advice)
                .bind(COST_VARIABLE, cost)
                .bind(TARGET, advice.getTarget())
                .bind(TARGET_CLASS, advice.getTargetClass())
                .bind(PARAMS, advice.getParameterArray())
                .bind(RETURN_OBJ, advice.getReturnObj())
                .bind(THROWABLE, advice.getThrowable())
                .is(conditionExpress);
    }

    protected Object getExpressionResult(String express, Advice advice, long cost) throws ExpressException {
        if (StringUtils.isBlank(express)) {
            return null;
        }
        return ExpressFactory.threadLocalExpress(advice)
                .bind(COST_VARIABLE, cost)
                .bind(TARGET, advice.getTarget())
                .bind(TARGET_CLASS, advice.getTargetClass())
                .bind(PARAMS, advice.getParameterArray())
                .bind(RETURN_OBJ, advice.getReturnObj())
                .bind(THROWABLE, advice.getThrowable())
                .get(express);
    }

    @Override
    public void before(Advice advice) throws Throwable {
        if (advice.isProcessTop()) {
            WatchView watchView = new WatchView();
            watchView.begin(advice.getTargetClass().getName(), advice.getBehavior().getName());
            TraceInfo traceInfo = ThreadUtil.getTraceInfo(Thread.currentThread());
            if (traceInfo != null) {
                watchView.setTraceId(traceInfo.getTraceId());
                watchView.setRpcId(traceInfo.getRpcId());
                watchView.setClusterTestBefore(traceInfo.isClusterTest());
            }
            advice.attach(watchView);
        }
    }

    @Override
    public void afterReturning(Advice advice) throws Throwable {
        finish(advice);
    }

    private void finish(Advice advice) throws ExpressException {
        if (advice.isProcessTop()) {
            WatchView watchView = advice.getProcessTop().attachment();
            if (watchView == null) {
                return;
            }
            watchView.end();
            TraceInfo traceInfo = ThreadUtil.getTraceInfo(Thread.currentThread());
            if (traceInfo != null) {
                watchView.setClusterTestAfter(traceInfo.isClusterTest());
            }
            Object value = getExpressionResult(express, advice, watchView.getCost());
            watchView.setWatches(value);
            if (isConditionMet(condition, advice, watchView.getCost())) {
                this.watchViews.add(watchView);
                if (this.watchViews.size() >= limits) {
                    if (latch != null) {
                        latch.countDown();
                    }
                }
            }
            advice.attach(null);
        }
    }

    @Override
    public void afterThrowing(Advice advice) throws Throwable {
        finish(advice);
    }

}
