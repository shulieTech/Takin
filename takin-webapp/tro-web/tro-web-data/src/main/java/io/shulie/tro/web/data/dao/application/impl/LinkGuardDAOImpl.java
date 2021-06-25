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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.shulie.tro.web.common.constant.GuardEnableConstants;
import io.shulie.tro.web.data.dao.application.LinkGuardDAO;
import io.shulie.tro.web.data.mapper.mysql.LinkGuardMapper;
import io.shulie.tro.web.data.model.mysql.LinkGuardEntity;
import io.shulie.tro.web.data.param.application.LinkGuardCreateParam;
import io.shulie.tro.web.data.param.application.LinkGuardUpdateUserParam;
import io.shulie.tro.web.data.result.linkguard.LinkGuardResult;
import io.shulie.tro.web.data.util.MPUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * @Author: fanxx
 * @Date: 2020/11/4 8:45 下午
 * @Description: 挡板dao
 */
@Service
public class LinkGuardDAOImpl extends ServiceImpl<LinkGuardMapper, LinkGuardEntity> implements LinkGuardDAO, MPUtil<LinkGuardEntity>  {

    @Override
    public int insert2(LinkGuardCreateParam param) {
        LinkGuardEntity entity = new LinkGuardEntity();
        BeanUtils.copyProperties(param, entity);
        if (param.getIsEnable() != null) {
            entity.setIsEnable(param.getIsEnable() ? GuardEnableConstants.GUARD_ENABLE : GuardEnableConstants.GUARD_UNABLE);
        }
        return this.getBaseMapper().insert(entity);
    }

    @Override
    public List<LinkGuardResult> selectByAppNameUnderCurrentUser(String appName, Long customerId) {
        List<LinkGuardResult> results = new ArrayList<>();
        LambdaQueryWrapper<LinkGuardEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LinkGuardEntity::getCustomerId, customerId)
            .eq(LinkGuardEntity::getApplicationName, appName);
        List<LinkGuardEntity> linkGuardEntities = this.getBaseMapper().selectList(wrapper);
        if (CollectionUtils.isEmpty(linkGuardEntities)) {
            return results;
        }
        return linkGuardEntities.stream().map(item -> {
            LinkGuardResult target = new LinkGuardResult();
            BeanUtils.copyProperties(item, target);
            // 启动
            target.setIsEnable(item.getIsEnable() == GuardEnableConstants.GUARD_ENABLE);
            return target;
        }).collect(Collectors.toList());
    }

    @Override
    public int allocationUser(LinkGuardUpdateUserParam param) {
        if (Objects.isNull(param.getApplicationId())) {
            return 0;
        }
        LambdaQueryWrapper<LinkGuardEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LinkGuardEntity::getApplicationId, param.getApplicationId());
        List<LinkGuardEntity> linkGuardEntityList = this.getBaseMapper().selectList(queryWrapper);
        if (CollectionUtils.isNotEmpty(linkGuardEntityList)) {
            for (LinkGuardEntity entity : linkGuardEntityList) {
                entity.setUserId(param.getUserId());
                this.getBaseMapper().updateById(entity);
            }
        }
        return 0;
    }

    @Override
    public List<LinkGuardEntity> listFromExportByApplicationId(Long applicationId) {
        return this.list(this.getLQW().select(LinkGuardEntity::getId, LinkGuardEntity::getMethodInfo,
                LinkGuardEntity::getGroovy, LinkGuardEntity::getIsEnable,LinkGuardEntity::getRemark)
                .eq(LinkGuardEntity::getApplicationId, applicationId));
    }

    @Override
    public void updateAppName(Long applicationId,String appName) {
        LambdaUpdateWrapper<LinkGuardEntity> wrapper = this.getLUW();
        wrapper.eq(LinkGuardEntity::getApplicationId,applicationId);
        wrapper.set(LinkGuardEntity::getApplicationName,appName);
        this.update(wrapper);
    }

}
