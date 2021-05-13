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
 * @Date: 2020/5/15 下午8:16
 * @Description:
 */
public enum MachineStatusEnum {
    /**
     * 状态 1、开通中 2、开通成功 3、开通失败 4：启动中 5、启动成功 6、启动失败 7、初始化中 8、初始化失败 9、运行中 10、销毁中 11、已过期 12、已锁定 13、销毁失败 14、已销毁
     */
    do_open(1, "开通中"),
    open_success(2, "开通成功"),
    open_failed(3, "开通失败"),

    do_start(4, "启动中"),
    start_success(5, "启动成功"),
    start_failed(6, "启动失败"),

    do_init(7, "初始化中"),
    init_failed(8, "初始化失败"),

    do_run(9, "运行中"),

    do_destory(10, "销毁中"),
    expired(11, "已过期"),
    locked(12, "已锁定"),

    destory_failed(13, "销毁失败"),
    destoryed(14, "已销毁");

    private Integer code;
    private String name;

    MachineStatusEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
