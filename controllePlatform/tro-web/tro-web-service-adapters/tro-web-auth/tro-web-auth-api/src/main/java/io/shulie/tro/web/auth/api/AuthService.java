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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pamirs.tro.entity.domain.entity.auth.Authority;
import com.pamirs.tro.entity.domain.entity.user.User;
import com.pamirs.tro.entity.domain.vo.user.UserLoginParam;
import io.shulie.tro.web.auth.api.exception.TroLoginException;
import io.shulie.tro.web.data.param.user.ResourceMenuQueryParam;
import io.shulie.tro.web.data.param.user.RoleResourceAuthDeleteParam;
import io.shulie.tro.web.data.param.user.RoleResourceAuthQueryParam;
import io.shulie.tro.web.data.param.user.RoleResourceAuthUpdateParam;
import io.shulie.tro.web.data.param.user.UserDeptConditionQueryParam;
import io.shulie.tro.web.data.param.user.UserDeptQueryParam;
import io.shulie.tro.web.data.param.user.UserRoleRelationBatchQueryParam;
import io.shulie.tro.web.data.result.user.ResourceAuthResult;
import io.shulie.tro.web.data.result.user.ResourceMenuResult;
import io.shulie.tro.web.data.result.user.UserDeptConditionResult;
import io.shulie.tro.web.data.result.user.UserDeptResult;
import io.shulie.tro.web.data.result.user.UserRoleRelationResult;

/**
 * @author shiyajian
 * create: 2020-09-01
 */
public interface AuthService {
    /**
     * 用户登录
     *
     * @param request
     * @param user
     * @return
     */
    User login(HttpServletRequest request, UserLoginParam user) throws TroLoginException;

    /**
     * 用户退出
     *
     * @param request
     * @return
     */
    String logout(HttpServletRequest request, HttpServletResponse response);

    /**
     * 重定向
     *
     * @param request
     * @return
     */
    void redirect(String redirectUrl, HttpServletRequest request, HttpServletResponse response);

    /**
     * 登录成功
     *
     * @param user
     */
    void loginSuccess(User user);

    /**
     * 根据用户id或者角色id获取用户授权信息
     *
     * @param ids
     * @return
     */
    List<Authority> getUserAuth(Integer type, List<String> ids);

    /**
     * 判断用户是否拥有接口权限
     *
     * @param user
     * @param url
     * @return
     */
    boolean hasPermissionUrl(User user, String url);

    /**
     * 用户是否已登录
     *
     * @param request
     * @param response
     * @return
     */
    User check(HttpServletRequest request, HttpServletResponse response);

    /**
     * 登录失败
     */
    void checkFail(HttpServletResponse response);

    /**
     * 查询可配置权限的（有页面的）菜单列表
     *
     * @return
     */
    List<ResourceMenuResult> selectAuthConfigMenu();

    /**
     * 查询全量菜单列表
     *
     * @return
     */
    List<ResourceMenuResult> selectMenu(ResourceMenuQueryParam queryParam);

    /**
     * 查询角色权限
     *
     * @param queryParam
     * @return
     */
    List<ResourceAuthResult> selectList(RoleResourceAuthQueryParam queryParam);

    /**
     * 更新角色权限
     *
     * @param updateParamList
     * @return
     */
    void updateRoleAuth(List<RoleResourceAuthUpdateParam> updateParamList);

    /**
     * 删除角色权限
     *
     * @param deleteParamList
     * @return
     */
    void deleteRoleAuth(List<RoleResourceAuthDeleteParam> deleteParamList);

    /**
     * 查询用户下所有角色列表
     *
     * @param param
     * @return
     */
    List<UserRoleRelationResult> selectUserRoleRelationBatch(UserRoleRelationBatchQueryParam param);

    /**
     * 根据用户id查询用户部门信息
     *
     * @param queryParam
     * @return
     */
    List<UserDeptResult> selectList(UserDeptQueryParam queryParam);

    /**
     * 根据部门id查询用户列表
     *
     * @param queryParam
     * @return
     */
    List<UserDeptConditionResult> selectUserByDeptIds(UserDeptConditionQueryParam queryParam);
}
