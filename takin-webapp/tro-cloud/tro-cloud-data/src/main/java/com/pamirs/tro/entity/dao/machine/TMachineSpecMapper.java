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

package com.pamirs.tro.entity.dao.machine;

import java.util.List;

import com.pamirs.tro.entity.domain.entity.machine.MachineSpec;
import com.pamirs.tro.entity.domain.entity.machine.MachineSpecExample;
import org.apache.ibatis.annotations.Param;

public interface TMachineSpecMapper {
    int deleteByPrimaryKey(Long id);

    int insert(MachineSpec record);

    int insertSelective(MachineSpec record);

    List<MachineSpec> selectByExample(MachineSpecExample example);

    MachineSpec selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") MachineSpec record, @Param("example") MachineSpecExample example);

    int updateByExample(@Param("record") MachineSpec record, @Param("example") MachineSpecExample example);

    int updateByPrimaryKeySelective(MachineSpec record);

    int updateByPrimaryKey(MachineSpec record);
}
