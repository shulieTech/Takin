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

package io.shulie.tro.web.app.service.application;

import java.util.List;

import io.shulie.tro.web.app.output.application.ApplicationExceptionOutput;
import io.shulie.tro.web.app.request.application.ApplicationErrorQueryRequest;
import io.shulie.tro.web.app.response.application.ApplicationErrorResponse;

/**
 * @author shiyajian
 * create: 2020-10-15
 */
public interface ApplicationErrorService {

    /**
     * 应用异常列表
     */
    List<ApplicationErrorResponse> list(ApplicationErrorQueryRequest queryRequest);

    /**
     * 根据ids
     *
     * @param appNames
     * @return
     */
    List<ApplicationExceptionOutput> getAppException(List<String> appNames);

}
