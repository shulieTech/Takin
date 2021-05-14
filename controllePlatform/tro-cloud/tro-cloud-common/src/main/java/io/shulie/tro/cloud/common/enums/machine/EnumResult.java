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

import lombok.Data;

/**
 * @Auther: vernon
 * @Date: 2019/12/23 21:52
 * @Description:
 */
@Data
public class EnumResult {
    /**
     * 用于逻辑的值
     */
    private String value = "";
    /**
     * '文案展示的值'
     */
    private String label = "";
    /**
     * 前端枚举序号
     */
    private Integer num = 0;
    /**
     * 前端不显示
     */
    private Boolean disable = false;

    public EnumResult disable() {
        this.disable = true;
        return this;
    }

    public EnumResult label(String label) {
        this.label = label;
        return this;

    }

    public EnumResult value(String value) {
        this.value = value;
        return this;
    }

    public EnumResult num(Integer num) {
        this.num = num;
        return this;
    }
}
