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

package io.shulie.tro.web.diff.api;

/**
 * @author shiyajian
 * create: 2020-10-19
 */
public interface PressScheduleApi {

    /**
     * 开启压测调度任务
     * 私有化版本：本地调用k8s模块的启动；
     * 云版本：通过http调用远端
     */
    void startSchedule();
}
