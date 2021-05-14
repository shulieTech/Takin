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

package io.shulie.tro.web.app.service.auth;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.pagehelper.PageInfo;
import com.pamirs.tro.entity.domain.entity.auth.TreeDeptModel;
import com.pamirs.tro.entity.domain.entity.user.DeptUser;
import com.pamirs.tro.entity.domain.entity.user.DeptUserQueryParam;
import com.pamirs.tro.entity.domain.entity.user.User;
import com.pamirs.tro.entity.domain.vo.user.UserLoginParam;
import io.shulie.tro.common.beans.page.PagingList;
import io.shulie.tro.web.app.common.Response;
import io.shulie.tro.web.app.input.user.UserQueryInput;
import io.shulie.tro.web.app.output.user.UserQueryOutput;
import io.shulie.tro.web.app.request.user.ResourceActionDeleteRequest;
import io.shulie.tro.web.app.request.user.ResourceActionQueryRequest;
import io.shulie.tro.web.app.request.user.ResourceActionUpdateRequest;
import io.shulie.tro.web.app.request.user.ResourceScopeQueryRequest;
import io.shulie.tro.web.app.request.user.ResourceScopeUpdateRequest;
import io.shulie.tro.web.app.response.user.ResourceActionResponse;
import io.shulie.tro.web.app.response.user.ResourceScopeResponse;
import io.shulie.tro.web.auth.api.exception.TroAuthException;
import io.shulie.tro.web.auth.api.exception.TroLoginException;
import io.shulie.tro.web.data.param.user.UserDeptConditionQueryParam;
import io.shulie.tro.web.data.param.user.UserDeptQueryParam;
import io.shulie.tro.web.data.result.user.UserDeptConditionResult;
import io.shulie.tro.web.data.result.user.UserDeptResult;

/**
 * @Author: fanxx
 * @Date: 2020/9/3 下午2:35
 * @Description:
 */
public interface TroAuthService {

    /**
     * 查询组织成员
     *
     * @param input
     * @return
     */
    PagingList<UserQueryOutput> selectUserByCondition(UserQueryInput input);

    /**
     * 获取树型部门数据
     *
     * @param deptName
     * @return
     */
    List<TreeDeptModel> getDeptTree(String deptName);

    /**
     * 获取部门用户
     *
     * @param param
     * @return
     */
    PageInfo<DeptUser> getDeptUser(DeptUserQueryParam param);

    /**
     * 获取用户被授权的菜单
     *
     * @return
     */
    Map<String, Boolean> getUserMenu();

    /**
     * 获取用户被授权的操作权限
     *
     * @return
     */
    Map<String, Boolean> getUserAction();

    /**
     * 获取用户被授权的数据权限
     *
     * @return
     */
    Map<String, List<Integer>> getUserData();

    /**
     * 获取操作权限
     *
     * @param queryRequest
     * @return
     */
    List<ResourceActionResponse> getResourceAction(ResourceActionQueryRequest queryRequest);

    /**
     * 保存菜单操作权限
     *
     * @param updateRequest
     * @return
     */
    Response updateResourceAction(ResourceActionUpdateRequest updateRequest);

    /**
     * 获取数据权限
     *
     * @param queryRequest
     * @return
     */
    List<ResourceScopeResponse> getResourceScope(ResourceScopeQueryRequest queryRequest);

    /**
     * 保存数据权限
     *
     * @param updateRequest
     * @return
     */
    Response updateResourceScope(ResourceScopeUpdateRequest updateRequest);

    /**
     * 删除操作权限
     *
     * @param deleteRequestList
     */
    void deleteResourceAction(List<ResourceActionDeleteRequest> deleteRequestList);

    /**
     * 获取用户被授权的接口列表（菜单权限鉴权）
     *
     * @param user
     * @return
     */
    List<String> getUserMenuResource(User user);

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

    /**
     * 用户登录
     *
     * @param request
     * @param user
     * @return
     * @throws TroAuthException
     */
    User login(HttpServletRequest request, UserLoginParam user) throws TroLoginException;

    /**
     * 用户退出
     *
     * @param request
     * @return
     * @throws TroAuthException
     */
    String logout(HttpServletRequest request, HttpServletResponse response) throws TroLoginException;

    /**
     * 重定向
     *
     * @param request
     * @return
     */
    void redirect(String redirectUrl, HttpServletRequest request, HttpServletResponse response);

    /**
     * 登录成功操作
     *
     * @param user
     */
    void loginSuccess(User user);

    /**
     * 检查用户登录状态
     *
     * @param request
     * @param response
     * @return
     */
    User check(HttpServletRequest request, HttpServletResponse response);

    /**
     * 登录失败操作
     *
     * @param response
     */
    void checkFail(HttpServletResponse response);

    /**
     * 判断用户是否拥有接口权限
     *
     * @param user
     * @param url
     * @return
     */
    boolean hasPermissionUrl(User user, String url);
}
