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

import io.shulie.tro.web.data.param.user.RoleCreateParam;
import io.shulie.tro.web.data.param.user.RoleDeleteParam;
import io.shulie.tro.web.data.param.user.RoleQueryParam;
import io.shulie.tro.web.data.param.user.RoleUpdateParam;
import io.shulie.tro.web.data.result.user.RoleQueryResult;

/**
 * @Author: fanxx
 * @Date: 2020/11/2 4:43 下午
 * @Description:
 */
public interface TroRoleDAO {

    /**
     * 查询当前租户下所有角色列表
     *
     * @param queryParam
     * @return
     */
    List<RoleQueryResult> selectList(RoleQueryParam queryParam);

    /**
     * 查询角色明细
     *
     * @param queryParam
     * @return
     */
    RoleQueryResult selectDetail(RoleQueryParam queryParam);

    /**
     * 新增角色
     *
     * @param createParam
     * @return
     */
    int insert(RoleCreateParam createParam);

    /**
     * 更新角色
     *
     * @param updateParam
     * @return
     */
    int update(RoleUpdateParam updateParam);

    /**
     * 删除角色
     *
     * @param deleteParam
     * @return
     */
    int delete(RoleDeleteParam deleteParam);

    /**
     * 根据角色id查询
     *
     * @param roleIds
     * @return
     */
    List<RoleQueryResult> selectRoleByIds(List<String> roleIds);

}
