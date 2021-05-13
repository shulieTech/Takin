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

package io.shulie.tro.cloud.common.enums.machine;

/**
 * @Author: fanxx
 * @Date: 2020/5/13 上午10:49
 * @Description:
 */
public enum MachineTaskEnum {
    /**
     * 机器任务类型：开通任务、销毁任务
     */
    OPEN(1, "开通任务"),
    DESTORY(2, "销毁任务");
    private Integer code;
    private String status;

    MachineTaskEnum(Integer code, String status) {
        this.code = code;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
