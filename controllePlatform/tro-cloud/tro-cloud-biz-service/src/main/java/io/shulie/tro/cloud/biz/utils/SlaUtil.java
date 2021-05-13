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

package io.shulie.tro.cloud.biz.utils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.Maps;
import io.shulie.tro.cloud.biz.input.scenemanage.SceneSlaRefInput;
import io.shulie.tro.cloud.common.bean.collector.SendMetricsEvent;

/**
 * @ClassName SlaUtil
 * @Description
 * @Author qianshui
 * @Date 2020/5/19 下午4:03
 */
public class SlaUtil {

    public static Map<String, Object> matchCondition(SceneSlaRefInput dto, SendMetricsEvent metricsEvnet) {
        Map<String, Object> resultMap = Maps.newHashMap();
        Integer targetType = dto.getRule().getIndexInfo();
        switch (targetType) {
            case 0:
                resultMap.put("type", "RT");
                resultMap.put("unit", "ms");
                matchCompare(resultMap, metricsEvnet.getAvgRt(), dto.getRule().getDuring(),
                    dto.getRule().getCondition());
                break;
            case 1:
                resultMap.put("type", "TPS");
                resultMap.put("unit", "");
                matchCompare(resultMap, metricsEvnet.getAvgTps(), dto.getRule().getDuring(),
                    dto.getRule().getCondition());
                break;
            case 2:
                resultMap.put("type", "成功率");
                resultMap.put("unit", "%");
                matchCompare(resultMap, metricsEvnet.getSuccessRate(), dto.getRule().getDuring(),
                    dto.getRule().getCondition());
                break;
            case 3:
                resultMap.put("type", "SA");
                resultMap.put("unit", "%");
                matchCompare(resultMap, metricsEvnet.getSa(), dto.getRule().getDuring(), dto.getRule().getCondition());
                break;
            default:
                resultMap.put("result", false);
        }
        return resultMap;
    }

    private static void matchCompare(Map<String, Object> resultMap, Double realValue, BigDecimal goalValue,
        Integer compareType) {
        if (realValue == null) {
            realValue = 0d;
        }
        switch (compareType) {
            case 0:
                resultMap.put("compare", ">=");
                resultMap.put("real", realValue);
                resultMap.put("result", new BigDecimal(realValue).compareTo(goalValue) >= 0);
                break;
            case 1:
                resultMap.put("compare", ">");
                resultMap.put("real", realValue);
                resultMap.put("result", new BigDecimal(realValue).compareTo(goalValue) > 0);
                break;
            case 2:
                resultMap.put("compare", "=");
                resultMap.put("real", realValue);
                resultMap.put("result", new BigDecimal(realValue).compareTo(goalValue) == 0);
                break;
            case 3:
                resultMap.put("compare", "<=");
                resultMap.put("real", realValue);
                resultMap.put("result", new BigDecimal(realValue).compareTo(goalValue) <= 0);
                break;
            case 4:
                resultMap.put("compare", "<");
                resultMap.put("real", realValue);
                resultMap.put("result", new BigDecimal(realValue).compareTo(goalValue) < 0);
                break;
            default:
                resultMap.put("result", false);
        }
    }
}
