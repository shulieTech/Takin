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
import io.shulie.tro.web.data.mapper.mysql.PressureMachineMapper;
import io.shulie.tro.web.data.param.perfomanceanaly.PressureMachineStatisticsInsertParam;
import io.shulie.tro.web.data.param.perfomanceanaly.PressureMachineStatisticsQueryParam;
import io.shulie.tro.web.data.result.perfomanceanaly.PressureMachineStatisticsDTO;
import io.shulie.tro.web.data.result.perfomanceanaly.PressureMachineStatisticsResult;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.*;

/**
 * @Author: mubai
 * @Date: 2020-11-13 11:43
 * @Description:
 */

@Component
public class PressureMachineStatisticsDaoImpl implements PressureMachineStatisticsDao {

    @Resource
    private PressureMachineMapper pressureMachineMapper;

    @Autowired
    private InfluxDBWriter influxDBWriter;

    @Override
    public void insert(PressureMachineStatisticsInsertParam param) {
        influxInsert(param);
    }


    private void influxInsert(PressureMachineStatisticsInsertParam baseEntity) {
        Map<String, Object> fields = Maps.newHashMap();
        fields.put("machine_total", baseEntity.getMachineTotal());
        fields.put("machine_pressured", baseEntity.getMachinePressured());
        fields.put("machine_free", baseEntity.getMachineFree());
        fields.put("machine_offline", baseEntity.getMachineOffline());
        Map<String, String> tags = Maps.newHashMap();
        influxDBWriter.insert("t_pressure_machine_statistics", tags, fields, System.currentTimeMillis());
    }


    @Override
    public List<PressureMachineStatisticsResult> queryByExample(PressureMachineStatisticsQueryParam param) {
        /**
         * 采用influxdb 进行查询
         */
        return influxdbQuery(param);
    }

    List<PressureMachineStatisticsResult> influxdbQuery(PressureMachineStatisticsQueryParam param) {
        StringBuilder influxDBSQL = new StringBuilder();
        influxDBSQL.append("select");
        influxDBSQL.append(" machine_total ,machine_pressured,machine_free,machine_offline,time as gmtCreate  ");
        influxDBSQL.append(" from t_pressure_machine_statistics");
        influxDBSQL.append(" where ");
        influxDBSQL.append("  time >= ").append("'").append(param.getStartTime()).append("'");
        influxDBSQL.append(" and time <= ").append("'").append(param.getEndTime()).append("'");
        influxDBSQL.append(" TZ('Asia/Shanghai')");

        List<PressureMachineStatisticsResult> dataList = influxDBWriter.query(influxDBSQL.toString(),
                PressureMachineStatisticsResult.class);

        if (CollectionUtils.isEmpty(dataList)) {
            return Lists.newArrayList();
        }
        return dataList;

    }

    @Override
    public PressureMachineStatisticsResult getNewlyStatistics() {
        StringBuilder influxDBSQL = new StringBuilder();
        influxDBSQL.append("select");
        influxDBSQL.append(" machine_total ,machine_pressured,machine_free,machine_offline,time as gmtCreate  ");
        influxDBSQL.append(" from t_pressure_machine_statistics");
        influxDBSQL.append(" order by time desc limit 1 ");
        influxDBSQL.append(" TZ('Asia/Shanghai')");

        List<PressureMachineStatisticsResult> dataList = influxDBWriter.query(influxDBSQL.toString(),
                PressureMachineStatisticsResult.class);

        if (CollectionUtils.isEmpty(dataList)) {
            return new PressureMachineStatisticsResult();
        }
        return dataList.get(0);

    }

    @Override
    public PressureMachineStatisticsResult statistics() {

        PressureMachineStatisticsResult result = new PressureMachineStatisticsResult();
        List<PressureMachineStatisticsDTO> statistics = pressureMachineMapper.statistics();
        Long totalNum = 0L;
        for (PressureMachineStatisticsDTO dto : statistics) {
            if (dto.getStatus() == -1) {
                result.setMachineOffline(dto.getCount().intValue());
            } else if (dto.getStatus() == 0) {
                result.setMachineFree(dto.getCount().intValue());
            } else if (dto.getStatus() == 1) {
                result.setMachinePressured(dto.getCount().intValue());
            }
            totalNum += dto.getCount();
        }
        result.setMachineTotal(totalNum.intValue());
        return result;
    }

    @Override
    public void clearRubbishData(String time) {
        if (StringUtils.isBlank(time)) {
            return;
        }
        StringBuilder influxDBSQL = new StringBuilder();
        influxDBSQL.append("delete");
        influxDBSQL.append(" from t_pressure_machine_statistics");
        influxDBSQL.append(" where time <= ");
        influxDBSQL.append("'");
        influxDBSQL.append(time);
        influxDBSQL.append("'");

        influxDBWriter.query(influxDBSQL.toString(), PressureMachineStatisticsResult.class);
    }
}
