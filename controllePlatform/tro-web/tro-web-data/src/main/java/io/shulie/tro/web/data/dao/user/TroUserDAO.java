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

package io.shulie.tro.web.data.dao.user;

import java.util.List;
import java.util.Map;

import io.shulie.tro.common.beans.page.PagingList;
import io.shulie.tro.web.data.model.mysql.TroUserEntity;
import io.shulie.tro.web.data.param.user.UserCreateParam;
import io.shulie.tro.web.data.param.user.UserQueryParam;
import io.shulie.tro.web.data.param.user.UserUpdateParam;
import io.shulie.tro.web.data.result.user.TroUserResult;
import io.shulie.tro.web.data.result.user.UserCommonResult;
import io.shulie.tro.web.data.result.user.UserDetailResult;

/**
 * @Author: fanxx
 * @Date: 2020/11/3 9:58 上午
 * @Description:
 */
public interface TroUserDAO {

    /**
     * 查询组织成员
     *
     * @param param
     * @return
     */
    PagingList<UserCommonResult> selectUserByCondition(UserQueryParam param);

    /**
     * 根据id查询用户信息
     *
     * @param userIds
     * @return
     */
    List<UserCommonResult> selectUserByIds(List<Long> userIds);

    /**
     * 根据id查询用户信息
     *
     * @param userIds
     * @return key:userId  value:user对象
     */
    Map<Long, UserCommonResult> selectUserMapByIds(List<Long> userIds);

    /**
     * 批量的插入 or  更新的操作
     *
     * @param list
     */
    int saveOrUpdateUserBatch(List<TroUserEntity> list);

    /**
     * 获取user，根据userID
     *
     * @param id
     * @return
     */

    TroUserResult selectById(Long id);

    /**
     * 根据userAppKey 获取用户
     *
     * @param userAppkey
     * @return
     */
    TroUserResult selectByUserAppKey(String userAppkey);

    Long insert(UserCreateParam createParam);

    /**
     * 更新用户信息
     */
    int updateUser(UserUpdateParam userUpdateParam);

    /**
     * 查询用户信息
     */
    UserDetailResult selectUserById(Long id);

    /**
     * 查询用户信息
     */
    UserDetailResult selectUserByName(String name);

    /**
     * 根据名称模糊搜索
     * @param userName
     * @return
     */
    List<UserCommonResult> selectByName(String userName);

    List<UserDetailResult> selectAllUser();
}
