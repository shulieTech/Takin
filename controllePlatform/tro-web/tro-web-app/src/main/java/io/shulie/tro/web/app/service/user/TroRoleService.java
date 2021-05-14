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

package io.shulie.tro.web.app.service.user;

import java.util.List;

import io.shulie.tro.web.app.common.Response;
import io.shulie.tro.web.app.request.user.RoleCreateRequest;
import io.shulie.tro.web.app.request.user.RoleDeleteRequest;
import io.shulie.tro.web.app.request.user.RoleDetailQueryRequest;
import io.shulie.tro.web.app.request.user.RoleQueryRequest;
import io.shulie.tro.web.app.request.user.RoleUpdateRequest;
import io.shulie.tro.web.app.request.user.UserRoleRelationBatchDeleteRequest;
import io.shulie.tro.web.app.request.user.UserRoleRelationCreateRequest;
import io.shulie.tro.web.app.response.user.RoleQueryResponse;

/**
 * @Author: fanxx
 * @Date: 2020/11/2 4:35 下午
 * @Description:
 */
public interface TroRoleService {
    /**
     * 角色列表
     *
     * @param request
     * @return
     */
    List<RoleQueryResponse> listRole(RoleQueryRequest request);

    /**
     * 角色详情
     *
     * @param request
     * @return
     */
    Response queryDetail(RoleDetailQueryRequest request);

    /**
     * 创建角色
     *
     * @param request
     * @return
     */
    Response addRole(RoleCreateRequest request);

    /**
     * 更新角色
     *
     * @param request
     * @return
     */
    Response updateRole(RoleUpdateRequest request);

    /**
     * 删除角色
     *
     * @param request
     * @return
     */
    Response deleteRole(RoleDeleteRequest request);

    /**
     * 给用户分配角色
     *
     * @param request
     * @return
     */
    Response insertRoleToUser(UserRoleRelationCreateRequest request);

    /**
     * 给用户重置（清空）角色
     *
     * @param request
     * @return
     */
    Response deleteUserRoleRelationBatch(UserRoleRelationBatchDeleteRequest request);

}
