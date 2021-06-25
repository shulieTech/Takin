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

package io.shulie.tro.web.data.param.machine;

import io.shulie.tro.common.beans.page.PagingDevice;
import lombok.Data;

/**
 * @Author: mubai
 * @Date: 2020-11-13 10:07
 * @Description:
 */

@Data
public class PressureMachineQueryParam extends PagingDevice {

    /**
     * id
     */
    private Long id;

    /**
     * 机器水位排序； 1：正序 ； -1：倒叙
     */
    private Integer machineUsageOrder;

    /**
     * 压力机名称
     */
    private String name;

    /**
     * 压力机IP
     */
    private String ip;

    /**
     * 标签
     */
    private String flag;

    /**
     * 状态 0：空闲 ；1：压测中  -1:离线
     */
    private Integer status;

}
