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

package io.shulie.tro.cloud.biz.cloudserver;

import java.util.List;

import com.pamirs.tro.entity.domain.entity.machine.PressureMachine;
import com.pamirs.tro.entity.domain.vo.machine.PressureMachineVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * @Author: fanxx
 * @Date: 2020/5/15 下午9:10
 * @Description:
 */
@Mapper
public interface PressureMachineConvert {
    PressureMachineConvert INSTAMCE = Mappers.getMapper(PressureMachineConvert.class);

    @Mappings({})
    PressureMachineVO of(PressureMachine pressureMachine);

    List<PressureMachineVO> ofs(List<PressureMachine> pressureMachineList);
}
