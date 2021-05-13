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

package com.pamirs.tro.entity.dao.apimanage;

import java.util.List;

import com.pamirs.tro.entity.annocation.DataAuth;
import com.pamirs.tro.entity.domain.entity.ApplicationApiManage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TApplicationApiManageMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ApplicationApiManage record);

    int insertSelective(ApplicationApiManage record);

    ApplicationApiManage selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ApplicationApiManage record);

    int updateByPrimaryKey(ApplicationApiManage record);

    List<ApplicationApiManage> query();

    List<ApplicationApiManage> querySimple(@Param("appName") String appName);

    @DataAuth()
    List<ApplicationApiManage> selectBySelective(ApplicationApiManage record);

    int insertBatch(@Param("list") List<ApplicationApiManage> list);

    void deleteByAppName(@Param("appName") String appName);
}
