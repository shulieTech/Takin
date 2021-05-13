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

import com.pamirs.tro.entity.domain.entity.auth.Dept;
import com.pamirs.tro.entity.domain.entity.auth.TreeDeptModel;

/**
 * @Author: fanxx
 * @Date: 2020/9/2 上午10:14
 * @Description:
 */
public interface DeptService {
    /**
     * 获取所有部门信息
     *
     * @return
     */
    List<Dept> getAllDepts(String deptName);

    /**
     * 获取部门树
     *
     * @return
     */
    List<TreeDeptModel> getDeptTree(String deptName);

    /**
     * 获取用户所有部门
     *
     * @param userId
     * @return
     */
    List<Dept> getDeptByUserId(String userId);
}
