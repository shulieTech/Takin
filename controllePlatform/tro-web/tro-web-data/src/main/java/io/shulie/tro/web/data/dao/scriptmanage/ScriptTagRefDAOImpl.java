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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import io.shulie.tro.web.data.mapper.mysql.ScriptTagRefMapper;
import io.shulie.tro.web.data.model.mysql.ScriptTagRefEntity;
import io.shulie.tro.web.data.result.scriptmanage.ScriptTagRefResult;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zhaoyong
 */
@Component
public class ScriptTagRefDAOImpl implements ScriptTagRefDAO{

    @Autowired
    private ScriptTagRefMapper scriptTagRefMapper;

    @Override
    public void addScriptTagRef(List<Long> tagIds, Long scriptId) {
        if (CollectionUtils.isNotEmpty(tagIds) && scriptId != null){
            List<ScriptTagRefResult> scriptTagRefResults = selectScriptTagRefByScriptId(scriptId);
            List<Long> existTagIds = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(scriptTagRefResults)){
                existTagIds = scriptTagRefResults.stream().map(ScriptTagRefResult::getTagId).collect(Collectors.toList());
            }
            for (Long tagId : tagIds){
                if (CollectionUtils.isNotEmpty(existTagIds) && existTagIds.contains(tagId)){
                    return;
                }
                ScriptTagRefEntity scriptTagRefEntity = new ScriptTagRefEntity();
                scriptTagRefEntity.setTagId(tagId);
                scriptTagRefEntity.setScriptId(scriptId);
                scriptTagRefMapper.insert(scriptTagRefEntity);
            }
        }
    }

    @Override
    public List<ScriptTagRefResult> selectScriptTagRefByScriptId(Long scriptId) {
        LambdaQueryWrapper<ScriptTagRefEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(
                ScriptTagRefEntity::getId,
                ScriptTagRefEntity::getScriptId,
                ScriptTagRefEntity::getTagId);
        wrapper.eq(ScriptTagRefEntity::getScriptId,scriptId);
        List<ScriptTagRefEntity> scriptTagRefEntities = scriptTagRefMapper.selectList(wrapper);
        return getScriptTagRefResultList(scriptTagRefEntities);
    }

    @Override
    public void deleteByIds(List<Long> scriptTagRefIds) {
        if (CollectionUtils.isNotEmpty(scriptTagRefIds)){
            scriptTagRefMapper.deleteBatchIds(scriptTagRefIds);
        }
    }

    @Override
    public List<ScriptTagRefResult> selectScriptTagRefByTagIds(List<Long> tagIds) {
        LambdaQueryWrapper<ScriptTagRefEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(
                ScriptTagRefEntity::getId,
                ScriptTagRefEntity::getScriptId,
                ScriptTagRefEntity::getTagId);
        wrapper.in(ScriptTagRefEntity::getTagId,tagIds);
        List<ScriptTagRefEntity> scriptTagRefEntities = scriptTagRefMapper.selectList(wrapper);
        return getScriptTagRefResultList(scriptTagRefEntities);
    }

    @Override
    public List<ScriptTagRefResult> selectScriptTagRefByScriptIds(List<Long> scriptIds) {
        LambdaQueryWrapper<ScriptTagRefEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(
                ScriptTagRefEntity::getId,
                ScriptTagRefEntity::getScriptId,
                ScriptTagRefEntity::getTagId);
        wrapper.in(ScriptTagRefEntity::getScriptId,scriptIds);
        List<ScriptTagRefEntity> scriptTagRefEntities = scriptTagRefMapper.selectList(wrapper);
        return getScriptTagRefResultList(scriptTagRefEntities);
    }

    @Override
    public void deleteByScriptId(Long scriptId) {
        LambdaUpdateWrapper<ScriptTagRefEntity> scriptTagRefEntityWrapper = new LambdaUpdateWrapper<>();
        scriptTagRefEntityWrapper.eq(ScriptTagRefEntity::getScriptId,scriptId);
        scriptTagRefMapper.delete(scriptTagRefEntityWrapper);
    }


    private List<ScriptTagRefResult> getScriptTagRefResultList(List<ScriptTagRefEntity> scriptTagRefEntities){
        if (CollectionUtils.isNotEmpty(scriptTagRefEntities)){
            return scriptTagRefEntities.stream().map(scriptTagRefEntity -> {
                ScriptTagRefResult scriptTagRefResult = new ScriptTagRefResult();
                scriptTagRefResult.setId(scriptTagRefEntity.getId());
                scriptTagRefResult.setScriptId(scriptTagRefEntity.getScriptId());
                scriptTagRefResult.setTagId(scriptTagRefEntity.getTagId());
                return scriptTagRefResult;
            }).collect(Collectors.toList());
        }
        return null;
    }
}
