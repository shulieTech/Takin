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

package io.shulie.tro.web.data.dao.linkmanage;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.google.common.collect.Lists;
import io.shulie.tro.web.data.mapper.mysql.LinkManageTableMapper;
import io.shulie.tro.web.data.model.mysql.LinkManageTableEntity;
import io.shulie.tro.web.data.param.linkmanage.LinkManageCreateParam;
import io.shulie.tro.web.data.param.linkmanage.LinkManageQueryParam;
import io.shulie.tro.web.data.param.linkmanage.LinkManageUpdateParam;
import io.shulie.tro.web.data.result.linkmange.LinkManageResult;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: fanxx
 * @Date: 2020/10/19 7:49 下午
 * @Description: 系统流程dao
 */
@Component
public class LinkManageDAOImpl implements LinkManageDAO {

    @Autowired
    private LinkManageTableMapper linkManageTableMapper;

    @Override
    public LinkManageResult selecLinkManageById(Long id) {
        LinkManageResult linkManageResult = new LinkManageResult();
        LambdaQueryWrapper<LinkManageTableEntity> linkManageWrapper = new LambdaQueryWrapper<>();
        linkManageWrapper.select(
            LinkManageTableEntity::getLinkId,
            LinkManageTableEntity::getLinkName,
            LinkManageTableEntity::getApplicationName,
            LinkManageTableEntity::getCustomerId,
            LinkManageTableEntity::getUserId
        );
        linkManageWrapper.eq(LinkManageTableEntity::getLinkId, id);
        LinkManageTableEntity linkManageTableEntity = linkManageTableMapper.selectOne(linkManageWrapper);
        if (!Objects.isNull(linkManageTableEntity)) {
            linkManageResult.setLinkId(linkManageTableEntity.getLinkId());
            linkManageResult.setLinkName(linkManageTableEntity.getLinkName());
            linkManageResult.setApplicationName(linkManageTableEntity.getApplicationName());
            linkManageResult.setCustomerId(linkManageTableEntity.getCustomerId());
            linkManageResult.setUserId(linkManageTableEntity.getUserId());
        }
        return linkManageResult;
    }

    @Override
    public List<LinkManageResult> selectList(LinkManageQueryParam queryParam) {
        List<LinkManageResult> linkManageResultList = Lists.newArrayList();
        LambdaQueryWrapper<LinkManageTableEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(
            LinkManageTableEntity::getLinkId,
            LinkManageTableEntity::getLinkName,
            LinkManageTableEntity::getApplicationName,
            LinkManageTableEntity::getCustomerId,
            LinkManageTableEntity::getUserId,
            LinkManageTableEntity::getFeatures
        );
        if (CollectionUtils.isNotEmpty(queryParam.getUserIdList())) {
            queryWrapper.in(LinkManageTableEntity::getUserId, queryParam.getUserIdList());
        }
        if (!StringUtils.isEmpty(queryParam.getSystemProcessName())) {
            queryWrapper.like(LinkManageTableEntity::getLinkName, queryParam.getSystemProcessName());
        }
        if (CollectionUtils.isNotEmpty(queryParam.getLinkIdList())) {
            queryWrapper.in(LinkManageTableEntity::getLinkId, queryParam.getLinkIdList());
        }
        queryWrapper.eq(LinkManageTableEntity::getIsDeleted, 0);
        List<LinkManageTableEntity> entityList = linkManageTableMapper.selectList(queryWrapper);
        if (!CollectionUtils.isEmpty(entityList)) {
            linkManageResultList = entityList.stream().map(linkManageTableEntity -> {
                LinkManageResult linkManageResult = new LinkManageResult();
                linkManageResult.setLinkId(linkManageTableEntity.getLinkId());
                linkManageResult.setLinkName(linkManageTableEntity.getLinkName());
                linkManageResult.setApplicationName(linkManageTableEntity.getApplicationName());
                linkManageResult.setCustomerId(linkManageTableEntity.getCustomerId());
                linkManageResult.setUserId(linkManageTableEntity.getUserId());
                linkManageResult.setFeatures(linkManageTableEntity.getFeatures());
                return linkManageResult;
            }).collect(Collectors.toList());
        }
        return linkManageResultList;
    }

    @Override
    public int insert(LinkManageCreateParam param) {
        LinkManageTableEntity entity = new LinkManageTableEntity();
        BeanUtils.copyProperties(param, entity);
        return linkManageTableMapper.insert(entity);
    }

    /**
     * 指定责任人-系统流程
     *
     * @param param
     * @return
     */
    @Override
    public int allocationUser(LinkManageUpdateParam param) {
        LambdaUpdateWrapper<LinkManageTableEntity> wrapper = new LambdaUpdateWrapper();
        wrapper.set(LinkManageTableEntity::getUserId, param.getUserId())
            .eq(LinkManageTableEntity::getLinkId, param.getLinkId());
        return linkManageTableMapper.update(null, wrapper);
    }
}
