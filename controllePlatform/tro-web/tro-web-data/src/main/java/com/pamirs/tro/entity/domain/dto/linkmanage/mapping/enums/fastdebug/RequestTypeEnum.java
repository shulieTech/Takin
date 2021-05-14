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

package com.pamirs.tro.entity.domain.dto.linkmanage.mapping.enums.fastdebug;

import lombok.Getter;

/**
 * @author 无涯
 * @Package com.pamirs.tro.entity.domain.dto.linkmanage.mapping.enums.fastdebug
 * @date 2020/12/31 2:40 下午
 */
@Getter
public enum RequestTypeEnum {
    TEXT("Text","text/plain"),
    JSON("JSON","application/json"),
    HTML("HTML","text/html"),
    XML("XML","text/xml"),
    JAVASCRIPT("JavaScript","application/javascript");
    private String code;
    private String desc;
    RequestTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }


}
