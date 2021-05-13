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

package io.shulie.tro.web.data.dao.scriptmanage;

import java.util.List;

import io.shulie.tro.web.data.result.scriptmanage.ScriptTagRefResult;

public interface ScriptTagRefDAO {
    /**
     * 新增脚本和tag关联关系
     * @param tagIds
     * @param scriptId
     */
    void addScriptTagRef(List<Long> tagIds, Long scriptId);

    /**
     * 根据脚本id查询关联关系
     * @param scriptId
     * @return
     */
    List<ScriptTagRefResult> selectScriptTagRefByScriptId(Long scriptId);

    /**
     * 批量删除关联关系
     * @param scriptTagRefIds
     */
    void deleteByIds(List<Long> scriptTagRefIds);

    /**
     * 根据tagId列表查询脚本和tag的关联关系
     * @param tagIds
     * @return
     */
    List<ScriptTagRefResult> selectScriptTagRefByTagIds(List<Long> tagIds);

    /**
     * 根据脚本id批量查询脚本关联关系
     * @param scriptIds
     * @return
     */
    List<ScriptTagRefResult> selectScriptTagRefByScriptIds(List<Long> scriptIds);

    /**
     * 根据脚本id删除关联关系
     * @param scriptDeployId
     */
    void deleteByScriptId(Long scriptDeployId);

}
