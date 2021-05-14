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

import io.shulie.tro.web.data.model.mysql.TroUserDeptRelationEntity;
import io.shulie.tro.web.data.param.user.UserDeptConditionQueryParam;
import io.shulie.tro.web.data.param.user.UserDeptQueryParam;
import io.shulie.tro.web.data.param.user.UserDeptRelationBatchDeleteParam;
import io.shulie.tro.web.data.param.user.UserDeptRelationBatchUserDeleteParam;
import io.shulie.tro.web.data.result.user.UserDeptConditionResult;
import io.shulie.tro.web.data.result.user.UserDeptResult;
import org.apache.ibatis.annotations.Param;

/**
 * @Author: fanxx
 * @Date: 2020/11/3 4:58 下午
 * @Description:
 */
public interface TroDeptUserRelationDAO {

    /**
     * 根据用户id查询用户部门信息
     *
     * @param queryParam
     * @return
     */
    List<UserDeptResult> selectList(UserDeptQueryParam queryParam);

    /**
     * 根据用户id查询用户部门信息
     *
     * @param queryParam
     * @return
     */
    List<Long> selectDeptIdList(UserDeptQueryParam queryParam);

    /**
     * 根据部门id查询部门所有用户
     *
     * @param queryParam
     * @return
     */
    List<UserDeptConditionResult> selectUserByDeptIds(@Param("param") UserDeptConditionQueryParam queryParam);

    /**
     * 新增用户部门关系
     *
     * @param list
     * @return
     */
    int batchInsert(List<TroUserDeptRelationEntity> list);

    /**
     * 删除用户部门关系
     *
     * @param deleteParam
     * @return
     */
    int deleteByDeptIdAndUserIds(UserDeptRelationBatchDeleteParam deleteParam);

    /**
     * 删除用户部门关系
     *
     * @param deleteParam
     * @return
     */
    int deleteByUserIds(UserDeptRelationBatchUserDeleteParam deleteParam);
}
