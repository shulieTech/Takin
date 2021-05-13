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

package io.shulie.tro.web.auth.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.google.common.collect.Lists;
import com.pamirs.tro.common.util.HttpUtils;
import com.pamirs.tro.entity.dao.auth.TResourceMapper;
import com.pamirs.tro.entity.dao.auth.TRoleUserRelationMapper;
import com.pamirs.tro.entity.domain.entity.TApplicationMnt;
import com.pamirs.tro.entity.domain.entity.auth.Authority;
import com.pamirs.tro.entity.domain.entity.auth.Resource;
import com.pamirs.tro.entity.domain.entity.auth.ResourceExample;
import com.pamirs.tro.entity.domain.entity.auth.Role;
import io.shulie.tro.web.auth.api.AuthService;
import io.shulie.tro.web.auth.api.ResourceService;
import io.shulie.tro.web.auth.api.RoleService;
import io.shulie.tro.web.auth.api.enums.ObjectTypeEnum;
import io.shulie.tro.web.auth.api.enums.ResourceTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: fanxx
 * @Date: 2020/9/2 下午3:15
 * @Description:
 */
@Component
@Slf4j
public class ResourceServiceImpl implements ResourceService {

    @Autowired
    TResourceMapper TResourceMapper;

    @Autowired
    TRoleUserRelationMapper TRoleUserRelationMapper;

    @Autowired
    RoleService roleService;

    @Autowired
    AuthService authService;

    public static void main(String[] args) {
        Map<String, String> pamirs = new HashMap<>();
        pamirs.put("userId", "90001");
        pamirs.put("authId", "100000378");
        String result = HttpUtils.sendGet0("http://uat.ydauth.yundasys.com:16120/ydauth/actions/outer/user/menu.action",
            pamirs);
        JSONObject jsonObject = JSON.parseObject(result);
        JSONArray menus = jsonObject.getJSONArray("menus");
        List<Resource> resourceList = new ArrayList<>();
        for (Object menu : menus) {
            JSONObject menuJsonObject = JSON.parseObject(menu.toString());
            Resource resource = new Resource();
            resource.setName(menuJsonObject.getString("name"));
            resource.setAlias(menuJsonObject.getString("describe"));
            resource.setValue(menuJsonObject.getString("url"));
            resourceList.add(resource);
        }
        System.out.println(JSONObject.toJSON(resourceList));
        System.out.println(result);
    }

    @Override
    public List<Resource> getAllMenuResource() {
        ResourceExample example = new ResourceExample();
        ResourceExample.Criteria criteria = example.createCriteria();
        criteria.andTypeEqualTo(ResourceTypeEnum.MENU.getCode());
        return TResourceMapper.selectByExample(example);
    }

    @Override
    public List<Resource> getAllDataResource() {
        ResourceExample example = new ResourceExample();
        ResourceExample.Criteria criteria = example.createCriteria();
        criteria.andTypeEqualTo(ResourceTypeEnum.DATA.getCode());
        return TResourceMapper.selectByExample(example);
    }

    @Override
    public List<Resource> getUserResourceById(ResourceTypeEnum resourceTypeEnum, String userId) {
        if (StringUtils.isBlank(userId)) {
            return Lists.newArrayList();
        }
        List<Resource> userResourceList = Lists.newArrayList();
        List<Authority> authorityList = Lists.newArrayList();
        if (ResourceTypeEnum.MENU.equals(resourceTypeEnum)) {
            authorityList = authService.getUserAuth(ObjectTypeEnum.USER.getCode(), Arrays.asList(userId));
        } else if (ResourceTypeEnum.DATA.equals(resourceTypeEnum)) {
            //获取用户角色
            List<Role> roleList = roleService.getRoleByUserId(Long.parseLong(userId));
            if (CollectionUtils.isEmpty(roleList)) {
                return userResourceList;
            }
            //获取角色id
            List<String> roleIdList = roleList
                .stream().map(Role::getId).map(String::valueOf).collect(Collectors.toList());
            //查询授权信息
            authorityList = authService.getUserAuth(ObjectTypeEnum.ROLE.getCode(), roleIdList);
        }
        if (CollectionUtils.isEmpty(authorityList)) {
            return userResourceList;
        }
        //获取资源信息
        List<Long> resourceIdList = authorityList
            .stream().map(Authority::getResourceId).map(Long::parseLong).collect(Collectors.toList());
        return getResourceByIds(resourceIdList);
    }

    @Override
    public List<Resource> getResourceByIds(List<Long> resourceIds) {
        if (CollectionUtils.isEmpty(resourceIds)) {
            return Lists.newArrayList();
        }
        ResourceExample resourceExample = new ResourceExample();
        ResourceExample.Criteria resourceExampleCriteria = resourceExample.createCriteria();
        resourceExampleCriteria.andIdIn(resourceIds);
        return TResourceMapper.selectByExample(resourceExample);
    }

    @Override
    public List<Resource> getResourceByApplicationIds(List<String> applicationIds) {
        if (CollectionUtils.isEmpty(applicationIds)) {
            return Lists.newArrayList();
        }
        ResourceExample resourceExample = new ResourceExample();
        ResourceExample.Criteria resourceExampleCriteria = resourceExample.createCriteria();
        resourceExampleCriteria.andTypeEqualTo(ResourceTypeEnum.DATA.getCode());
        resourceExampleCriteria.andValueIn(applicationIds);
        return TResourceMapper.selectByExample(resourceExample);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addResourceForApplication(List<TApplicationMnt> applicationMnts) {
        int count = 0;
        if (CollectionUtils.isEmpty(applicationMnts)) {
            return count;
        }
        for (TApplicationMnt applicationMnt : applicationMnts) {
            //判断该应用是否已经存在应用资源
            ResourceExample resourceExample = new ResourceExample();
            ResourceExample.Criteria criteria = resourceExample.createCriteria();
            criteria.andValueEqualTo(String.valueOf(applicationMnt.getApplicationId()));
            List<Resource> resourceList = TResourceMapper.selectByExample(resourceExample);
            if (CollectionUtils.isNotEmpty(resourceList)) {
                log.warn("该应用已存在应用资源:" + applicationMnt.getApplicationId());
                continue;
            }
            Resource resource = new Resource();
            resource.setType(ResourceTypeEnum.DATA.getCode());
            resource.setName(applicationMnt.getApplicationName());
            resource.setValue(String.valueOf(applicationMnt.getApplicationId()));
            int res = TResourceMapper.insertSelective(resource);
            count = count + res;
        }
        return count;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteResourceByApplicationIds(List<String> applicationIds) {
        int count = 0;
        if (CollectionUtils.isEmpty(applicationIds)) {
            return count;
        }
        List<Resource> resourceList = getResourceByApplicationIds(applicationIds);
        if (CollectionUtils.isEmpty(resourceList)) {
            return count;
        }
        for (Resource resource : resourceList) {
            int res = TResourceMapper.deleteByPrimaryKey(resource.getId());
            count = count + res;
        }
        return count;
    }
}
