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

import com.alibaba.excel.util.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.google.common.collect.Lists;
import io.shulie.tro.web.data.convert.linkmanage.BusinessLinkManageConvert;
import io.shulie.tro.web.data.mapper.mysql.BusinessLinkManageTableMapper;
import io.shulie.tro.web.data.mapper.mysql.LinkManageTableMapper;
import io.shulie.tro.web.data.model.mysql.BusinessLinkManageTableEntity;
import io.shulie.tro.web.data.model.mysql.LinkManageTableEntity;
import io.shulie.tro.web.data.param.linkmanage.BusinessLinkManageCreateParam;
import io.shulie.tro.web.data.param.linkmanage.BusinessLinkManageQueryParam;
import io.shulie.tro.web.data.param.linkmanage.BusinessLinkManageUpdateParam;
import io.shulie.tro.web.data.result.linkmange.BusinessLinkResult;
import io.shulie.tro.web.data.result.linkmange.TechLinkResult;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @Author: fanxx
 * @Date: 2020/10/16 5:21 下午
 * @Description:
 */
@Component
public class BusinessLinkManageDAOImpl implements BusinessLinkManageDAO {

    @Autowired
    private BusinessLinkManageTableMapper businessLinkManageTableMapper;

    @Autowired
    private LinkManageTableMapper linkManageTableMapper;

    @Override
    public BusinessLinkResult selectBussinessLinkById(Long id) {
        BusinessLinkResult businessLinkResult = new BusinessLinkResult();
        LambdaQueryWrapper<BusinessLinkManageTableEntity> businessLinkManageWrapper = new LambdaQueryWrapper<>();
        businessLinkManageWrapper.select(
            BusinessLinkManageTableEntity::getLinkId,
            BusinessLinkManageTableEntity::getLinkName,
            BusinessLinkManageTableEntity::getEntrace,
            BusinessLinkManageTableEntity::getCreateTime,
            BusinessLinkManageTableEntity::getUpdateTime,
            BusinessLinkManageTableEntity::getIsChange,
            BusinessLinkManageTableEntity::getCanDelete,
            BusinessLinkManageTableEntity::getIsCore,
            BusinessLinkManageTableEntity::getBusinessDomain,
            BusinessLinkManageTableEntity::getLinkLevel,
            BusinessLinkManageTableEntity::getRelatedTechLink,
            BusinessLinkManageTableEntity::getCustomerId,
            BusinessLinkManageTableEntity::getUserId
        );
        businessLinkManageWrapper.eq(BusinessLinkManageTableEntity::getLinkId, id);
        businessLinkManageWrapper.eq(BusinessLinkManageTableEntity::getIsDeleted, 0);
        BusinessLinkManageTableEntity businessLinkManageTableEntity = businessLinkManageTableMapper.selectOne(businessLinkManageWrapper);
        if (Objects.isNull(businessLinkManageTableEntity)) {
            return businessLinkResult;
        } else {
            businessLinkResult.setId(String.valueOf(businessLinkManageTableEntity.getLinkId()));
            businessLinkResult.setLinkName(businessLinkManageTableEntity.getLinkName());
            businessLinkResult.setEntrance(businessLinkManageTableEntity.getEntrace());
            businessLinkResult.setIschange(String.valueOf(businessLinkManageTableEntity.getIsChange()));
            businessLinkResult.setCreateTime(businessLinkManageTableEntity.getCreateTime());
            businessLinkResult.setUpdateTime(businessLinkManageTableEntity.getUpdateTime());
            businessLinkResult.setCandelete(String.valueOf(businessLinkManageTableEntity.getCanDelete()));
            businessLinkResult.setIsCore(String.valueOf(businessLinkManageTableEntity.getIsCore()));
            businessLinkResult.setLinkLevel(businessLinkManageTableEntity.getLinkLevel());
            businessLinkResult.setBusinessDomain(businessLinkManageTableEntity.getBusinessDomain());
            businessLinkResult.setCustomerId(businessLinkManageTableEntity.getCustomerId());
            businessLinkResult.setUserId(businessLinkManageTableEntity.getUserId());
        }

        LambdaQueryWrapper<LinkManageTableEntity> linkManageWrapper = new LambdaQueryWrapper<>();
        linkManageWrapper.select(
            LinkManageTableEntity::getLinkId,
            LinkManageTableEntity::getLinkName,
            LinkManageTableEntity::getChangeBefore,
            LinkManageTableEntity::getChangeAfter,
            LinkManageTableEntity::getChangeRemark,
            LinkManageTableEntity::getChangeType
        );
        linkManageWrapper.eq(LinkManageTableEntity::getLinkId, businessLinkManageTableEntity.getRelatedTechLink());
        LinkManageTableEntity linkManageTableEntity = linkManageTableMapper.selectOne(linkManageWrapper);
        TechLinkResult techLinkResult = new TechLinkResult();
        businessLinkResult.setTechLinkResult(techLinkResult);
        if (Objects.isNull(linkManageTableEntity)) {
            return businessLinkResult;
        } else {
            techLinkResult.setLinkId(linkManageTableEntity.getLinkId());
            techLinkResult.setTechLinkName(linkManageTableEntity.getLinkName());
            techLinkResult.setBodyBefore(linkManageTableEntity.getChangeBefore());
            techLinkResult.setBodyAfter(linkManageTableEntity.getChangeAfter());
            techLinkResult.setChangeRemark(linkManageTableEntity.getChangeRemark());
            techLinkResult.setChangeType(String.valueOf(linkManageTableEntity.getChangeType()));
        }
        return businessLinkResult;
    }

