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

import io.shulie.tro.web.data.param.user.RoleResourceAuthDeleteParam;
import io.shulie.tro.web.data.param.user.RoleResourceAuthQueryParam;
import io.shulie.tro.web.data.param.user.RoleResourceAuthUpdateParam;
import io.shulie.tro.web.data.result.user.ResourceAuthResult;

/**
 * @Author: fanxx
 * @Date: 2020/11/3 8:00 下午
 * @Description:
 */
public interface TroRoleResourceAuthDAO {
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
     */
    void updateRoleAuth(List<RoleResourceAuthUpdateParam> updateParamList);

    /**
     * 删除角色权限
     *
     * @param deleteParamList
     */
    void deleteRoleAuth(List<RoleResourceAuthDeleteParam> deleteParamList);

    /**
     * 删除角色权限
     *
     * @param roleIdList
     */
    void clearRoleAuth(List<Long> roleIdList);
}
