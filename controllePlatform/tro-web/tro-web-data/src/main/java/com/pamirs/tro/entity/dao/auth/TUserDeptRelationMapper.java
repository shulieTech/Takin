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

package com.pamirs.tro.entity.dao.auth;

import java.util.List;

import com.pamirs.tro.entity.domain.entity.auth.UserDeptRelation;
import com.pamirs.tro.entity.domain.entity.auth.UserDeptRelationExample;
import com.pamirs.tro.entity.domain.entity.user.DeptUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TUserDeptRelationMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserDeptRelation record);

    int insertSelective(UserDeptRelation record);

    List<UserDeptRelation> selectByExample(UserDeptRelationExample example);

    UserDeptRelation selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserDeptRelation record);

    int updateByPrimaryKey(UserDeptRelation record);

    List<DeptUser> selectByDept(@Param("deptIds") List<String> deptIds, @Param("name") String name);

    List<DeptUser> selectUserIdByScope(@Param("deptIds") List<String> deptIds, @Param("id") Long id);

    List<DeptUser> selectByRole(@Param("roleId") String roleId);
}
