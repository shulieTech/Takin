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
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.pamirs.tro.entity.domain.entity.user.DeptUser;
import com.pamirs.tro.entity.domain.entity.user.DeptUserQueryParam;
import com.pamirs.tro.entity.domain.entity.user.RoleUserQueryParam;
import com.pamirs.tro.entity.domain.entity.user.User;
import io.shulie.tro.common.beans.page.PagingList;
import io.shulie.tro.web.data.param.user.UserQueryParam;
import io.shulie.tro.web.data.result.user.UserCommonResult;

/**
 * @Author: fanxx
 * @Date: 2020/9/2 上午10:04
 * @Description:
 */
public interface UserService {

    /**
     * 查询组织成员
     *
     * @param param
     * @return
     */
    PagingList<UserCommonResult> selectUserByCondition(UserQueryParam param);

    /**
     * 获取部门成员列表
     *
     * @param param
     * @return
     */
    PageInfo<DeptUser> getSimpleUserList(DeptUserQueryParam param);

    /**
     * 根据角色id查询用户列表
     *
     * @param param
     */
    List<DeptUser> getSimpleUserByRoleId(RoleUserQueryParam param);

    /**
     * 根据角色id查询用户id
     *
     * @param roleId
     * @return
     */
    List<String> getUserIdListByRoleId(String roleId);

    /**
     * 根据用户id查询用户
     *
     * @param userIds
     * @return
     */
    List<User> getUserById(List<Long> userIds);

    /**
     * 根据id查询用户信息
     *
     * @param userIds
     * @return key:userId  value:user对象
     */
    Map<Long, User> getUserMapByIds(List<Long> userIds);

    List<UserCommonResult> getByName(String userName);
}
