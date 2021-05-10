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
package com.pamirs.pradar.internal.adapter;


import com.pamirs.pradar.internal.config.ShadowJob;

/**
 * @author <a href="tangyuhan@shulie.io">yuhan.tang</a>
 * @since 2020-03-18 15:29
 */
public interface JobAdapter {

    String getJobName();

    /**
     * @param shaDowJob
     */
    boolean registerShadowJob(ShadowJob shaDowJob) throws Throwable;

    boolean disableShaDowJob(ShadowJob shaDowJob) throws Throwable;


    String SHADOW_QUARTZ = "quartz";
    String SHADOW_XXL_JOB = "xxl-job";
    String SHADOW_ELASTIC_JOB = "elastic-job";
    String SHADOW_LTS = "lts";
    String SCHEDULE_JOB = "schedule-job";
}
