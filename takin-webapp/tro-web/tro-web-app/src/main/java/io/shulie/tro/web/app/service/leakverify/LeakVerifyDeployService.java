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

package io.shulie.tro.web.app.service.leakverify;

import java.util.List;

import io.shulie.tro.web.app.output.leakverify.LeakVerifyDeployDetailOutput;
import io.shulie.tro.web.app.output.leakverify.LeakVerifyDeployOutput;

/**
 * @author zhaoyong
 */
public interface LeakVerifyDeployService {

    /**
     * 漏数实例列表
     *
     * @param leakVerifyId
     * @return
     */
    List<LeakVerifyDeployOutput> listLeakVerifyDeploy(Long leakVerifyId);

    /**
     * 获取漏数实例详情，如果回传leakVerifyDeployId，查询表数据，如果不回传，查询实时数据
     *
     * @param leakVerifyDeployId
     * @return
     */
    List<LeakVerifyDeployDetailOutput> queryLeakVerifyDeployDetail(Long leakVerifyDeployId);
}
