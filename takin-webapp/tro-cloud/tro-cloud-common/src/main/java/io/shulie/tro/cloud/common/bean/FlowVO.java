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

package io.shulie.tro.cloud.common.bean;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

/**
 * @ClassName FlowVO
 * @Description
 * @Author qianshui
 * @Date 2020/5/26 下午7:44
 */
@Data
public class FlowVO implements Serializable {

    private Integer concurrenceNum;

    private TimeBean pressureTestTime;

    private Integer pressureMode;

    private TimeBean increasingTime;

    private Integer step;

    /**
     * 平均并发
     */
    private BigDecimal avgConcurrent;

    /**
     * 施压类型,0:并发,1:tps,2:自定义;不填默认为0
     */
    private Integer pressureType;

    @Override
    public String toString() {
        return "FlowVO{" +
            "concurrenceNum=" + concurrenceNum +
            ", pressureTestTime=" + pressureTestTime +
            ", pressureMode=" + pressureMode +
            ", increasingTime=" + increasingTime +
            ", step=" + step +
            '}';
    }
}
