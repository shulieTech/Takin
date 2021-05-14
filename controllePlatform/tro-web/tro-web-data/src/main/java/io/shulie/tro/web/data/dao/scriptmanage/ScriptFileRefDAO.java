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

import io.shulie.tro.web.data.result.scriptmanage.ScriptFileRefResult;

/**
 * @author zhaoyong
 */
public interface ScriptFileRefDAO {
    /**
     * 根据脚本发布id获取文件id列表
     * @param scriptDeployId
     * @return
     */
    List<ScriptFileRefResult> selectFileIdsByScriptDeployId(Long scriptDeployId);

    /**
     * 批量删除关联关系
     * @param scriptFileRefIds
     */
    void deleteByIds(List<Long> scriptFileRefIds);

    /**
     * 根据脚本发布实例id批量查询关联关系
     * @param scriptDeployIds
     * @return
     */
    List<ScriptFileRefResult> selectFileIdsByScriptDeployIds(List<Long> scriptDeployIds);

    /**
     *
     * @param fileIds
     * @param id
     */
    void createScriptFileRefs(List<Long> fileIds, Long id);

}
