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

package com.pamirs.tro.entity.domain.dto.linkmanage.mapping;

import java.util.ArrayList;
import java.util.List;

import com.pamirs.tro.entity.domain.dto.linkmanage.mapping.enums.HttpTypeEnum;

/**
 * @Auther: vernon
 * @Date: 2019/12/23 22:37
 * @Description:
 */
public class HttpTypeEnumMapping {
    public static HttpTypeEnum getByCode(String code) {
        for (HttpTypeEnum httpTypeEnum : HttpTypeEnum.values()) {
            if (httpTypeEnum.getCode().equalsIgnoreCase(code)) {
                return httpTypeEnum;
            }
        }
        return HttpTypeEnum.GET;
    }

    public static EnumResult parse(HttpTypeEnum httpTypeEnum) {
        EnumResult result = new EnumResult();
        if (httpTypeEnum != null) {
            switch (httpTypeEnum) {
                case DEFAULT:
                    result.label(httpTypeEnum.getDesc()).value(httpTypeEnum.getCode()).num(0);
                    break;
                case PUT:
                    result.label(httpTypeEnum.getDesc()).value(httpTypeEnum.getCode()).num(1);
                    break;
                case GET:
                    result.label(httpTypeEnum.getDesc()).value(httpTypeEnum.getCode()).num(2);
                    break;
                case POST:
                    result.label(httpTypeEnum.getDesc()).value(httpTypeEnum.getCode()).num(3);
                    break;
                case OPTIONS:
                    result.label(httpTypeEnum.getDesc()).value(httpTypeEnum.getCode()).num(4);
                    break;
                case DELETE:
                    result.label(httpTypeEnum.getDesc()).value(httpTypeEnum.getCode()).num(5);
                    break;
                case HEAD:
                    result.label(httpTypeEnum.getDesc()).value(httpTypeEnum.getCode()).num(6);
                    break;
                case TRACE:
                    result.label(httpTypeEnum.getDesc()).value(httpTypeEnum.getCode()).num(7);
                    break;
                case CONNECT:
                    result.label(httpTypeEnum.getDesc()).value(httpTypeEnum.getCode()).num(8);
                    break;

            }
        } else {
            result.label(httpTypeEnum.getDesc()).value(httpTypeEnum.getCode()).num(0);
        }
        return result;
    }

    public static List<EnumResult> neededEnumResults() {
        List<EnumResult> enumResults = new ArrayList<>();
        for (HttpTypeEnum linkEnum : HttpTypeEnum.values()) {
            enumResults.add(parse(linkEnum));
        }
        return enumResults;
    }
}
