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

import io.shulie.tro.web.data.model.mysql.TroDeptEntity;
import io.shulie.tro.web.data.param.user.DeptCreateParam;
import io.shulie.tro.web.data.param.user.DeptDeleteParam;
import io.shulie.tro.web.data.param.user.DeptUpdateParam;
import io.shulie.tro.web.data.result.user.DeptQueryResult;

public interface TroDeptDAO {

    /**
     * 查询部门明细
     *
     * @param id
     * @return
     */
    DeptQueryResult selectDetailById(Long id);

    /**
     * 查询部门明细
     *
     * @param name
     * @return
     */
    DeptQueryResult selectDetailByName(String name);

    /**
     * 新增部门
     *
     * @param createParam
     * @return
     */
    int insert(DeptCreateParam createParam);

    /**
     * 更新部门
     *
     * @param updateParam
     * @return
     */
    int update(DeptUpdateParam updateParam);

    /**
     * 删除部门
     *
     * @param deleteParam
     * @return
     */
    int delete(DeptDeleteParam deleteParam);

    /**
     * 所有部门
     *
     * @return
     */
    List<TroDeptEntity> getAllDept();

    /**
     * 递归查询上级部门，直到根节点
     *
     * @param depts
     * @return
     */
    List<TroDeptEntity> recursionDept(List<TroDeptEntity> depts);
}
