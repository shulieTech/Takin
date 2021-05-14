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

package io.shulie.tro.web.data.dao.application;

import java.util.List;
import java.util.Objects;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.shulie.tro.web.data.mapper.mysql.ShadowJobConfigMapper;
import io.shulie.tro.web.data.model.mysql.ShadowJobConfigEntity;
import io.shulie.tro.web.data.param.application.ShadowJobCreateParam;
import io.shulie.tro.web.data.param.application.ShadowJobUpdateUserParam;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: fanxx
 * @Date: 2020/11/9 9:02 下午
 * @Description:
 */
@Component
public class ApplicationShadowJobDAOImpl implements ApplicationShadowJobDAO {

    @Autowired
    private ShadowJobConfigMapper shadowJobConfigMapper;

    @Override
    public int insert(ShadowJobCreateParam param) {
        ShadowJobConfigEntity entity = new ShadowJobConfigEntity();
        BeanUtils.copyProperties(param, entity);
        return shadowJobConfigMapper.insert(entity);
    }

    @Override
    public int allocationUser(ShadowJobUpdateUserParam param) {
        if (Objects.isNull(param.getApplicationId())) {
            return 0;
        }
        LambdaQueryWrapper<ShadowJobConfigEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShadowJobConfigEntity::getApplicationId, param.getApplicationId());
        List<ShadowJobConfigEntity> shadowJobConfigEntityList = shadowJobConfigMapper.selectList(queryWrapper);
        if (CollectionUtils.isNotEmpty(shadowJobConfigEntityList)) {
            for (ShadowJobConfigEntity entity : shadowJobConfigEntityList) {
                entity.setUserId(param.getUserId());
                shadowJobConfigMapper.updateById(entity);
            }
        }
        return 0;
    }
}
