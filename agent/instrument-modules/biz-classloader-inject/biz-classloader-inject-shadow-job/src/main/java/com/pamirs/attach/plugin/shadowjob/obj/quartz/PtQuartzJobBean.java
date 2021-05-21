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
package com.pamirs.attach.plugin.shadowjob.obj.quartz;

import com.pamirs.pradar.internal.PradarInternalService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * @author angju
 * @date 2021/3/27 21:42
 */
public class PtQuartzJobBean extends QuartzJobBean {

    public Job busJob;
//    private Method method;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        try {
            if (busJob != null){
                PradarInternalService.startTrace(null, busJob.getClass().getName(), "execute");
                PradarInternalService.setClusterTest(true);
                busJob.execute(context);
                PradarInternalService.setClusterTest(false);
                PradarInternalService.endTrace();
            }
        } catch (Throwable e) {
            //
        }
    }


    public Job getBusJob() {
        return busJob;
    }


    public void setBusJob(Job busJob) {
        this.busJob = busJob;
    }
}
