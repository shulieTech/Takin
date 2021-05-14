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

import com.pamirs.tro.entity.domain.entity.auth.RoleUserRelation;
import com.pamirs.tro.entity.domain.entity.auth.RoleUserRelationExample;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TRoleUserRelationMapper {
    int deleteByPrimaryKey(Long id);

    int insert(RoleUserRelation record);

    int insertSelective(RoleUserRelation record);

    List<RoleUserRelation> selectByExample(RoleUserRelationExample example);

    RoleUserRelation selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(RoleUserRelation record);

    int updateByPrimaryKey(RoleUserRelation record);
}
