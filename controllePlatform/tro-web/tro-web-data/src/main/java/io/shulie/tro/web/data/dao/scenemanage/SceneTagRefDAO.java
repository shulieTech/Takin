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

import io.shulie.tro.web.data.param.sceneManage.SceneTagRefInsertParam;
import io.shulie.tro.web.data.param.sceneManage.SceneTagRefQueryParam;
import io.shulie.tro.web.data.result.scenemanage.SceneTagRefResult;

/**
 * @Author: mubai
 * @Date: 2020-11-30 11:45
 * @Description:
 */
public interface SceneTagRefDAO {

    /**
     * 创建场景标签
     *
     * @param paramList
     */
    void createSceneTagRefBatch(List<SceneTagRefInsertParam> paramList);

    void createSceneTagRef(SceneTagRefInsertParam refInsertParam);

    /**
     * 根据场景id查询场景标签关联关系
     *
     * @param sceneId
     * @return
     */
    List<SceneTagRefResult> selectBySceneId(Long sceneId);

    /**
     * 根据场景id删除场景标签关联关系
     *
     * @param sceneId
     */
    void deleteBySceneId(Long sceneId);

    void deleteByIds(List<Long> ids);

    void addSceneTagRef(List<Long> tagIds, Long sceneId);

    List<SceneTagRefResult> selectBySceneIds(List<Long> sceneIds);

    List<SceneTagRefResult> selectByExample(SceneTagRefQueryParam queryParam) ;

}
