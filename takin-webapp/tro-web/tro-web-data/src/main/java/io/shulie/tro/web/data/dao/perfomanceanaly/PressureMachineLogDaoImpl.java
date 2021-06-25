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

package io.shulie.tro.web.data.dao.perfomanceanaly;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.shulie.tro.web.data.common.InfluxDBWriter;
import io.shulie.tro.web.data.param.machine.PressureMachineLogInsertParam;
import io.shulie.tro.web.data.param.machine.PressureMachineLogQueryParam;
import io.shulie.tro.web.data.result.perfomanceanaly.PressureMachineLogResult;
import io.shulie.tro.web.data.result.perfomanceanaly.PressureMachineStatisticsResult;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;

/**
 * @Author: mubai
 * @Date: 2020-11-16 11:14
 * @Description:
 */

@Component
public class PressureMachineLogDaoImpl implements PressureMachineLogDao {

    @Autowired
    private InfluxDBWriter influxDBWriter;

    @Override
    public void insert(PressureMachineLogInsertParam param) {

        influxDbInsert(param);

    }


    @Override
    public List<PressureMachineLogResult> queryList(PressureMachineLogQueryParam queryParam) {
        return influxDbQuery(queryParam);
    }

    @Override
    public void clearRubbishData(String time) {
        if (StringUtils.isBlank(time)) {
            return;
        }
        StringBuilder influxDBSQL = new StringBuilder();
        influxDBSQL.append("delete");
        influxDBSQL.append(" from t_pressure_machine_log");
        influxDBSQL.append(" where time <= ");
        influxDBSQL.append("'");
        influxDBSQL.append(time);
        influxDBSQL.append("'");

        influxDBWriter.query(influxDBSQL.toString(), PressureMachineStatisticsResult.class);

    }


    void influxDbInsert(PressureMachineLogInsertParam param) {
        Map<String, Object> fields = Maps.newHashMap();
        fields.put("name", param.getName());
        fields.put("ip", param.getIp());
        fields.put("flag", param.getFlag());
        fields.put("cpu", param.getCpu());
        fields.put("memory", param.getMemory());
        fields.put("machine_usage", param.getMachineUsage());
        fields.put("disk", param.getDisk());
        fields.put("cpu_usage", param.getCpuUsage());
        fields.put("cpu_load", param.getCpuLoad());
        fields.put("memory_used", param.getMemoryUsed());
        fields.put("disk_io_wait", param.getDiskIoWait());
        fields.put("transmitted_total", param.getTransmittedTotal());
        fields.put("transmitted_in", param.getTransmittedIn());
        fields.put("transmitted_in_usage", param.getTransmittedInUsage());
        fields.put("transmitted_out", param.getTransmittedOut());
        fields.put("transmitted_out_usage", param.getTransmittedOutUsage());
        fields.put("transmitted_usage", param.getTransmittedUsage());
        fields.put("scene_names", param.getSceneNames());
        fields.put("status", param.getStatus());

        Map<String, String> tags = Maps.newHashMap();
        tags.put("machine_id", String.valueOf(param.getMachineId()));
        influxDBWriter.insert("t_pressure_machine_log", tags, fields, System.currentTimeMillis());


    }


    List<PressureMachineLogResult> influxDbQuery(PressureMachineLogQueryParam param) {
        StringBuilder influxDBSQL = new StringBuilder();
        influxDBSQL.append("select");
        influxDBSQL.append(" cpu_usage,cpu_load,memory_used,disk_io_wait,transmitted_usage,time as gmtCreate ");
        influxDBSQL.append(" from t_pressure_machine_log");
        influxDBSQL.append(" where ");
        influxDBSQL.append("  time >= ").append("'").append(param.getStartTime()).append("'");
        influxDBSQL.append(" and time <= ").append("'").append(param.getEndTime()).append("'");
        influxDBSQL.append(" and machine_id =").append("'").append(param.getMachineId()).append("'");
        influxDBSQL.append(" TZ('Asia/Shanghai')");

        List<PressureMachineLogResult> dataList = influxDBWriter.query(influxDBSQL.toString(),
                PressureMachineLogResult.class);

        if (CollectionUtils.isEmpty(dataList)) {
            return Lists.newArrayList();
        }
        return dataList;
    }
}
