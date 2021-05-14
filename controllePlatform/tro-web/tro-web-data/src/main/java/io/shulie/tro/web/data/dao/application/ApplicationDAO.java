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

package io.shulie.tro.web.data.dao.application;

import java.util.List;

import io.shulie.tro.web.data.param.application.ApplicationCreateParam;
import io.shulie.tro.web.data.param.application.ApplicationQueryParam;
import io.shulie.tro.web.data.param.application.ApplicationUpdateParam;
import io.shulie.tro.web.data.result.application.ApplicationDetailResult;
import io.shulie.tro.web.data.result.application.ApplicationResult;

/**
 * @author shiyajian
 * create: 2020-09-20
 */
public interface ApplicationDAO {

    List<ApplicationDetailResult> getApplications(List<String> appNames);

    List<ApplicationResult> getApplicationByName(List<String> appNames);

    List<ApplicationDetailResult> getApplicationListByUserIds(List<Long> userIdList);

    /**
     * 获取应用
     *
     * @param ids
     * @return
     */
    List<ApplicationResult> getApplicationByIds(List<Long> ids);

    List<ApplicationDetailResult> getApplicationList(ApplicationQueryParam param);

    List<ApplicationDetailResult> getApplicationList(List<String> appNames);

    List<String> getAllApplicationName(ApplicationQueryParam param);


    int insert(ApplicationCreateParam param);

    ApplicationDetailResult getApplicationById(Long appId);

    ApplicationDetailResult getApplicationByCustomerIdAndName(Long customerId, String appName);

    /**
     * 指定责任人-应用管理
     *
     * @param param
     * @return
     */
    int allocationUser(ApplicationUpdateParam param);

}
