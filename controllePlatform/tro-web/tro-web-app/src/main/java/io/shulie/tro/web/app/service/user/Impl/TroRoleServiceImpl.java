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

package io.shulie.tro.web.app.service.user.Impl;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.alibaba.excel.util.CollectionUtils;

import com.google.common.collect.Lists;
import io.shulie.tro.web.app.common.Response;
import io.shulie.tro.web.app.input.user.UserRoleRelationBatchDeleteInput;
import io.shulie.tro.web.app.input.user.UserRoleRelationCreateInput;
import io.shulie.tro.web.app.request.user.RoleCreateRequest;
import io.shulie.tro.web.app.request.user.RoleDeleteRequest;
import io.shulie.tro.web.app.request.user.RoleDetailQueryRequest;
import io.shulie.tro.web.app.request.user.RoleQueryRequest;
import io.shulie.tro.web.app.request.user.RoleUpdateRequest;
import io.shulie.tro.web.app.request.user.UserRoleRelationBatchDeleteRequest;
import io.shulie.tro.web.app.request.user.UserRoleRelationCreateRequest;
import io.shulie.tro.web.app.response.user.RoleQueryResponse;
import io.shulie.tro.web.app.service.user.TroRoleService;
import io.shulie.tro.web.auth.api.RoleService;
import io.shulie.tro.web.data.param.user.RoleCreateParam;
import io.shulie.tro.web.data.param.user.RoleDeleteParam;
import io.shulie.tro.web.data.param.user.RoleQueryParam;
import io.shulie.tro.web.data.param.user.RoleUpdateParam;
import io.shulie.tro.web.data.param.user.UserRoleRelationBatchDeleteParam;
import io.shulie.tro.web.data.param.user.UserRoleRelationCreateParam;
import io.shulie.tro.web.data.result.user.RoleQueryResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: fanxx
 * @Date: 2020/11/2 4:35 下午
 * @Description:
 */
@Component
@Slf4j
public class TroRoleServiceImpl implements TroRoleService {

    @Autowired
    private RoleService roleService;

    @Override
    public List<RoleQueryResponse> listRole(RoleQueryRequest request) {
        List<RoleQueryResponse> responseList = Lists.newArrayList();
        RoleQueryParam param = new RoleQueryParam();
        param.setName(request.getRoleName());
        List<RoleQueryResult> resultList = roleService.selectList(param);
        if (CollectionUtils.isEmpty(resultList)) {
            return responseList;
        }
        resultList.sort(Comparator.comparing(RoleQueryResult::getCreateTime).reversed());
        responseList = resultList.stream().map(result -> {
            RoleQueryResponse response = new RoleQueryResponse();
            BeanUtils.copyProperties(result, response);
            return response;
        }).collect(Collectors.toList());
        return responseList;
    }

    @Override
    public Response queryDetail(RoleDetailQueryRequest request) {
        RoleQueryParam param = new RoleQueryParam();
        param.setId(request.getId());
        RoleQueryResult result = roleService.selectDetail(param);
        RoleQueryResponse queryResponse = new RoleQueryResponse();
        if (Objects.isNull(result.getId())) {
            return Response.fail("0", "该角色不存在");
        }
        BeanUtils.copyProperties(result, queryResponse);
        return Response.success(queryResponse);
    }

    @Override
    public Response addRole(RoleCreateRequest request) {
        RoleQueryParam existNameQuery = new RoleQueryParam();
        existNameQuery.setName(request.getRoleName());
        RoleQueryResult result = roleService.selectDetail(existNameQuery);
        if (!Objects.isNull(result.getId())) {
            return Response.fail("0", "角色名称已存在");
        }
        RoleCreateParam param = new RoleCreateParam();
        param.setName(request.getRoleName());
        param.setDescription(request.getRoleDesc());
        int count = roleService.insert(param);
        return count > 0 ? Response.success("添加成功") : Response.fail("0", "添加失败");
    }

    @Override
    public Response updateRole(RoleUpdateRequest request) {
        RoleQueryParam existRoleQuery = new RoleQueryParam();
        existRoleQuery.setId(request.getId());
        RoleQueryResult existRoleResult = roleService.selectDetail(existRoleQuery);
        if (Objects.isNull(existRoleResult.getId())) {
            return Response.fail("0", "角色不存在");
        }
        if (!request.getRoleName().equals(existRoleResult.getName())) {
            RoleQueryParam existNameQuery = new RoleQueryParam();
            existNameQuery.setName(request.getRoleName());
            RoleQueryResult existNameResult = roleService.selectDetail(existNameQuery);
            if (!Objects.isNull(existNameResult.getId())) {
                return Response.fail("0", "角色名称已存在");
            }
        }
        RoleUpdateParam param = new RoleUpdateParam();
        param.setId(request.getId());
        param.setName(request.getRoleName());
        param.setDescription(request.getRoleDesc());
        int count = roleService.update(param);
        return count > 0 ? Response.success("更新成功") : Response.fail("0", "更新失败");
    }

    @Override
    public Response deleteRole(RoleDeleteRequest request) {
        RoleDeleteParam param = new RoleDeleteParam();
        BeanUtils.copyProperties(request, param);
        int count = roleService.delete(param);
        return count > 0 ? Response.success("删除成功") : Response.fail("0", "删除失败");
    }

    /**
     * 给用户分配角色
     *
     * @param request
     * @return
     */
    @Override
    public Response insertRoleToUser(UserRoleRelationCreateRequest request) {
        UserRoleRelationCreateInput input = new UserRoleRelationCreateInput();
        input.setRoleId(request.getRoleIds());
        input.setUserId(request.getAccountIds());
        if (null == input || CollectionUtils.isEmpty(input.getUserId())) {
            return Response.fail("0", "用户为空");
        }
        if (null != input.getRoleId() && input.getRoleId().size() > 10) {
            return Response.fail("0", "角色数量超过限制");
        }
        UserRoleRelationCreateParam param = new UserRoleRelationCreateParam();
        BeanUtils.copyProperties(input, param);

        int count = roleService.insertRoleToUser(param);
        return count > 0 ? Response.success("分配成功") : Response.fail("0", "分配失败");
    }

    /**
     * 给用户重置（清空）角色
     *
     * @param request
     * @return
     */
    @Override
    public Response deleteUserRoleRelationBatch(UserRoleRelationBatchDeleteRequest request) {
        UserRoleRelationBatchDeleteInput input = new UserRoleRelationBatchDeleteInput();
        input.setUserId(request.getAccountIds());
        UserRoleRelationBatchDeleteParam param = new UserRoleRelationBatchDeleteParam();
        BeanUtils.copyProperties(input, param);
        roleService.deleteUserRoleRelationBatch(param);
        return Response.success("重置成功");
    }

}
