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
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.shulie.tro.web.data.mapper.mysql.ScriptFileRefMapper;
import io.shulie.tro.web.data.model.mysql.ScriptFileRefEntity;
import io.shulie.tro.web.data.result.scriptmanage.ScriptFileRefResult;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zhaoyong
 */
@Component
public class ScriptFileRefDAOImpl implements ScriptFileRefDAO{

    @Autowired
    private ScriptFileRefMapper scriptFileRefMapper;

    @Override
    public List<ScriptFileRefResult> selectFileIdsByScriptDeployId(Long scriptDeployId) {
        LambdaQueryWrapper<ScriptFileRefEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(
                ScriptFileRefEntity::getId,
                ScriptFileRefEntity::getScriptDeployId,
                ScriptFileRefEntity::getFileId);
        wrapper.eq(ScriptFileRefEntity::getScriptDeployId,scriptDeployId);
        List<ScriptFileRefEntity> scriptFileRefEntities = scriptFileRefMapper.selectList(wrapper);
        return getScriptFileRefResults(scriptFileRefEntities);
    }


    @Override
    public void deleteByIds(List<Long> scriptFileRefIds) {
        if (CollectionUtils.isNotEmpty(scriptFileRefIds)){
            scriptFileRefMapper.deleteBatchIds(scriptFileRefIds);
        }
    }

    @Override
    public List<ScriptFileRefResult> selectFileIdsByScriptDeployIds(List<Long> scriptDeployIds) {
        if (CollectionUtils.isEmpty(scriptDeployIds)){
            return null;
        }
        LambdaQueryWrapper<ScriptFileRefEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(
                ScriptFileRefEntity::getId,
                ScriptFileRefEntity::getScriptDeployId,
                ScriptFileRefEntity::getFileId);
        wrapper.in(ScriptFileRefEntity::getScriptDeployId,scriptDeployIds);
        List<ScriptFileRefEntity> scriptFileRefEntities = scriptFileRefMapper.selectList(wrapper);
        return getScriptFileRefResults(scriptFileRefEntities);
    }

    @Override
    public void createScriptFileRefs(List<Long> fileIds, Long scriptDeployId) {
        if (CollectionUtils.isNotEmpty(fileIds) && scriptDeployId != null){
            fileIds.forEach(fileId ->{
                ScriptFileRefEntity scriptFileRefEntity = new ScriptFileRefEntity();
                scriptFileRefEntity.setFileId(fileId);
                scriptFileRefEntity.setScriptDeployId(scriptDeployId);
                scriptFileRefMapper.insert(scriptFileRefEntity);
            });
        }
    }

    private List<ScriptFileRefResult> getScriptFileRefResults(List<ScriptFileRefEntity> scriptFileRefEntities){
        if (CollectionUtils.isNotEmpty(scriptFileRefEntities)){
            return scriptFileRefEntities.stream().map(scriptFileRefEntity -> {
                ScriptFileRefResult scriptFileRefResult = new ScriptFileRefResult();
                scriptFileRefResult.setId(scriptFileRefEntity.getId());
                scriptFileRefResult.setFileId(scriptFileRefEntity.getFileId());
                scriptFileRefResult.setScriptDeployId(scriptFileRefEntity.getScriptDeployId());
                return scriptFileRefResult;
            }).collect(Collectors.toList());
        }
        return null;
    }
}
