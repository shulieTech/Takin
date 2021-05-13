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

package io.shulie.tro.web.app.service.user;

import io.shulie.tro.web.app.request.user.DeptCreateRequest;
import io.shulie.tro.web.app.request.user.DeptDeleteRequest;
import io.shulie.tro.web.app.request.user.DeptQueryRequest;
import io.shulie.tro.web.app.request.user.DeptUpdateRequest;
import io.shulie.tro.web.app.response.user.DeptQueryResponse;

/**
 * @Author: fanxx
 * @Date: 2021/3/8 5:40 下午
 * @Description:
 */
public interface TroDeptService {
    void createDept(DeptCreateRequest createRequest);

    void updateDept(DeptUpdateRequest updateRequest);

    void deleteDept(DeptDeleteRequest deleteRequest);

    DeptQueryResponse queryDept(DeptQueryRequest queryRequest);
}
