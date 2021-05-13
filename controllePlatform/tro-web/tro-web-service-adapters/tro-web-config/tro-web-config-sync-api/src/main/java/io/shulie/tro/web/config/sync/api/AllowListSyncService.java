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

import java.util.List;

import io.shulie.tro.web.config.entity.AllowList;

/**
 * 白名单 ( dubbo 、http )
 * 当应用发起调用的时候，只有白名单里面的接口，才能调用成功，否则熔断
 * 为了避免实际压测中，因为梳理遗漏，导致流量发送到一个未知接口可能带来的各种问题
 *
 * @author shiyajian
 * create: 2020-09-01
 */
public interface AllowListSyncService {

    void syncAllowList(String namespace, String applicationName, List<AllowList> allows);

}
