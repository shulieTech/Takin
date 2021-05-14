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

package io.shulie.tro.web.app.service.scenemanage.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import io.shulie.tro.web.app.exception.ExceptionCode;
import io.shulie.tro.web.app.exception.TroWebException;
import io.shulie.tro.web.app.request.scenemanage.SceneTagCreateRequest;
import io.shulie.tro.web.app.request.scenemanage.SceneTagRefCreateRequest;
import io.shulie.tro.web.app.response.scenemanage.SceneTagRefResponse;
import io.shulie.tro.web.app.response.tagmanage.TagManageResponse;
import io.shulie.tro.web.app.service.scenemanage.SceneTagService;
import io.shulie.tro.web.data.dao.scenemanage.SceneTagRefDAO;
import io.shulie.tro.web.data.dao.tagmanage.TagManageDAO;
import io.shulie.tro.web.data.param.sceneManage.SceneTagRefQueryParam;
import io.shulie.tro.web.data.param.tagmanage.TagManageParam;
import io.shulie.tro.web.data.result.scenemanage.SceneTagRefResult;
import io.shulie.tro.web.data.result.tagmanage.TagManageResult;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: mubai
 * @Date: 2020-11-30 14:27
 * @Description:
 */

@Service
public class SceneTagServiceImpl implements SceneTagService {
    @Autowired
    private TagManageDAO tagManageDAO;

    @Autowired
    private SceneTagRefDAO sceneTagRefDAO;

    @Override
    public void createSceneTag(SceneTagCreateRequest request) {
        List<TagManageParam> paramList = new ArrayList<>();
        TagManageParam param = new TagManageParam();
        param.setTagName(request.getTagName());
        param.setTagType(1);
        paramList.add(param);
        tagManageDAO.addScriptTags(paramList,1);
    }

    @Override
    public List<TagManageResponse> getAllSceneTags() {
        //查询场景标签： type：1
        List<TagManageResult> tagManageResults = tagManageDAO.selectTagByType(1);
        return tagResult2TagResp(tagManageResults);
    }

    @Override
    public void createSceneTagRef(SceneTagRefCreateRequest sceneTagCreateRefRequest) {
        if (sceneTagCreateRefRequest == null || sceneTagCreateRefRequest.getSceneId() == null) {
            return;
        }
        if (sceneTagCreateRefRequest.getTagNames().size() > 10) {
            throw new TroWebException(ExceptionCode.SCRIPT_MANAGE_TAG_ADD_VALID_ERROR, "每个场景关联标签数不能超过10");
        }
        List<String> collect = sceneTagCreateRefRequest.getTagNames().stream().filter(o -> o.length() > 10).collect(
            Collectors.toList());
        if (CollectionUtils.isNotEmpty(collect)) {
            throw new TroWebException(ExceptionCode.SCRIPT_MANAGE_TAG_ADD_VALID_ERROR, "存在脚本名称长度超过10");
        }
        if (sceneTagCreateRefRequest.getSceneId() != null) {
            sceneTagRefDAO.deleteBySceneId(sceneTagCreateRefRequest.getSceneId());
            if (CollectionUtils.isNotEmpty(sceneTagCreateRefRequest.getTagNames())) {
                List<TagManageParam> tagManageParams = sceneTagCreateRefRequest.getTagNames().stream().distinct().map(
                    tagName -> {
                        TagManageParam tagManageParam = new TagManageParam();
                        tagManageParam.setTagName(tagName);
                        //默认为可用状态
                        tagManageParam.setTagStatus(0);
                        //默认为脚本类型
                        tagManageParam.setTagType(1);
                        return tagManageParam;
                    }).collect(Collectors.toList());
                List<Long> tagIds = tagManageDAO.addScriptTags(tagManageParams,1);
                sceneTagRefDAO.addSceneTagRef(tagIds, sceneTagCreateRefRequest.getSceneId());
            }
        }
    }

    @Override
    public List<SceneTagRefResponse> getSceneTagRefBySceneIds(List<Long> sceneIds) {
        if (CollectionUtils.isEmpty(sceneIds)) {
            return Lists.newArrayList();
        }
        List<SceneTagRefResult> refResultList = sceneTagRefDAO.selectBySceneIds(sceneIds);
        return refResult2RefResp(refResultList);
    }

    @Override
    public List<SceneTagRefResponse> getTagRefByTagIds(List<Long> tagIds) {
        if (CollectionUtils.isEmpty(tagIds)) {
            return Lists.newArrayList();
        }
        SceneTagRefQueryParam param = new SceneTagRefQueryParam();
        param.setTagIds(tagIds);
        List<SceneTagRefResult> refResultList = sceneTagRefDAO.selectByExample(param);
        return refResult2RefResp(refResultList);
    }

    List<TagManageResponse> tagResult2TagResp(List<TagManageResult> tagManageResults) {
        if (CollectionUtils.isEmpty(tagManageResults)) {
            return Lists.newArrayList();
        }
        return tagManageResults.stream().map(tagManageResult -> {
            TagManageResponse tagManageResponse = new TagManageResponse();
            tagManageResponse.setId(tagManageResult.getId());
            tagManageResponse.setTagName(tagManageResult.getTagName());
            return tagManageResponse;
        }).collect(Collectors.toList());

    }

    List<SceneTagRefResponse> refResult2RefResp(List<SceneTagRefResult> sceneTagRefResults) {
        if (CollectionUtils.isEmpty(sceneTagRefResults)) {
            return Lists.newArrayList();
        }
        List<SceneTagRefResponse> responseList = new ArrayList<>();
        sceneTagRefResults.stream().forEach(sceneTagRefResult -> {
            SceneTagRefResponse response = new SceneTagRefResponse();
            BeanUtils.copyProperties(sceneTagRefResult, response);
            responseList.add(response);
        });
        return responseList;
    }

}
