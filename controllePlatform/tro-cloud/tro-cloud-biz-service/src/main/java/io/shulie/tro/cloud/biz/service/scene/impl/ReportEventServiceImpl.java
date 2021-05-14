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

package io.shulie.tro.cloud.biz.service.scene.impl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import io.shulie.tro.cloud.common.bean.collector.Metrics;
import io.shulie.tro.cloud.common.influxdb.InfluxWriter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.shulie.tro.cloud.biz.service.scene.ReportEventService;

/**
 * @ClassName ReportEventServiceImpl
 * @Description
 * @Author qianshui
 * @Date 2020/7/20 下午4:23
 */
@Service
@Slf4j
public class ReportEventServiceImpl implements ReportEventService {

    private static final List<Integer> indexs = Arrays.asList(99, 90, 75, 50);
    private static final String PERCENTAGE = "%";
    private static final String MS = "ms";
    @Autowired
    private InfluxWriter influxWriter;

    @Override
    public Map<String, String> queryAndCalcRtDistribute(String tableName, String bindRef) {
        // TODO sum_rt 取名改下
        StringBuffer sql = new StringBuffer();
        sql.append("select sum_rt as avgRt from ");
        sql.append(tableName);
        sql.append(" where transaction='");
        sql.append(bindRef);
        sql.append("'");
        List<Metrics> dataList = influxWriter.query(sql.toString(), Metrics.class);
        if (CollectionUtils.isEmpty(dataList)) {
            return null;
        }
        dataList.sort(((o1, o2) -> {
            if (o1.getAvgRt() < o2.getAvgRt()) {
                return -1;
            }
            return 1;
        }));
        int size = dataList.size();
        Map<String, String> resultMap = Maps.newLinkedHashMap();
        indexs.forEach(index -> {
            resultMap.put(index + PERCENTAGE, dataList.get(calcIndex(size, index)).getAvgRt() + MS);
        });
        return resultMap;
    }

    private int calcIndex(int size, int percentage) {
        if (percentage <= 0) {
            return 0;
        }
        if (percentage >= 100) {
            return size - 1;
        }
        BigDecimal b1 = new BigDecimal(size);
        BigDecimal b2 = new BigDecimal(percentage);
        int index = b1.multiply(b2.divide(new BigDecimal(100))).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
        if (index >= size) {
            index = size - 1;
        }
        return index;
    }
}