    @Override
    public List<BusinessLinkResult> selectBussinessLinkByIdList(List<Long> ids) {
        List<BusinessLinkResult> resultList = Lists.newArrayList();
        LambdaQueryWrapper<BusinessLinkManageTableEntity> businessLinkManageWrapper = new LambdaQueryWrapper<>();
        businessLinkManageWrapper.select(
                BusinessLinkManageTableEntity::getLinkId,
                BusinessLinkManageTableEntity::getLinkName
        );
        businessLinkManageWrapper.in(BusinessLinkManageTableEntity::getLinkId, ids);
        businessLinkManageWrapper.eq(BusinessLinkManageTableEntity::getIsDeleted, 0);
        List<BusinessLinkManageTableEntity> entityList = businessLinkManageTableMapper.selectList(businessLinkManageWrapper);
        if (CollectionUtils.isNotEmpty(entityList)) {
            resultList = entityList.stream().map(businessLinkManageTableEntity -> {
                BusinessLinkResult businessLinkResult = new BusinessLinkResult();
                businessLinkResult.setId(String.valueOf(businessLinkManageTableEntity.getLinkId()));
                businessLinkResult.setLinkName(businessLinkManageTableEntity.getLinkName());
                return businessLinkResult;
            }).collect(Collectors.toList());
        }
        return resultList;
    }

    @Override
    public List<BusinessLinkResult> selectList(BusinessLinkManageQueryParam queryParam) {
        List<BusinessLinkResult> resultList = Lists.newArrayList();
        LambdaQueryWrapper<BusinessLinkManageTableEntity> businessLinkManageWrapper = new LambdaQueryWrapper<>();
        businessLinkManageWrapper.select(
                BusinessLinkManageTableEntity::getLinkId,
                BusinessLinkManageTableEntity::getLinkName
        );
        if (CollectionUtils.isNotEmpty(queryParam.getUserIdList())) {
            businessLinkManageWrapper.in(BusinessLinkManageTableEntity::getUserId, queryParam.getUserIdList());
        }
        if (!StringUtils.isEmpty(queryParam.getBussinessActiveName())) {
            businessLinkManageWrapper.like(BusinessLinkManageTableEntity::getLinkName, queryParam.getBussinessActiveName());
        }
        businessLinkManageWrapper.eq(BusinessLinkManageTableEntity::getIsDeleted, 0);
        List<BusinessLinkManageTableEntity> entityList = businessLinkManageTableMapper.selectList(businessLinkManageWrapper);
        if (CollectionUtils.isNotEmpty(entityList)) {
            resultList = entityList.stream().map(businessLinkManageTableEntity -> {
                BusinessLinkResult businessLinkResult = new BusinessLinkResult();
                businessLinkResult.setId(String.valueOf(businessLinkManageTableEntity.getLinkId()));
                businessLinkResult.setLinkName(businessLinkManageTableEntity.getLinkName());
                return businessLinkResult;
            }).collect(Collectors.toList());
        }
        return resultList;
    }

    @Override
    public int insert(BusinessLinkManageCreateParam param) {
        BusinessLinkManageTableEntity entity = new BusinessLinkManageTableEntity();
        BeanUtils.copyProperties(param, entity);
        return businessLinkManageTableMapper.insert(entity);
    }

    /**
     * 指定责任人-业务活动
     *
     * @param param
     * @return
     */
    @Override
    public int allocationUser(BusinessLinkManageUpdateParam param) {
        LambdaUpdateWrapper<BusinessLinkManageTableEntity> wrapper = new LambdaUpdateWrapper();
        wrapper.set(BusinessLinkManageTableEntity::getUserId, param.getUserId())
            .eq(BusinessLinkManageTableEntity::getLinkId, param.getLinkId());
        return businessLinkManageTableMapper.update(null, wrapper);
    }

    @Override
    public List<BusinessLinkResult> getList() {
        LambdaQueryWrapper<BusinessLinkManageTableEntity> wrapper = new LambdaQueryWrapper();
        wrapper.orderByDesc(BusinessLinkManageTableEntity::getUpdateTime);
        List<BusinessLinkManageTableEntity> entities = businessLinkManageTableMapper.selectList(wrapper);
        return BusinessLinkManageConvert.INSTANCE.ofList(entities);
    }

    @Override
    public List<BusinessLinkResult> getListByIds(List<Long> ids) {
        LambdaQueryWrapper<BusinessLinkManageTableEntity> wrapper = new LambdaQueryWrapper();
        wrapper.in(BusinessLinkManageTableEntity::getLinkId, ids);
        wrapper.orderByDesc(BusinessLinkManageTableEntity::getUpdateTime);
        List<BusinessLinkManageTableEntity> entities = businessLinkManageTableMapper.selectList(wrapper);
        return BusinessLinkManageConvert.INSTANCE.ofList(entities);
    }
}
