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

package io.shulie.tro.web.app.service.scenemanage;

import java.util.List;

import io.shulie.tro.web.app.request.scenemanage.SceneSchedulerTaskCreateRequest;
import io.shulie.tro.web.app.request.scenemanage.SceneSchedulerTaskQueryRequest;
import io.shulie.tro.web.app.request.scenemanage.SceneSchedulerTaskUpdateRequest;
import io.shulie.tro.web.app.response.scenemanage.SceneSchedulerTaskResponse;

/**
 * @Author: mubai
 * @Date: 2020-12-01 10:30
 * @Description:
 */
public interface SceneSchedulerTaskService {

    Long insert(SceneSchedulerTaskCreateRequest request) ;

    void delete(Long id) ;

    void update(SceneSchedulerTaskUpdateRequest updateRequest,Boolean  needVerifyTime);

    SceneSchedulerTaskResponse selectBySceneId(Long sceneId) ;

    void deleteBySceneId(Long sceneId) ;

    List<SceneSchedulerTaskResponse> selectBySceneIds(List<Long> sceneIds);

    List<SceneSchedulerTaskResponse> selectByExample(SceneSchedulerTaskQueryRequest request);

    void executeSchedulerPressureTask();



}
