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

package io.shulie.tro.web.app.service.log;

import io.shulie.tro.common.beans.page.PagingList;
import io.shulie.tro.web.app.request.log.OperationLogCreateRequest;
import io.shulie.tro.web.app.request.log.OperationLogQueryRequest;
import io.shulie.tro.web.app.response.log.OperationLogResponse;

/**
 * @Author: fanxx
 * @Date: 2020/9/23 8:34 下午
 * @Description:
 */
public interface OperationLogService {

    /**
     * 操作日志列表
     */
    PagingList<OperationLogResponse> list(OperationLogQueryRequest queryRequest);

    /**
     * 记录日志
     */
    void record(OperationLogCreateRequest createRequest);
}
