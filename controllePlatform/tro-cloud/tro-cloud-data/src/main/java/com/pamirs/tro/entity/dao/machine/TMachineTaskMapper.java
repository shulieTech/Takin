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

import com.pamirs.tro.entity.domain.entity.machine.MachineTask;
import com.pamirs.tro.entity.domain.entity.machine.MachineTaskExample;
import org.apache.ibatis.annotations.Param;

public interface TMachineTaskMapper {
    int deleteByPrimaryKey(Long id);

    int insert(MachineTask record);

    int insertSelective(MachineTask record);

    List<MachineTask> selectByExampleWithBLOBs(MachineTaskExample example);

    List<MachineTask> selectByExample(MachineTaskExample example);

    MachineTask selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") MachineTask record, @Param("example") MachineTaskExample example);

    int updateByExampleWithBLOBs(@Param("record") MachineTask record, @Param("example") MachineTaskExample example);

    int updateByExample(@Param("record") MachineTask record, @Param("example") MachineTaskExample example);

    int updateByPrimaryKeySelective(MachineTask record);

    int updateByPrimaryKeyWithBLOBs(MachineTask record);

    int updateByPrimaryKey(MachineTask record);
}
