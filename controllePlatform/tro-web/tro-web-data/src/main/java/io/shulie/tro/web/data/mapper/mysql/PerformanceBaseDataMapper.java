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

import io.shulie.tro.web.data.model.mysql.PerformanceBaseDataEntity;
import io.shulie.tro.web.data.param.perfomanceanaly.PerformanceBaseQueryParam;
import org.apache.ibatis.annotations.Select;

/**
 * @ClassName PerformanceBaseDataMapper
 * @Description
 * @Author qianshui
 * @Date 2020/11/4 下午2:21
 */
public interface PerformanceBaseDataMapper extends MyBatisPlusMapper<PerformanceBaseDataEntity> {

    @Select("select agent_id, app_name, app_ip, process_id, process_name from t_performance_base_data"
        + " where timestamp >= #{startTime} and timestamp <= #{endTime}"
        + " and app_name=#{appName} and app_ip=#{appIp} and agent_id=#{agentId} limit 1")
    PerformanceBaseDataEntity selectOneRecord(PerformanceBaseQueryParam param);
}
