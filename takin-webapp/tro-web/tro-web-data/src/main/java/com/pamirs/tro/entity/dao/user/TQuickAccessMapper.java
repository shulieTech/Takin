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

package com.pamirs.tro.entity.dao.user;

import java.util.List;

import com.pamirs.tro.entity.domain.entity.user.QuickAccess;
import com.pamirs.tro.entity.domain.entity.user.QuickAccessExample;
import org.apache.ibatis.annotations.Param;

public interface TQuickAccessMapper {
    int deleteByPrimaryKey(Long id);

    int insert(QuickAccess record);

    int insertSelective(QuickAccess record);

    List<QuickAccess> selectByExample(QuickAccessExample example);

    QuickAccess selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") QuickAccess record, @Param("example") QuickAccessExample example);

    int updateByExample(@Param("record") QuickAccess record, @Param("example") QuickAccessExample example);

    int updateByPrimaryKeySelective(QuickAccess record);

    int updateByPrimaryKey(QuickAccess record);
}
