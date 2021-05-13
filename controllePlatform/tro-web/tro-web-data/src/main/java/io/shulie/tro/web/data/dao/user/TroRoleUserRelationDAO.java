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

import io.shulie.tro.web.data.model.mysql.TroRoleUserRelationEntity;
import io.shulie.tro.web.data.param.user.UserRoleQueryParam;
import io.shulie.tro.web.data.param.user.UserRoleRelationBatchDeleteParam;
import io.shulie.tro.web.data.param.user.UserRoleRelationBatchQueryParam;
import io.shulie.tro.web.data.param.user.UserRoleRelationCreateParam;
import io.shulie.tro.web.data.param.user.UserRoleRelationQueryParam;
import io.shulie.tro.web.data.result.user.UserRoleRelationResult;
import io.shulie.tro.web.data.result.user.UserRoleResult;

/**
 * @Author: fanxx
 * @Date: 2020/11/3 5:53 下午
 * @Description:
 */
public interface TroRoleUserRelationDAO {

    /**
     * 根据用户id查询角色信息
     *
     * @param queryParam
     * @return
     */
    List<UserRoleResult> selectList(UserRoleQueryParam queryParam);

    /**
     * 给用户分配角色
     *
     * @param param
     * @return
     */
    int insertRoleToUser(UserRoleRelationCreateParam param);

    /**
     * 新增用户角色关系
     *
     * @param list
     * @return
     */
    int batchInsert(List<TroRoleUserRelationEntity> list);

    /**
     * 查询该用户下是否存在这个角色
     *
     * @param param
     * @return
     */
    UserRoleRelationResult selectUserRoleRelation(UserRoleRelationQueryParam param);

    /**
     * 查询用户下所有角色列表
     *
     * @param param
     * @return
     */
    List<UserRoleRelationResult> selectUserRoleRelationBatch(UserRoleRelationBatchQueryParam param);

    /**
     * 给用户重置（清空）角色
     *
     * @param param
     * @return
     */
    int deleteUserRoleRelationBatch(UserRoleRelationBatchDeleteParam param);

}
