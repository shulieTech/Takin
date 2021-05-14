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

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.shulie.tro.web.data.mapper.mysql.WhiteListMapper;
import io.shulie.tro.web.data.model.mysql.WhiteListEntity;
import io.shulie.tro.web.data.param.application.ApplicationWhiteListCreateParam;
import io.shulie.tro.web.data.param.application.ApplicationWhiteListUpdateParam;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: fanxx
 * @Date: 2020/11/9 9:19 下午
 * @Description:
 */
@Component
public class ApplicationWhiteListDAOImpl implements ApplicationWhiteListDAO {

    @Autowired
    private WhiteListMapper whiteListMapper;

    @Override
    public int insert(ApplicationWhiteListCreateParam param) {
        WhiteListEntity entity = new WhiteListEntity();
        BeanUtils.copyProperties(param, entity);
        return whiteListMapper.insert(entity);
    }

    @Override
    public int insertBatch(List<ApplicationWhiteListCreateParam> paramList) {
        int count = 0;
        if (CollectionUtils.isNotEmpty(paramList)) {
            for (ApplicationWhiteListCreateParam param : paramList) {
                WhiteListEntity entity = new WhiteListEntity();
                entity.setInterfaceName(param.getInterfaceName());
                entity.setType(param.getType());
                entity.setDictType(param.getDictType());
                entity.setUseYn(Integer.parseInt(param.getUseYn()));
                entity.setApplicationId(Long.parseLong(param.getApplicationId()));
                entity.setCustomerId(param.getCustomerId());
                entity.setUserId(param.getUserId());
                entity.setIsHandwork(param.getIsHandwork());
                entity.setIsGlobal(param.getIsGlobal());
                count = count + whiteListMapper.insert(entity);
            }
        }
        return count;
    }

    @Override
    public int allocationUser(ApplicationWhiteListUpdateParam param) {
        if (StringUtils.isBlank(param.getApplicationId())) {
            return 0;
        }
        LambdaQueryWrapper<WhiteListEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WhiteListEntity::getApplicationId, param.getApplicationId());
        List<WhiteListEntity> whiteListEntityList = whiteListMapper.selectList(queryWrapper);
        if (CollectionUtils.isNotEmpty(whiteListEntityList)) {
            for (WhiteListEntity entity : whiteListEntityList) {
                entity.setUserId(param.getUserId());
                whiteListMapper.updateById(entity);
            }
        }
        return 0;
    }
}
