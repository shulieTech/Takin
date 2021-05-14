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

import com.pamirs.tro.entity.domain.dto.linkmanage.mapping.enums.LinkChangeTypeEnum;

/**
 * @Auther: vernon
 * @Date: 2019/12/23 22:37
 * @Description:
 */
public class LinkChangeTypeEnumMapping {
    public static LinkChangeTypeEnum getByCode(String code) {
        for (LinkChangeTypeEnum linkChangeTypeEnum : LinkChangeTypeEnum.values()) {
            if (linkChangeTypeEnum.getCode().equalsIgnoreCase(code)) {
                return linkChangeTypeEnum;
            }
        }
        return LinkChangeTypeEnum.NO_FLOW_;
    }

    public static EnumResult parse(LinkChangeTypeEnum linkChangeTypeEnum) {
        EnumResult result = new EnumResult();
        if (linkChangeTypeEnum != null) {
            switch (linkChangeTypeEnum) {
                case NO_FLOW_:
                    result.label(linkChangeTypeEnum.getDesc()).value(linkChangeTypeEnum.getCode()).num(0);
                    break;
                case ADD_LINK_:
                    result.label(linkChangeTypeEnum.getDesc()).value(linkChangeTypeEnum.getCode()).num(1);
                    break;

            }
        } else {
        }
        return result;
    }

    public static List<EnumResult> neededEnumResults() {
        List<EnumResult> enumResults = new ArrayList<>();
        for (LinkChangeTypeEnum statusEnum : LinkChangeTypeEnum.values()) {
            enumResults.add(parse(statusEnum));
        }
        return enumResults;
    }
}
