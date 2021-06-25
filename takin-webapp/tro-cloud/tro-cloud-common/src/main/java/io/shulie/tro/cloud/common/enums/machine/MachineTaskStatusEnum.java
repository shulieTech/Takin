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
 * @Date: 2020/5/13 下午5:25
 * @Description:
 */
public enum MachineTaskStatusEnum {
    /**
     * 1、开通中 2、开通失败 3、开通成功 4、销毁中 5、销毁失败 6、销毁成功
     */
    do_open(1, "开通中"),
    open_failed(2, "开通失败"),
    open_success(3, "开通成功"),
    do_destory(4, "销毁中"),
    destory_failed(5, "销毁失败"),
    destory_success(6, "销毁成功");
    private Integer code;
    private String name;

    MachineTaskStatusEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getStatus() {
        return name;
    }

    public void setStatus(String status) {
        this.name = status;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
