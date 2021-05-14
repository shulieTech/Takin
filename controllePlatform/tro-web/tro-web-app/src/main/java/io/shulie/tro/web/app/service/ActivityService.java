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

import io.shulie.tro.common.beans.page.PagingList;
import io.shulie.tro.web.app.request.activity.ActivityCreateRequest;
import io.shulie.tro.web.app.request.activity.ActivityQueryRequest;
import io.shulie.tro.web.app.request.activity.ActivityUpdateRequest;
import io.shulie.tro.web.app.request.activity.ActivityVerifyRequest;
import io.shulie.tro.web.app.response.activity.ActivityListResponse;
import io.shulie.tro.web.app.response.activity.ActivityResponse;
import io.shulie.tro.web.app.response.activity.ActivityVerifyResponse;

/**
 * 业务活动
 *
 * @author shiyajian
 * create: 2020-12-30
 */
public interface ActivityService {

    void createActivity(ActivityCreateRequest request);

    void updateActivity(ActivityUpdateRequest request);

    void deleteActivity(Long activityId);

    PagingList<ActivityListResponse> pageActivities(ActivityQueryRequest request);

    ActivityResponse getActivityById(Long id);

    ActivityVerifyResponse verifyActivity(ActivityVerifyRequest request);

    ActivityVerifyResponse getVerifyStatus(Long activityId);
}
