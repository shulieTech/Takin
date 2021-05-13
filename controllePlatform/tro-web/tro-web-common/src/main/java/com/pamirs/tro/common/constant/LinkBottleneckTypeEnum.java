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

package com.pamirs.tro.common.constant;

/**
 * 瓶颈类型(1、基础资源负载及异常，2、异步处理，3、TPS/RT稳定性，4，其他)
 *
 * @author shulie
 * @version v1.0
 * @2018年5月16日
 */
public enum LinkBottleneckTypeEnum {
    BOTTLENECK_LOAD_EXECEPTION(1, "基础资源负载及异常"),
    BOTTLENECK_ASYN(2, "异步处理"),
    BOTTLENECK_TPS_RT(3, "TPS/RT稳定性"),
    BOTTLENECK_OTHER(4, "RT响应时间");

    private int code;
    private String name;

    /**
     * 构造方法
     *
     * @param name
     */
    LinkBottleneckTypeEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return this.code;
    }

    public String getName() {
        return this.name;
    }
}
