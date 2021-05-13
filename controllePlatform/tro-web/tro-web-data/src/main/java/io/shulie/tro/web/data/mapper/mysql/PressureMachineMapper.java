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

package io.shulie.tro.web.data.mapper.mysql;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.shulie.tro.web.data.model.mysql.PressureMachineEntity;
import io.shulie.tro.web.data.result.perfomanceanaly.PressureMachineStatisticsDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface PressureMachineMapper extends BaseMapper<PressureMachineEntity> {

    @Select(" select * from  t_pressure_machine where ip = #{ip}")
    PressureMachineEntity getByIp(@Param("ip") String ip);

    @Select("select status ,count(1) as `count`  from  t_pressure_machine where is_deleted=0  group by status ")
    List<PressureMachineStatisticsDTO> statistics();
}
