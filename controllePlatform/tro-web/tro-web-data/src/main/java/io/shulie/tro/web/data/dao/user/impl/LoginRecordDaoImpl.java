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

package io.shulie.tro.web.data.dao.user.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import io.shulie.tro.web.data.dao.user.LoginRecordDao;
import io.shulie.tro.web.data.mapper.mysql.LoginRecordMapper;
import io.shulie.tro.web.data.model.mysql.LoginRecordEntity;
import io.shulie.tro.web.data.param.user.LoginRecordParam;
import io.shulie.tro.web.data.param.user.LoginRecordSearchParam;
import io.shulie.tro.web.data.result.user.LoginRecordResult;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author 无涯
 * @Package io.shulie.tro.web.data.dao.user.impl
 * @date 2021/4/7 5:22 下午
 */
@Component
public class LoginRecordDaoImpl implements LoginRecordDao {
    @Autowired
    private LoginRecordMapper loginRecordMapper;
    @Override
    public void insert(LoginRecordParam param) {
        LoginRecordEntity entity = new LoginRecordEntity();
        BeanUtils.copyProperties(param,entity);
        loginRecordMapper.insert(entity);
    }

    @Override
    public List<LoginRecordResult> getList(LoginRecordSearchParam param) {
        LambdaQueryWrapper<LoginRecordEntity> wrapper = new LambdaQueryWrapper();
        if(param.getStartTime() != null) {
            wrapper.ge(LoginRecordEntity::getGmtCreate,param.getStartTime());
        }
        if(param.getEndTime() != null) {
            wrapper.le(LoginRecordEntity::getGmtCreate, param.getEndTime());
        }
        if(StringUtils.isNotBlank(param.getUserName())) {
            wrapper.eq(LoginRecordEntity::getUserName,param.getUserName());
        }
        if(StringUtils.isNotBlank(param.getIp())) {
            wrapper.eq(LoginRecordEntity::getIp,param.getIp());
        }
        List<LoginRecordEntity> entities = loginRecordMapper.selectList(wrapper);
        if(CollectionUtils.isEmpty(entities)) {
            return Lists.newArrayList();
        }
        return entities.stream().map(entity -> {
            LoginRecordResult result = new LoginRecordResult();
            BeanUtils.copyProperties(entity,result);
            return result;
        }).collect(Collectors.toList());
    }
}
