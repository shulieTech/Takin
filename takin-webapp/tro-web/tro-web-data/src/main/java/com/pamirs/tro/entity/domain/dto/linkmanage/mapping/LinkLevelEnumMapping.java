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

import com.pamirs.tro.entity.domain.dto.linkmanage.mapping.enums.LinkLevelEnum;

/**
 * @Auther: vernon
 * @Date: 2019/12/23 22:42
 * @Description:
 */
public class LinkLevelEnumMapping {
    public static LinkLevelEnum getByCode(String code) {
        for (LinkLevelEnum linkLevelEnum : LinkLevelEnum.values()) {
            if (linkLevelEnum.getCode().equalsIgnoreCase(code)) {
                return linkLevelEnum;
            }
        }
        return LinkLevelEnum.p0;
    }

    public static EnumResult parse(LinkLevelEnum linkLevelEnum) {
        EnumResult result = new EnumResult();
        if (linkLevelEnum != null) {
            switch (linkLevelEnum) {
                case p0:
                    result.label(linkLevelEnum.getDesc()).value(linkLevelEnum.getCode()).num(0);
                    break;
                case p1:
                    result.label(linkLevelEnum.getDesc()).value(linkLevelEnum.getCode()).num(1);
                    break;
                case p2:
                    result.label(linkLevelEnum.getDesc()).value(linkLevelEnum.getCode()).num(2);
                    break;
                case p3:
                    result.label(linkLevelEnum.getDesc()).value(linkLevelEnum.getCode()).num(3);
                    break;

            }
        } else {
        }
        return result;
    }

    public static List<EnumResult> neededEnumResults() {
        List<EnumResult> enumResults = new ArrayList<>();
        for (LinkLevelEnum statusEnum : LinkLevelEnum.values()) {
            enumResults.add(parse(statusEnum));
        }
        return enumResults;
    }
}
