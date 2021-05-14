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

package io.shulie.tro.web.config.sync.file.impl;

import java.util.List;

import io.shulie.tro.web.config.entity.ShadowJob;
import io.shulie.tro.web.config.sync.api.ShadowJobSyncService;
import org.springframework.stereotype.Component;

/**
 * @author shiyajian
 * create: 2020-09-17
 */
@Component
public class ShadowJobSyncServiceImpl implements ShadowJobSyncService {

    @Override
    public void syncShadowJob(String namespace, String applicationName, List<ShadowJob> shadowJobs) {
        // TODO 写到redis，不写文件
    }
}
