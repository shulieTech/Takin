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

package io.shulie.tro.web.app.service;

import java.util.List;

import io.shulie.tro.web.app.request.leakcheck.LeakSqlBatchRefsRequest;
import io.shulie.tro.web.app.request.leakcheck.LeakSqlCreateRequest;
import io.shulie.tro.web.app.request.leakcheck.LeakSqlDeleteRequest;
import io.shulie.tro.web.app.request.leakcheck.LeakSqlDetailRequest;
import io.shulie.tro.web.app.request.leakcheck.LeakSqlUpdateRequest;
import io.shulie.tro.web.app.request.leakcheck.SqlTestRequest;
import io.shulie.tro.web.app.request.leakverify.VerifyTaskConfig;
import io.shulie.tro.web.app.response.leakcheck.LeakSqlBatchRefsResponse;
import io.shulie.tro.web.app.response.leakcheck.LeakSqlRefsResponse;

/**
 * @Author: fanxx
 * @Date: 2020/12/31 3:23 下午
 * @Description:
 */
public interface LeakSqlService {
    void createLeakCheckConfig(LeakSqlCreateRequest createRequest);

    void updateLeakCheckConfig(LeakSqlUpdateRequest updateRequest);

    void deleteLeakCheckConfig(LeakSqlDeleteRequest deleteRequest);

    LeakSqlRefsResponse getLeakCheckConfigDetail(LeakSqlDetailRequest detailRequest);

    List<LeakSqlBatchRefsResponse> getBatchLeakCheckConfig(LeakSqlBatchRefsRequest refsRequest);

    List<VerifyTaskConfig> getVerifyTaskConfig(LeakSqlBatchRefsRequest refsRequest);

    Boolean getSceneLeakConfig(Long sceneId);

    String testSqlConnection(SqlTestRequest sqlTestRequest);
}
