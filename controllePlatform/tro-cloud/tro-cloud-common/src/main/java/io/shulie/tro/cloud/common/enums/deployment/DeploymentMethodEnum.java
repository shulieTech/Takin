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

package io.shulie.tro.cloud.common.enums.deployment;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 无涯
 * @Package io.shulie.tro.cloud.common.enums.deployment
 * @date 2021/4/25 1:58 下午
 */
@AllArgsConstructor
@Getter
public enum DeploymentMethodEnum {
    PRIVATE(0,"private"),
    PUBLIC( 1,"public"),
    LOCAL_THREAD(2,"localThread");
    public Integer type;
    private String desc;

    public static String getByType(Integer type) {
        if(type == null) {
            return DeploymentMethodEnum.PUBLIC.getDesc();
        }
        for(DeploymentMethodEnum methodEnum :DeploymentMethodEnum.values()) {
            if(methodEnum.type.equals(type)) {
                return methodEnum.getDesc();
            }
        }
        // 默认私有化
        return DeploymentMethodEnum.PRIVATE.getDesc();
    }

}
