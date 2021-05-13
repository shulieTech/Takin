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

package io.shulie.tro.web.data.dao.tagmanage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import io.shulie.tro.web.data.mapper.mysql.TagManageMapper;
import io.shulie.tro.web.data.model.mysql.TagManageEntity;
import io.shulie.tro.web.data.param.tagmanage.TagManageParam;
import io.shulie.tro.web.data.result.tagmanage.TagManageResult;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zhaoyong
 */
@Component
public class TagManageDAOImpl implements TagManageDAO {

    @Autowired
    private TagManageMapper tagManageMapper;

    @Override
    public List<TagManageResult> selectAllScript() {
        LambdaQueryWrapper<TagManageEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(
            TagManageEntity::getId,
            TagManageEntity::getTagName
        );
        wrapper.eq(TagManageEntity::getTagStatus, 0);
        wrapper.eq(TagManageEntity::getTagType, 0);
        List<TagManageEntity> tagManageEntities = tagManageMapper.selectList(wrapper);
        return entityToResult(tagManageEntities);

    }

    private List<TagManageEntity> getByTagNameAndTagType(String tagName,Integer type) {
        LambdaQueryWrapper<TagManageEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(
            TagManageEntity::getId,
            TagManageEntity::getTagName
        );
        wrapper.eq(TagManageEntity::getTagName, tagName);
        if (type!=null){
            wrapper.eq(TagManageEntity::getTagType,type);
        }
        List<TagManageEntity> tagManageEntities = tagManageMapper.selectList(wrapper);
        if (CollectionUtils.isNotEmpty(tagManageEntities)) {
            return tagManageEntities;
        }
        return null;
    }

    @Override
    public List<Long> addScriptTags(List<TagManageParam> tagManageParams,Integer type) {
        List<Long> tagManageIds = new ArrayList<>();
        for (TagManageParam tagManageParam : tagManageParams) {
            List<TagManageEntity> tagManageList = getByTagNameAndTagType(tagManageParam.getTagName(),type);
            if (CollectionUtils.isNotEmpty(tagManageList)) {
                tagManageIds.add(tagManageList.get(0).getId());
                continue;
            }
            TagManageEntity tagManageEntity = new TagManageEntity();
            BeanUtils.copyProperties(tagManageParam, tagManageEntity);
            tagManageMapper.insert(tagManageEntity);
            tagManageIds.add(tagManageEntity.getId());
        }
        return tagManageIds;
    }

    @Override
    public List<TagManageResult> selectDataSourceTags() {
        LambdaQueryWrapper<TagManageEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(
            TagManageEntity::getId,
            TagManageEntity::getTagName
        );
        wrapper.eq(TagManageEntity::getTagStatus, 0);
        wrapper.eq(TagManageEntity::getTagType, 1);
        List<TagManageEntity> tagManageEntities = tagManageMapper.selectList(wrapper);
        if (CollectionUtils.isNotEmpty(tagManageEntities)) {
            return tagManageEntities.stream().map(tagManageEntity -> {
                TagManageResult tagManageResult = new TagManageResult();
                tagManageResult.setId(tagManageEntity.getId());
                tagManageResult.setTagName(tagManageEntity.getTagName());
                return tagManageResult;
            }).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public List<TagManageResult> selectScriptTagsByIds(List<Long> tagIds) {
        LambdaQueryWrapper<TagManageEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(
            TagManageEntity::getId,
            TagManageEntity::getTagName
        );
        wrapper.eq(TagManageEntity::getTagStatus, 0);
        wrapper.eq(TagManageEntity::getTagType, 0);
        wrapper.in(TagManageEntity::getId, tagIds);
        List<TagManageEntity> tagManageEntities = tagManageMapper.selectList(wrapper);
        return entityToResult(tagManageEntities);
    }

    @Override
    public List<TagManageResult> selectTagByType(Integer type) {
        if (type == null) {
            return Lists.newArrayList();
        }
        LambdaQueryWrapper<TagManageEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(
            TagManageEntity::getId,
            TagManageEntity::getTagName
        );
        wrapper.eq(TagManageEntity::getTagStatus, 0);
        wrapper.eq(TagManageEntity::getTagType, type);
        List<TagManageEntity> tagManageEntities = tagManageMapper.selectList(wrapper);
        return entityToResult(tagManageEntities);
    }

    List<TagManageResult> entityToResult(List<TagManageEntity> tagManageEntities) {
        if (CollectionUtils.isEmpty(tagManageEntities)) {
            return Lists.newArrayList();
        }
        return tagManageEntities.stream().map(tagManageEntity -> {
            TagManageResult tagManageResult = new TagManageResult();
            tagManageResult.setId(tagManageEntity.getId());
            tagManageResult.setTagName(tagManageEntity.getTagName());
            return tagManageResult;
        }).collect(Collectors.toList());
    }

    @Override
    public List<TagManageResult> selectDataSourceTagsByIds(List<Long> tagIds) {
        LambdaQueryWrapper<TagManageEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(TagManageEntity::getId, tagIds);
        queryWrapper.eq(TagManageEntity::getTagStatus, 0);
        List<TagManageEntity> tagManageEntityList = tagManageMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(tagManageEntityList)) {
            return Collections.emptyList();
        }
        return tagManageEntityList.stream().map(tagManageEntity -> {
            TagManageResult tagManageResult = new TagManageResult();
            tagManageResult.setId(tagManageEntity.getId());
            tagManageResult.setTagName(tagManageEntity.getTagName());
            return tagManageResult;
        }).collect(Collectors.toList());
    }

    @Override
    public List<Long> addDatasourceTags(List<TagManageParam> tagManageParams) {
        List<Long> tagManageIds = new ArrayList<>();
        for (TagManageParam tagManageParam : tagManageParams) {
            List<TagManageEntity> tagManageList = getTagManageEntityByTagName(tagManageParam.getTagName());
            if (CollectionUtils.isNotEmpty(tagManageList)) {
                tagManageIds.add(tagManageList.get(0).getId());
                continue;
            }
            TagManageEntity tagManageEntity = new TagManageEntity();
            BeanUtils.copyProperties(tagManageParam, tagManageEntity);
            tagManageMapper.insert(tagManageEntity);
            tagManageIds.add(tagManageEntity.getId());
        }
        return tagManageIds;
    }

    private List<TagManageEntity> getTagManageEntityByTagName(String tagName) {
        LambdaQueryWrapper<TagManageEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(
            TagManageEntity::getId,
            TagManageEntity::getTagName
        );
        wrapper.eq(TagManageEntity::getTagName, tagName);
        List<TagManageEntity> tagManageEntities = tagManageMapper.selectList(wrapper);
        if (CollectionUtils.isNotEmpty(tagManageEntities)) {
            return tagManageEntities;
        }
        return null;
    }
}
