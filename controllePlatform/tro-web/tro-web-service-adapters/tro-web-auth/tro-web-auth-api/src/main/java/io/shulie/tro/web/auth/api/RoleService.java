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

package io.shulie.tro.web.auth.api;

import java.util.List;

import com.pamirs.tro.entity.domain.entity.TApplicationMnt;
import com.pamirs.tro.entity.domain.entity.auth.BaseRole;
import com.pamirs.tro.entity.domain.entity.auth.Role;
import io.shulie.tro.web.data.param.user.RoleCreateParam;
import io.shulie.tro.web.data.param.user.RoleDeleteParam;
import io.shulie.tro.web.data.param.user.RoleQueryParam;
import io.shulie.tro.web.data.param.user.RoleUpdateParam;
import io.shulie.tro.web.data.param.user.UserRoleRelationBatchDeleteParam;
import io.shulie.tro.web.data.param.user.UserRoleRelationCreateParam;
import io.shulie.tro.web.data.result.user.RoleQueryResult;

/**
 * @author shiyajian
 * create: 2020-09-01
 */
public interface RoleService {

    List<RoleQueryResult> selectList(RoleQueryParam queryParam);

    RoleQueryResult selectDetail(RoleQueryParam queryParam);

    int insert(RoleCreateParam createParam);

    int update(RoleUpdateParam updateParam);

    int delete(RoleDeleteParam deleteParam);


    /**
     * 获取所有基础角色信息
     *
     * @return
     */
    List<BaseRole> getAllBaseRoles();

    /**
     * 根据角色id获取角色信息
     *
     * @param roleIds
     * @return
     */
    List<Role> getRolesByIds(List<Long> roleIds);

    /**
     * 根据用户id获取角色信息
     *
     * @param userId
     * @return
     */
    List<Role> getRoleByUserId(Long userId);

    /**
     * 根据应用id获取角色信息
     *
     * @param applicationIds
     * @return
     */
    List<Role> getRoleByApplicationId(List<String> applicationIds);

    /**
     * 根据应用信息创建应用角色
     *
     * @param applicationMnts
     * @return
     */
    int addRoleForApplication(List<TApplicationMnt> applicationMnts);

    /**
     * 根据应用id删除应用角色
     *
     * @param applicationIds
     * @return
     */
    int deleteRoleByApplicationIds(List<String> applicationIds);

    /**
     * 给角色添加用户
     *
     * @param roleId
     * @param userIds
     * @return
     */
    int addUserToRole(String roleId, List<String> userIds);

    /**
     * 从角色中删除用户(按条件删除)
     *
     * @param userIds
     * @param roleId
     * @return
     */
    int deleteUserFromRole(String roleId, List<String> userIds);

    /**
     * 从角色中删除用户(批量删除)
     *
     * @param roleId
     * @return
     */
    int deleteBatchUserByRoleId(String roleId);


    /**
     * 给用户分配角色
     *
     * @param param
     * @return
     */
    int insertRoleToUser(UserRoleRelationCreateParam param);

    /**
     * 给用户重置（清空）角色
     *
     * @param param
     * @return
     */
    int deleteUserRoleRelationBatch(UserRoleRelationBatchDeleteParam param);
}
