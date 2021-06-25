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
import com.pamirs.tro.entity.domain.dto.linkmanage.mapping.enums.fastdebug.RequestTypeEnum;

/**
 * @author 无涯
 * @Package com.pamirs.tro.entity.domain.dto.linkmanage.mapping.fastdebug
 * @date 2020/12/31 3:05 下午
 */
public class DebugRequestTypeEnumMapping {

    public static String getByCode(String code) {
        for (RequestTypeEnum requestTypeEnum : RequestTypeEnum.values()) {
            if (requestTypeEnum.getCode().equalsIgnoreCase(code)) {
                return requestTypeEnum.getDesc();
            }
        }
        return RequestTypeEnum.JSON.getDesc();
    }

    public static EnumResult parse(RequestTypeEnum requestTypeEnum) {
        EnumResult result = new EnumResult();
        if (requestTypeEnum != null) {
            switch (requestTypeEnum) {
                case TEXT:
                    result.label(requestTypeEnum.getCode()).value(requestTypeEnum.getDesc()).num(0);
                    break;
                case JSON:
                    result.label(requestTypeEnum.getCode()).value(requestTypeEnum.getDesc()).num(1);
                    break;
                case HTML:
                    result.label(requestTypeEnum.getCode()).value(requestTypeEnum.getDesc()).num(2);
                    break;
                case XML:
                    result.label(requestTypeEnum.getCode()).value(requestTypeEnum.getDesc()).num(3);
                    break;
                case JAVASCRIPT:
                    result.label(requestTypeEnum.getCode()).value(requestTypeEnum.getDesc()).num(4);
                    break;
            }
        }
        return result;
    }
    public static List<EnumResult> neededEnumResults() {
        List<EnumResult> enumResults = new ArrayList<>();
        for (RequestTypeEnum requestTypeEnum : RequestTypeEnum.values()) {
            enumResults.add(parse(requestTypeEnum));
        }
        return enumResults;
    }
}
