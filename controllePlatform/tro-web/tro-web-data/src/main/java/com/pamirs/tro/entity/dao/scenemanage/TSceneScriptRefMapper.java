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

package com.pamirs.tro.entity.dao.scenemanage;

import java.util.List;

import com.pamirs.tro.entity.domain.entity.scenemanage.SceneScriptRef;
import com.pamirs.tro.entity.domain.query.SceneScriptRefQueryParam;
import org.apache.ibatis.annotations.Param;

public interface TSceneScriptRefMapper {

    int deleteByPrimaryKey(Long id);

    int deleteByIds(@Param("ids") List<Long> ids);

    Long insertSelective(SceneScriptRef record);

    void batchInsert(@Param("items") List<SceneScriptRef> records);

    SceneScriptRef selectByPrimaryKey(Long id);

    List<SceneScriptRef> selectBySceneIdAndScriptType(@Param("sceneId") Long sceneId,
        @Param("scriptType") Integer scriptType);

    int updateByPrimaryKeySelective(SceneScriptRef record);

    SceneScriptRef selectByExample(SceneScriptRefQueryParam param);

}
