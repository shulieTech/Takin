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

package io.shulie.tro.web.app.init.sync;

/**
 * @author shiyajian
 * create: 2020-09-17
 */
public interface ConfigSyncService {

    void syncGuard(String userAppKey, long applicationId, String applicationName);

    void syncClusterTestSwitch(String userAppKey);

    void syncAllowListSwitch(String userAppKey);

    void syncAllowList(String userAppKey, long applicationId, String applicationName);

    void syncShadowJob(String userAppKey, long applicationId, String applicationName);

    void syncShadowDB(String userAppKey, long applicationId, String applicationName);

    void syncShadowConsumer(String userAppKey, long applicationId, String applicationName);

    void syncBlockList(String userAppKey);

}
