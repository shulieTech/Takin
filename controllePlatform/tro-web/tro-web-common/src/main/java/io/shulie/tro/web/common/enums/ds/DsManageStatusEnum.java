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

package io.shulie.tro.web.common.enums.ds;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 影子库/表 枚举类
 *
 * @author loseself
 * @date 2021/3/30 9:35 上午
 **/
@Getter
@AllArgsConstructor
public enum DsManageStatusEnum {

    ENABLE(0, "启用"),
    UNABLE(1, "禁用");

    /**
     * 状态
     */
    private final Integer code;

    /**
     * 状态描述
     */
    private final String desc;

    /**
     * 通过状态获取描述
     *
     * @param code 输入的状态
     * @return 状态描述
     */
    public static String getDescByStatus(Integer code) {
        return Arrays.stream(values())
                .filter(dsManageStatusEnum -> dsManageStatusEnum.getCode().equals(code))
                .findFirst()
                .map(DsManageStatusEnum::getDesc).orElse("");
    }

    /**
     * 根据 desc 获得枚举
     *
     * @param desc desc
     * @return 枚举
     */
    public static DsManageStatusEnum getEnumByDesc(String desc) {
        return Arrays.stream(values()).filter(dsManageStatusEnum -> dsManageStatusEnum.getDesc().equals(desc))
            .findFirst().orElse(null);
    }

}
