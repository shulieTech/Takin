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

package io.shulie.tro.web.data.dao.scenemanage;

import java.util.List;

import io.shulie.tro.web.data.param.sceneManage.SceneSchedulerTaskInsertParam;
import io.shulie.tro.web.data.param.sceneManage.SceneSchedulerTaskQueryParam;
import io.shulie.tro.web.data.param.sceneManage.SceneSchedulerTaskUpdateParam;
import io.shulie.tro.web.data.result.scenemanage.SceneSchedulerTaskResult;

/**
 * @Author: mubai
 * @Date: 2020-11-30 21:26
 * @Description:
 */

public interface SceneSchedulerTaskDao {

    Long create(SceneSchedulerTaskInsertParam param);

    void delete(Long id);

    SceneSchedulerTaskResult selectBySceneId(Long sceneId);

    void update(SceneSchedulerTaskUpdateParam param);

    void deleteBySceneId(Long sceneId) ;

    List<SceneSchedulerTaskResult> selectBySceneIds(List<Long> sceneIds);

    List<SceneSchedulerTaskResult> selectByExample(SceneSchedulerTaskQueryParam queryParam);
}
