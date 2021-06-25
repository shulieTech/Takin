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

package com.pamirs.tro.entity.domain.dto.linkmanage.mapping.fastdebug;

import java.util.ArrayList;
import java.util.List;

import com.pamirs.tro.entity.domain.dto.linkmanage.mapping.EnumResult;
import com.pamirs.tro.entity.domain.dto.linkmanage.mapping.enums.fastdebug.HttpTypeEnum;

/**
 * @author 无涯
 * @Package com.pamirs.tro.entity.domain.dto.linkmanage.mapping.fastdebug
 * @date 2020/12/31 2:25 下午
 */
public class DebugHttpTypeEnumMapping {

    public static EnumResult parse(HttpTypeEnum httpTypeEnum) {
        EnumResult result = new EnumResult();
        if (httpTypeEnum != null) {
            switch (httpTypeEnum) {
                case GET:
                    result.label(httpTypeEnum.name()).value(httpTypeEnum.name()).num(0);
                    break;
                case POST:
                    result.label(httpTypeEnum.name()).value(httpTypeEnum.name()).num(1);
                    break;
                case PUT:
                    result.label(httpTypeEnum.name()).value(httpTypeEnum.name()).num(2);
                    break;
                case DELETE:
                    result.label(httpTypeEnum.name()).value(httpTypeEnum.name()).num(3);
                    break;
            }
        }
        return result;
    }

    public static List<EnumResult> neededEnumResults() {
        List<EnumResult> enumResults = new ArrayList<>();
        for (HttpTypeEnum httpTypeEnum : HttpTypeEnum.values()) {
            enumResults.add(parse(httpTypeEnum));
        }
        return enumResults;
    }
}
