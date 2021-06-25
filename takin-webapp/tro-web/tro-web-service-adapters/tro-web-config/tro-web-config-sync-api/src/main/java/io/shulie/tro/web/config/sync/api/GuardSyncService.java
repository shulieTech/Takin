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

import io.shulie.tro.web.config.entity.Guard;

/**
 * 挡板
 * 可以理解为 Mock 方法，当方法执行到这里，会返回配置的值，不会实现里面具体的逻辑；
 *
 * @author shiyajian
 * create: 2020-09-01
 */
public interface GuardSyncService {

    /**
     * 同步挡板信息
     */
    void syncGuard(String namespace, String applicationName, List<Guard> newGuards);

}
