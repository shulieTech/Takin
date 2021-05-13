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
import io.shulie.tro.web.data.mapper.mysql.ApplicationApiManageMapper;
import io.shulie.tro.web.data.model.mysql.ApplicationApiManageEntity;
import io.shulie.tro.web.data.param.application.ApplicationApiCreateParam;
import io.shulie.tro.web.data.param.application.ApplicationApiUpdateUserParam;
import io.shulie.tro.web.data.result.application.ApplicationDetailResult;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: fanxx
 * @Date: 2020/11/4 5:55 下午
 * @Description:
 */
@Component
public class ApplicationApiDAOImpl implements ApplicationApiDAO {

    @Autowired
    private ApplicationApiManageMapper apiManageMapper;

    @Autowired
    private ApplicationDAO applicationDAO;

    @Override
    public int insert(ApplicationApiCreateParam param) {
        ApplicationApiManageEntity entity = new ApplicationApiManageEntity();
        entity.setApi(param.getApi());
        entity.setApplicationName(param.getApplicationName());
        entity.setCreateTime(param.getCreateTime());
        entity.setUpdateTime(param.getUpdateTime());
        entity.setMethod(param.getRequestMethod());
        entity.setCustomerId(param.getCustomerId());
        entity.setUserId(param.getUserId());
        return apiManageMapper.insert(entity);
    }

    @Override
    public int allocationUser(ApplicationApiUpdateUserParam param) {
        if (Objects.isNull(param.getApplicationId())) {
            return 0;
        }
        ApplicationDetailResult applicationDetailResult = applicationDAO.getApplicationById(param.getApplicationId());
        if (Objects.isNull(applicationDetailResult)) {
            return 0;
        }
        LambdaQueryWrapper<ApplicationApiManageEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ApplicationApiManageEntity::getApplicationName, applicationDetailResult.getApplicationName());
        queryWrapper.eq(ApplicationApiManageEntity::getCustomerId, applicationDetailResult.getCustomerId());
        List<ApplicationApiManageEntity> apiManageEntityList = apiManageMapper.selectList(
            queryWrapper);
        if (CollectionUtils.isNotEmpty(apiManageEntityList)) {
            for (ApplicationApiManageEntity entity : apiManageEntityList) {
                entity.setUserId(param.getUserId());
                entity.setApplicationId(param.getApplicationId());
                apiManageMapper.updateById(entity);
            }
        }
        return 0;
    }
}
