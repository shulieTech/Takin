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
package com.pamirs.pradar.pressurement.agent.listener.impl;

import java.util.Iterator;

import com.pamirs.pradar.ErrorTypeEnum;
import com.pamirs.pradar.internal.adapter.JobAdapter;
import com.pamirs.pradar.internal.config.ShadowJob;
import com.pamirs.pradar.pressurement.agent.event.IEvent;
import com.pamirs.pradar.pressurement.agent.event.impl.ShadowJobDisableEvent;
import com.pamirs.pradar.pressurement.agent.event.impl.ShadowJobRegisterEvent;
import com.pamirs.pradar.pressurement.agent.listener.EventResult;
import com.pamirs.pradar.pressurement.agent.listener.PradarEventListener;
import com.pamirs.pradar.pressurement.agent.shared.dynamic.ShaDowJobListener;
import com.pamirs.pradar.pressurement.agent.shared.service.ErrorReporter;
import com.pamirs.pradar.pressurement.agent.shared.service.GlobalConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="tangyuhan@shulie.io">yuhan.tang</a>
 * @since 2020-03-18 14:48
 */
public class ShadowImplListener implements ShaDowJobListener, PradarEventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShadowImplListener.class);

    @Override
    public String disableAll() {
        Iterator<ShadowJob> iterator = GlobalConfig.getInstance().getRegisteredJobs().values().iterator();
        StringBuilder errorMessage = new StringBuilder();
        while (iterator.hasNext()) {
            ShadowJob registerdJob = iterator.next();
            try {
                JobAdapter jobAdapter = getJobAdapter(registerdJob);
                boolean success = jobAdapter.disableShaDowJob(registerdJob);
                if (success) {
                    iterator.remove();
                } else {
                    errorMessage.append(registerdJob.getErrorMessage()).append(",");
                }
            } catch (Throwable e) {
                registerdJob.setErrorMessage(e.getMessage());
                errorMessage.append(registerdJob.getErrorMessage()).append(",");
            }
        }
        return errorMessage.toString();
    }

    private JobAdapter getJobAdapter(ShadowJob shaDowJob) {
        JobAdapter jobAdapter = GlobalConfig.getInstance().getJobAdaptor(shaDowJob.getJobType());
//        if (null == jobAdapter) {
//            throw new IllegalArgumentException(String.format("参数异常 JobType:%s ，Pradar未能支持相关类型", shaDowJob.getJobType()));
//        }
        return jobAdapter;
    }

    @Override
    public EventResult onEvent(IEvent event) {
        try {
            if (event instanceof ShadowJobRegisterEvent) {
                ShadowJobRegisterEvent shadowJobRegisterEvent = (ShadowJobRegisterEvent) event;
                ShadowJob shadowJob = (ShadowJob) shadowJobRegisterEvent.getTarget();
                GlobalConfig.getInstance().addNeedRegisterJobs(shadowJob);
                JobAdapter jobAdapter = getJobAdapter(shadowJob);
                if (jobAdapter == null) {
                    return EventResult.success(event.getTarget());
                }
                boolean success = jobAdapter.registerShadowJob(shadowJob);
                LOGGER.info("shadow job register is {}, className:{} ", success, jobAdapter.getJobName());
                if (success) {
                    GlobalConfig.getInstance().addRegisteredJob(shadowJob);
                    shadowJob.setActive(0);
                } else {
                    shadowJob.setActive(1);
                    ErrorReporter.buildError()
                            .setErrorType(ErrorTypeEnum.ShadowJob)
                            .setErrorCode("shadow-job-0001")
                            .setMessage("影子job注册失败")
                            .setDetail(shadowJob.getClassName() + "-" + shadowJob.getErrorMessage())
                            .report();
                }
            } else if (event instanceof ShadowJobDisableEvent) {
                ShadowJobDisableEvent shadowJobDisableEvent = (ShadowJobDisableEvent) event;
                ShadowJob shadowJob = (ShadowJob) shadowJobDisableEvent.getTarget();
                JobAdapter jobAdapter = getJobAdapter(shadowJob);
                GlobalConfig.getInstance().addNeedStopJobs(shadowJob);
                if (jobAdapter == null) {
                    return EventResult.success(event.getTarget());
                }

                boolean success = jobAdapter.disableShaDowJob(shadowJob);
                if (success) {
                    GlobalConfig.getInstance().removeRegisteredJob(shadowJob);
                }
            }
        } catch (Throwable e) {
            EventResult.error(event.getTarget(), e.getMessage());
        }

        return EventResult.success(event.getTarget());
    }

    @Override
    public int order() {
        return 0;
    }
}
