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

import lombok.Data;

/**
 * @ClassName TimeBean
 * @Description
 * @Author qianshui
 * @Date 2020/4/17 下午9:51
 */
@Data
public class TimeBean implements Serializable {

    private static final long serialVersionUID = -4490980949244068326L;

    private Long time;

    private String unit;

    public TimeBean(){

    }

    public TimeBean(Long time, String unit) {
        this.time = time;
        this.unit = unit;
    }
}
