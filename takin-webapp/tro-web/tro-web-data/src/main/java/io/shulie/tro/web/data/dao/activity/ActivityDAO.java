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

package io.shulie.tro.web.data.dao.activity;

import java.util.List;

import io.shulie.tro.common.beans.page.PagingList;
import io.shulie.tro.web.data.param.activity.ActivityCreateParam;
import io.shulie.tro.web.data.param.activity.ActivityExistsQueryParam;
import io.shulie.tro.web.data.param.activity.ActivityQueryParam;
import io.shulie.tro.web.data.param.activity.ActivityUpdateParam;
import io.shulie.tro.web.data.result.activity.ActivityListResult;
import io.shulie.tro.web.data.result.activity.ActivityResult;

/**
 * @author shiyajian
 * create: 2020-12-30
 */
public interface ActivityDAO {

    List<Long> exists(ActivityExistsQueryParam param);

    int createActivity(ActivityCreateParam createParam);

    ActivityResult getActivityById(Long activityId);

    int updateActivity(ActivityUpdateParam updateParam);

    void deleteActivity(Long activityId);

    PagingList<ActivityListResult> pageActivities(ActivityQueryParam param);
}
