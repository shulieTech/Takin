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

import io.shulie.tro.web.app.request.scenemanage.SceneTagCreateRequest;
import io.shulie.tro.web.app.request.scenemanage.SceneTagRefCreateRequest;
import io.shulie.tro.web.app.response.scenemanage.SceneTagRefResponse;
import io.shulie.tro.web.app.response.tagmanage.TagManageResponse;
import io.shulie.tro.web.data.result.scenemanage.SceneTagRefResult;

/**
 * @Author: mubai
 * @Date: 2020-11-30 14:26
 * @Description:
 */
public interface SceneTagService {

    void createSceneTag(SceneTagCreateRequest request);

    List<TagManageResponse> getAllSceneTags();

    void createSceneTagRef(SceneTagRefCreateRequest refCreateRequests);

    List<SceneTagRefResponse> getSceneTagRefBySceneIds(List<Long> sceneIds);

    List<SceneTagRefResponse> getTagRefByTagIds(List<Long> tagIds) ;

}
