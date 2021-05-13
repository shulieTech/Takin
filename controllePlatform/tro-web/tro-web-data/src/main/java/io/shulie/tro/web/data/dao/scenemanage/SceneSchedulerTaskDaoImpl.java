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

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import io.shulie.tro.web.data.mapper.mysql.SceneSchedulerTaskMapper;
import io.shulie.tro.web.data.model.mysql.SceneSchedulerTaskEntity;
import io.shulie.tro.web.data.param.sceneManage.SceneSchedulerTaskInsertParam;
import io.shulie.tro.web.data.param.sceneManage.SceneSchedulerTaskQueryParam;
import io.shulie.tro.web.data.param.sceneManage.SceneSchedulerTaskUpdateParam;
import io.shulie.tro.web.data.result.scenemanage.SceneSchedulerTaskResult;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;

/**
 * @Author: mubai
 * @Date: 2020-11-30 21:30
 * @Description:
 */

@Repository
public class SceneSchedulerTaskDaoImpl implements SceneSchedulerTaskDao {

    @Resource
    private SceneSchedulerTaskMapper sceneSchedulerTaskMapper;

    @Override
    public Long create(SceneSchedulerTaskInsertParam param) {
        SceneSchedulerTaskEntity entity = new SceneSchedulerTaskEntity();
        BeanUtils.copyProperties(param, entity);
        return Long.valueOf(sceneSchedulerTaskMapper.insert(entity));
    }

    @Override
    public void delete(Long id) {
        sceneSchedulerTaskMapper.deleteById(id);
    }

    @Override
    public SceneSchedulerTaskResult selectBySceneId(Long sceneId) {
        SceneSchedulerTaskEntity entity = sceneSchedulerTaskMapper.selectBySceneId(sceneId);
        if (entity == null) {
            return null;
        }
        SceneSchedulerTaskResult result = new SceneSchedulerTaskResult();
        BeanUtils.copyProperties(entity, result);
        return result;
    }

    @Override
    public void update(SceneSchedulerTaskUpdateParam param) {
        SceneSchedulerTaskEntity entity = new SceneSchedulerTaskEntity();
        BeanUtils.copyProperties(param, entity);
        sceneSchedulerTaskMapper.updateById(entity);
    }

    @Override
    public void deleteBySceneId(Long sceneId) {
        if (null == sceneId) {
            return;
        }
        sceneSchedulerTaskMapper.deleteBySceneId(sceneId);

    }

    @Override
    public List<SceneSchedulerTaskResult> selectBySceneIds(List<Long> sceneIds) {
        LambdaQueryWrapper<SceneSchedulerTaskEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(
            SceneSchedulerTaskEntity::getId,
            SceneSchedulerTaskEntity::getContent,
            SceneSchedulerTaskEntity::getExecuteTime,
            SceneSchedulerTaskEntity::getSceneId
        );
        wrapper.in(SceneSchedulerTaskEntity::getSceneId, sceneIds);
        wrapper.eq(SceneSchedulerTaskEntity::getIsDeleted,false);
        List<SceneSchedulerTaskEntity> sceneSchedulerTaskEntities = sceneSchedulerTaskMapper.selectList(wrapper);
        return entrys2ResultList(sceneSchedulerTaskEntities);
    }

    @Override
    public List<SceneSchedulerTaskResult> selectByExample(SceneSchedulerTaskQueryParam queryParam) {
        LambdaQueryWrapper<SceneSchedulerTaskEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(
            SceneSchedulerTaskEntity::getId,
            SceneSchedulerTaskEntity::getExecuteTime,
            SceneSchedulerTaskEntity::getSceneId,
            SceneSchedulerTaskEntity::getIsExecuted,
            SceneSchedulerTaskEntity::getUserId
        );
        wrapper.lt(SceneSchedulerTaskEntity::getExecuteTime, queryParam.getEndTime());
        wrapper.eq(SceneSchedulerTaskEntity::getIsExecuted, 0);
        wrapper.eq(SceneSchedulerTaskEntity::getIsDeleted,false);
        List<SceneSchedulerTaskEntity> sceneSchedulerTaskEntities = sceneSchedulerTaskMapper.selectList(wrapper);
        return entrys2ResultList(sceneSchedulerTaskEntities);
    }

    SceneSchedulerTaskResult enty2Result(SceneSchedulerTaskEntity entity) {
        SceneSchedulerTaskResult result = new SceneSchedulerTaskResult();
        BeanUtils.copyProperties(entity, result);
        return result;
    }

    List<SceneSchedulerTaskResult> entrys2ResultList(List<SceneSchedulerTaskEntity> sceneSchedulerTaskEntities) {
        if (CollectionUtils.isEmpty(sceneSchedulerTaskEntities)) {
            return Lists.newArrayList();
        }
        List<SceneSchedulerTaskResult> resultList = new ArrayList<>();
        sceneSchedulerTaskEntities.stream().forEach(entity -> {
            resultList.add(enty2Result(entity));
        });
        return resultList;
    }

}
