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

package io.shulie.tro.web.data.dao.blacklist;

import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.pamirs.tro.common.util.DateUtils;
import io.shulie.tro.common.beans.page.PagingList;
import io.shulie.tro.web.common.enums.blacklist.BlacklistEnableEnum;
import io.shulie.tro.web.common.vo.blacklist.BlacklistVO;
import io.shulie.tro.web.data.mapper.mysql.BlackListMapper;
import io.shulie.tro.web.data.model.mysql.BlackListEntity;
import io.shulie.tro.web.data.param.blacklist.BlackListCreateParam;
import io.shulie.tro.web.data.param.blacklist.BlacklistCreateNewParam;
import io.shulie.tro.web.data.param.blacklist.BlacklistSearchParam;
import io.shulie.tro.web.data.param.blacklist.BlacklistUpdateParam;
import io.shulie.tro.web.data.result.blacklist.BlacklistResult;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: fanxx
 * @Date: 2020/11/4 8:18 下午
 * @Description: 黑名单dao
 */
@Component
public class BlackListDAOImpl implements BlackListDAO {

    @Autowired
    private BlackListMapper blackListMapper;

    @Override
    public int insert(BlackListCreateParam param) {
        BlackListEntity entity = new BlackListEntity();
        entity.setRedisKey(param.getRedisKey());
        entity.setUseYn(param.getUseYn());
        entity.setGmtCreate(param.getCreateTime());
        entity.setGmtModified(param.getUpdateTime());
        return blackListMapper.insert(entity);
    }

    @Override
    public void newInsert(BlacklistCreateNewParam param) {
        BlackListEntity entity = new BlackListEntity();
        BeanUtils.copyProperties(param,entity);
        blackListMapper.insert(entity);
    }

    @Override
    public void batchInsert(List<BlacklistCreateNewParam> params) {
        params.forEach(param -> {
            BlackListEntity entity = new BlackListEntity();
            BeanUtils.copyProperties(param,entity);
            blackListMapper.insert(entity);
        });

    }

    @Override
    public void update(BlacklistUpdateParam param) {
        BlackListEntity entity = new BlackListEntity();
        BeanUtils.copyProperties(param,entity);
        blackListMapper.updateById(entity);
    }


    @Override
    public void delete(Long id) {
        blackListMapper.deleteById(id);
    }

    @Override
    public void batchDelete(List<Long> ids) {
        blackListMapper.deleteBatchIds(ids);
    }

    @Override
    public void logicalDelete(List<Long> ids) {
        LambdaUpdateWrapper<BlackListEntity> wrapper = new LambdaUpdateWrapper<>();
        wrapper.in(BlackListEntity::getBlistId,ids);
        wrapper.set(BlackListEntity::getIsDeleted,true);
        blackListMapper.update(null,wrapper);
    }

    @Override
    public PagingList<BlacklistVO> pageList(BlacklistSearchParam param) {
        LambdaQueryWrapper<BlackListEntity> wrapper = getBlackListEntityLambdaQueryWrapper(param);
        Page<BlackListEntity> page = new Page<>(param.getCurrent() + 1, param.getPageSize());
        wrapper.orderByDesc(BlackListEntity::getGmtModified, BlackListEntity::getBlistId);
        if(StringUtils.isNotBlank(param.getRedisKey())) {
            wrapper.like(BlackListEntity::getRedisKey,param.getRedisKey());
        }
        IPage<BlackListEntity> infoEntityIPage = blackListMapper.selectPage(page,wrapper);
        if(CollectionUtils.isEmpty(infoEntityIPage.getRecords())) {
            return PagingList.empty();
        }
        List<BlacklistVO> results = infoEntityIPage.getRecords().stream().map(entity -> {
            BlacklistVO result = new BlacklistVO();
            BeanUtils.copyProperties(entity, result);
            result.setGmtCreate(DateUtils.dateToString(entity.getGmtCreate(),DateUtils.FORMATE_YMDHMS));
            result.setGmtModified(DateUtils.dateToString(entity.getGmtModified(),DateUtils.FORMATE_YMDHMS));
            return result;
        }).collect(Collectors.toList());
        return PagingList.of(results, page.getTotal());
    }

    @Override
    public List<BlacklistResult> selectList(BlacklistSearchParam param) {
        LambdaQueryWrapper<BlackListEntity> wrapper = getBlackListEntityLambdaQueryWrapper(param);
        if(StringUtils.isNotBlank(param.getRedisKey())) {
            wrapper.eq(BlackListEntity::getRedisKey,param.getRedisKey());
        }
        List<BlackListEntity> entities = blackListMapper.selectList(wrapper);
        if(CollectionUtils.isEmpty(entities)) {
            return Lists.newArrayList();
        }
        List<BlacklistResult> results = entities.stream().map(entity -> {
            BlacklistResult result = new BlacklistResult();
            BeanUtils.copyProperties(entity, result);
            return result;
        }).collect(Collectors.toList());
        return results;
    }

    private LambdaQueryWrapper<BlackListEntity> getBlackListEntityLambdaQueryWrapper(BlacklistSearchParam param) {
        LambdaQueryWrapper<BlackListEntity> wrapper = new LambdaQueryWrapper<>();
        if(param.getApplicationId() != null) {
            wrapper.eq(BlackListEntity::getApplicationId, param.getApplicationId());
        }
        // 数据权限放开
        //if (param.getCustomerId() != null) {
        //    wrapper.eq(BlackListEntity::getCustomerId, param.getCustomerId());
        //}
        //if (param.getUserId() != null) {
        //    wrapper.eq(BlackListEntity::getUserId, param.getUserId());
        //}
        wrapper.eq(BlackListEntity::getIsDeleted,false);
        return wrapper;
    }

    @Override
    public BlacklistResult selectById(Long id) {
        BlackListEntity entity = blackListMapper.selectById(id);
        if(entity == null) {
            return null;
        }
        BlacklistResult result = new BlacklistResult();
        BeanUtils.copyProperties(entity, result);
        return result;
    }

    @Override
    public List<BlacklistResult> selectByIds(List<Long> ids) {
        LambdaQueryWrapper<BlackListEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(BlackListEntity::getBlistId, ids);
        List<BlackListEntity> entities = blackListMapper.selectList(wrapper);
        if(CollectionUtils.isEmpty(entities)) {
            return Lists.newArrayList();
        }
        List<BlacklistResult> results = entities.stream().map(entity -> {
            BlacklistResult result = new BlacklistResult();
            BeanUtils.copyProperties(entity, result);
            return result;
        }).collect(Collectors.toList());
        return results;
    }

    @Override
    public List<BlacklistResult> getAllEnabledBlockList(List<Long> appIds) {
        LambdaQueryWrapper<BlackListEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BlackListEntity::getIsDeleted, false);
        wrapper.eq(BlackListEntity::getUseYn, BlacklistEnableEnum.ENABLE.getStatus());
        wrapper.in(BlackListEntity::getApplicationId,appIds);
        List<BlackListEntity> entities = blackListMapper.selectList(wrapper);
        if(CollectionUtils.isEmpty(entities)) {
            return Lists.newArrayList();
        }
        List<BlacklistResult> results = entities.stream().map(entity -> {
            BlacklistResult result = new BlacklistResult();
            BeanUtils.copyProperties(entity, result);
            return result;
        }).collect(Collectors.toList());
        return results;
    }
}
