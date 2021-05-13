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

import com.pamirs.tro.entity.domain.entity.machine.PressureMachine;
import com.pamirs.tro.entity.domain.entity.machine.PressureMachineExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TPressureMachineMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PressureMachine record);

    int insertSelective(PressureMachine record);

    List<PressureMachine> selectByExampleWithBLOBs(PressureMachineExample example);

    List<PressureMachine> selectByExample(PressureMachineExample example);

    PressureMachine selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") PressureMachine record,
        @Param("example") PressureMachineExample example);

    int updateByExampleWithBLOBs(@Param("record") PressureMachine record,
        @Param("example") PressureMachineExample example);

    int updateByExample(@Param("record") PressureMachine record, @Param("example") PressureMachineExample example);

    int updateByPrimaryKeySelective(PressureMachine record);

    int updateByPrimaryKeyWithBLOBs(PressureMachine record);

    int updateByPrimaryKey(PressureMachine record);
}
