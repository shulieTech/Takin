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

package io.shulie.tro.web.data.dao.application.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.shulie.tro.web.data.dao.application.ShadowMqConsumerDAO;
import io.shulie.tro.web.data.mapper.mysql.ShadowMqConsumerMapper;
import io.shulie.tro.web.data.model.mysql.ShadowMqConsumerEntity;
import io.shulie.tro.web.data.util.MPUtil;
import org.springframework.stereotype.Service;

/**
 * @author liuchuan
 * @date 2021/4/8 2:21 下午
 */
@Service
public class ShadowMqConsumerDAOImpl
        extends ServiceImpl<ShadowMqConsumerMapper, ShadowMqConsumerEntity>
        implements ShadowMqConsumerDAO, MPUtil<ShadowMqConsumerEntity> {

    @Override
    public List<ShadowMqConsumerEntity> listByApplicationId(Long applicationId) {
        return this.list(this.getLQW().eq(ShadowMqConsumerEntity::getApplicationId, applicationId));
    }

    @Override
    public void updateAppName(Long applicationId,String appName) {
        LambdaUpdateWrapper<ShadowMqConsumerEntity> wrapper = this.getLUW();
        wrapper.eq(ShadowMqConsumerEntity::getApplicationId,applicationId);
        wrapper.set(ShadowMqConsumerEntity::getApplicationName,appName);
        this.update(wrapper);
    }

}
