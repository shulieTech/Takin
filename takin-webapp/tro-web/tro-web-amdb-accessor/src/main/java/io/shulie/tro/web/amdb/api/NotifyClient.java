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

package io.shulie.tro.web.amdb.api;

/**
 * @author shiyajian
 * create: 2020-12-29
 */
public interface NotifyClient {

    /**
     * 通知AMDB开始计算入口
     * @return
     */
    boolean startApplicationEntrancesCalculate(String applicationName, String serviceName,String method,
        String type,String extend);

    /**
     * 通知AMDB结束计算入口
     * @return
     */
    boolean stopApplicationEntrancesCalculate(String applicationName, String serviceName,String method,
        String type,String extend);
}
