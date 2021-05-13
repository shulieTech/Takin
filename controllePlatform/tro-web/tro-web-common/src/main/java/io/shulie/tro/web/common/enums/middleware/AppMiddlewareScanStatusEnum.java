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

package io.shulie.tro.web.common.enums.middleware;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 无涯
 * @Package io.shulie.tro.enums
 * @date 2021/2/23 10:35 上午
 */
@Getter
@AllArgsConstructor
public enum AppMiddlewareScanStatusEnum {
    NOT_ENTERED(1,"未录入"),
    NO_SUPPORT_REQUIRED(2,"无需支持"),
    NOT_SUPPORTED(3,"未支持"),
    SUPPORTED(4,"已支持")
    ;
    private Integer status;
    private String desc;

    public static String getDescByStatus(Integer status) {
        for(AppMiddlewareScanStatusEnum statusEnum :AppMiddlewareScanStatusEnum.values()) {
            if(statusEnum.getStatus().equals(status)) {
                return  statusEnum.getDesc();
            }
        }
        return "";
    }


}
