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

package io.shulie.tro.cloud.data.dao.scenemanage.impl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.shulie.tro.cloud.data.converter.senemange.SceneManageEntityConverter;
import io.shulie.tro.cloud.data.dao.scenemanage.SceneManageDAO;
import io.shulie.tro.cloud.data.mapper.mysql.SceneManageMapper;
import io.shulie.tro.cloud.data.model.mysql.SceneManageEntity;
import io.shulie.tro.cloud.data.result.scenemanage.SceneManageListFromUpdateScriptResult;
import io.shulie.tro.cloud.data.result.scenemanage.SceneManageListResult;
import io.shulie.tro.cloud.data.util.MPUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

/**
 * @author 无涯
 * @Package io.shulie.tro.cloud.data.dao.scenemanage
 * @date 2020/10/26 4:40 下午
 */
@Component
public class SceneManageDAOImpl
    extends ServiceImpl<SceneManageMapper, SceneManageEntity>
    implements SceneManageDAO, MPUtil<SceneManageEntity> {

    @Override
    public SceneManageListResult queryBySceneName(String pressureTestSceneName) {
        LambdaQueryWrapper<SceneManageEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SceneManageEntity::getSceneName,pressureTestSceneName);
        SceneManageEntity sceneManageEntity = this.getBaseMapper().selectOne(wrapper);
        return SceneManageEntityConverter.INSTANCE.ofSceneManageEntity(sceneManageEntity);
    }

    @Override
    public List<SceneManageListFromUpdateScriptResult> listFromUpdateScript() {
        List<SceneManageEntity> entities = this.list(this.getTenantLQW()
            .select(SceneManageEntity::getId,SceneManageEntity::getCustomId, SceneManageEntity::getFeatures));
        // 转换出参
        return entities.isEmpty() ? Collections.emptyList()
            : entities.stream().map(entity -> {
                SceneManageListFromUpdateScriptResult result = new SceneManageListFromUpdateScriptResult();
                BeanUtils.copyProperties(entity, result);
                return result;
            })
                .collect(Collectors.toList());
    }

}
