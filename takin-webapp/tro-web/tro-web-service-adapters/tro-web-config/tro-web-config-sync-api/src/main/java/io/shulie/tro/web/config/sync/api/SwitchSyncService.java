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

package io.shulie.tro.web.config.sync.api;

/**
 * 全局开关类
 *
 * @author shiyajian
 * create: 2020-09-01
 */
public interface SwitchSyncService {

    /**
     * 调整全局压测开关
     */
    void turnClusterTestSwitch(String namespace, boolean on);

    /**
     * 调整白名单开关
     */
    void turnAllowListSwitch(String namespace, boolean on);

}
